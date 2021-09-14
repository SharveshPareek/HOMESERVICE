package com.sharvesh.homeservice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.sharvesh.homeservice.Adapter.AddressAdapter;
import com.sharvesh.homeservice.Modal.UserDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ProfilePage extends AppCompatActivity {

    TextView user_name,email,addNewAddress;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference signRef,addRef;

    RecyclerView recyclerView;
    ArrayList<UserDB> addressList = new ArrayList<>();
    AddressAdapter addressAdapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);

        Window status = this.getWindow();
        status.setStatusBarColor(ContextCompat.getColor(this, R.color.red));

        progressDialog = new ProgressDialog(this,R.style.ProgressDialog);
        progressDialog.setMessage("Please wait for a minute");
        progressDialog.setCancelable(false);
        progressDialog.show();

        user_name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        addNewAddress = findViewById(R.id.addNewAddress);

        recyclerView = findViewById(R.id.address_RV);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        signRef = database.getReference("Users");
        addRef = database.getReference("Users").child(auth.getUid()).child("address");

        signRef.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                user_name.setText(snapshot.child("name").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                for (DataSnapshot addressSnapShot : snapshot.child("address").getChildren()){
                    UserDB userDB = addressSnapShot.getValue(UserDB.class);
                    userDB.setName(userDB.getName());
                    userDB.setAddress(userDB.getAddress());
                    userDB.setPincode(userDB.getPincode());
                    userDB.setPhno(userDB.getPhno());
                    userDB.setKey(addressSnapShot.getKey());
                    userDB.setFrom("Profile");
                    addressList.add(userDB);
                }
                progressDialog.dismiss();
                recyclerView.setLayoutManager(new LinearLayoutManager(ProfilePage.this));
                addressAdapter = new AddressAdapter(ProfilePage.this,addressList);
                recyclerView.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(ProfilePage.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(ProfilePage.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.address_layout);
                dialog.setCanceledOnTouchOutside(false);

                LinearLayout ET_LL = dialog.findViewById(R.id.ET_LL);
                ET_LL.setVisibility(View.VISIBLE);
                LinearLayout TV_LL = dialog.findViewById(R.id.TV_LL);
                TV_LL.setVisibility(View.GONE);

                EditText nameET = dialog.findViewById(R.id.nameET);
                EditText addressET = dialog.findViewById(R.id.addressET);
                EditText pincodeET = dialog.findViewById(R.id.pincodeET);
                EditText phnoET = dialog.findViewById(R.id.phnoET);

                TextView update = dialog.findViewById(R.id.update);
                update.setText("Add Address");
                TextView cancel = dialog.findViewById(R.id.cancel);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameET.getText().toString();
                        String address = addressET.getText().toString();
                        String pincode = pincodeET.getText().toString();
                        String phno = phnoET.getText().toString();

                        if (name.isEmpty()){
                            Toast.makeText(ProfilePage.this, "Enter Name", Toast.LENGTH_SHORT).show();
                        } else if (address.isEmpty()){
                            Toast.makeText(ProfilePage.this, "Enter Address", Toast.LENGTH_SHORT).show();
                        } else if (pincode.isEmpty() || pincode.length()<6){
                            Toast.makeText(ProfilePage.this, "Enter Pincode Properly", Toast.LENGTH_SHORT).show();
                        } else if (phno.isEmpty() || phno.length()<10){
                            Toast.makeText(ProfilePage.this, "Enter valid Phone Number", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.show();
                            HashMap<String,Object> addressMap = new HashMap<>();
                            addressMap.put("address",address);
                            addressMap.put("pincode",pincode);
                            addressMap.put("phno",phno);
                            addressMap.put("name",name);

                            addRef.push().setValue(addressMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfilePage.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
}