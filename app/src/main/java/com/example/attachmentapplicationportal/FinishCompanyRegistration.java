package com.example.attachmentapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;



import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class FinishCompanyRegistration extends AppCompatActivity {

    CircleImageView imgProfile;
    TextView txtCompanyLogo;
    EditText name,email,password,phone,services,location;
    DatabaseReference reference,reference2;
    MaterialButton signupbtn;
    String regex;
    //Compile regular expression to get the pattern
    Pattern pattern;

    String sImage;

    private static final int REQUEST_OPEN_IMAGE_SELECT_DIALOGUE= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_company_registration);

        imgProfile=findViewById(R.id.imgProfile);
        txtCompanyLogo=findViewById(R.id.txtCompanyLogo);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        phone=findViewById(R.id.phone);
        services=findViewById(R.id.services);
        location=findViewById(R.id.location);

        signupbtn=findViewById(R.id.signupbtn);

        regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        pattern = Pattern.compile(regex);

        sImage=null;

        Bundle extras=getIntent().getExtras();
        String Code=extras.getString("Code");


        reference= FirebaseDatabase.getInstance().getReference("Keys");
        reference2= FirebaseDatabase.getInstance().getReference("Companies");

        ProgressDialog progressDialog = new ProgressDialog(FinishCompanyRegistration.this);
        progressDialog.setMessage("Setting up , Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KeyClass keyClass=snapshot.child(Code).getValue(KeyClass.class);

                assert keyClass != null;
                name.setText(keyClass.getCompany());
                email.setText(keyClass.getEmail());

                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("DB error",error.toString());
                Toast.makeText(FinishCompanyRegistration.this, "Database initialization Error", Toast.LENGTH_SHORT).show();
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String Services = services.getText().toString().trim();
                String Location = location.getText().toString().trim();

                Matcher matcher = pattern.matcher(Email);

                if (Name.isEmpty()) {
                    name.setError("Company Name is required");
                    name.requestFocus();
                } else if (Email.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                } else if (!pattern.matcher(Email).matches()) {
                    email.setError("Please enter a valid email");
                    email.requestFocus();
                } else if (Password.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                } else if (Location.isEmpty()) {
                    location.setError("Address is required");
                    location.requestFocus();
                } else if (Phone.isEmpty()) {
                    phone.setError("Phone is required");
                    phone.requestFocus();
                } else {
                    if (matcher.matches()) {
                        if (Password.length() >= 4) {
                            if(Services.isEmpty()){
                                Services="...";
                            }
                            if(sImage.isEmpty()){
                                sImage="...";
                            }

                            CompanyClass companyClass = new CompanyClass(Name, Email, Phone, sImage, Password,Services,Location);

                           ProgressDialog d = new ProgressDialog(FinishCompanyRegistration.this);
                            d.setMessage("Saving, Please wait...");
                            d.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            d.setCancelable(false);
                            d.show();

                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    reference2.child(companyClass.getName().toString()).setValue(companyClass);
                                    reference.child(Code).removeValue();
                                    d.dismiss();
                                    AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(FinishCompanyRegistration.this).create();
                                    alertDialog.setTitle("Success");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setMessage("You have successfully signed up");
                                    alertDialog.setIcon(android.R.drawable.ic_menu_save);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                   Intent intent=new Intent(FinishCompanyRegistration.this,CompanyHome.class);
                                                   intent.putExtra("Name",companyClass.getName().toString());
                                                   startActivity(intent);
                                                    dialog.dismiss();
                                                    clearTexts();
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("DB error",error.toString());
                                    Toast.makeText(FinishCompanyRegistration.this, "Database initialization Error", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else{
                            password.setError("Password must be at least 4 characters");
                            password.requestFocus();
                        }


                    } else {
                        email.setError("Please enter a valid email");
                        email.requestFocus();
                    }
                }






            }
        });


        txtCompanyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if permission to read storage
                if(ContextCompat.checkSelfPermission(FinishCompanyRegistration.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    //When permission not granted , Request
                    ActivityCompat.requestPermissions(FinishCompanyRegistration.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }else{
                    //When permission is granted

                    selectImage();
                }
            }
        });

    }

    private void clearTexts() {
        imgProfile.setImageDrawable(null);
        name.setText(null);
        email.setText(null);
        password.setText(null);
        phone.setText(null);
        services.setText(null);
        location.setText(null);

    }


    private void selectImage() {
        //Clear previous data
        imgProfile.setImageBitmap(null);
        //initialise intent
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);

        //Set type
        intent.setType("image/*");


        startActivityForResult(Intent.createChooser(intent,"Select Image"),
                100);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if(requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //when permission granted
            selectImage();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(requestCode==100&&resultCode==RESULT_OK&&data!=null){
            //when result is ok ,initialize uri
            Uri uri =data.getData();



            try {
                //initialize bitmap
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                //set image view to the selected image
                imgProfile.setImageBitmap(bitmap);
                //initialize byte array stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                //compress bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                //initialize byte array
                byte [] bytes = stream.toByteArray();

                //get base 64 encoded String

                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
                //Set Encoded String to textView
                //Toast.makeText(AddObject.this, sImage, Toast.LENGTH_SHORT).show();



            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }



    //Funtion to decode string back to image

    private void decode(){
        //initialise byte array from encoded string
        byte [] bytes=Base64.decode(sImage,Base64.DEFAULT);

        //Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        //Set bitmap on image view
        imgProfile.setImageBitmap(bitmap);
    }




}