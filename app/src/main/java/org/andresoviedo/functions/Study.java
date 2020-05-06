package org.andresoviedo.functions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import org.andresoviedo.R;

public class Study extends Activity {
    Button button1;
    Chronometer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        button1=findViewById(R.id.button1);
        timer = findViewById(R.id.chronometer);

        button1.setOnClickListener(v -> {
            String str = button1.getText().toString();//获取按钮字符串
            if(str.equals("开始学习")){ //切换按钮文字
                button1.setText("结束学习");
/*                SharedPreferences.Editor editor=getSharedPreferences("studytime",MODE_PRIVATE).edit();
                editor.putInt("studytime",0);
                editor.apply();*/
                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                setFormat(timer);
                timer.start();
            }
            else{
                button1.setText("开始学习");
                SharedPreferences pref=getSharedPreferences("studyTime",MODE_PRIVATE);
                int credit = pref.getInt("studyTime",0);
                credit = credit + (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60) % 60;
                SharedPreferences.Editor editor=getSharedPreferences("studyTime",MODE_PRIVATE).edit();
                editor.putInt("studyTime",credit);
                editor.apply();
                timer.stop();
                Toast.makeText(Study.this, "你已经完成今日学习，获得金币", Toast.LENGTH_SHORT).show();
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

