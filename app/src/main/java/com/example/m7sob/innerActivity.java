package com.example.m7sob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class innerActivity extends AppCompatActivity {

    TextView name,total,profit,shop;
    EditText ordCode,shopMoney,paidMoney;
    String user;
    DatabaseReference ref;
    captain cap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);
        user = PreferenceManager.getDefaultSharedPreferences(this).getString("user","no user");
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(user).child("active").setValue(true);
        cap = (captain) getIntent().getBundleExtra("cap").getSerializable("cap");
        name = findViewById(R.id.name);
        name.setText(cap.getName());
        shop = findViewById(R.id.shop_day);
        profit = findViewById(R.id.profit_day);
        total = findViewById(R.id.total_day);
        shop = findViewById(R.id.shop_day);
        ordCode = findViewById(R.id.order_code);
        shopMoney = findViewById(R.id.shop_money);
        paidMoney = findViewById(R.id.client_paid);
        name.setText(cap.getName());
        int totl=0,sh=0;
        String date = new SimpleDateFormat("EEEE//yyyy-M-dd", Locale.ENGLISH).format(new Date());
        if(cap != null){

            HashMap<String,order> or = cap.getOrders();
            Iterator it = or.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                order o = (order) pair.getValue();
                if(date.equals(o.getDate())){
                    totl += o.getCustomer();
                    sh += o.getShop();
                }
            }
            shop.setText(sh + " EGP");
            total.setText(totl + " EGP");
            profit.setText((totl-sh) + " EGP");
        }
    }

    public void onComponentClick(View view){
        int id = view.getId();

        if(haveInternetConnection()) {
            if (id == R.id.logout) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putString("user", "no user");
                editor.commit();
                ref.child(user).child("active").setValue(false);
                Intent intent = new Intent(innerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (id == R.id.add) {
                if(TextUtils.isEmpty(shopMoney.getText().toString()) || TextUtils.isEmpty(paidMoney.getText().toString()) || TextUtils.isEmpty(ordCode.getText().toString())){
                    Toast.makeText(this, getResources().getString(R.string.req), Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(shopMoney.getText().toString()) > Integer.parseInt(paidMoney.getText().toString())) {
                    Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
                else {
                    String date = new SimpleDateFormat("EEEE//yyyy-M-dd", Locale.ENGLISH).format(new Date());
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date());
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    String user = preferences.getString("user", "no user");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(user).child("orders").child(ordCode.getText().toString()).setValue(new order(ordCode.getText().toString(), date, time, Integer.parseInt(shopMoney.getText().toString()), Integer.parseInt(paidMoney.getText().toString())));
                    cap.getOrders().put(ordCode.getText().toString(), new order(ordCode.getText().toString(), date, time, Integer.parseInt(shopMoney.getText().toString()), Integer.parseInt(paidMoney.getText().toString())));
                    Toast.makeText(innerActivity.this, getResources().getString(R.string.added), Toast.LENGTH_SHORT).show();
                    String[] res = total.getText().toString().split(" ");
                    int totalInt = Integer.parseInt(res[0]);
                    total.setText((totalInt + Integer.parseInt(paidMoney.getText().toString())) + " EGP");
                    res = shop.getText().toString().split(" ");
                    int shopInt = Integer.parseInt(res[0]);
                    shop.setText((shopInt + Integer.parseInt(shopMoney.getText().toString())) + " EGP");
                    res = profit.getText().toString().split(" ");
                    int profitInt = Integer.parseInt(res[0]);
                    profit.setText((profitInt + (Integer.parseInt(paidMoney.getText().toString()) - Integer.parseInt(shopMoney.getText().toString()))) + " EGP");
                    ordCode.getText().clear();
                    shopMoney.getText().clear();
                    paidMoney.getText().clear();
                }
            }
            else if (id == R.id.day) {
                Intent intent = new Intent(innerActivity.this, OrdersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cap", cap);
                intent.putExtra("cap", bundle);
                startActivity(intent);
                finish();
            }
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.not_internet), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean haveInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info: networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI") || info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                    return true;
        }
        return false;
    }

}