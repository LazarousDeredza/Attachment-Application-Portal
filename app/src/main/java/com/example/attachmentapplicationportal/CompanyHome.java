package com.example.attachmentapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class CompanyHome extends AppCompatActivity {

    CardView  recruit,advertise,profile;
    CircleImageView imgProfile;
    FloatingActionButton logout;
    TextView name,address,phone,email;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        Bundle extras=getIntent().getExtras();
        String key=extras.getString("Name");



        imgProfile=findViewById(R.id.imgProfile);
        logout=findViewById(R.id.logout);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);



        recruit=findViewById(R.id.recruit);
        advertise=findViewById(R.id.advertise);
        profile=findViewById(R.id.profile);


        reference= FirebaseDatabase.getInstance().getReference("Companies");

        ProgressDialog progressDialog = new ProgressDialog(CompanyHome.this);
        progressDialog.setMessage("Setting up , Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CompanyClass companyClass=snapshot.child(key).getValue(CompanyClass.class);
                assert companyClass != null;
                name.setText(companyClass.getName());
                address.setText(companyClass.getAddress());
                phone.setText(companyClass.getPhone());
                email.setText(companyClass.getEmail());

                setImage(companyClass.getLogo());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error",error.getMessage());
                Toast.makeText(CompanyHome.this, "Error Initialising Database", Toast.LENGTH_SHORT).show();
            }
        });







    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(CompanyHome.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(CompanyHome.this, "Logout successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    });

        recruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CompanyHome.this,AdvertHistory.class);
                intent.putExtra("Name",key);
                startActivity(intent);
               // Toast.makeText(CompanyHome.this, "Logout successfully", Toast.LENGTH_SHORT).show();
               // finish();
            }
        });

        advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(CompanyHome.this, "Advertise Clicked", Toast.LENGTH_SHORT).show();*/
                Intent intent=new Intent(CompanyHome.this,AdvertiseJob.class);
                intent.putExtra("Name",key);
                startActivity(intent);
               // finish();

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(CompanyHome.this, "profile clicked", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(CompanyHome.this,CompanyProfileUpdate.class);
                intent.putExtra("Name",key);
                startActivity(intent);
            }
        });

    }

    private void setImage(String logo) {
        if (!logo.equals("...")){

            byte [] bytes= Base64.decode(logo,Base64.DEFAULT);

            //Initialize bitmap
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            //Set bitmap on image view
            imgProfile.setImageBitmap(bitmap);
        }

    }


}