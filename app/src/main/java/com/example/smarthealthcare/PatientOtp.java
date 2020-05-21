package com.example.smarthealthcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthealthcare.Model.PatientModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import dmax.dialog.SpotsDialog;
import in.aabhasjindal.otptextview.OtpTextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class PatientOtp extends AppCompatActivity {

    TextView vc;

    int randnum;

    PatientModel model;

    String verifyCode;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference patient;

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
        patient = db.getReference("Patients");




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
                wait = new SpotsDialog.Builder().setContext(PatientOtp.this).build();
                wait.show();
                verifyCode =otpTextView.getOTP().toString();
                verifyotp();

            }
        });

    }


    private void verifyotp() {
        if (verifyCode.equals(String.valueOf(randnum))){
            wait.dismiss();
            startActivity(new Intent(PatientOtp.this,PatientHome.class));
            finish();
        }else {
            wait.dismiss();
            Toast.makeText(PatientOtp.this,"Error",Toast.LENGTH_LONG).show();
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
            Toast.makeText(PatientOtp.this,"OTP sent!!"+stringBuffer,Toast.LENGTH_LONG).show();

            //return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            Toast.makeText(PatientOtp.this,"Error SMS " + e.getMessage(),Toast.LENGTH_LONG).show();
            //return "Error "+e;
        }
    }

    @Override
    protected void onStart() {
        Random random = new Random();

        randnum = random.nextInt(999999);

        message = "&message=" +"Your OTP is"+randnum+"Please dont share your otp to anyone";

        super.onStart();
        patient.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        model = dataSnapshot.getValue(PatientModel.class);
                        phoneno=model.getPhone().toString();
                        vc.setText("Please type the verification code send to "+phoneno);
                        numbers = "&numbers=" + phoneno;
                        sendSms(numbers,message);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(PatientOtp.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });
        //sendCampaign(apiKey,secretKey,useType,phoneno,message,senderId)

    }

}
