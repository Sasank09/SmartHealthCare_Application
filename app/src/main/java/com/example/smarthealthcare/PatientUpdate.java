package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;

public class PatientUpdate extends AppCompatActivity {

    Button submit;

    TextView patientname;

    EditText patientage,patientbp,patientheart,patientdescrip;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference patient;

    PatientModel patientModel;

    long maxid=0;

    ArrayList<PatientModel> list;

    String outptString;
    PatientModel model;

    public byte encryptkey[] = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    public Cipher cipher,dicipher;
    public SecretKeySpec secretKeySpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_update);
        submit = (Button)findViewById(R.id.submit);

        patientname =(TextView)findViewById(R.id.patient_checkup_name);

        patientage = (EditText)findViewById(R.id.patient_age);
        patientbp = (EditText)findViewById(R.id.patient_bp);
        patientheart= (EditText)findViewById(R.id.patient_heart);
        patientdescrip = (EditText)findViewById(R.id.patient_desc);
        patientModel = new PatientModel();


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        patient = db.getReference("Patients");

        // patientname.setText(patientModel.getName());

        try {
            cipher = Cipher.getInstance("AES");
            dicipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(encryptkey,"AES");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(patient!=null)
            patient.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("hello", "updated");
                    list = new ArrayList<>();
                    model = dataSnapshot.getValue(PatientModel.class);
                    patientname.setText(model.getName());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PatientUpdate.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Update() throws Exception {
        if (patientage.getText().toString().equals("")){
            Toast.makeText(PatientUpdate.this,"Please enter age",Toast.LENGTH_LONG).show();
        }
        if (patientbp.getText().toString().equals("")){
            Toast.makeText(PatientUpdate.this,"Please enter bp",Toast.LENGTH_LONG).show();
        }
        if (patientheart.getText().toString().equals("")){
            Toast.makeText(PatientUpdate.this,"Please enter heart beat",Toast.LENGTH_LONG).show();
        }
        if (patientdescrip.getText().toString().equals("")){
            Toast.makeText(PatientUpdate.this,"Please enter description",Toast.LENGTH_LONG).show();
        }
        final android.app.AlertDialog wait = new SpotsDialog.Builder().setContext(PatientUpdate.this).build();
        wait.show();

        outptString = encrypt(patientdescrip.getText().toString());

        //PatientModel patientModel = new PatientModel();
        patientModel.setage(Integer.parseInt(patientage.getText().toString()));
        patientModel.setbp((patientbp.getText().toString()));
        patientModel.setheartbeat(Integer.parseInt(patientheart.getText().toString()));
        patientModel.setdescription(patientdescrip.getText().toString());
        patientModel.setprescription("doctor-offline");

        Map<String, Object> hopperUpdates = new HashMap<>();

        hopperUpdates.put("age", Integer.parseInt(patientage.getText().toString()));
        hopperUpdates.put("bp", (patientbp.getText().toString()));
        hopperUpdates.put("heartbeat", Integer.parseInt(patientheart.getText().toString()));
        hopperUpdates.put("description", outptString);
        hopperUpdates.put("prescription","doctor-offline");
        hopperUpdates.put("key",FirebaseAuth.getInstance().getCurrentUser().getUid());


        patient.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(hopperUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                wait.dismiss();
                Toast.makeText(PatientUpdate.this,"Submittted",Toast.LENGTH_LONG).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                wait.dismiss();
                Toast.makeText(PatientUpdate.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });





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
