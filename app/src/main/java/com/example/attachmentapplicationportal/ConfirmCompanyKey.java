package com.example.attachmentapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
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

import java.util.Objects;

public class ConfirmCompanyKey extends AppCompatActivity {

    EditText code,key;
    MaterialButton signupbtn;
    TextView generateKey;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_company_key);

        code=findViewById(R.id.code);
        key=findViewById(R.id.key);
        signupbtn=findViewById(R.id.signupbtn);
        generateKey=findViewById(R.id.generateKey);
        reference= FirebaseDatabase.getInstance().getReference("Keys");

        generateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConfirmCompanyKey.this,GenerateKey.class);
                startActivity(intent);
                key.setText(null);
                code.setText(null);
                finish();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Code=code.getText().toString().trim();
                String Key=key.getText().toString().trim();

                if (Code.isEmpty()) {
                    code.setError("Code is required");
                    code.requestFocus();
                }else if (Key.isEmpty()){

                    key.setError("Key is required");
                    key.requestFocus();
                } else{
                    //Validation
                  //  Toast.makeText(ConfirmCompanyKey.this, "Validating", Toast.LENGTH_SHORT).show();

                    ProgressDialog d = new ProgressDialog(ConfirmCompanyKey.this);
                    d.setMessage("Validating Key, Please wait...");
                    d.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    d.setCancelable(false);
                    d.show();
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(Code)){
                                if (Objects.equals(snapshot.child(Code).child("key").getValue(), Key)){
                                    Toast.makeText(ConfirmCompanyKey.this, "Key Validated Successfully ", Toast.LENGTH_SHORT).show();
                                  d.dismiss();
                                    Intent intent=new Intent(ConfirmCompanyKey.this,FinishCompanyRegistration.class);
                                    intent.putExtra("Code",Code);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    d.dismiss();
                                    AlertDialog errorD = new AlertDialog.Builder(ConfirmCompanyKey.this).create();
                                    errorD.setTitle("Error");
                                    errorD.setCancelable(false);
                                    errorD.setMessage("Provided Code Doesn't match with the provided key");
                                    errorD.setIcon(android.R.drawable.stat_sys_warning);
                                    errorD.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                }
                                            });
                                    errorD.show();
                                }

                            }else{
                                d.dismiss();
                                AlertDialog errorD = new AlertDialog.Builder(ConfirmCompanyKey.this).create();
                                errorD.setTitle("Error");
                                errorD.setCancelable(false);
                                errorD.setMessage("Provided Code Not Recognized");
                                errorD.setIcon(android.R.drawable.stat_sys_warning);
                                errorD.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });
                                errorD.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("DB error",error.toString());
                            Toast.makeText(ConfirmCompanyKey.this, "Database initialization Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

    }
}