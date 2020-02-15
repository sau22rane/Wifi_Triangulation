package com.example.wifi_triangulation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

public class MainActivity extends AppCompatActivity {

    class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        }
    }

    WifiScanReceiver wifiReciever = new WifiScanReceiver();
    IntentFilter receiver = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
    Button ref;
    RecyclerView Recycle;
    int rssi;
    int level;
    ArrayList<String> WifiAdapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Recycle = (RecyclerView) findViewById(R.id.Recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        Recycle.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        registerReceiver(wifiReciever,receiver);
        ref = (Button) findViewById(R.id.Refresh);
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WifiAdapters = new ArrayList<String>();
                List<ScanResult> wifiScanList;
                WifiManager mainWifiObj;
                mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                mainWifiObj.startScan();
                wifiScanList = mainWifiObj.getScanResults();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            123);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

                } else {

                    //Toast.makeText(getApplicationContext(), level, Toast.LENGTH_LONG).show();
                    for (ScanResult scanResult : wifiScanList) {
                        double level1 = scanResult.level;
                        WifiAdapters.add(scanResult.SSID+" " + level1);
                        Log.d("Rane", scanResult.SSID+" " + level1);
                    }

                    CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, WifiAdapters);
                    Recycle.setAdapter(customAdapter);
                }
            }
        });
//        Toast.makeText(getApplicationContext(),  wifiScanList.toString(),Toast.LENGTH_LONG).show();
    }
}
