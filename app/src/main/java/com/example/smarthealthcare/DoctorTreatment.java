package com.example.smarthealthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthealthcare.Model.PatientModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;

public class DoctorTreatment extends AppCompatActivity implements DoctorPatientInterAction.OnDoctorPatientListener{

    TextView patientName,patientEmail,patientPhone,patientpres,patientage,patientbp,patientheart,patientdescrip;

    Button doctortremButton;

    EditText editTextPress;

    Dialog dialog;

    ImageView dimg;

    String outptString;

    FirebaseDatabase db;
    DatabaseReference patient;

    DatabaseReference mg;

    DoctorPatientInterAction doctorPatientInterAction;
    ArrayList<PatientModel> list;
    ArrayList<PatientModel> mlist;
    RecyclerView recyclerView;


    public byte encryptkey[] = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    public Cipher cipher,dicipher;
    public SecretKeySpec secretKeySpec;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_treatment);
        db = FirebaseDatabase.getInstance();

        dialog = new Dialog(this);

        patient = db.getReference("Patients");

        recyclerView = findViewById(R.id.docpatrv);

        try {
            cipher = Cipher.getInstance("AES");
            dicipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(encryptkey,"ECC");


    }
    @Override
    protected void onStart() {
        super.onStart();
        if(patient!=null){
            patient.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Log.d("hello","updated");
                        list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                             //list.add(ds.getValue( PatientModel.class));
                            // mlist.add(ds.getKey());
                            if (ds.getValue(PatientModel.class).getprescription()!=null &&
                                    ds.getValue(PatientModel.class).getprescription().equals("doctor-offline")){
                                list.add(ds.getValue( PatientModel.class));
                            }
                            else{
                                Toast.makeText(DoctorTreatment.this,"No Records!!",Toast.LENGTH_LONG).show();
                            }

                        }
                       /* for(PatientModel patientModel:list){
                            if(!patientModel.getprescription().equals("doctor-offline") ){
                                Log.d("hello","updated");
                                mlist.add(patientModel);
                            }
                        }*/


                        doctorPatientInterAction = new DoctorPatientInterAction(list,DoctorTreatment.this);
                        recyclerView.setAdapter(doctorPatientInterAction);
                    }
                    else{
                        Toast.makeText(DoctorTreatment.this,"No Records!!",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(DoctorTreatment.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }


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



    @Override
    public void OnDoctorPatientClick(final PatientModel patientModel) {
        Toast.makeText(DoctorTreatment.this,patientModel.getName(),Toast.LENGTH_LONG).show();
        String name = patientModel.getName();
        String phone = patientModel.getPhone();
        String bp = String.valueOf(patientModel.getbp());
        String age = String.valueOf(patientModel.getage());
        String heart = String.valueOf(patientModel.getheartbeat());
        final String key = patientModel.getKey();



        try {
            if (patientModel.getdescription()!=null)
                outptString = decrypt(patientModel.getdescription().toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        dialog.setContentView(R.layout.popup);

        dimg =(ImageView)dialog.findViewById(R.id.diaclose);
        patientName = (TextView)dialog.findViewById(R.id.dianame);
        patientPhone = (TextView)dialog.findViewById(R.id.diaphone);
        patientage = (TextView)dialog.findViewById(R.id.daiage);
        patientbp = (TextView)dialog.findViewById(R.id.diabp);
        patientheart = (TextView)dialog.findViewById(R.id.diaheartbeat);
        patientdescrip = (TextView)dialog.findViewById(R.id.diades);
        editTextPress = (EditText)dialog.findViewById(R.id.presction);

        doctortremButton =(Button)dialog.findViewById(R.id.diabuttton);

        patientName.setText("Name:"+name);
        patientPhone.setText("Phone:"+phone);
        patientage.setText("Age:"+age);
        patientbp.setText("BP:"+bp);
        patientheart.setText("Heart Beat:"+heart);
        patientdescrip.setText("Description:"+outptString);

        final Map<String, Object> hopperUpdates = new HashMap<>();

       // hopperUpdates.put("prescription","doctor-offline");

        dimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        doctortremButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPress.getText().toString().equals("")) {
                    Toast.makeText(DoctorTreatment.this, "Please enter prescription", Toast.LENGTH_LONG).show();
                }
                hopperUpdates.put("prescription", editTextPress.getText().toString());
                patient.child(key)
                        .updateChildren(hopperUpdates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DoctorTreatment.this,"Submittted",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                //deleteRc();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DoctorTreatment.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        dialog.getWindow();
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.));
        dialog.show();

    }

    private void deleteRc() {

        ArrayList<PatientModel> mlst = new ArrayList<>();
        for(PatientModel obj:list){
            if(!obj.getprescription().equals("doctor-offline") ){
                Log.d("hello","updated");
                mlst.add(obj);
            }
        }
        doctorPatientInterAction = new DoctorPatientInterAction(mlst,DoctorTreatment.this);
        recyclerView.setAdapter(doctorPatientInterAction);

    }


    /*@Override
    public void OnPatientClick(final PatientModel patientModel) {

        String name = patientModel.getName();
        String phone = patientModel.getName();
        String bp = String.valueOf(patientModel.getbp());
        String age = String.valueOf(patientModel.getage());
        String heart = String.valueOf(patientModel.getheartbeat());

        try {
            if (patientModel.getdescription()!=null)
                outptString = decrypt(patientModel.getdescription().toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        dialog.setContentView(R.layout.popup);

        dimg =(ImageView)dialog.findViewById(R.id.diaclose);
        patientName = (TextView)dialog.findViewById(R.id.dianame);
        patientPhone = (TextView)dialog.findViewById(R.id.diaphone);
        patientage = (TextView)dialog.findViewById(R.id.daiage);
        patientbp = (TextView)dialog.findViewById(R.id.diabp);
        patientheart = (TextView)dialog.findViewById(R.id.diaheartbeat);
        patientdescrip = (TextView)dialog.findViewById(R.id.diades);
        editTextPress = (EditText)dialog.findViewById(R.id.presction);

        doctortremButton =(Button)dialog.findViewById(R.id.diabuttton);

        patientName.setText(name);
        patientPhone.setText(phone);
        patientage.setText(age);
        patientbp.setText(bp);
        patientheart.setText(heart);
        patientdescrip.setText(outptString);

        dimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        doctortremButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPress.getText().toString().equals("")){
                    Toast.makeText(DoctorTreatment.this,"Please enter prescription",Toast.LENGTH_LONG).show();
                }
                patientModel.setprescription(editTextPress.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.getWindow();
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.));
        dialog.show();

    }*/
}
