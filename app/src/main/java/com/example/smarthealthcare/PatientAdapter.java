package com.example.smarthealthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthcare.Model.PatientModel;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> {

    ArrayList<PatientModel> list;
    OnPatientListener onPatientListener;

    public PatientAdapter(ArrayList<PatientModel> list, OnPatientListener onPatientListener) {
        this.list = list;
        this.onPatientListener=onPatientListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card,parent,false);

        return new MyViewHolder(view, onPatientListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.MyViewHolder holder, int position) {
        holder.patientname.setText(list.get(position).getName());
        holder.patientemail.setText(list.get(position).getEmail());
        holder.patientphone.setText(list.get(position).getPhone());
        holder.patientage.setText(String.valueOf(list.get(position).getage()));
        holder.patientbp.setText(String.valueOf(list.get(position).getbp()));
        holder.patientheart.setText(String.valueOf(list.get(position).getheartbeat()));
        holder.patientdesc.setText(list.get(position).getdescription());
        holder.patientprec.setText(list.get(position).getprescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView patientname;
        TextView patientemail;
        TextView patientphone;
        TextView patientage;
        TextView patientbp;
        TextView patientheart;
        TextView patientdesc;
        TextView patientprec;

        OnPatientListener onPatientListener;

        public MyViewHolder(@NonNull View itemView, OnPatientListener onPatientListener) {
            super(itemView);
            patientname =itemView.findViewById(R.id.patient_name);
            patientemail =itemView.findViewById(R.id.patient_email);
            patientphone =itemView.findViewById(R.id.patient_phone);
            patientage =itemView.findViewById(R.id.patient_age);
            patientbp =itemView.findViewById(R.id.patient_bp);
            patientheart =itemView.findViewById(R.id.patient_heart);
            patientdesc =itemView.findViewById(R.id.patient_desc);
            patientprec =itemView.findViewById(R.id.patient_prescri);

            this.onPatientListener = onPatientListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPatientListener.OnPatientClick(list.get(getAdapterPosition()));

        }
    }

    public interface OnPatientListener{
        void OnPatientClick(PatientModel patientModel);
    }
}