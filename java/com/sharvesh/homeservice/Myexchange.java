package com.sharvesh.homeservice;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.sharvesh.homeservice.Adapter.DetailsAdapter;
import com.sharvesh.homeservice.Modal.UserDB;

import java.util.ArrayList;

public class Myexchange extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView myServiceRV;
    TextView noResult;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference exchangeRef;

    ArrayList<UserDB> detailsList = new ArrayList<>();
    DetailsAdapter detailsAdapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myexchange);

        myServiceRV = findViewById(R.id.myServiceRV);
        noResult = findViewById(R.id.noResult);
        noResult.setVisibility(View.GONE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Exchange");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        exchangeRef = database.getReference("Exchange").child(auth.getUid());

        progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("Please wait a moment!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        exchangeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detailsList.clear();
                for (DataSnapshot detailSnapshot : snapshot.getChildren()) {
                    UserDB userDB = detailSnapshot.getValue(UserDB.class);
                    userDB.setName(userDB.getName());
                    userDB.setAddress(userDB.getAddress());
                    userDB.setPincode(userDB.getPincode());
                    userDB.setPhno(userDB.getPhno());
                    userDB.setDate(userDB.getDate());
                    userDB.setTime(userDB.getTime());
                    userDB.setItem(userDB.getItem());
                    userDB.setBrand(userDB.getBrand());
                    userDB.setDescribe(userDB.getDescribe());
                    userDB.setModel(userDB.getModel());
                    userDB.setYear(userDB.getYear());
                    detailsList.add(userDB);
                }
                progressDialog.dismiss();
                if (detailsList.size() == 0) {
                    myServiceRV.setVisibility(View.GONE);
                    noResult.setVisibility(View.VISIBLE);
                } else {
                    myServiceRV.setVisibility(View.VISIBLE);
                    noResult.setVisibility(View.GONE);
                    myServiceRV.setLayoutManager(new LinearLayoutManager(Myexchange.this));
                    detailsAdapter = new DetailsAdapter(Myexchange.this, detailsList);
                    myServiceRV.setAdapter(detailsAdapter);
                    detailsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(Myexchange.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}