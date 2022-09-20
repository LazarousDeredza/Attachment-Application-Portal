package com.example.attachmentapplicationportal;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class SendMailClass extends  Application {

    private Activity mActivity;
    private Context mContext;



    public void SendMail(String jobTittle, String location, String email, String username, String program) {
        Log.e("Data=",jobTittle+","+location+","+email+","+username+","+program);



         ProgressDialog progressDialog = new ProgressDialog(SendMailClass.this);
        progressDialog.setMessage("Applying Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();


        Handler mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                AlertDialog alertDialog = new AlertDialog.Builder(SendMailClass.this).create();
                alertDialog.setTitle("Success : Email Sent");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("You have Applied successfully");
                alertDialog.setIcon(android.R.drawable.ic_menu_save);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent =new Intent(SendMailClass.this,ConfirmCompanyKey.class);
                                startActivity(intent);
                              //  finish();
                            }
                        });

                alertDialog.show();

            }
        };





        Handler nHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);


                AlertDialog errorD = new AlertDialog.Builder(SendMailClass.this).create();
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
                String messageToSend ="Attachment Application Portal Application";






                GMailSender sender = new GMailSender(username, password);
                try {
                    sender.sendMail("Attachment Portal Company Registration Code", messageToSend, username,
                            email);

                    Log.e("SendMailSuccess", "Sending Email Successfully");
                    //Toast.makeText(GenerateKey.this, "SendMailSuccess", Toast.LENGTH_SHORT).show();

                    // progressDialog.setMessage("Finishing");

                   // progressDialog.dismiss();


                    mHandler.sendEmptyMessage(0);


                } catch (Exception e) {
                 //   progressDialog.dismiss();

                    //Toast.makeText(GenerateKey.this, "SendMail Error", Toast.LENGTH_SHORT).show();
                    Log.e("SendMailError", e.getMessage(), e);

                    nHandler.sendEmptyMessage(0);

                }
            }
        });
        thread.start();








    }
}
