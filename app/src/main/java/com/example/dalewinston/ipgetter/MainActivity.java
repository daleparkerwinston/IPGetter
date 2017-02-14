package com.example.dalewinston.ipgetter;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textViewShowIP = (TextView) findViewById(R.id.textViewShowIP);
        Button buttonGetIP = (Button) findViewById(R.id.buttonCaptureIP);
        Button buttonSendData = (Button) findViewById(R.id.buttonSendData);

        final ArrayList<String> ipList = new ArrayList<String>();

        buttonGetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifiIpAddress = getWifiIpAddress(getApplicationContext());
                if (wifiIpAddress != null) {
                    textViewShowIP.setText(getWifiIpAddress(getApplicationContext()));
                    ipList.add(wifiIpAddress);
                } else {
                    textViewShowIP.setText(R.string.ip_fail);
                }
            }
        });

        buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIpData(ipList);
            }
        });

    }

    protected String getWifiIpAddress(Context context) {
        String ipAddressString = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endian if needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    protected void sendIpData(ArrayList<String> ipList) {

    }
}
