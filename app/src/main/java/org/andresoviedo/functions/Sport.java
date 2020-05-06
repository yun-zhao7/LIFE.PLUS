package org.andresoviedo.functions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.andresoviedo.R;



public class Sport extends Activity {
    View bu,km,qianka,title,button4;
    protected EditText distance;
    protected EditText steps;
    protected EditText calorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        bu=(TextView)findViewById(R.id.bu);
        km=(TextView)findViewById(R.id.km);
        qianka=(TextView)findViewById(R.id.qianka);
        steps=(EditText)findViewById(R.id.steps);
        distance=(EditText)findViewById(R.id.distance);
        calorie=(EditText)findViewById(R.id.calorie);
        title=(TextView)findViewById(R.id.title);
        button4=(Button)findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todaydistance = distance.getText().toString();
                String todaysteps = steps.getText().toString();
                String todaycalorie = calorie.getText().toString();
                SharedPreferences pref=getSharedPreferences("walkDistance",MODE_PRIVATE);
                int sportquantity = pref.getInt("walkDistance",0);
                sportquantity = sportquantity+1000*Integer.parseInt(todaydistance)+Integer.parseInt(todaysteps)+10*Integer.parseInt(todaycalorie);
                SharedPreferences.Editor editor=getSharedPreferences("walkDistance",MODE_PRIVATE).edit();
                editor.putInt("walkDistance",sportquantity);
                editor.apply();
                Toast.makeText(Sport.this, "你已经完成今日运动，获得金币", Toast.LENGTH_SHORT).show();
            }
        });
    }}
