package com.example.smarthealthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PatientHome extends AppCompatActivity {

    Button patientUpdate,patientCheckup,patientLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        patientUpdate = (Button)findViewById(R.id.patient_update);
        patientCheckup = (Button)findViewById(R.id.patient_checkup);
        patientLogout = (Button)findViewById(R.id.patient_logout);

        patientUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHome.this,PatientUpdate.class);
                startActivity(intent);

            }
        });

        patientCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHome.this,PatientCheckup.class);
                startActivity(intent);
            }
        });

        patientLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PatientHome.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(PatientHome.this,"logout successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
