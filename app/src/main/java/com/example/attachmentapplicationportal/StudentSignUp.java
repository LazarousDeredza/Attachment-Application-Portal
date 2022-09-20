package com.example.attachmentapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


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

public class StudentSignUp extends AppCompatActivity {

    DatabaseReference reference;

    EditText name, surname, username, email, password, phone,edit_text_Program;
    MaterialButton signupbtn;
    String regex;
    //Compile regular expression to get the pattern
    Pattern pattern;
    TextView textProgram;
    Dialog dialog;

    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        edit_text_Program = findViewById(R.id.edit_text_Program);

        signupbtn = findViewById(R.id.signupbtn);

        textProgram = findViewById(R.id.textProgram);
        arrayList = new ArrayList<>();

        addProgramsToDropDown();


        regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        pattern = Pattern.compile(regex);


        reference = FirebaseDatabase.getInstance().getReference().child("Students");

        textProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialise Dialog
                dialog = new Dialog(StudentSignUp.this);

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
                        StudentSignUp.this, android.R.layout.simple_list_item_1, arrayList);

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





        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Surname = surname.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Username = username.getText().toString().trim().toLowerCase();
                String Password = password.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String Program = textProgram.getText().toString().trim();


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
                } else {
                    if (matcher.matches()) {
                        if (Password.length() >= 4) {
                            StudentClass studentClass = new StudentClass(Name, Surname, Username, Email, Password, Phone, "", Program, "missing", "missing", "missing", "missing", "not set", "not set", "...");

                            ProgressDialog progressDialog = new ProgressDialog(StudentSignUp.this);
                            progressDialog.setMessage("Signing up...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(Program).child(Username).exists()) {
                                        progressDialog.dismiss();
                                        username.setError("Username already exists");
                                        Toast.makeText(StudentSignUp.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                                        username.requestFocus();
                                    } else {
                                        progressDialog.dismiss();

                                        reference.child(Program).child(Username).setValue(studentClass);
                                        AlertDialog alertDialog = new AlertDialog.Builder(StudentSignUp.this).create();
                                        alertDialog.setTitle("Success");
                                        alertDialog.setCancelable(false);
                                        alertDialog.setMessage("You have successfully signed up");
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

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Error db", error.getMessage());

                                    Toast.makeText(StudentSignUp.this, "Database Initialisation Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
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
}