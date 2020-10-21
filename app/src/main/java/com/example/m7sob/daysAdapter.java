package com.example.m7sob;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class daysAdapter extends RecyclerView.Adapter<daysAdapter.dayViewHolder> {

    private List<Integer>all;
    private List<Integer> shop;
    private List<String> days;
    private onDayClickListener listener;
    private String type;
    private List<order>myOrders;

    public daysAdapter() {
    }

    public daysAdapter(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public daysAdapter.dayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull daysAdapter.dayViewHolder holder, int position) {
        if(type == null) {
            holder.day.setText(days.get(position));
            holder.total.setText(all.get(position) + " EGP");
            holder.shop.setText(shop.get(position) + " EGP");
            holder.paid.setText((all.get(position) - shop.get(position)) + " EGP");
        }
        else{
            holder.day.setText(myOrders.get(position).getCode());
            holder.total.setText(myOrders.get(position).getCustomer() + " EGP");
            holder.shop.setText(myOrders.get(position).getShop() + " EGP");
            holder.paid.setText((myOrders.get(position).getCustomer() - myOrders.get(position).getShop()) + " EGP");
        }
    }

    @Override
    public int getItemCount() {
        if(type == null){
            if(all == null)
                return 0;
            return all.size();
        }
        else{
            if(myOrders == null)
                return 0;
            return myOrders.size();
        }
    }

    public List<Integer> getAll() {
        return all;
    }

    public void setAll(List<Integer> all) {
        this.all = all;
    }

    public List<Integer> getShop() {
        return shop;
    }

    public void setShop(List<Integer> shop) {
        this.shop = shop;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public List<order> getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(List<order> myOrders) {
        this.myOrders = myOrders;
    }

    public onDayClickListener getListener() {
        return listener;
    }

    public void setListener(onDayClickListener listener) {
        this.listener = listener;
    }

    interface onDayClickListener{
        void onDayClick(String date);
    }

    public class dayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView total,shop,paid,day;
        public dayViewHolder(@NonNull View itemView) {
            super(itemView);
            total = itemView.findViewById(R.id.total_day);
            shop = itemView.findViewById(R.id.shop_day);
            paid = itemView.findViewById(R.id.prof_day);
            day = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(type == null)
                listener.onDayClick(days.get(getLayoutPosition()));
        }
    }
}
