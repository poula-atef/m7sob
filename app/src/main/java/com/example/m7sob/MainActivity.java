package com.example.m7sob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    EditText et,pass;
    int goOut = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.et);
        pass = findViewById(R.id.pass_et);
        if(haveInternetConnection()){
            String user = PreferenceManager.getDefaultSharedPreferences(this).getString("user", "no user");
            if (!user.equals("no user")) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean found = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            captain cap = snapshot.getValue(captain.class);
                            String phone = snapshot.getKey();
                            if (phone.equals(user)) {
                                Intent intent = new Intent(MainActivity.this, innerActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("cap", cap);
                                intent.putExtra("cap", bundle);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                et.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.login)).setVisibility(View.VISIBLE);
            }
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.not_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void onComponentClick(View view){
        int id = view.getId();
        if(id == R.id.login){
            if(!TextUtils.isEmpty(et.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean found = false;
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            captain cap = snapshot.getValue(captain.class);
                            String phone = snapshot.getKey();
                            if(phone.equals(et.getText().toString())) {
                                found = true;
                                if (cap.getPassword().equals(pass.getText().toString())) {
                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                                    editor.putString("user", phone);
                                    editor.commit();
                                    Intent intent = new Intent(MainActivity.this, innerActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("cap", cap);
                                    intent.putExtra("cap", bundle);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.active), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if(!found)
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.not_reg), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else{
                Toast.makeText(this, getResources().getString(R.string.req), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(goOut == 0) {
            goOut++;
            Toast.makeText(this, getResources().getString(R.string.back), Toast.LENGTH_SHORT).show();
        }
        else{
            finish();
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