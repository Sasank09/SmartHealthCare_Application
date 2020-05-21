package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smarthealthcare.Model.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class PatientRegister extends AppCompatActivity {

    EditText patientName,patientEmail,patientPhone,patientPass,patientConfPass,patientOTP,patientAddress;
    Button registr,sendotp,verify;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference patient;
    long maxid=0;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    String verifyCode;

    AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        patient = db.getReference("Patients");

//       credential = PhoneAuthProvider.getCredential(patientPhone.getText().toString(),auth.getCurrentUser().getProviderId());
        patient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid=dataSnapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientRegister.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        patientName = (EditText)findViewById(R.id.patient_name);
        patientEmail = (EditText)findViewById(R.id.patient_email);
        patientPhone = (EditText)findViewById(R.id.patient_phone);
        patientPass = (EditText)findViewById(R.id.patient_pass);
        patientConfPass = (EditText)findViewById(R.id.patient_con_pass);
       // patientOTP = (EditText)findViewById(R.id.patient_otp);
        patientAddress = (EditText)findViewById(R.id.patient_address);

        registr = (Button) findViewById(R.id.register);
        //sendotp = (Button) findViewById(R.id.send_otp);
        //verify = (Button) findViewById(R.id.verify_otp);

        registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reg();
            }
        });

        mcallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signwithphone(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verifyCode=s;
                Toast.makeText(PatientRegister.this,"OTP sent",Toast.LENGTH_LONG).show();
            }
        };

    }

    public void verifyotp(){
        //Toast.makeText(AdminRegister.this,verifyCode,Toast.LENGTH_LONG).show();
        if(verifyCode!=null){
            verifyphone(patientOTP.getText().toString(),verifyCode);
            //Toast.makeText(AdminRegister.this,"verified succesfully",Toast.LENGTH_LONG).show();

        }
    }

    private void verifyphone(String toString, String verifyCode) {
        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(verifyCode,toString);
        signwithphone(credential);
    }

    public void sendSMS(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+patientPhone.getText().toString(),
                60, TimeUnit.SECONDS,this,
                mcallbacks
        );
    }
    public void signwithphone(PhoneAuthCredential credential){
        auth.getCurrentUser().linkWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(PatientRegister.this,"verified succesfully",Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PatientRegister.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void Reg() {
        if (patientName.getText().toString().equals("")){
            Toast.makeText(PatientRegister.this,"Please enter name",Toast.LENGTH_LONG).show();
        }
        if (patientEmail.getText().toString().equals("")){
            Toast.makeText(PatientRegister.this,"Please enter Email",Toast.LENGTH_LONG).show();
        }
        if (patientPhone.getText().toString().equals("")){
            Toast.makeText(PatientRegister.this,"Please enter Phone",Toast.LENGTH_LONG).show();
        }
        if (patientPass.getText().toString().equals("")){
            Toast.makeText(PatientRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();
        }
        if (!patientConfPass.getText().toString().trim().equals(patientPass.getText().toString().trim())){
            Toast.makeText(PatientRegister.this,"Please enter same password",Toast.LENGTH_LONG).show();
            patientConfPass.setError("password must be same");
        }
        if (patientAddress.getText().toString().equals("")){
            Toast.makeText(PatientRegister.this,"Please enter Address",Toast.LENGTH_LONG).show();
        }

        final android.app.AlertDialog wait = new SpotsDialog.Builder().setContext(PatientRegister.this).build();
        wait.show();
        credential = PhoneAuthProvider.getCredential(patientPhone.getText().toString(),auth.getCurrentUser().getProviderId());

        auth.createUserWithEmailAndPassword(patientEmail.getText().toString(), patientPass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        PatientModel patientModel = new PatientModel();
                        patientModel.setName(patientName.getText().toString());
                        patientModel.setEmail(patientEmail.getText().toString());
                        patientModel.setPhone(patientPhone.getText().toString());
                        patientModel.setPassword(patientPass.getText().toString());
                        patientModel.setAddress(patientAddress.getText().toString());


                        patient.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(patientModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        wait.dismiss();
                                        //Toast.makeText(PatientRegister.this,"Registered",Toast.LENGTH_LONG).show();
                                        //finish();
                                        sendSMS();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                wait.dismiss();
                                Toast.makeText(PatientRegister.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientRegister.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


}
