package com.sharvesh.homeservice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.sharvesh.homeservice.Adapter.AddressAdapter;
import com.sharvesh.homeservice.Modal.UserDB;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CardView book_service, exchangeCard;
    LinearLayout cardLL;
    ScrollView bookLL, exchangeLL;
    ImageView EXE_icon, Bicon;
    TextView EXE_TV, BS_TV;
    Toolbar toolbar;
    DrawerLayout drawerlayout;
    NavigationView navigationView;

    public static RelativeLayout homeLayout, selectLayout;

    TextView addNewAddress;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference addRef, bookRef, exchangeRef;

    RecyclerView recyclerView;
    ArrayList<UserDB> addressList = new ArrayList<>();
    AddressAdapter addressAdapter;

    //booking
    public static LinearLayout addressLL, EXE_addressLL;
    EditText item, brand, model, year, describe;
    Button btn_book;
    TextView Bselectaddress;

    //exechange
    EditText EXE_item, EXE_brand, EXE_model, EXE_year, EXE_describe;
    Button EXE_btn;
    TextView EXE_selectAddress;

    public static EditText name, address, phno, pincode, EXE_name, EXE_address, EXE_phno, EXE_pincode;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Window status = this.getWindow();
        status.setStatusBarColor(ContextCompat.getColor(this, R.color.background_black));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Service");

        drawerlayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navview);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //RL
        homeLayout = findViewById(R.id.homeLayout);
        selectLayout = findViewById(R.id.selectLayout);
        //LL
        bookLL = findViewById(R.id.bookLL);
        cardLL = findViewById(R.id.cardLL);
        exchangeLL = findViewById(R.id.exchangeLL);
        //Card
        book_service = findViewById(R.id.book_service);
        exchangeCard = findViewById(R.id.exchangeCard);
        //image
        EXE_icon = findViewById(R.id.EXE_icon);
        Bicon = findViewById(R.id.Bicon);
        EXE_TV = findViewById(R.id.EXE_TV);
        BS_TV = findViewById(R.id.BS_TV);
        //booking
        addressLL = findViewById(R.id.addressLL);
        item = findViewById(R.id.item);
        brand = findViewById(R.id.brand_name);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        describe = findViewById(R.id.describe);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phno = findViewById(R.id.phno);
        pincode = findViewById(R.id.pincode);
        btn_book = findViewById(R.id.book_service_btn);
        Bselectaddress = findViewById(R.id.selectAddress);

        //exechange
        EXE_item = findViewById(R.id.EXE_item);
        EXE_brand = findViewById(R.id.EXE_brand_name);
        EXE_model = findViewById(R.id.EXE_model);
        EXE_year = findViewById(R.id.EXE_year);
        EXE_describe = findViewById(R.id.EXE_describe);
        EXE_name = findViewById(R.id.EXE_name);
        EXE_address = findViewById(R.id.EXE_address);
        EXE_phno = findViewById(R.id.EXE_phno);
        EXE_pincode = findViewById(R.id.EXE_pincode);
        EXE_addressLL = findViewById(R.id.EXE_addressLL);
        EXE_btn = findViewById(R.id.EXE_btn);
        EXE_selectAddress = findViewById(R.id.EXE_selectAddress);

        progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("Please wait a moment!");
        progressDialog.setCancelable(false);

        addNewAddress = findViewById(R.id.addNewAddress);
        recyclerView = findViewById(R.id.address_RV);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        addRef = database.getReference("Users").child(auth.getUid()).child("address");
        bookRef = database.getReference("Booking").child(auth.getUid());
        exchangeRef = database.getReference("Exchange").child(auth.getUid());

        book_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchangeLL.getVisibility() == View.VISIBLE) {
                    book_service.setCardBackgroundColor(getResources().getColor(R.color.red));
                    Bicon.setImageDrawable(getResources().getDrawable(R.drawable.request_white));
                    BS_TV.setTextColor(getResources().getColor(R.color.white));
                    exchangeCard.setCardBackgroundColor(getResources().getColor(R.color.white));
                    EXE_TV.setTextColor(getResources().getColor(R.color.background_black));
                    EXE_icon.setImageDrawable(getResources().getDrawable(R.drawable.exe_red));

                    exchangeLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.rightout));
                    bookLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.leftin));
                    bookLL.setVisibility(View.VISIBLE);
                    addressLL.setVisibility(View.GONE);
                    exchangeLL.setVisibility(View.GONE);
                } else {
                    book_service.setCardBackgroundColor(getResources().getColor(R.color.red));
                    Bicon.setImageDrawable(getResources().getDrawable(R.drawable.request_white));
                    BS_TV.setTextColor(getResources().getColor(R.color.white));
                    exchangeCard.setCardBackgroundColor(getResources().getColor(R.color.white));

                    EXE_TV.setTextColor(getResources().getColor(R.color.background_black));
                    EXE_icon.setImageDrawable(getResources().getDrawable(R.drawable.exe_red));
                    bookLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.leftin));
                    bookLL.setVisibility(View.VISIBLE);
                    addressLL.setVisibility(View.GONE);
                }

                if (selectLayout.getVisibility() == View.VISIBLE) {
                    homeLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.leftin));
                    homeLayout.setVisibility(View.VISIBLE);
                    selectLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.fadeout));
                    selectLayout.setVisibility(View.GONE);
                }

            }
        });

        exchangeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookLL.getVisibility() == View.VISIBLE) {
                    exchangeCard.setCardBackgroundColor(getResources().getColor(R.color.red));
                    EXE_icon.setImageDrawable(getResources().getDrawable(R.drawable.exe_white));
                    EXE_TV.setTextColor(getResources().getColor(R.color.white));
                    book_service.setCardBackgroundColor(getResources().getColor(R.color.white));
                    Bicon.setImageDrawable(getResources().getDrawable(R.drawable.request_red));
                    BS_TV.setTextColor(getResources().getColor(R.color.background_black));

                    bookLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.leftout));
                    exchangeLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.rightin));
                    exchangeLL.setVisibility(View.VISIBLE);
                    EXE_addressLL.setVisibility(View.GONE);
                    bookLL.setVisibility(View.GONE);
                } else {
                    exchangeCard.setCardBackgroundColor(getResources().getColor(R.color.red));
                    EXE_icon.setImageDrawable(getResources().getDrawable(R.drawable.exe_white));
                    EXE_TV.setTextColor(getResources().getColor(R.color.white));
                    book_service.setCardBackgroundColor(getResources().getColor(R.color.white));
                    Bicon.setImageDrawable(getResources().getDrawable(R.drawable.request_red));
                    BS_TV.setTextColor(getResources().getColor(R.color.background_black));

                    exchangeLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.rightin));
                    exchangeLL.setVisibility(View.VISIBLE);
                    EXE_addressLL.setVisibility(View.GONE);
                }

                if (selectLayout.getVisibility() == View.VISIBLE) {
                    homeLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.leftin));
                    homeLayout.setVisibility(View.VISIBLE);
                    selectLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.fadeout));
                    selectLayout.setVisibility(View.GONE);
                }

            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemST = item.getText().toString();
                String brandST = brand.getText().toString();
                String modelST = model.getText().toString();
                String discribeST = describe.getText().toString();
                String yearST = year.getText().toString();
                String nameST = name.getText().toString();
                String addressST = address.getText().toString();
                String phnoST = phno.getText().toString();
                String pincodeST = pincode.getText().toString();

                if (itemST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Item", Toast.LENGTH_SHORT).show();
                } else if (brandST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Brand name", Toast.LENGTH_SHORT).show();
                } else if (modelST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Model details", Toast.LENGTH_SHORT).show();
                } else if (discribeST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Please Discribe your problem", Toast.LENGTH_SHORT).show();
                } else if (yearST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter year of purchase", Toast.LENGTH_SHORT).show();
                } else if (addressLL.getVisibility() != View.VISIBLE) {
                    Toast.makeText(HomePage.this, "Select your address", Toast.LENGTH_SHORT).show();
                } else if (addressLL.getVisibility() == View.VISIBLE && nameST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (addressLL.getVisibility() == View.VISIBLE && addressST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Address", Toast.LENGTH_SHORT).show();
                } else if (addressLL.getVisibility() == View.VISIBLE && pincodeST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                } else if (addressLL.getVisibility() == View.VISIBLE && pincodeST.length() < 6) {
                    Toast.makeText(HomePage.this, "Enter Valid Pincode", Toast.LENGTH_SHORT).show();
                } else if (addressLL.getVisibility() == View.VISIBLE && phnoST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() == View.VISIBLE && phnoST.length() < 10) {
                    Toast.makeText(HomePage.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    HashMap<String, Object> bookMap = new HashMap<>();
                    bookMap.put("date", date);
                    bookMap.put("time", time);
                    bookMap.put("item", itemST);
                    bookMap.put("brand", brandST);
                    bookMap.put("model", modelST);
                    bookMap.put("describe", discribeST);
                    bookMap.put("year", yearST);
                    bookMap.put("address", addressST);
                    bookMap.put("pincode", pincodeST);
                    bookMap.put("phno", phnoST);
                    bookMap.put("name", nameST);

                    bookRef.push().setValue(bookMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            setText("Service Booked Successful", date, time, itemST, brandST, modelST, discribeST, yearST, addressST, pincodeST, phnoST, nameST);
                            Intent confromation = new Intent(HomePage.this, Conformation.class);
                            startActivity(confromation);
                            overridePendingTransition(R.anim.rightin, R.anim.fadeout);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(HomePage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        EXE_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemST = EXE_item.getText().toString();
                String brandST = EXE_brand.getText().toString();
                String modelST = EXE_model.getText().toString();
                String discribeST = EXE_describe.getText().toString();
                String yearST = EXE_year.getText().toString();
                String nameST = EXE_name.getText().toString();
                String addressST = EXE_address.getText().toString();
                String phnoST = EXE_phno.getText().toString();
                String pincodeST = EXE_pincode.getText().toString();

                if (itemST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Item", Toast.LENGTH_SHORT).show();
                } else if (brandST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Brand name", Toast.LENGTH_SHORT).show();
                } else if (modelST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Model details", Toast.LENGTH_SHORT).show();
                } else if (discribeST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Discribe your exepected exechange product", Toast.LENGTH_SHORT).show();
                } else if (yearST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter year of purchase", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() != View.VISIBLE) {
                    Toast.makeText(HomePage.this, "Select your address", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() == View.VISIBLE && nameST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() == View.VISIBLE && addressST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Address", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() == View.VISIBLE && pincodeST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() == View.VISIBLE && pincodeST.length() < 6) {
                    Toast.makeText(HomePage.this, "Enter Valid Pincode", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() == View.VISIBLE && phnoST.isEmpty()) {
                    Toast.makeText(HomePage.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                } else if (EXE_addressLL.getVisibility() == View.VISIBLE && phnoST.length() < 10) {
                    Toast.makeText(HomePage.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();

                    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    HashMap<String, Object> exMap = new HashMap<>();
                    exMap.put("date", date);
                    exMap.put("time", time);
                    exMap.put("item", itemST);
                    exMap.put("brand", brandST);
                    exMap.put("model", modelST);
                    exMap.put("describe", discribeST);
                    exMap.put("year", yearST);
                    exMap.put("pincode", pincodeST);
                    exMap.put("phno", phnoST);
                    exMap.put("name", nameST);
                    exMap.put("address", addressST);

                    exchangeRef.push().setValue(exMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            setText("Exchange Requested", date, time, itemST, brandST, modelST, discribeST, yearST, addressST, pincodeST, phnoST, nameST);
                            Intent confromation = new Intent(HomePage.this, Conformation.class);
                            startActivity(confromation);
                            overridePendingTransition(R.anim.rightin, R.anim.fadeout);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(HomePage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        EXE_selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.fadeout));
                homeLayout.setVisibility(View.GONE);
                selectLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.rightin));
                selectLayout.setVisibility(View.VISIBLE);
                callAdapter("Exchange");
            }
        });

        Bselectaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.fadeout));
                homeLayout.setVisibility(View.GONE);
                selectLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.rightin));
                selectLayout.setVisibility(View.VISIBLE);
                callAdapter("Book");
            }
        });

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(HomePage.this);
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

                        if (name.isEmpty()) {
                            Toast.makeText(HomePage.this, "Enter Name", Toast.LENGTH_SHORT).show();
                        } else if (address.isEmpty()) {
                            Toast.makeText(HomePage.this, "Enter Address", Toast.LENGTH_SHORT).show();
                        } else if (pincode.isEmpty() || pincode.length() < 6) {
                            Toast.makeText(HomePage.this, "Enter Pincode Properly", Toast.LENGTH_SHORT).show();
                        } else if (phno.isEmpty() || phno.length() < 10) {
                            Toast.makeText(HomePage.this, "Enter valid Phone Number", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.show();
                            HashMap<String, Object> addressMap = new HashMap<>();
                            addressMap.put("address", address);
                            addressMap.put("pincode", pincode);
                            addressMap.put("phno", phno);
                            addressMap.put("name", name);

                            addRef.push().setValue(addressMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(HomePage.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();

            }
        });

    }

    private void setText(String title, String date, String time, String itemST, String brandST, String modelST, String discribeST, String yearST, String addressST, String pincodeST, String phnoST, String nameST) {
        Conformation.tv_titleST = title;
        Conformation.itemST = itemST;
        Conformation.brand_nameST = brandST;
        Conformation.modelST = modelST;
        Conformation.discribeST = discribeST;
        Conformation.yearST = yearST;
        Conformation.service_timeST = time;
        Conformation.service_dateST = date;
        Conformation.nameST = nameST;
        Conformation.phnoST = phnoST;
        Conformation.addressST = addressST;
        Conformation.pincodeST = pincodeST;
    }

    private void callAdapter(String from) {

        addRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                for (DataSnapshot addressSnapShot : snapshot.getChildren()) {
                    UserDB userDB = addressSnapShot.getValue(UserDB.class);
                    userDB.setName(userDB.getName());
                    userDB.setAddress(userDB.getAddress());
                    userDB.setPincode(userDB.getPincode());
                    userDB.setPhno(userDB.getPhno());
                    userDB.setKey(addressSnapShot.getKey());
                    userDB.setFrom(from);
                    addressList.add(userDB);
                }
                progressDialog.dismiss();
                recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                addressAdapter = new AddressAdapter(HomePage.this, addressList);
                recyclerView.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(HomePage.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        } else if (selectLayout.getVisibility() == View.VISIBLE) {
            homeLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.leftin));
            homeLayout.setVisibility(View.VISIBLE);
            selectLayout.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.fadeout));
            selectLayout.setVisibility(View.GONE);
        } else if (bookLL.getVisibility() == View.VISIBLE) {
            book_service.setCardBackgroundColor(getResources().getColor(R.color.white));
            Bicon.setImageDrawable(getResources().getDrawable(R.drawable.request_red));
            BS_TV.setTextColor(getResources().getColor(R.color.background_black));
            bookLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.fadeout));
            bookLL.setVisibility(View.GONE);
        } else if (exchangeLL.getVisibility() == View.VISIBLE) {
            exchangeCard.setCardBackgroundColor(getResources().getColor(R.color.white));
            EXE_TV.setTextColor(getResources().getColor(R.color.background_black));
            EXE_icon.setImageDrawable(getResources().getDrawable(R.drawable.exe_red));
            exchangeLL.setAnimation(AnimationUtils.loadAnimation(HomePage.this, R.anim.fadeout));
            exchangeLL.setVisibility(View.GONE);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile:
                Intent profile = new Intent(HomePage.this, ProfilePage.class);
                startActivity(profile);
                //overridePendingTransition(R.anim.leftin,R.anim.rightout);
                drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myserice:
                Intent myservice = new Intent(HomePage.this, MyService.class);
                startActivity(myservice);
                //overridePendingTransition(R.anim.rightin,R.anim.leftout);
                drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myexchange:
                Intent myex = new Intent(HomePage.this, Myexchange.class);
                startActivity(myex);
                //overridePendingTransition(R.anim.rightin,R.anim.leftout);
                drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                auth.signOut();
                Intent login = new Intent(HomePage.this, Login.class);
                startActivity(login);
                finish();
                break;
        }
        return true;
    }
}