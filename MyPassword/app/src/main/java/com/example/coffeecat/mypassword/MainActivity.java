package com.example.coffeecat.mypassword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static int passwdTry = 0;
    private static String fingerPrint ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     *
     * @param strSrc
     *            要加密的字符串
     * @param encName
     *            加密类型
     * @return
     */
    public static String doEncrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }




    public void onClick_Event(View view) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String datetime = df.format(new Date());
        String ps =  ((EditText) findViewById(R.id.editText)).getText().toString();
        String s = doEncrypt(ps, "");
        String part = s.substring(18,60) + datetime;
       // String part = s.substring(17,60) + datetime;
        fingerPrint = s.substring(0,18);
        s =  doEncrypt(part, "");
        
        String x = "c2dd22cd612d676d33c22d62bc1d613c83d2917d6c"; 保护密码的sha256第19个hex字符至60个hex字符
       
        x = x + datetime;
        String sx = doEncrypt(x,"");
        if(s.equals(sx))
        {
            System.out.println(1);
            EditText ed = (EditText) findViewById(R.id.editText2);
            ed.setVisibility(View.VISIBLE);
            Button bt = (Button) findViewById(R.id.button2);
            bt.setVisibility(View.VISIBLE);
            bt.setClickable(true);
            EditText ed1 = (EditText) findViewById(R.id.editText);
            ed1.setVisibility(View.INVISIBLE);
            Button bt1 = (Button) findViewById(R.id.button);
            bt1.setVisibility(View.INVISIBLE);
            bt1.setClickable(false);
            passwdTry = 0;

        }else
        {
            System.out.println(0);
            passwdTry++;
            EditText ed = (EditText) findViewById(R.id.editText2);
            ed.setVisibility(View.INVISIBLE);
            Button bt = (Button) findViewById(R.id.button2);
            bt.setVisibility(View.INVISIBLE);
            bt.setClickable(false);
            if(passwdTry>=3){
                EditText ed1 = (EditText) findViewById(R.id.editText);
                ed1.setVisibility(View.INVISIBLE);
                Button bt1 = (Button) findViewById(R.id.button);
                bt1.setVisibility(View.INVISIBLE);
                bt1.setClickable(false);
            }
        }

    }

    public void onClick2_Event(View view) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String datetime = df.format(new Date());
        String ps =  ((EditText) findViewById(R.id.editText2)).getText().toString();
        ps = ps + fingerPrint;
        String str = doEncrypt(ps, "");

        String part1;
        String part2;
        String part3;
        String part4;
        int a = 0;
        int b = str.length();
        int c = 0;
        int p = str.length();
        do
        {

            System.out.println("a:"+String.valueOf(a));
            System.out.println(str.length());
            part1 = str.substring(a,a+4);
            System.out.println(part1);
            a=a+1;
        }while(part1.toUpperCase().equals(part1.toLowerCase()) && a+4 < str.length());
        do
        {

            System.out.println("b:"+String.valueOf(b));
            part2 = str.substring(b-3,b);
            System.out.println(part2);
            b=b-1;
        }while(part2.toLowerCase().equals(part2.toUpperCase())  && b-3 >= 0);

        do
        {

            System.out.println("c:"+String.valueOf(c));
            part3 = str.substring(c,c+1);
            System.out.println(part2);
            c=c+1;
        }while( !part3.toLowerCase().equals(part3.toUpperCase())  && c+1 < str.length());
        c = c % 7;
        do
        {

            System.out.println("p:"+String.valueOf(p));
            part4 = str.substring(p-1,p);
            System.out.println(part2);
            p=p-1;
        }while(part4.toLowerCase().equals(part4.toUpperCase())  && p-1 >= 0);
        String sA = "~!@#$%^&*()_+-=";
        System.out.println("xxxdddddxxxxx");
        p = p % sA.length() ;
        String apt = sA.substring(p,p+1);
        String tmppart =  part1.toUpperCase() + part2.toLowerCase();

        System.out.println("xxxxxxxx");


        String part = tmppart.substring(0,c) + apt + tmppart.substring(c,7);

        TextView tx = (TextView) findViewById(R.id.textView);
        tx.setText(part);
    }
}
