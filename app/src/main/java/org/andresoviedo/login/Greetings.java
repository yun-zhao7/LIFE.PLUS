package org.andresoviedo.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.andresoviedo.MainActivity;
import org.andresoviedo.R;
import org.andresoviedo.app.model3D.view.ModelActivity;

public class Greetings extends Activity{

    View login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);

        login= findViewById(R.id.loginId);
        register = findViewById(R.id.registerID);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Greetings.this.startActivity(new Intent(Greetings.this, loginActivity.class));
                Greetings.this.finish();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Greetings.this.startActivity(new Intent(Greetings.this, RegisterActivity.class));
                Greetings.this.finish();
            }
        });

    }
}
