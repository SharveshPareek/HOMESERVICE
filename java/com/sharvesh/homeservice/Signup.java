package com.sharvesh.homeservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sharvesh.homeservice.Modal.UserDB;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {


    EditText name, email, pass, cpass, address, pincode, phno;
    Button register;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference signRef;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Window status = this.getWindow();
        status.setStatusBarColor(ContextCompat.getColor(this, R.color.red));

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.con_pass);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        phno = findViewById(R.id.phNo);
        register = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        signRef = database.getReference("Users");

        progressDialog = new ProgressDialog(this,R.style.ProgressDialog);
        progressDialog.setMessage("Please wait a moment!");
        progressDialog.setCancelable(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameST = name.getText().toString();
                String emailST = email.getText().toString();
                String passST = pass.getText().toString();
                String cpassST = cpass.getText().toString();
                String addressST = address.getText().toString();
                String pincodeST = pincode.getText().toString();
                String phnoST = phno.getText().toString();

                if (nameST.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (emailST.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Email ID", Toast.LENGTH_SHORT).show();
                }else if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",emailST)){
                    Toast.makeText(Signup.this, "Enter Valid Email-ID", Toast.LENGTH_SHORT).show();
                } else if (passST.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (passST.length() < 6) {
                    Toast.makeText(Signup.this, "Password must be atleast 6 charcter", Toast.LENGTH_SHORT).show();
                } else if (cpassST.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!passST.equals(cpassST)) {
                    Toast.makeText(Signup.this, "Enter Confirm Password as same as Password", Toast.LENGTH_SHORT).show();
                } else if (addressST.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Address", Toast.LENGTH_SHORT).show();
                } else if (pincodeST.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                } else if (pincodeST.length() < 6) {
                    Toast.makeText(Signup.this, "Enter Valid Pincode", Toast.LENGTH_SHORT).show();
                } else if (phnoST.isEmpty()) {
                    Toast.makeText(Signup.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                } else if (phnoST.length() < 10) {
                    Toast.makeText(Signup.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                } else{
                    progressDialog.show();
                    HashMap<String ,Object> addressMap = new HashMap<>();
                    addressMap.put("address",addressST);
                    addressMap.put("pincode",pincodeST);
                    addressMap.put("phno",phnoST);
                    addressMap.put("name",nameST);
                    auth.createUserWithEmailAndPassword(emailST,passST).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                String uid = auth.getUid();
                                signRef.child(uid).child("name").setValue(nameST);
                                signRef.child(uid).child("email").setValue(emailST);
                                signRef.child(uid).child("pass").setValue(passST);
                                signRef.child(uid).child("phno").setValue(phnoST);
                                signRef.child(uid).child("address").push().setValue(addressMap);
                                Toast.makeText(Signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent Home = new Intent(Signup.this, HomePage.class);
                                startActivity(Home);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Signup.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}