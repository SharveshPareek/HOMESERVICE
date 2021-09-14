package com.sharvesh.homeservice.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sharvesh.homeservice.HomePage;
import com.sharvesh.homeservice.Modal.UserDB;
import com.sharvesh.homeservice.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    ArrayList<UserDB> addressList;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(auth.getUid()).child("address");

    public AddressAdapter(Context context, ArrayList<UserDB> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.address_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(addressList.get(position).getName());
        holder.address.setText(addressList.get(position).getAddress());
        holder.pincode.setText(addressList.get(position).getPincode());
        holder.phno.setText(addressList.get(position).getPhno());

        String from = addressList.get(position).getFrom();

        if (from.matches("Profile")){
            holder.RE_LL.setVisibility(View.VISIBLE);
        } else {
            holder.RE_LL.setVisibility(View.GONE);
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (from.matches("Exchange")){
                        HomePage.homeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.leftin));
                        HomePage.homeLayout.setVisibility(View.VISIBLE);
                        HomePage.EXE_addressLL.setVisibility(View.VISIBLE);
                        HomePage.selectLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeout));
                        HomePage.selectLayout.setVisibility(View.GONE);
                        HomePage.EXE_name.setText(addressList.get(position).getName());
                        HomePage.EXE_address.setText(addressList.get(position).getAddress());
                        HomePage.EXE_pincode.setText(addressList.get(position).getPincode());
                        HomePage.EXE_phno.setText(addressList.get(position).getPhno());
                    } else {
                        HomePage.homeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.leftin));
                        HomePage.homeLayout.setVisibility(View.VISIBLE);
                        HomePage.addressLL.setVisibility(View.VISIBLE);
                        HomePage.selectLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fadeout));
                        HomePage.selectLayout.setVisibility(View.GONE);
                        HomePage.name.setText(addressList.get(position).getName());
                        HomePage.address.setText(addressList.get(position).getAddress());
                        HomePage.pincode.setText(addressList.get(position).getPincode());
                        HomePage.phno.setText(addressList.get(position).getPhno());
                    }
                }
            });
        }

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressList.size() == 1){
                    Toast.makeText(context, "Please add another address before removing this ! ", Toast.LENGTH_LONG).show();
                } else {
                    String key = addressList.get(position).getKey();
                    reference.child(key).removeValue();
                    Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ET_LL.setVisibility(View.VISIBLE);
                holder.TV_LL.setVisibility(View.GONE);
                holder.nameET.setText(holder.name.getText().toString());
                holder.addressET.setText(holder.address.getText().toString());
                holder.phnoET.setText(holder.phno.getText().toString());
                holder.pincodeET.setText(holder.pincode.getText().toString());
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = holder.nameET.getText().toString();
                String address = holder.addressET.getText().toString();
                String pincode = holder.pincodeET.getText().toString();
                String phno = holder.phnoET.getText().toString();

                if (name.isEmpty()){
                    Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty()){
                    Toast.makeText(context, "Enter Address", Toast.LENGTH_SHORT).show();
                } else if (pincode.isEmpty() || pincode.length()<6){
                    Toast.makeText(context, "Enter Pincode Properly", Toast.LENGTH_SHORT).show();
                } else if (phno.isEmpty() || phno.length()<10){
                    Toast.makeText(context, "Enter valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String,Object> addressMap = new HashMap<>();
                    addressMap.put("address",address);
                    addressMap.put("pincode",pincode);
                    addressMap.put("phno",phno);
                    addressMap.put("name",name);
                    String key = addressList.get(position).getKey();
                    reference.child(key).updateChildren(addressMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            holder.ET_LL.setVisibility(View.GONE);
                            holder.TV_LL.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ET_LL.setVisibility(View.GONE);
                holder.TV_LL.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView name,address,pincode,phno,edit,remove,update,cancel;
        LinearLayout ET_LL,TV_LL,RE_LL;
        EditText nameET,addressET,pincodeET,phnoET;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.address_card);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            phno = itemView.findViewById(R.id.phno);
            edit = itemView.findViewById(R.id.edit);
            remove = itemView.findViewById(R.id.remove);
            update = itemView.findViewById(R.id.update);
            cancel = itemView.findViewById(R.id.cancel);

            ET_LL = itemView.findViewById(R.id.ET_LL);
            TV_LL = itemView.findViewById(R.id.TV_LL);
            RE_LL = itemView.findViewById(R.id.RE_LL);

            nameET = itemView.findViewById(R.id.nameET);
            addressET = itemView.findViewById(R.id.addressET);
            pincodeET = itemView.findViewById(R.id.pincodeET);
            phnoET = itemView.findViewById(R.id.phnoET);
        }
    }
}
