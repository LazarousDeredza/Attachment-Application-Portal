package com.example.attachmentapplicationportal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class GenerateKey extends AppCompatActivity {

    EditText email,name;
    MaterialButton  signupbtn;
    CircleImageView back;
    String regex;
    //Compile regular expression to get the pattern
    Pattern pattern;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_key);

        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        signupbtn=findViewById(R.id.signupbtn);
        back=findViewById(R.id.emergencyone);

        regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        pattern = Pattern.compile(regex);


        reference= FirebaseDatabase.getInstance().getReference("Keys");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GenerateKey.this,ConfirmCompanyKey.class);
                name.setText(null);
                email.setText(null);
                startActivity(intent);
                finish();
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = name.getText().toString().trim();
                String Email = email.getText().toString().trim();

                Matcher matcher = pattern.matcher(Email);

                if (Name.isEmpty()) {
                    name.setError("Company Name is required");
                    name.requestFocus();
                }else if (Email.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                } else{
                    if (matcher.matches()) {

                        Random random=new Random();
                        int Cd=  random.nextInt(5000 - 100)+100;

                        int leftLimit = 97; // letter 'a'
                        int rightLimit = 122; // letter 'z'
                        int targetStringLength = 7;
                        Random rnd = new Random();

                        String Ky = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            Ky = rnd.ints(leftLimit, rightLimit + 1)
                                    .limit(targetStringLength)
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();
                        }


                        ProgressDialog progressDialog = new ProgressDialog(GenerateKey.this);
                        progressDialog.setMessage("Generating Key...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        progressDialog.show();








                        String finalKy = Ky;



                        Handler mHandler=new Handler(){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);

                                AlertDialog alertDialog = new AlertDialog.Builder(GenerateKey.this).create();
                                alertDialog.setTitle("Success");
                                alertDialog.setCancelable(false);
                                alertDialog.setMessage("You have successfully generated a new key\nIt has been sent to Your Email");
                                alertDialog.setIcon(android.R.drawable.ic_menu_save);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Intent intent =new Intent(GenerateKey.this,ConfirmCompanyKey.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                alertDialog.show();

                            }
                        };





                        Handler nHandler=new Handler(){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);


                                AlertDialog errorD = new AlertDialog.Builder(GenerateKey.this).create();
                                errorD.setTitle("Error");
                                errorD.setCancelable(false);
                                errorD.setMessage("There was an error in sending your email\nPlease check your internet and try again");
                                errorD.setIcon(android.R.drawable.stat_sys_warning);
                                errorD.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });
                                errorD.show();
                            }
                        };




                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {




                                String username = "ninja.ld49@gmail.com";
                                String password = "uxbhldqealdgttae";
                                String messageToSend ="Attachment Application Portal \nNew Company Registration(NB :This is a one time use key and code) \n \n Code : "+Cd+" \n Key : "+ finalKy +"\n\n Thank you for working with us, Have a great day";






                                GMailSender sender = new GMailSender(username, password);
                                try {
                                    sender.sendMail("Attachment Portal Company Registration Code", messageToSend, username,
                                            Email);

                                    Log.e("SendMailSuccess", "Sending Email Successfully");
                                    //Toast.makeText(GenerateKey.this, "SendMailSuccess", Toast.LENGTH_SHORT).show();

                                    progressDialog.setMessage("Finishing");

                                    KeyClass keyClass=new KeyClass(String.valueOf(Cd), finalKy,Name,Email);
                                    reference.child(String.valueOf(Cd)).setValue(keyClass);

                                    progressDialog.dismiss();


                                    mHandler.sendEmptyMessage(0);


                                } catch (Exception e) {
                                    progressDialog.dismiss();

                                    //Toast.makeText(GenerateKey.this, "SendMail Error", Toast.LENGTH_SHORT).show();
                                    Log.e("SendMailError", e.getMessage(), e);

                                    nHandler.sendEmptyMessage(0);

                                }
                            }
                        });
                        thread.start();













                    }else{
                        email.setError("Please enter a valid email");
                        email.requestFocus();
                        Toast.makeText(GenerateKey.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }
}