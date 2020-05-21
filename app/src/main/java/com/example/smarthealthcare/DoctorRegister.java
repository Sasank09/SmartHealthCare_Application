package com.example.smarthealthcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthealthcare.Model.DoctorModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class DoctorRegister extends AppCompatActivity {

    EditText doctorName,doctorEmail,doctorPhone,doctorPass,doctorConfPass,doctorOTP,doctorAddress,doctorQuali,doctorSpec;
    Button registr,sendotp,verify;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference doctor;

    long maxid=0;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        doctor = db.getReference("Doctors");


        doctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid=dataSnapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorRegister.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        doctorName = (EditText)findViewById(R.id.doctor_name);
        doctorEmail = (EditText)findViewById(R.id.doctor_email);
        doctorPhone = (EditText)findViewById(R.id.doctor_phone);
        doctorPass = (EditText)findViewById(R.id.doctor_pass);
        doctorConfPass = (EditText)findViewById(R.id.doctor_con_pass);
       // doctorOTP = (EditText)findViewById(R.id.doctor_otp);
        doctorAddress = (EditText)findViewById(R.id.doctor_address);
        doctorQuali = (EditText)findViewById(R.id.doctor_quali);
        doctorSpec = (EditText)findViewById(R.id.doctor_specilist);

        registr = (Button) findViewById(R.id.register);
       // sendotp = (Button) findViewById(R.id.send_otp);
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

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verifyCode=s;
                Toast.makeText(DoctorRegister.this,"OTP sent",Toast.LENGTH_LONG).show();
            }
        };
    }





    public void verifyotp(){
        //Toast.makeText(AdminRegister.this,verifyCode,Toast.LENGTH_LONG).show();
        if(verifyCode!=null){
            verifyphone(doctorOTP.getText().toString(),verifyCode);
            //Toast.makeText(AdminRegister.this,"verified succesfully",Toast.LENGTH_LONG).show();

        }
    }

    private void verifyphone(String toString, String verifyCode) {
        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(verifyCode,toString);
        signwithphone(credential);
    }

    public void sendSMS(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                doctorPhone.getText().toString(),
                60, TimeUnit.SECONDS,this,
                mcallbacks
        );
    }
    public void signwithphone(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(DoctorRegister.this,"verified succesfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DoctorRegister.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void otp() {
        }

        private void Reg() {
            if (doctorName.getText().toString().equals("")){
                Toast.makeText(DoctorRegister.this,"Please enter name",Toast.LENGTH_LONG).show();
            }
            if (doctorEmail.getText().toString().equals("")){
                Toast.makeText(DoctorRegister.this,"Please enter Email",Toast.LENGTH_LONG).show();
            }
            if (doctorPhone.getText().toString().equals("")){
                Toast.makeText(DoctorRegister.this,"Please enter Phone",Toast.LENGTH_LONG).show();
            }
            if (doctorPass.getText().toString().equals("")){
                Toast.makeText(DoctorRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();
            }
            if (!doctorConfPass.getText().toString().trim().equals(doctorPass.getText().toString().trim())){
                Toast.makeText(DoctorRegister.this,"Please enter same password",Toast.LENGTH_LONG).show();
            }
            if (doctorAddress.getText().toString().equals("")){
                Toast.makeText(DoctorRegister.this,"Please enter Address",Toast.LENGTH_LONG).show();
            }
            if (doctorQuali.getText().toString().equals("")){
                Toast.makeText(DoctorRegister.this,"Please enter Qualification",Toast.LENGTH_LONG).show();
            }
            if (doctorSpec.getText().toString().equals("")){
                Toast.makeText(DoctorRegister.this,"Please enter Specality",Toast.LENGTH_LONG).show();
            }

            final android.app.AlertDialog wait = new SpotsDialog.Builder().setContext(DoctorRegister.this).build();
            wait.show();

            auth.createUserWithEmailAndPassword(doctorEmail.getText().toString(), doctorPass.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            DoctorModel doctorModel = new DoctorModel();
                            doctorModel.setName(doctorName.getText().toString());
                            doctorModel.setEmail(doctorEmail.getText().toString());
                            doctorModel.setPhone(doctorPhone.getText().toString());
                            doctorModel.setPassword(doctorPass.getText().toString());
                            doctorModel.setAddress(doctorAddress.getText().toString());
                            doctorModel.setQualification(doctorQuali.getText().toString());
                            doctorModel.setSpeciality(doctorSpec.getText().toString());

                            doctor.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(doctorModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            wait.dismiss();
                                            Toast.makeText(DoctorRegister.this,"Registered",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    wait.dismiss();
                                    Toast.makeText(DoctorRegister.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    wait.dismiss();
                    Toast.makeText(DoctorRegister.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
