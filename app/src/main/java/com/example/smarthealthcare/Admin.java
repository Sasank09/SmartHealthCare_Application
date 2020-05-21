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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class Admin extends AppCompatActivity {
    EditText AdminEmail,AdminPass;
    Button registr,login;

    FirebaseAuth auth;
    FirebaseDatabase db;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    String verifyCode;

    AuthCredential credential;
    private static int REQUEST_LOGIN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        AdminEmail = (EditText)findViewById(R.id.Admin_email);
        AdminPass = (EditText)findViewById(R.id.Admin_pass);

       // registr = (Button) findViewById(R.id.admin_register);

        login = (Button) findViewById(R.id.admin_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_LOGIN){
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            if (resultCode==RESULT_OK){
                if(!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()){
                    startActivity(new Intent(Admin.this,AdminHome.class));
                    finish();
                    return;
                }
                else if(idpResponse==null){
                    Toast.makeText(Admin.this,"Canclled",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private void mLogin() {
        if (AdminEmail.getText().toString().equals("")){
            Toast.makeText(Admin.this,"Please enter Email",Toast.LENGTH_LONG).show();
        }
        if (AdminPass.getText().toString().equals("")){
            Toast.makeText(Admin.this,"Please enter Password",Toast.LENGTH_LONG).show();
        }
        final android.app.AlertDialog wait = new SpotsDialog.Builder().setContext(Admin.this).build();
        wait.show();
        auth.signInWithEmailAndPassword(AdminEmail.getText().toString(),AdminPass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        wait.dismiss();
                        startActivity(new Intent(Admin.this,AdminHome.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                wait.dismiss();
                Toast.makeText(Admin.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showotpDialog() {

    }
}
