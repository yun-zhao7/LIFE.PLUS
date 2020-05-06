package org.andresoviedo.communication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.andresoviedo.R;

public class Chat extends Activity {
    View tv_service,button7,chatchat;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tv_service=(TextView)findViewById(R.id.tv_service);
        chatchat=(EditText)findViewById(R.id.chatchat);
        button7=(Button)findViewById(R.id.button7);}
}
