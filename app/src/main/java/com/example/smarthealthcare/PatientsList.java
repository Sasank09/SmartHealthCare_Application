package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.smarthealthcare.Model.PatientModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientsList extends AppCompatActivity  implements PatientAdapter.OnPatientListener{

    FirebaseDatabase db;
    DatabaseReference patient;

    PatientAdapter patientAdapter;
    ArrayList<PatientModel> list;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_list);

        db = FirebaseDatabase.getInstance();
        patient = db.getReference("Patients");

        recyclerView = findViewById(R.id.rv);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(patient!=null){
            patient.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Log.d("hello","updated");
                        list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(PatientModel.class));
                        }
                        patientAdapter = new PatientAdapter(list,PatientsList.this);
                        recyclerView.setAdapter(patientAdapter);
                    }
                    else{
                        Toast.makeText(PatientsList.this,"No Records!!",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(PatientsList.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void OnPatientClick(PatientModel patientModel) {
Toast.makeText(PatientsList.this,patientModel.getName(),Toast.LENGTH_LONG).show();
    }
}
