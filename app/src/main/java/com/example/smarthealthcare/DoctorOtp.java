package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthealthcare.Model.DoctorModel;
import com.example.smarthealthcare.Model.PatientModel;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import in.aabhasjindal.otptextview.OtpTextView;

public class DoctorOtp extends AppCompatActivity {

    TextView vc;

    int randnum;

    DoctorModel model;

    String verifyCode;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference doctor;

    Button dotp,submit;

    String phoneno,message1;

    OtpTextView otpTextView;

    android.app.AlertDialog wait;

    //String apiKey = "apikey=" + "mmpDukg16BQ-NMOzLQU7mOlog5wAdFaiKkPu9A34Jc";
    String message;
    //String sender = "&sender=" + "TXTLCL";
    String numbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);

        otpTextView = (OtpTextView)findViewById(R.id.otp_view);

        vc = (TextView)findViewById(R.id.vcno);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        doctor = db.getReference("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        dotp = (Button)findViewById(R.id.button) ;
        submit = (Button)findViewById(R.id.button2) ;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendCampaign(apiKey,secretKey,useType,phoneno,message,senderId);
                sendSms(numbers,message);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wait = new SpotsDialog.Builder().setContext(DoctorOtp.this).build();
                wait.show();
                verifyCode =otpTextView.getOTP().toString();
                verifyotp();

            }
        });

    }


    private void verifyotp() {
        if (verifyCode.equals(String.valueOf(randnum))){
            wait.dismiss();
            startActivity(new Intent(DoctorOtp.this,DoctorHone.class));
            finish();
        }else {
            wait.dismiss();
            Toast.makeText(DoctorOtp.this,"Error",Toast.LENGTH_LONG).show();
        }
    }
    public void sendSms(String numbers,String message) {

        //sbPostData.append("1=1");
//final string
        /*mainUrl = sbPostData.toString();
        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.d("RESPONSE", ""+response);

            //finally close connection
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }*/
        try {
            // Construct data
            String apiKey = "apikey=" + "mmpDukg16BQ-NMOzLQU7mOlog5wAdFaiKkPu9A34Jc";
            //String message = "&message=" + "haii";
            String sender = "&sender=" + "TXTLCL";
            //String numbers = "&numbers=" + "7989124286";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            Toast.makeText(DoctorOtp.this,"OTP sent!!"+stringBuffer,Toast.LENGTH_LONG).show();

            //return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            Toast.makeText(DoctorOtp.this,"Error SMS " + e.getMessage(),Toast.LENGTH_LONG).show();
            //return "Error "+e;
        }
    }

    @Override
    protected void onStart() {
        Random random = new Random();

        randnum = random.nextInt(999999);

        message = "&message=" +"Your OTP is "+randnum+" Please dont share your otp to anyone";

        super.onStart();
        doctor.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        model = dataSnapshot.getValue(DoctorModel.class);
                        phoneno=model.getPhone().toString();
                        vc.setText("Please type the verification code send to "+phoneno);
                        numbers = "&numbers=" + phoneno;
                        sendSms(numbers,message);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DoctorOtp.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });
        //sendCampaign(apiKey,secretKey,useType,phoneno,message,senderId)

    }

}
