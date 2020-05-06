package org.andresoviedo.functions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.andresoviedo.R;

public class Coin extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);
        TextView coinPoints;
        coinPoints=findViewById(R.id.coinNumber);

        SharedPreferences pref=getSharedPreferences("coinnum",MODE_PRIVATE);
        int coinNum = pref.getInt("CoinNumber",0);
        String coinNums = String.valueOf(coinNum);
        coinPoints.setText(coinNums);

    }
}
