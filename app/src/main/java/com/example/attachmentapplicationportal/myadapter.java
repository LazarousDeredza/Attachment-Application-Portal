package com.example.attachmentapplicationportal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>
{
    public StudentClass student;
    List<JobAdvertsClass> adverts;
    String us,pr;


    public myadapter(List<JobAdvertsClass> adverts) {
        this.adverts = adverts;
    }

    @NonNull
    @NotNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
          holder.recid.setText(String.valueOf(adverts.get(position).jobTittle));
          holder.recfname.setText(adverts.get(position).getCompanyName());
          holder.reclname.setText(adverts.get(position).getLocation());
        holder.startDate.setText(adverts.get(position).getApplicationsStartDate());
        holder.endDate.setText(adverts.get(position).getApplicationsEndDate());
        holder.email.setText(adverts.get(position).getEmail());
        holder.phone.setText(adverts.get(position).getPhone());

          holder.apply.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  // this is to delete the record from room database
                 // userDao.deleteById(users.get(position).getUid());
                  // this is to delete the record from Array List which is the source of recview data
                //  users.remove(position);

                  Toast.makeText(view.getContext(), String.valueOf(adverts.get(position).jobTittle), Toast.LENGTH_SHORT).show();

                 // StudentHome studentHome=new StudentHome();

                Log.e("Data=",adverts.get(position).jobTittle+","+adverts.get(position).getLocation()+","+adverts.get(position).getEmail()+","+us+","+pr);
                 // studentHome.SendMail(adverts.get(position).getJobTittle(),adverts.get(position).getLocation(),adverts.get(position).getEmail(),us,pr);
                  //update the fresh list of ArrayList data to recview


                  ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                  progressDialog.setMessage("Sending Email, Please Wait...");
                  progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                  progressDialog.setCancelable(false);
                  progressDialog.show();





                  Handler mHandler=new Handler(){
                      @Override
                      public void handleMessage(@NonNull Message msg) {
                          super.handleMessage(msg);

                          AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                          alertDialog.setTitle("Success");
                          alertDialog.setCancelable(false);
                          alertDialog.setMessage("You Email Sent Successfully\n");
                          alertDialog.setIcon(android.R.drawable.ic_menu_save);
                          alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(DialogInterface dialog, int which) {
                                          dialog.dismiss();

                                      }
                                  });

                          alertDialog.show();

                      }};





                  Handler nHandler=new Handler(){
                      @Override
                      public void handleMessage(@NonNull Message msg) {
                          super.handleMessage(msg);


                          AlertDialog errorD = new AlertDialog.Builder(view.getContext()).create();
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
                          String messageToSend = null;
                          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                              messageToSend = LocalDate.now().toString()+ "\n \n"+
                                "Dear Sir/Madam"+ "\n \n"+
                              "My Name is "+student.getFirstName()+" "+student.getLastName() +", a" +
                               "Student studying torwards "+student.getProgram()+ " at " +
                               student.getInstitution() +".\n I am looking for attachment vacancy " +
                               "related to my program and i came across yout advert for " +
                               adverts.get(position).jobTittle+" wanted in "+adverts.get(position).location + "" +
                               "  that was posted on Attachment Portal and i am interested \n \n"+
                              "Below are my work related documents : \n\n"+
                              "CV : "+ "https://firebasestorage.googleapis.com/v0/b/attachment-application-portal.appspot.com/o/"+student.getFirstName()+"_"+student.getLastName()+"%2F"+student.getCv().replaceAll(" ","%20")+"?alt=media"+"\n\n"+
                                      "ID : "+ "https://firebasestorage.googleapis.com/v0/b/attachment-application-portal.appspot.com/o/"+student.getFirstName()+"_"+student.getLastName()+"%2F"+student.getID().replaceAll(" ","%20")+"?alt=media \n\n"+
                                      "Letter From School : "+ "https://firebasestorage.googleapis.com/v0/b/attachment-application-portal.appspot.com/o/"+student.getFirstName()+"_"+student.getLastName()+"%2F"+student.getLetter().replaceAll(" ","%20")+"?alt=media"+" \n\n"+
                                      "Results : "+ "https://firebasestorage.googleapis.com/v0/b/attachment-application-portal.appspot.com/o/"+student.getFirstName()+"_"+student.getLastName()+"%2F"+student.getResults().replaceAll(" ","%20")+"?alt=media"+"  \n \n"+


                              "Thank you for your time and consideration, i am looking forward to your response \n" +
                                      "Have a wonderful day" ;

                          }


                          GMailSender sender = new GMailSender(username, password);
                          try {
                              sender.sendMail("Attachment Vacancy Application - " +adverts.get(position).jobTittle, messageToSend, username,
                                      adverts.get(position).getEmail());

                              Log.e("SendMailSuccess", "Sending Email Successfully");
                            //  Toast.makeText(view.getContext(), "Email Sent Successfully", Toast.LENGTH_SHORT).show();
                              progressDialog.setMessage("Finishing");

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



                  notifyDataSetChanged();








              }
          });
    }

   /* private void sendMail(String tittle, String location, String email) {


        ProgressDialog progressDialog = new ProgressDialog(myadapter.this);
        progressDialog.setMessage("Generating Key...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();






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




    }*/


    @Override
    public int getItemCount() {
        return adverts.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
       {
           TextView recid,recfname, reclname,startDate,endDate,email,phone;
           ImageButton apply;
           public myviewholder(@NonNull @NotNull View itemView) {
               super(itemView);

               recid=itemView.findViewById(R.id.recid);
               recfname=itemView.findViewById(R.id.recfname);
               reclname=itemView.findViewById(R.id.reclname);
               startDate=itemView.findViewById(R.id.startDate);
               endDate=itemView.findViewById(R.id.endDate);
               phone=itemView.findViewById(R.id.phone);
               email=itemView.findViewById(R.id.email);


               apply =itemView.findViewById(R.id.btnApply);
           }
       }
}
