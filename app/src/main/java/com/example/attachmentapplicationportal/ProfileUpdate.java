package com.example.attachmentapplicationportal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProfileUpdate extends AppCompatActivity {


    DatabaseReference reference;
    CircleImageView imgProfile;
    EditText name, surname, username, email, password, phone,edit_text_Program,edit_text_Institution;
    MaterialButton signupbtn;
    String regex;
    //Compile regular expression to get the pattern
    Pattern pattern;
    TextView textProgram, textInstitution,
            txtstartDate,txtendDate,txtResults,
    txtLetter,txtID,txtCV;
    Dialog dialog;
    ProgressDialog progressDialog;

    ArrayList<String> arrayList, institutionList;
    Button btnstartDate,btnendDate
            ,btnResults,btnLetter,
            btnID,btnCV;

    String sImage;
    String FileType;
    String DateType;
    String Password;

     ProgressDialog mprogress;

     StorageReference mStorage;


    DatePickerDialog datePickerDialog;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_update);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        sImage="";
        DateType="";
        Password="";


        Bundle extras=getIntent().getExtras();
        String Username=extras.getString("Username");
        String Program=extras.getString("Program");




        calendar = Calendar.getInstance();

        imgProfile=findViewById(R.id.imgProfile);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        edit_text_Program = findViewById(R.id.edit_text_Program);
        edit_text_Institution = findViewById(R.id.edit_text_institution);

        txtstartDate = findViewById(R.id.txtstartDate);
        txtendDate = findViewById(R.id.txtendDate);
        txtResults = findViewById(R.id.txtResults);
        txtLetter = findViewById(R.id.txtLetter);
        txtID = findViewById(R.id.txtID);
        txtCV = findViewById(R.id.txtCV);

        mStorage= FirebaseStorage.getInstance().getReference();
        mprogress=new ProgressDialog(this);


        btnstartDate = findViewById(R.id.btnstartDate);
        btnendDate = findViewById(R.id.btnendDate);
        btnResults = findViewById(R.id.btnResults);
        btnLetter = findViewById(R.id.btnLetter);
        btnID = findViewById(R.id.btnID);
        btnCV = findViewById(R.id.btnCV);
        

        signupbtn = findViewById(R.id.signupbtn);

        textProgram = findViewById(R.id.textProgram);
        textInstitution = findViewById(R.id.textInstitution);
        arrayList = new ArrayList<>();
        institutionList = new ArrayList<>();

        addProgramsToDropDown();
        addInstitutionsToDropDown();

        regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        pattern = Pattern.compile(regex);


        reference= FirebaseDatabase.getInstance().getReference("Students");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog=new ProgressDialog(ProfileUpdate.this);
                progressDialog.setMessage("Loading Profile...");
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setIndeterminate(false);
                progressDialog.show();


                StudentClass studentClass=snapshot.child(Program).child(Username).getValue(StudentClass.class);
                if(studentClass!=null){

                    String firstName,lastName,regNumber,Email,
                            password,contact,institution,
                            program,results,letter,ID,cv,
                            startDate,endDate,img;


                    progressDialog.dismiss();



                    firstName=studentClass.getFirstName();
                    lastName=studentClass.getLastName();
                    regNumber=studentClass.getRegNumber();
                    img=studentClass.getImg();
                    Email=studentClass.getEmail();
                    contact=studentClass.getContact();
                    institution=studentClass.getInstitution();
                    program=studentClass.getProgram();

                    results=studentClass.getResults();
                    letter=studentClass.getLetter();
                    ID=studentClass.getID();
                    cv=studentClass.getCv();
                    startDate=studentClass.getStartDate();
                    endDate=studentClass.getEndDate();


                    if (firstName!=null){
                        name.setText(firstName);
                    }
                    if (lastName!=null){
                        surname.setText(lastName);
                    }
                    if (email!=null){
                        email.setText(Email);
                    }
                    if (contact!=null){
                        phone.setText(contact);
                    }
                    if (institution!=null){
                        textInstitution.setText(institution);
                    }
                    if (regNumber!=null){
                        username.setText(regNumber);
                    }
                    if (program!=null){
                        textProgram.setText(program);
                    }

                    if (Objects.equals(img, "...")||!Objects.equals(img, null)){
                        sImage = img;
                        setImage(img);
                    }else{
                        sImage = "...";
                    }



                    if (!Objects.equals(results, "missing")){
                        txtResults.setText(results);
                    }else{
                        txtResults.setText("missing");
                    }
                    if (!Objects.equals(letter, "missing")){
                        txtLetter.setText(letter);
                    }else{
                        txtLetter.setText("missing");
                    }
                    if (!Objects.equals(ID, "missing")){
                        txtID.setText(ID);
                    }else{
                        txtID.setText("missing");
                    }
                    if (!Objects.equals(cv, "missing")){
                        txtCV.setText(cv);
                    }else{
                        txtCV.setText("missing");
                    }
                    if (!Objects.equals(startDate, "not set")){
                     txtstartDate.setText(startDate);
                    }else{
                        txtstartDate.setText("not set");
                    }
                    if (!Objects.equals(endDate, "not set")){
                        txtendDate.setText(endDate);
                    }else{
                        txtendDate.setText("not set");
                    }

                    Toast.makeText(ProfileUpdate.this, institution, Toast.LENGTH_LONG).show();

                    Password=studentClass.getPassword();

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(ProfileUpdate.this,"No Profile Found",Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("Error",error.getMessage());
                Toast.makeText(ProfileUpdate.this, "Error Initialising Database", Toast.LENGTH_SHORT).show();
            }
        });




        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString().trim();
                String Surname = surname.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Username = username.getText().toString().trim().toLowerCase();
               // String Password = password.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String Program = textProgram.getText().toString().trim();
                String Institution = textInstitution.getText().toString().trim();

                String Results=txtResults.getText().toString();
                String CV=txtCV.getText().toString();
                String ID=txtID.getText().toString();
                String Letter=txtLetter.getText().toString();
                String StartDate=txtstartDate.getText().toString();
                String EndDate=txtendDate.getText().toString();


                Matcher matcher = pattern.matcher(Email);


                if (Name.isEmpty()) {
                    name.setError("Name is required");
                    name.requestFocus();
                }else if (Program.isEmpty()){
                    textProgram.setError("Program is required");
                    textProgram.requestFocus();
                } else if (Surname.isEmpty()) {
                    surname.setError("Surname is required");
                    surname.requestFocus();
                } else if (Email.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                } else if (!pattern.matcher(Email).matches()) {
                    email.setError("Please enter a valid email");
                    email.requestFocus();
                } else if (Username.isEmpty()) {
                    username.setError("Username is required");
                    username.requestFocus();
                } else if (Password.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                } else if (Phone.isEmpty()) {
                    phone.setError("Phone is required");
                    phone.requestFocus();
                }else if (Institution.isEmpty()){
                    textInstitution.setError("Select Your Institution");
                    textInstitution.requestFocus();
                }else{

                    if(!matcher.matches()){
                        email.setError("Please enter a valid email");
                        email.requestFocus();
                    }else {
                        progressDialog = new ProgressDialog(ProfileUpdate.this);
                        progressDialog.setMessage("Updating Your Profile...");
                        progressDialog.setCancelable(false);
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        progressDialog.setIndeterminate(false);
                        progressDialog.show();

                      //  StudentClass studentClass = snapshot.child(Program).child(Username).getValue(StudentClass.class);

                        StudentClass student = new StudentClass(Name, Surname, Username, Email, Password, Phone, Institution, Program, Results, Letter, ID, CV, StartDate, EndDate, sImage);


                                    progressDialog.dismiss();

                                    reference.child(Program).child(Username).setValue(student);
                                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileUpdate.this).create();
                                    alertDialog.setTitle("Success");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setMessage("Profile Updated successfully");
                                    alertDialog.setIcon(android.R.drawable.ic_menu_save);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();


                    }
                }
            }
        });






        textProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialise Dialog
                dialog = new Dialog(ProfileUpdate.this);

                //Set Custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);


                //Set Custom Heigth and Width
                dialog.getWindow().setLayout(650, 800);

                //set Transparent Background

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                //show Dialog
                dialog.show();

                //Initialise Dialog Components

                EditText editText = dialog.findViewById(R.id.edit_text_Program);
                ListView listView = dialog.findViewById(R.id.list_view);

                //Initialise Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        ProfileUpdate.this, android.R.layout.simple_list_item_1, arrayList);

                listView.setAdapter(adapter);

                //Set Listener
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Filter the list
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        //Filter the list
                    }
                });

                //Set Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Get the selected item
                        String selectedItem = arrayList.get(position);

                        //Set the selected item to the text view
                        textProgram.setText(selectedItem);

                        //Close the dialog
                        dialog.dismiss();
                    }


                });
            }
        });

        textInstitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialise Dialog
                dialog = new Dialog(ProfileUpdate.this);

                //Set Custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner3);


                //Set Custom Heigth and Width
                dialog.getWindow().setLayout(650, 800);

                //set Transparent Background

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                //show Dialog
                dialog.show();

                //Initialise Dialog Components

                EditText editText = dialog.findViewById(R.id.edit_text_institution);
                ListView listView = dialog.findViewById(R.id.list_view);

                //Initialise Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        ProfileUpdate.this, android.R.layout.simple_list_item_1, institutionList);

                listView.setAdapter(adapter);

                //Set Listener
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Filter the list
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        //Filter the list
                    }
                });

                //Set Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Get the selected item
                        String selectedItem = institutionList.get(position);

                        //Set the selected item to the text view
                        textInstitution.setText(selectedItem);

                        //Close the dialog
                        dialog.dismiss();
                    }


                });
            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ProfileUpdate.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    //When permission not granted , Request
                    ActivityCompat.requestPermissions(ProfileUpdate.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }else{
                    //When permission is granted

                    selectImage();
                }
            }
        });



        btnstartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateType="startDate";
                pickDate();
            }
        });
        btnendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateType="endDate";
                pickDate();
            }
        });




        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileType="results";
                fetchFile();
            }
        });
        btnLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileType="letter";
                fetchFile();
            }
        });
        btnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileType="ID";
                fetchFile();
            }
        });
        btnCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileType="cv";
                fetchFile();
            }
        });

        
        
    }

    private void pickDate() {
        final Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        datePickerDialog = new DatePickerDialog(ProfileUpdate.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                String Day = String.valueOf(day);
                String Month = String.valueOf(month + 1);

                if (Day.length() == 1) {
                    Day = "0" + Day;
                }
                if (Month.length() == 1) {
                    Month = "0" + Month;
                }

               // startDate.setText(Day + "-" + Month + "-" + year);

                if(DateType.equals("startDate")){
                    txtstartDate.setText(Day + "-" + Month + "-" + year);
                }else{
                    txtendDate.setText(Day + "-" + Month + "-" + year);
                }
            }
        }, mDay, mMonth, mYear);
        datePickerDialog.show();

    }

    private void fetchFile() {


          //Check if permission to read storage
          if (ContextCompat.checkSelfPermission(ProfileUpdate.this,
                  Manifest.permission.READ_EXTERNAL_STORAGE)
                  != PackageManager.PERMISSION_GRANTED) {
              //When permission not granted , Request
              ActivityCompat.requestPermissions(ProfileUpdate.this,
                      new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
          } else {
              //When permission is granted

              SelectFiles();
      }
    }

    private void SelectFiles( ) {

        //initialise intent
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);

        //Set type
        intent.setType("application/pdf");


        startActivityForResult(Intent.createChooser(intent,"Select Document"),
                200);
    }


    private void addProgramsToDropDown() {
        arrayList.add(" Computer Science");
        arrayList.add(" Computer Engineering");
        arrayList.add(" Electrical Engineering");
        arrayList.add(" Electronics Engineering");
        arrayList.add(" Mechanical Engineering");
        arrayList.add(" Civil Engineering");
        arrayList.add(" Industrial Engineering");
        arrayList.add(" Architecture");
        arrayList.add(" Geography");
        arrayList.add(" Geology");
        arrayList.add(" Geophysics");
        arrayList.add(" Geochemistry");
        arrayList.add(" Geosystems");
        arrayList.add(" Geotechnical Engineering");
        arrayList.add(" Environmental Engineering");
        arrayList.add(" Environmental Science");
        arrayList.add(" Environmental Management");
        arrayList.add(" Environmental Studies");
        arrayList.add(" BSc Agribusiness");
        arrayList.add(" Agriculture");
        arrayList.add(" Agriculture and Food Science");
        arrayList.add(" Agriculture and Food Technology");
        arrayList.add(" Agriculture and Food Engineering");
        arrayList.add(" Agriculture and Food Management");
        arrayList.add(" Agriculture and Food Economics");
        arrayList.add(" Agriculture and Food Management");
        arrayList.add(" Agriculture and Food Economics");
        arrayList.add(" Biotechnology");
        arrayList.add(" Biochemistry");
        arrayList.add(" Biochemistry and Biotechnology");
        arrayList.add(" Development Studies");
        arrayList.add(" Development Studies and Management");
        arrayList.add(" Electrical and Electronics Engineering");
        arrayList.add(" Electrical and Electronics Engineering and Management");
        arrayList.add(" Food and Nutrition");
        arrayList.add(" Geography and Environmental Studies");
        arrayList.add(" Industrial and Production Engineering");
        arrayList.add(" Journalism");
        arrayList.add(" Journalism and Mass Communication");
        arrayList.add(" Kinesiology and Physical Education");
        arrayList.add(" Law");
        arrayList.add(" Nursing");
        arrayList.add(" Nursing and Midwifery");
        arrayList.add(" Occupational Therapy");
        arrayList.add(" Physical Therapy");
        arrayList.add(" Psychology");
        arrayList.add(" Public Administration");
        arrayList.add(" Public Administration and Management");
        arrayList.add(" Quality Assurance and Management");
        arrayList.add(" Social Work and Management");
        arrayList.add(" Recreation and Leisure Studies");
        arrayList.add(" Tourism and Hospitality");
        arrayList.add(" Teaching");
        arrayList.add(" Telecommunications");
        arrayList.add(" Telecommunications and Computer Engineering");
        arrayList.add(" Software Engineering");


    }
    private void addInstitutionsToDropDown() {
        institutionList.add(" G.Z.U");
        institutionList.add(" U.Z");
        institutionList.add(" C.U.T");
        institutionList.add(" M.S.U");
        institutionList.add(" Z.E.G.U");
        institutionList.add(" R.C.U");
        institutionList.add(" B.U.S.E");
        institutionList.add(" H.I.T");
        institutionList.add(" R.C.U");
        institutionList.add(" Z.O.U");
        institutionList.add(" N.U.S.T");
        institutionList.add(" TELONE");

    }

    private void setImage(String Logos) {
        if (!Logos.equals("...")){

            byte [] bytes= Base64.decode(Logos,Base64.DEFAULT);

            //Initialize bitmap
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            //Set bitmap on image view
            imgProfile.setImageBitmap(bitmap);
        }

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
        if(requestCode==200 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //when permission granted
            SelectFiles();
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
        else{
            mprogress.setMessage("Uploading Document to Database");
            mprogress.setCancelable(false);
            mprogress.show();
            //Check condition
            if(requestCode==200&&resultCode==RESULT_OK&&data!=null){
                //when result is ok ,initialize uri
                Uri urii =data.getData();

                Cursor returnCursor=getContentResolver().query(urii,null,null,null,null);

                int nameIndex=returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex=returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();

                String FileName=returnCursor.getString(nameIndex).replaceAll("[^a-zA-Z0-9\\s.] ","");
                String fileSize=Long.toString(returnCursor.getLong(sizeIndex));
                int size= Integer.parseInt(fileSize);
                int sizeinMB=(size/1024)/1024;

                if(sizeinMB>=8){
                    mprogress.dismiss();
                    Toast.makeText(this, "The Selected Document is Too larger ("+sizeinMB+"MB) \n 8MB is the Max Size", Toast.LENGTH_LONG).show();


                }else{
                    StorageReference filepath=mStorage.child(name.getText().toString().trim().concat("_").concat(surname.getText().toString().trim())).child(FileName);

                    filepath.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mprogress.dismiss();
                            Toast.makeText(ProfileUpdate.this, "Document Uploaded Sucessfully", Toast.LENGTH_SHORT).show();

                            AlertDialog builder=new AlertDialog.Builder(ProfileUpdate.this)
                                    .setIcon(android.R.drawable.ic_menu_save)
                                    //set title
                                    .setTitle("Success")
                                    //set message
                                    .setMessage(FileType+ " added successfully ,\n File Name = : "+FileName+ " \n File Size = : "+sizeinMB +" MB")
                                    //set positive button
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //set what would happen when positive button is clicked
                                            if (FileType.equals("cv")){
                                                txtCV.setText(FileName);
                                            }else if(FileType.equals("letter")) {
                                                txtLetter.setText(FileName);
                                            }else if(FileType.equals("ID")) {
                                                txtID.setText(FileName);
                                            }else if(FileType.equals("results")) {
                                                txtResults.setText(FileName);
                                            }

                                        }
                                    })

                                    .show();

                        }
                    });

                }

            }
        }
    }
}