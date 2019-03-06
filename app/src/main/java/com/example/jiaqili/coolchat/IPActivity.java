package com.example.jiaqili.coolchat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class IPActivity extends BaseActivity {
    private static  String HOST ;// 本机IP地址
    private static TextView localIP,remoteIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        localIP=(TextView) findViewById(R.id.tvLocalIP);
        remoteIP=(TextView) findViewById(R.id.etRemoteIP);
        HOST=getHostIP();
        localIP.setText(HOST);
    }
    public String getHostIP(){
        //获取本机IP地址，注意在AndroidManifest里申请相关权限
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //首先通过getSystemService()方法获得WifiManager的实例，这是一个系统服务类，专门用于管理WiFi网络
        if (!wifiManager.isWifiEnabled()) {   //判断wifi是否开启
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //调用WifiManager实例的getConnectionInfo()方法可以得到WiFi信息
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIP(ipAddress);
        return ip;
    }
    public String intToIP(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
    public void btnGetConfirmedClick(View view){
        String ip=remoteIP.getText().toString();
        if (!ip.isEmpty()){
            Intent intent=new Intent();
            intent.putExtra("data_return",ip);
            setResult(RESULT_OK,intent);
            Toast.makeText(IPActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
            finish();//结束当前活动，跳转到聊天界面
        }
        else {
            Toast.makeText(IPActivity.this,"请先输入对方IP地址",Toast.LENGTH_SHORT).show();
        }
    }
}
