package com.example.team17;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    Context context;

    ArrayList<RequestData> list;

    public RequestAdapter(Context context, ArrayList<RequestData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RequestData data= list.get(position);
        holder.title.setText(data.getTitle());
        holder.description.setText(data.getDescription());
        holder.status.setText(data.getStatus());
        holder.amount.setText(data.getAmount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, status, amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.product_title);
            description=itemView.findViewById(R.id.product_description);
            status=itemView.findViewById(R.id.product_status);
            amount=itemView.findViewById(R.id.product_amount);
        }
    }
}
