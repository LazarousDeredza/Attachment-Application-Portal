package com.example.attachmentapplicationportal;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class myadapter2 extends RecyclerView.Adapter<myadapter2.myviewholder>
{
    List<JobAdvertsClass> adverts;

    public myadapter2(List<JobAdvertsClass> adverts) {
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

          holder.delbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  // this is to delete the record from room database
                 // userDao.deleteById(users.get(position).getUid());
                  // this is to delete the record from Array List which is the source of recview data
                //  users.remove(position);

                  Toast.makeText(view.getContext(), String.valueOf(adverts.get(position).jobTittle), Toast.LENGTH_SHORT).show();

                  //update the fresh list of ArrayList data to recview
                  notifyDataSetChanged();
              }
          });
        holder.delbtn.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
       {
           TextView recid,recfname, reclname,startDate,endDate,email,phone;
           ImageButton delbtn;
           public myviewholder(@NonNull @NotNull View itemView) {
               super(itemView);

               recid=itemView.findViewById(R.id.recid);
               recfname=itemView.findViewById(R.id.recfname);
               reclname=itemView.findViewById(R.id.reclname);
               startDate=itemView.findViewById(R.id.startDate);
               endDate=itemView.findViewById(R.id.endDate);
               phone=itemView.findViewById(R.id.phone);
               email=itemView.findViewById(R.id.email);


               delbtn=itemView.findViewById(R.id.btnApply);
           }
       }
}
