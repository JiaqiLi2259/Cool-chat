package com.example.jiaqili.coolchat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int RESULT_CODE=1;
    private static String IP=null;
    private static boolean IP_FLAG=false;
    private static final int PORT_SEND = 8001;// 发送方的端口号(最好大于1024)
    private static final int PORT_RECEIVE=4567;//接收方的端口号(最好大于1024)
    private List<MessageHandle> msgList=new ArrayList<>();
    private MessageHandleAdapter adapter;
    private UdpMessageTool myUdpSend;//发送端的声明
    private UdpMessageTool myUdpReceive;//接受端的声明
    private static String dataSend=null;//发送的数据
    private static String dataReceive=null;//接收的数据
    private RecyclerView recyclerView;
    private EditText editText;//获取编辑发送内容的控件
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            dataReceive=(String) msg.obj;
            if (!TextUtils.isEmpty(dataReceive)) {
                MessageHandle messageHandle=new MessageHandle(dataReceive,MessageHandle.TYPE_RECEIVE);
                msgList.add(messageHandle);//主线程中操作：更新数据列表
                adapter.notifyItemInserted(msgList.size()-1);//主线程中操作：有新消息时刷新RecyclerView
                recyclerView.scrollToPosition(msgList.size()-1);//主线程中操作：定位到最后一个View
                //测试用：Toast.makeText(MainActivity.this, "收到的数据是：" + dataReceive, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialMessage();//初始化
        adapter=new MessageHandleAdapter(msgList);
        editText=(EditText) findViewById(R.id.etInputText);
        recyclerView=(RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new Thread(new Runnable() {             //启动监听线程
            @Override
            public void run() {
                while (true){
                    receiveDataByUdp();
                }
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int i=item.getItemId();
        switch (i){
            case R.id.itemIP:
                //NULL
                Intent intentIP=new Intent(MainActivity.this,IPActivity.class);
                startActivityForResult(intentIP,RESULT_CODE);
                //测试用：Toast.makeText(MainActivity.this,"您已进入设置IP地址界面",Toast.LENGTH_SHORT).show();
                break;
            case R.id.itemAbout:
                //NULL
                Intent intentAbout=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.itemExit:
                ActivityCollector.finishAll();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case RESULT_CODE:
                    String returnData=data.getStringExtra("data_return");
                    IP=returnData;
                    IP_FLAG=true;
                    //测试用：Toast.makeText(MainActivity.this,returnData,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
        else {
            Toast.makeText(MainActivity.this,"未检测到对方IP地址",Toast.LENGTH_SHORT).show();
        }
    }

    public void btnSendClick(View view){
        dataSend=editText.getText().toString();//获取用户要发送的数据
        if (!dataSend.isEmpty()){             //判断用户是否输入发送内容
            if (IP_FLAG){
                MessageHandle msg=new MessageHandle(dataSend,MessageHandle.TYPE_SEND);
                msgList.add(msg);//主线程中操作：更新数据列表
                adapter.notifyItemInserted(msgList.size()-1);//主线程中操作：有新消息时刷新RecyclerView
                recyclerView.scrollToPosition(msgList.size()-1);//主线程中操作：定位到最后一个View
                new Thread(new Runnable() {      //启动发送进程
                    @Override
                    public void run() {
                        sendDataByUDP(dataSend);
                    }
                }).start();
                editText.setText("");//清空输入框
            }
            else {
                Toast.makeText(MainActivity.this,"请先进入菜单设置IP",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MainActivity.this,"请先输入内容",Toast.LENGTH_SHORT).show();
        }
    }
    private void sendDataByUDP(String data) {
        try {
            myUdpSend = UdpMessageTool.getInstance(PORT_SEND);
            myUdpSend.setTimeOut(5000);// 设置超时为5s
            // 向服务器发数据
            myUdpSend.send(IP, PORT_RECEIVE, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        myUdpSend.close();
    }
    private void receiveDataByUdp(){
        try {
            myUdpReceive=UdpMessageTool.getInstance(PORT_RECEIVE);
            myUdpReceive.setTimeOut(5000);
            String result = myUdpReceive.receive();
            if (result != null) {
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        myUdpReceive.close();
    }
    private void initialMessage(){  //测试用
        /*MessageHandle msg1=new MessageHandle("Hello, my friend!", MessageHandle.TYPE_RECEIVE);
        msgList.add(msg1);
        MessageHandle msg2=new MessageHandle("Hi, who's that speaking?",MessageHandle.TYPE_SEND);
        msgList.add(msg2);
        MessageHandle msg3=new MessageHandle("This is Jarvis speaking.It's my pleasure to serve you!",MessageHandle.TYPE_RECEIVE);
        msgList.add(msg3);*/
    }
}
