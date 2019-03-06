package com.example.jiaqili.coolchat;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView textView1,textView2,textView3,textView4,textView5,textView6,
            textView7,textView8,textView9,textView10,textView11,textView12;
    private String data1="1、应用介绍";
    private String data2="酷聊APP是一款基于UDP协议的双人聊天软件，在同一个局域网内能实现文本内容和手机输入法中自带表情图的收发。";
    private String data3="在数据收发的模块中，使用套接字DatagramSocket接受或发送数据报，用DatagramPacket存放数据报，从而实现基于UDP协议提供无连接的数据报服务。" +
            "在聊天界面的模块中，使用目前比较流行的RecyclerView控件，内部通过继承RecyclerView.Adapter适配器实现聊天内容的可滚动展示。" ;
    private String data4=" ";
    private String data5="2、使用流程";
    private String data6="①你和好友的手机连接同一个局域网（比如同一个路由器）";
    private String data7="②打开菜单，进入“设置IP”界面，输入好友的IP地址（同理对方也要如此操作）";
    private String data8="③点击“确认”按钮，并返回聊天界面";
    private String data9="④在相应区域内编辑要发送的内容，开始聊天";
    private String data10=" ";
    private String data11="3、手机要求";
    private String data12="本应用在Android 8.1上做了充分测试，并且向下兼容到Android 4.1。经实测可以在局域网内通信，" +
            "但考虑到UDP协议的局限性，不建议在互联网上使用。本应用在华为、小米、一加、OPPO、Vivo等机型上的测试均可以通过。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initializeWidget();
    }

    public void initializeWidget(){
        textView1=(TextView) findViewById(R.id.txt1);
        textView2=(TextView) findViewById(R.id.txt2);
        //textView3=(TextView) findViewById(R.id.txt3);
        textView4=(TextView) findViewById(R.id.txt4);
        textView5=(TextView) findViewById(R.id.txt5);
        textView6=(TextView) findViewById(R.id.txt6);
        textView7=(TextView) findViewById(R.id.txt7);
        textView8=(TextView) findViewById(R.id.txt8);
        textView9=(TextView) findViewById(R.id.txt9);
        textView10=(TextView) findViewById(R.id.txt10);
        textView11=(TextView) findViewById(R.id.txt11);
        textView12=(TextView) findViewById(R.id.txt12);
        textView1.setText(data1);
        textView2.setText("       "+data2);
        //textView3.setText("       "+data3);
        textView4.setText("       "+data4);
        textView5.setText(data5);
        textView6.setText(" "+data6);
        textView7.setText(" "+data7);
        textView8.setText("         "+data8);
        textView9.setText("         "+data9);
        textView10.setText(data10);
        textView11.setText(data11);
        textView12.setText("       "+data12);

        textView6.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener());
        textView7.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener());
        textView8.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener());
        textView9.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener());
    }
    private class OnTvGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            textView6.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            final String newText6 = autoSplitText(textView6,"、、");
            if (!TextUtils.isEmpty(newText6)) {
                textView6.setText(newText6);
            }
            textView7.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            final String newText7 = autoSplitText(textView7,"、、");
            if (!TextUtils.isEmpty(newText7)) {
                textView7.setText(newText7);
            }
            textView8.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            final String newText8 = autoSplitText(textView8,"、、");
            if (!TextUtils.isEmpty(newText8)) {
                textView8.setText(newText8);
            }
            textView9.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            final String newText9 = autoSplitText(textView9,"、、");
            if (!TextUtils.isEmpty(newText9)) {
                textView9.setText(newText9);
            }
        }
    }
    private String autoSplitText(final TextView tv, final String indent) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将缩进处理成空格
        String indentSpace = "";
        float indentWidth = 0;
        if (!TextUtils.isEmpty(indent)) {
            float rawIndentWidth = tvPaint.measureText(indent);
            if (rawIndentWidth < tvWidth) {
                while ((indentWidth = tvPaint.measureText(indentSpace)) < rawIndentWidth) {
                    indentSpace += " ";
                }
            }
        }

        //将原始文本按行拆分
        String [] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    //若从手动换行的第二行开始加上悬挂缩进，条件应该为(lineWidth < 0.1f && cnt != 0)
                    if (lineWidth < 0.1f ) {
                        sbNewText.append(indentSpace);
                        lineWidth += indentWidth;
                    }
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }
}
