package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class Patient extends AppCompatActivity {

    EditText patientEmail,patientPass;
    Button registr,login;

    FirebaseAuth auth;
    FirebaseDatabase db;

    private static int REQUEST_LOGIN = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
//        patient = db.getReference("patient");

        patientEmail = (EditText)findViewById(R.id.patient_email);
        patientPass = (EditText)findViewById(R.id.patient_pass);

        login = (Button) findViewById(R.id.patient_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin();
            }
        });

    }


    private void mLogin() {
        if (patientEmail.getText().toString().equals("")){
            Toast.makeText(Patient.this,"Please enter Email",Toast.LENGTH_LONG).show();
        }
        if (patientPass.getText().toString().equals("")){
            Toast.makeText(Patient.this,"Please enter Password",Toast.LENGTH_LONG).show();
        }
        final android.app.AlertDialog wait = new SpotsDialog.Builder().setContext(Patient.this).build();
        wait.show();
        auth.signInWithEmailAndPassword(patientEmail.getText().toString(),patientPass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        wait.dismiss();
                        startActivity(new Intent(Patient.this,PatientOtp.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                wait.dismiss();
                Toast.makeText(Patient.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
