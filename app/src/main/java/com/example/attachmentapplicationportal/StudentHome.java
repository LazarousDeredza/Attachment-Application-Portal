package com.example.attachmentapplicationportal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentHome extends AppCompatActivity {

    DatabaseReference reference,reference2;
    ProgressDialog progressDialog;
    RelativeLayout studentHome;
    TextView txtName,txtEmail,txtPhone,txtInstituitionAndProgram;

    CircleImageView imgProfile;

    FloatingActionButton profile,logout;
    FloatingActionsMenu floatingActionsMenu;

    RecyclerView recview;
    String Username,Program;
    StudentClass student;

    String sImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);


        Bundle extras=getIntent().getExtras();
        Username=extras.getString("Username");
        Program =extras.getString("Program");

        txtName=(TextView)findViewById(R.id.txtName);
        txtEmail=(TextView)findViewById(R.id.txtEmail);
        txtPhone=(TextView)findViewById(R.id.txtPhone);
        imgProfile=findViewById(R.id.imgProfile);
        txtInstituitionAndProgram=(TextView)findViewById(R.id.txtInstituitionAndProgram);

        profile=(FloatingActionButton) findViewById(R.id.profile);
        logout=(FloatingActionButton) findViewById(R.id.logout);
        floatingActionsMenu=(FloatingActionsMenu) findViewById(R.id.floatingActionsMenu);


        txtName.setText(null);
        txtEmail.setText(null);
        txtPhone.setText(null);
        txtInstituitionAndProgram.setText(null);
        sImage="";


       // listView=(ListView)findViewById(R.id.listviewtxt);
        //arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
       // listView.setAdapter(arrayAdapter);

       // module=(Module)getApplicationContext();

       // btnApply=(Button) findViewById(R.id.btnApply);






        studentHome=findViewById(R.id.studentHome);

        reference= FirebaseDatabase.getInstance().getReference("Students");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog=new ProgressDialog(StudentHome.this);
                progressDialog.setMessage("Loading Profile...");
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setIndeterminate(false);
                progressDialog.show();


                StudentClass studentClass=snapshot.child(Program).child(Username).getValue(StudentClass.class);

                student=snapshot.child(Program).child(Username).getValue(StudentClass.class);

                if(studentClass!=null){

                    String firstName,lastName,regNumber,email,
                            password,contact,institution,
                            program,results,letter,ID,cv,
                            startDate,endDate,img;


                    progressDialog.dismiss();



                    firstName=studentClass.getFirstName();
                    lastName=studentClass.getLastName();
                    regNumber=studentClass.getRegNumber();
                    img=studentClass.getImg();
                    email=studentClass.getEmail();
                    password=studentClass.getPassword();
                    contact=studentClass.getContact();
                    institution=studentClass.getInstitution();
                    program=studentClass.getProgram();


                    if (firstName!=null){
                        txtName.setText(firstName);
                    }
                    if (lastName!=null){
                        txtName.setText(txtName.getText().toString().concat(" ").concat(lastName));
                    }
                    if (email!=null){
                        txtEmail.setText(email);
                    }
                    if (contact!=null){
                        txtPhone.setText(contact);
                    }
                    if (institution!=null){
                        txtInstituitionAndProgram.setText(institution);
                    }

                    if (program!=null){
                        txtInstituitionAndProgram.setText(txtInstituitionAndProgram.getText().toString().concat(" : ").concat(program));
                    }

                    if (Objects.equals(img, "...")||!Objects.equals(img, null)){
                        sImage = img;
                        setImage(img);
                    }else{
                        sImage = "...";
                    }

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(StudentHome.this,"No Profile Found",Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("Error",error.getMessage());
                Toast.makeText(StudentHome.this, "Error Initialising Database", Toast.LENGTH_SHORT).show();
            }
        });


        ProgressDialog dialog=new ProgressDialog(StudentHome.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Available Attachment Jobs ...");
        dialog.show();


        reference2= FirebaseDatabase.getInstance().getReference("JobAdverts");

        List<JobAdvertsClass> adverts=new ArrayList<>();

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                JobAdvertsClass jb=snapshot.getValue(JobAdvertsClass.class);

                adverts.add(jb);

                dialog.dismiss();

                recview=findViewById(R.id.recview);
                recview.setLayoutManager(new LinearLayoutManager(StudentHome.this));


                myadapter adapter=new myadapter(adverts);
                adapter.pr=Program;
                adapter.us=Username;
                adapter.student=student;
                recview.setAdapter(adapter);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionsMenu.collapse();
                Intent intent=new Intent(StudentHome.this,ProfileUpdate.class);
                intent.putExtra("Username",Username);
                intent.putExtra("Program",Program);
                startActivity(intent);
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionsMenu.collapse();
                Intent intent=new Intent(StudentHome.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(StudentHome.this, "logout successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        floatingActionsMenu.collapse();



        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
               floatingActionsMenu.expand();
               // Toast.makeText(StudentHome.this, "Expanded", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMenuCollapsed() {
               floatingActionsMenu.collapse();
               // Toast.makeText(StudentHome.this, "Collapsed", Toast.LENGTH_SHORT).show();

            }
        });



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

}