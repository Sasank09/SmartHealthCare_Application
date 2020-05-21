package com.example.smarthealthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorHone extends AppCompatActivity {



    Button doctortreatment,doctorLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_hone);
        doctortreatment = (Button)findViewById(R.id.doctor_treatment);
        doctorLogout = (Button)findViewById(R.id.doctor_logout);

        doctortreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorHone.this,DoctorTreatment.class);
                startActivity(intent);

            }
        });

        doctorLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(DoctorHone.this,"logout successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
