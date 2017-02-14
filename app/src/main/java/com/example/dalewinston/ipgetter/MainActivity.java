package com.example.dalewinston.ipgetter;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

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
        final TextView textViewPublicIP = (TextView) findViewById(R.id.textViewPublicIP);
        Button buttonGetIP = (Button) findViewById(R.id.buttonCaptureIP);
        Button buttonSendData = (Button) findViewById(R.id.buttonSendData);

        final ArrayList<String> ipList = new ArrayList<String>();
        final ArrayList<String> publicIpList = new ArrayList<String>();


        buttonGetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get host ip
                String wifiIpAddress = getWifiIpAddress(getApplicationContext());
                if (wifiIpAddress != null) {
                    String hostIpString = R.string.host_ip_string + " " + wifiIpAddress;
                    textViewShowIP.setText(hostIpString);
                    ipList.add(wifiIpAddress);
                } else {
                    textViewShowIP.setText(R.string.ip_fail);
                }

                getPublicIpAddress(publicIpList, textViewPublicIP);

                // Get public ip

            }
        });

        buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIpData(ipList);
            }
        });

    }

    protected void getPublicIpAddress(final ArrayList<String> publicIpList, final TextView textView) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlIpify = "https://api.ipify.org";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlIpify, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                publicIpList.add(response);
                String publicIPString = "Public IP: " + response;
                textView.setText(publicIPString);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                textView.setText(error.toString());
            }
        });
        requestQueue.add(stringRequest);
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
