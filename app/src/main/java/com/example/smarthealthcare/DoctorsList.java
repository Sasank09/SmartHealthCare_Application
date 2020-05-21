package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.smarthealthcare.Model.DoctorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorsList extends AppCompatActivity implements DoctorAdapter.OnDoctortListener{


    FirebaseDatabase db;
    DatabaseReference doctor;

    DoctorAdapter doctorAdapter;
    ArrayList<DoctorModel> list;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);
        db = FirebaseDatabase.getInstance();
        doctor = db.getReference("Doctors");

        recyclerView = findViewById(R.id.doct_rv);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(doctor!=null){
            doctor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Log.d("hello","updated");
                        list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(DoctorModel.class));
                        }
                        doctorAdapter = new DoctorAdapter(list,DoctorsList.this);
                        recyclerView.setAdapter(doctorAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(DoctorsList.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }

  

    @Override
    public void OnDoctorClick(DoctorModel doctorModel) {
        Toast.makeText(DoctorsList.this,doctorModel.getName(),Toast.LENGTH_LONG).show();

    }
}
