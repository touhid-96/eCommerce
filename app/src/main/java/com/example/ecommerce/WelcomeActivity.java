package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = WelcomeActivity.this;

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                //super.run();
                try
                {
                    sleep(1500);
                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally
                {
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
