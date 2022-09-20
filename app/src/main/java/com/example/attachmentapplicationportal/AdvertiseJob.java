package com.example.attachmentapplicationportal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdvertiseJob extends AppCompatActivity {

    DatabaseReference reference,reference2;
    EditText jobTitle,location,startDate,endDate,qualifcations,description;
    MaterialButton signupbtn;

    CompanyClass company;

    DatePickerDialog datePickerDialog;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise_job);
        company=new CompanyClass();

        Bundle extras=getIntent().getExtras();
        String key=extras.getString("Name");


        jobTitle=findViewById(R.id.jobTitle);
        location=findViewById(R.id.location);
        startDate=findViewById(R.id.startDate);
        endDate=findViewById(R.id.endDate);
        qualifcations=findViewById(R.id.qualifcations);
        description=findViewById(R.id.description);
        signupbtn=findViewById(R.id.signupbtn);
        calendar = Calendar.getInstance();


        ProgressDialog progressDialog = new ProgressDialog(AdvertiseJob.this);
        progressDialog.setMessage("Setting up , Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        reference= FirebaseDatabase.getInstance().getReference("Companies");
        reference2= FirebaseDatabase.getInstance().getReference("JobAdverts");



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                company=snapshot.child(key).getValue(CompanyClass.class);
                CompanyClass companyClass=snapshot.child(key).getValue(CompanyClass.class);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error",error.getMessage());
                Toast.makeText(AdvertiseJob.this, "Error Initialising Database", Toast.LENGTH_SHORT).show();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String jobTitle,location,startDate,endDate,qualifcations,description;

                String JobTitle=jobTitle.getText().toString();
                String Location=location.getText().toString();
                String StartDate=startDate.getText().toString();
                String EndDate=endDate.getText().toString();
                String Qualifcations=qualifcations.getText().toString();
                String Description=description.getText().toString();
                String CompanyName=company.getName();
                String Email=company.getEmail();
                String Phone=company.getPhone();

               // dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm aaa");
              //  String Startdate = dateFormat.format(LocalDate.parse(StartDate));
                //String Enddate = dateFormat.format(LocalDate.parse(EndDate);

                if (JobTitle.isEmpty()) {
                    jobTitle.setError("Job Title is required");
                    jobTitle.requestFocus();
                }else if (Location.isEmpty()){

                    location.setError("Location is required");
                    location.requestFocus();
                } else if (StartDate.isEmpty()) {
                    startDate.setError("Application End Date is required");
                    startDate.requestFocus();
                } else if (EndDate.isEmpty()) {
                    endDate.setError("Application End Date is required");
                    endDate.requestFocus();
                } else if (Qualifcations.isEmpty()) {
                    qualifcations.setError("Enter Qualifications");
                    qualifcations.requestFocus();
                } else if (Description.isEmpty()) {
                    description.setError("Enter Job Description");
                    description.requestFocus();
                } else {

                   /* String companyName, String location,
                            String jobTittle, String email,
                            String qualifications, String jobSummary,
                            String applicationsStartDate,
                            String applicationsEndDate)*/

                    JobAdvertsClass jobAdvertsClass=new JobAdvertsClass(CompanyName,Location,JobTitle,Email,Qualifcations,Description,StartDate,EndDate,Phone);

                    ProgressDialog progressDialog = new ProgressDialog(AdvertiseJob.this);
                    progressDialog.setMessage("Posting Advert , Please wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    reference2.child(JobTitle+"-"+System.currentTimeMillis()).setValue(jobAdvertsClass);
                    progressDialog.dismiss();


                    AlertDialog alertDialog = new AlertDialog.Builder(AdvertiseJob.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("You have successfully Posted Your Advert");
                    alertDialog.setIcon(android.R.drawable.ic_menu_save);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    clearTexts();
                                }
                            });
                    alertDialog.show();

                }





            }
        });



        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(AdvertiseJob.this, new DatePickerDialog.OnDateSetListener() {
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

                        startDate.setText(Day + "-" + Month + "-" + year);
                    }
                }, mDay, mMonth, mYear);
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(AdvertiseJob.this, new DatePickerDialog.OnDateSetListener() {
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

                        endDate.setText(Day + "-" + Month + "-" + year);
                    }
                }, mDay, mMonth, mYear);
                datePickerDialog.show();
            }
        });

    }

    private void clearTexts() {
        jobTitle.setText(null);
        location.setText(null);
        startDate.setText(null);
        endDate.setText(null);
        qualifcations.setText(null);
        description.setText(null);

    }


}