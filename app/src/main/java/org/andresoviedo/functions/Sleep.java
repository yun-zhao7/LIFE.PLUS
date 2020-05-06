package org.andresoviedo.functions;

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import org.andresoviedo.R;


public class Sleep extends Activity {
    Button button1;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        button1=findViewById(R.id.button1);
        timer = findViewById(R.id.chronometer);

        button1.setOnClickListener(v -> {
            String str = button1.getText().toString();//获取按钮字符串
            if(str.equals("开始睡眠")){ //切换按钮文字
                button1.setText("结束睡眠");
/*                SharedPreferences.Editor editor=getSharedPreferences("sleeptime",MODE_PRIVATE).edit();
                editor.putInt("sleeptime",0);
                editor.apply();*/
                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                setFormat(timer);
                timer.start();
            }
            else{
                button1.setText("开始睡眠");
                SharedPreferences pref=getSharedPreferences("sleepTime",MODE_PRIVATE);
                int credit = pref.getInt("sleepTime",0);
                credit = credit + (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60) % 60;
                SharedPreferences.Editor editor=getSharedPreferences("sleepTime",MODE_PRIVATE).edit();
                editor.putInt("sleepTime",credit);
                editor.apply();
                timer.stop();
                Toast.makeText(Sleep.this, "你已经完成今日睡眠，获得金币", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setFormat(Chronometer chronometer){
        int hour = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60 / 60);
        int minute = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60) % 60;
        int second = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000) % 60;
        if (hour < 1) {
            if (minute == 59 && second == 59) {
                chronometer.setFormat("0" + "%s");
            } else {
                chronometer.setFormat("0" + String.valueOf(hour) + ":%s");
            }
        } else if (hour < 10) {
            chronometer.setFormat("0" + "%s");
        } else {
            chronometer.setFormat("%s");
        }
    }
}
