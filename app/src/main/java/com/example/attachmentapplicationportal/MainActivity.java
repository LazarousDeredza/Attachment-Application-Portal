package com.example.attachmentapplicationportal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String Ctgry;
    DatabaseReference reference, reference2;
    MaterialButton loginbtn, registerbtn;
    RadioGroup radioCategoryGroup;
    RadioButton radioCategoryButton;

    EditText username, password, email;

    TextView textProgram, textCompany;
    Dialog dialog;

    ArrayList<String> arrayList, companyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtn);


        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        textProgram = findViewById(R.id.textProgram);
        textCompany = findViewById(R.id.textCompany);
        arrayList = new ArrayList<>();
        companyList = new ArrayList<>();

        addProgramsToDropDown();


        textProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialise Dialog
                dialog = new Dialog(MainActivity.this);

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
                        MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

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

        textCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialise Dialog
                dialog = new Dialog(MainActivity.this);

                //Set Custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner2);


                //Set Custom Heigth and Width
                dialog.getWindow().setLayout(650, 800);

                //set Transparent Background

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                //show Dialog
                dialog.show();

                //Initialise Dialog Components

                EditText editText = dialog.findViewById(R.id.edit_text_Company);
                ListView listView = dialog.findViewById(R.id.list_view);

                //Initialise Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        MainActivity.this, android.R.layout.simple_list_item_1, companyList);

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
                        String selectedItem = companyList.get(position);

                        //Set the selected item to the text view
                        textCompany.setText(selectedItem);

                        //Close the dialog
                        dialog.dismiss();
                    }


                });
            }
        });

        radioCategoryGroup = findViewById(R.id.radioCategoryGroup);


        radioCategoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedButton = radioCategoryGroup.getCheckedRadioButtonId();
                radioCategoryButton = findViewById(selectedButton);
                Ctgry = radioCategoryButton.getText().toString();
                if (Ctgry.equals("Company")) {
                    // Toast.makeText(MainActivity.this,"Company Selected",Toast.LENGTH_SHORT).show();

                    addCompaniesToDropDown();

                    textCompany.setVisibility(View.VISIBLE);
                    textProgram.setVisibility(View.GONE);

                    username.setVisibility(View.GONE);
                    email.setVisibility(View.VISIBLE);


                } else if (Ctgry.equals("Student")) {
                    //  Toast.makeText(MainActivity.this,"Student Selected",Toast.LENGTH_SHORT).show();
                    username.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);

                    textCompany.setVisibility(View.GONE);
                    textProgram.setVisibility(View.VISIBLE);
                }


            }
        });



/*
        radioCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funSelekted();

            }
        });
*/


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedCategory = radioCategoryGroup.getCheckedRadioButtonId();
                radioCategoryButton = (RadioButton) findViewById(selectedCategory);

                if (radioCategoryGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "Please Pick a category", Toast.LENGTH_SHORT).show();
                } else {
                    Ctgry = radioCategoryButton.getText().toString();
                    if (Ctgry.equals("Company")) {
                        Intent intent = new Intent(MainActivity.this, ConfirmCompanyKey.class);
                        startActivity(intent);

                    } else if (Ctgry.equals("Student")) {
                        Intent intent = new Intent(MainActivity.this, StudentSignUp.class);

                        startActivity(intent);
                    }
                }


            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedCategory = radioCategoryGroup.getCheckedRadioButtonId();
                radioCategoryButton = (RadioButton) findViewById(selectedCategory);


                String Username = username.getText().toString().toLowerCase().trim();
                String Password = password.getText().toString();
                String Program = textProgram.getText().toString().trim();

                String Email = email.getText().toString();
                String Company = textCompany.getText().toString().trim();


                if (radioCategoryGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "Please Pick a category", Toast.LENGTH_SHORT).show();
                } else {

                    Ctgry = radioCategoryButton.getText().toString();
                    if (Ctgry.equals("Company")) {

                        // Toast.makeText(MainActivity.this, "Company Selected", Toast.LENGTH_SHORT).show();

                        if (Company.isEmpty()) {
                            textCompany.setError("Please Select your Company");
                            textCompany.requestFocus();
                        } else if (Email.isEmpty()) {
                            email.setError("Please enter your Email");
                            email.requestFocus();
                        } else if (Password.isEmpty()) {
                            password.setError("Please enter your password");
                            password.requestFocus();
                        } else if (Email.isEmpty() && Password.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (!(Email.isEmpty() && Password.isEmpty())) {

                           // Toast.makeText(MainActivity.this, "Validating Company Details", Toast.LENGTH_SHORT).show();

                            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                            progressDialog.setMessage("Signing In...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.show();


                            reference2=FirebaseDatabase.getInstance().getReference("Companies");
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child(Company).child("email").getValue().toString().equals(Email)){

                                        if(snapshot.child(Company).child("password").getValue().toString().equals(Password)){
                                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this, CompanyHome.class);
                                            progressDialog.dismiss();
                                            intent.putExtra("Name", Company);
                                            startActivity(intent);
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Email Not Found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                    Log.e("Error db", error.getMessage());
                                    Toast.makeText(MainActivity.this, "Database Initialisation Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }
                    else if (Ctgry.equals("Student")) {


                        if (Program.isEmpty()) {
                            textProgram.setError("Please Select your program");
                            textProgram.requestFocus();
                        } else if (Username.isEmpty()) {
                            username.setError("Please enter your username");
                            username.requestFocus();
                        } else if (Password.isEmpty()) {
                            password.setError("Please enter your password");
                            password.requestFocus();
                        } else if (Username.isEmpty() && Password.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                        } else if (!(Username.isEmpty() && Password.isEmpty())) {

                            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                            progressDialog.setMessage("Signing In...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            reference = FirebaseDatabase.getInstance().getReference().child("Students");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.child(Program).child(Username).exists()) {
                                        StudentClass studentClass = snapshot.child(Program).child(Username).getValue(StudentClass.class);
                                        assert studentClass != null;
                                        if (studentClass.getPassword().equals(Password)) {
                                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this, StudentHome.class);
                                            progressDialog.dismiss();
                                            intent.putExtra("Username", Username);
                                            intent.putExtra("Program", Program);
                                            startActivity(intent);
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    progressDialog.dismiss();
                                    Log.e("Error db", error.getMessage());
                                    Toast.makeText(MainActivity.this, "Database Initialisation Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

            }
        }
    });


}

    private void addCompaniesToDropDown() {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Initialising Company List...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        reference2 = FirebaseDatabase.getInstance().getReference("Companies");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                companyList = new ArrayList<>();
                for (DataSnapshot company : snapshot.getChildren()) {
                    companyList.add(company.child("name").getValue().toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("Error db", error.getMessage());

                Toast.makeText(MainActivity.this, "Database Initialisation Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void funSelekted() {
        int selectedButton = radioCategoryGroup.getCheckedRadioButtonId();
        radioCategoryButton = findViewById(selectedButton);
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