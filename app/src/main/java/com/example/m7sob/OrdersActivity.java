package com.example.m7sob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity implements daysAdapter.onDayClickListener {

    DatabaseReference reference;
    String user;
    HashMap<String,Integer>all;
    HashMap<String,Integer>shop;
    RecyclerView rec;
    captain cap;
    List<String>days;
    List<Integer>al;
    List<Integer>shp;
    daysAdapter adapter;
    boolean in = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        rec = findViewById(R.id.rec);
        cap = (captain) getIntent().getBundleExtra("cap").getSerializable("cap");
        user = PreferenceManager.getDefaultSharedPreferences(this).getString("user","no user");
        reference = FirebaseDatabase.getInstance().getReference("Users");
        all = new HashMap<String, Integer>();
        shop = new HashMap<String, Integer>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String phone = snapshot.getKey();
                    if(phone.equals(user)){
                        captain cap = snapshot.getValue(captain.class);
                        HashMap<String,order> or = cap.getOrders();
                        Iterator it = or.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            order o = (order) pair.getValue();
                            if(all.containsKey(o.getDate())) {
                                int val = all.get(o.getDate());
                                all.remove(o.getDate());
                                all.put(o.getDate(),o.getCustomer() + val);
                            }
                            else
                                all.put(o.getDate(),o.getCustomer());
                            if(shop.containsKey(o.getDate())){
                                int val = shop.get(o.getDate());
                                shop.remove(o.getDate());
                                shop.put(o.getDate(),o.getShop() + val);
                            }
                            else
                                shop.put(o.getDate(),o.getShop());
                        }
                        break;
                    }
                }
                days = new ArrayList<>();
                al = new ArrayList<>();
                shp = new ArrayList<>();
                Iterator it = all.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Integer i = (Integer) pair.getValue();
                    days.add(pair.getKey().toString());
                    al.add(i);
                    shp.add(shop.get(pair.getKey().toString()));
                    it.remove();
                }
                adapter = new daysAdapter();
                adapter.setAll(al);
                adapter.setDays(days);
                adapter.setShop(shp);
                adapter.setListener(OrdersActivity.this);
                rec.setAdapter(adapter);
                rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rec.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!in){
            Intent intent = new Intent(OrdersActivity.this,innerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("cap",cap);
            intent.putExtra("cap",bundle);
            startActivity(intent);
            finish();
        }
        else{
            rec.setAdapter(adapter);
            in = false;
        }
    }

    @Override
    public void onDayClick(String date) {
        List<order> myOrders = new ArrayList<>();

        HashMap<String,order> or = cap.getOrders();
        Iterator it = or.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            order o = (order) pair.getValue();
            if(date.equals(o.getDate()))
                myOrders.add((order)pair.getValue());
        }

        daysAdapter adapter1 = new daysAdapter("days");
        adapter1.setMyOrders(myOrders);
        rec.setAdapter(adapter1);
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(this));
        in = true;
    }
}