package com.sharvesh.homeservice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    EditText email, pass;
    Button login;
    TextView signup, forgot_pass;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(this,R.style.ProgressDialog);
        progressDialog.setMessage("Please wait a moment!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null){
            progressDialog.dismiss();
            Intent home = new Intent(Login.this, HomePage.class);
            startActivity(home);
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window status = this.getWindow();
        status.setStatusBarColor(ContextCompat.getColor(this, R.color.red));

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        forgot_pass = findViewById(R.id.forgot_pass);

        progressDialog = new ProgressDialog(this,R.style.ProgressDialog);
        progressDialog.setMessage("Please wait a moment!");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, Signup.class);
                startActivity(register);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailST = email.getText().toString();
                String passST = pass.getText().toString();

                if (emailST.isEmpty()) {
                    Toast.makeText(Login.this, "Enter Email-ID", Toast.LENGTH_SHORT).show();
                } else if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", emailST)) {
                    Toast.makeText(Login.this, "Enter Valid Email-ID", Toast.LENGTH_SHORT).show();
                } else if (passST.isEmpty()) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (passST.length() < 6) {
                    Toast.makeText(Login.this, "Password must be atleast 6 character", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(emailST,passST).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(Login.this, HomePage.class);
                                startActivity(home);
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Login.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_forgot_password);
                dialog.setCanceledOnTouchOutside(false);

                EditText Demail = dialog.findViewById(R.id.for_email);
                Button send = dialog.findViewById(R.id.send);
                Button cancel = dialog.findViewById(R.id.cancel);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailST = Demail.getText().toString();
                        if (emailST.isEmpty()){
                            Toast.makeText(Login.this, "Enter Email-ID", Toast.LENGTH_SHORT).show();
                        } else if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", emailST)){
                            Toast.makeText(Login.this, "Enter a valid Email-ID", Toast.LENGTH_SHORT).show();
                        } else {
                            auth.sendPasswordResetEmail(emailST).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Login.this, "Email has sent Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}