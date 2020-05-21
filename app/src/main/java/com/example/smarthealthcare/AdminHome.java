package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {

    Button adminDoct,adminPat,adminlogout,adminAddDoct,adminAddPat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminDoct =(Button) findViewById(R.id.admin_doc_details);
        adminPat =(Button) findViewById(R.id.admin_pat_details);
        adminlogout =(Button) findViewById(R.id.admin_logout);
        adminAddPat=(Button)findViewById(R.id.admin_add_patient);
        adminAddDoct=(Button)findViewById(R.id.admin_add_doctor);

        adminDoct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,DoctorsList.class);
                startActivity(intent);

            }
        });

        adminPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,PatientsList.class);
                startActivity(intent);
            }
        });
        adminlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(AdminHome.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(AdminHome.this,MainActivity.class));
                                finish();
                            }
                        });
            }
        });
        adminAddDoct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,DoctorRegister.class);
                startActivity(intent);

            }
        });
        adminAddPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,PatientRegister.class);
                startActivity(intent);

            }
        });

    }
}
