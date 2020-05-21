package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import in.aabhasjindal.otptextview.OtpTextView;

public class Doctor extends AppCompatActivity {

    EditText doctorEmail,doctorPass,doctorPhone;
    Button registr,login;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference doctor;

    Dialog dialog;

    ConstraintLayout m_layou;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    String verifyCode;

    AuthCredential credential;

    OtpTextView otpTextView;

    Button resend,submit;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        //doctor = db.getReference("Doctor");

        m_layou = findViewById(R.id.otplayout);

        dialog = new Dialog(this);

        doctorPhone = (EditText) findViewById(R.id.doctor_phone);

        doctorEmail = (EditText) findViewById(R.id.doctor_email);
        doctorPass = (EditText) findViewById(R.id.doctor_pass);

        login = (Button) findViewById(R.id.doctor_login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin();
            }
        });

    }


    private void mLogin() {
        if (doctorEmail.getText().toString().equals("")){
            Toast.makeText(Doctor.this,"Please enter Email",Toast.LENGTH_LONG).show();
            return;
        }
        if (doctorPass.getText().toString().equals("")){
            Toast.makeText(Doctor.this,"Please enter Password",Toast.LENGTH_LONG).show();
            return;
        }
        final android.app.AlertDialog wait = new SpotsDialog.Builder().setContext(Doctor.this).build();
        wait.show();
        auth.signInWithEmailAndPassword(doctorEmail.getText().toString(),doctorPass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        wait.dismiss();
                        startActivity(new Intent(Doctor.this,DoctorOtp.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                wait.dismiss();
                Toast.makeText(Doctor.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
