package com.example.attachmentapplicationportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdvertHistory extends AppCompatActivity {
    DatabaseReference reference,reference2;
    ProgressDialog progressDialog;
    RelativeLayout HistoryHome;
    TextView signuptitle;

    FloatingActionButton profile,logout;
    FloatingActionsMenu floatingActionsMenu;

    RecyclerView recview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_history);


        HistoryHome=findViewById(R.id.HistoryHome);
        signuptitle=findViewById(R.id.signuptitle);
        ProgressDialog dialog=new ProgressDialog(AdvertHistory.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Please Wait ...");
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
                recview.setLayoutManager(new LinearLayoutManager(AdvertHistory.this));


                myadapter2 adapter=new myadapter2(adverts);
                recview.setAdapter(adapter);
                signuptitle.setText(adapter.getItemCount()+" Posts");

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
    }
}