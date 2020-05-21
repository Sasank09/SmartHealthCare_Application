package com.example.smarthealthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthcare.Model.PatientModel;

import java.util.ArrayList;

public class DoctorPatientInterAction extends RecyclerView.Adapter<DoctorPatientInterAction.MyViewHolder> {

    ArrayList<PatientModel> list;
   OnDoctorPatientListener onDoctorPatientListener;

    public DoctorPatientInterAction(ArrayList<PatientModel> list, OnDoctorPatientListener onDoctorPatientListener) {
        this.list = list;
        this.onDoctorPatientListener = onDoctorPatientListener;
    }

    @NonNull
    @Override
    public DoctorPatientInterAction.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_patient,parent,false);

        return new MyViewHolder(view, onDoctorPatientListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorPatientInterAction.MyViewHolder holder, int position) {

        holder.patientname.setText(list.get(position).getName());
        holder.patientemail.setText(list.get(position).getEmail());
        holder.patientphone.setText(list.get(position).getPhone());
        holder.patientage.setText(String.valueOf(list.get(position).getage()));
        holder.patientbp.setText(String.valueOf(list.get(position).getbp()));
        holder.patientheart.setText(String.valueOf(list.get(position).getheartbeat()));
        holder.patientkey.setText(list.get(position).getKey());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView patientname;
        TextView patientemail;
        TextView patientphone;
        TextView patientage;
        TextView patientbp;
        TextView patientheart;
        TextView patientkey;


        OnDoctorPatientListener onDoctorPatientListener;

        public MyViewHolder(@NonNull View itemView, OnDoctorPatientListener onDoctorPatientListener) {
            super(itemView);
            patientname =itemView.findViewById(R.id.patient_name);
            patientemail =itemView.findViewById(R.id.patient_email);
            patientphone =itemView.findViewById(R.id.patient_phone);
            patientage =itemView.findViewById(R.id.patient_age);
            patientbp =itemView.findViewById(R.id.patient_bp);
            patientheart =itemView.findViewById(R.id.patient_heart);
            patientkey =itemView.findViewById(R.id.patient_key);

            this.onDoctorPatientListener=onDoctorPatientListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onDoctorPatientListener.OnDoctorPatientClick(list.get(getAdapterPosition()));

        }
    }
    public interface OnDoctorPatientListener{
        void OnDoctorPatientClick(PatientModel patientModel);
    }
}
