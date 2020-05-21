package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthealthcare.Model.PatientModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;

public class PatientCheckup extends AppCompatActivity{

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference patient;

    TextView patientName,patientEmail,patientPhone,patientpres,patientage,patientbp,patientheart,patientdescrip;

    PatientModel model;

    String outptString;

    public byte encryptkey[] = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    public Cipher cipher,dicipher;
    public SecretKeySpec secretKeySpec;

    long maxid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_checkup);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        patient = db.getReference("Patients");

        patientName = (TextView)findViewById(R.id.patient_name);
        patientEmail = (TextView)findViewById(R.id.patient_email);
        patientage = (TextView)findViewById(R.id.patient_age);
        patientbp = (TextView)findViewById(R.id.patient_bp1);
        patientheart = (TextView)findViewById(R.id.patient_heartbeat);
        patientPhone = (TextView)findViewById(R.id.patient_phone);
        patientdescrip = (TextView)findViewById(R.id.patient_descript);
        patientpres = (TextView)findViewById(R.id.patient_pres);


        try {
            cipher = Cipher.getInstance("AES");
            dicipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(encryptkey,"ECC");


        if(patient!=null){
            patient.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Log.d("hello","updated");
                        model = dataSnapshot.getValue(PatientModel.class);
                        patientName.setText(model.getName());
                        patientEmail.setText(model.getEmail());
                        patientage.setText(String.valueOf(model.getage()));
                        patientPhone.setText(model.getPhone());
                        patientbp.setText(String.valueOf(model.getbp()));
                        patientheart.setText(String.valueOf(model.getheartbeat()));
                        patientpres.setText(model.getprescription());

                        try {
                            if (model.getdescription()!=null)
                            outptString = decrypt(model.getdescription().toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        patientdescrip.setText(outptString);
                        // Log.d("err",outptString.toString());
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(PatientCheckup.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }


    }
    @Override
    protected void onStart() {
        super.onStart();


    }

private String encrypt(String string){
        byte[] stringbyte = string.getBytes();
        byte[] encryptbye = new byte[stringbyte.length];
    try {
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        encryptbye = cipher.doFinal(stringbyte);
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    String returnString = null;
    try {
        returnString = new String(encryptbye,"ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return returnString;

}
private String decrypt(String string) throws UnsupportedEncodingException {
        byte[] Encrypbyte = string.getBytes("ISO-8859-1");
        String decryted = string;

        byte[] decrytion;

    try {
        dicipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        decrytion= dicipher.doFinal(Encrypbyte);
        decryted=new String(decrytion);

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }

    return decryted;

}

}