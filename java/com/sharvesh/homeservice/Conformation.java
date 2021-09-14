package com.sharvesh.homeservice;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

public class Conformation extends AppCompatActivity {

    TextView tv_title, item, brand_name, model, discribe, year,
            service_time, service_date, name, phno, address, pincode;

    static String tv_titleST, itemST, brand_nameST, modelST, discribeST, yearST,
            service_timeST, service_dateST, nameST, phnoST, addressST, pincodeST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conformation);

        Window status = this.getWindow();
        status.setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        tv_title = findViewById(R.id.tv_title);
        item = findViewById(R.id.item);
        brand_name = findViewById(R.id.brand_name);
        model = findViewById(R.id.model);
        discribe = findViewById(R.id.discribe);
        year = findViewById(R.id.year);
        service_time = findViewById(R.id.service_time);
        service_date = findViewById(R.id.service_date);
        name = findViewById(R.id.name);
        phno = findViewById(R.id.phno);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        tv_title.setText(tv_titleST);
        item.setText(itemST);
        brand_name.setText(brand_nameST);
        model.setText(modelST);
        discribe.setText(discribeST);
        year.setText(yearST);
        service_time.setText(service_timeST);
        service_date.setText(service_dateST);
        name.setText(nameST);
        phno.setText(phnoST);
        address.setText(addressST);
        pincode.setText(pincodeST);

    }

    @Override
    public void onBackPressed() {
        Intent confromation = new Intent(Conformation.this, HomePage.class);
        startActivity(confromation);
        overridePendingTransition(R.anim.leftin, R.anim.fadeout);
    }
}