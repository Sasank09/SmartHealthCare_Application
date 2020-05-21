package com.example.smarthealthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthcare.Model.DoctorModel;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {


    ArrayList<DoctorModel> list;

    OnDoctortListener onDoctortListener;

    public DoctorAdapter(ArrayList<DoctorModel> list, OnDoctortListener onDoctortListener) {
        this.list = list;
        this.onDoctortListener = onDoctortListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_card,parent,false);

        return new DoctorAdapter.MyViewHolder(view, onDoctortListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.MyViewHolder holder, int position) {
        holder.doctorname.setText(list.get(position).getName());
        holder.doctoremail.setText(list.get(position).getEmail());
        holder.doctorphone.setText(list.get(position).getPhone());
        holder.doctoraddress.setText(list.get(position).getAddress());
        holder.doctorquali.setText(list.get(position).getQualification());
        holder.doctorspec.setText(list.get(position).getSpeciality());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView doctorname;
        TextView doctoremail;
        TextView doctorphone;
        TextView doctoraddress;
        TextView doctorquali;
        TextView doctorspec;

        OnDoctortListener onDoctortListener;
        
        public MyViewHolder(@NonNull View itemView, OnDoctortListener onDoctortListener) {
            super(itemView);
            doctorname =itemView.findViewById(R.id.doctor_name);
            doctoremail =itemView.findViewById(R.id.doctor_email);
            doctorphone =itemView.findViewById(R.id.doctor_phone);
            doctoraddress =itemView.findViewById(R.id.doctor_address);
            doctorquali =itemView.findViewById(R.id.doctor_qualification);
            doctorspec =itemView.findViewById(R.id.doctor_specail);

            this.onDoctortListener = onDoctortListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDoctortListener.OnDoctorClick(list.get(getAdapterPosition()));

        }
    }
    public interface OnDoctortListener{
        void OnDoctorClick(DoctorModel doctorModel);
    }
}
