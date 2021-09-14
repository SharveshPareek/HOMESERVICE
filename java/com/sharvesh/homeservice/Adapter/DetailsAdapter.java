package com.sharvesh.homeservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sharvesh.homeservice.Modal.UserDB;
import com.sharvesh.homeservice.R;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    Context context;
    ArrayList<UserDB> detailsList;

    public DetailsAdapter(Context context, ArrayList<UserDB> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.be_model,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item.setText(detailsList.get(position).getItem());
        holder.brand_name.setText(detailsList.get(position).getBrand());
        holder.model.setText(detailsList.get(position).getModel());
        holder.discribe.setText(detailsList.get(position).getDescribe());
        holder.year.setText(detailsList.get(position).getYear());
        holder.service_time.setText(detailsList.get(position).getTime());
        holder.service_date.setText(detailsList.get(position).getDate());
        holder.name.setText(detailsList.get(position).getName());
        holder.phno.setText(detailsList.get(position).getPhno());
        holder.address.setText(detailsList.get(position).getAddress());
        holder.pincode.setText(detailsList.get(position).getPincode());
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item, brand_name, model, discribe,year,
                service_time, service_date, name, phno, address, pincode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            brand_name = itemView.findViewById(R.id.brand_name);
            model = itemView.findViewById(R.id.model);
            discribe = itemView.findViewById(R.id.discribe);
            year = itemView.findViewById(R.id.year);
            service_time = itemView.findViewById(R.id.service_time);
            service_date = itemView.findViewById(R.id.service_date);
            name = itemView.findViewById(R.id.name);
            phno = itemView.findViewById(R.id.phno);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
        }
    }
}
