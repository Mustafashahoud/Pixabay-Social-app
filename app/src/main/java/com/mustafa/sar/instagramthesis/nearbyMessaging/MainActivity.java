package com.mustafa.sar.instagramthesis.nearbyMessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mustafa.sar.instagramthesis.R;

public class MainActivity extends AppCompatActivity {

    private Button sendVoice;
    private Button sendText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_voice);
        setButtonsListener();
    }

    private void setButtonsListener(){
        sendVoice = findViewById(R.id.sendVoice);
        sendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
                startActivity(intent);
            }
        });
        sendText = findViewById(R.id.sendText);
        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextActivity.class);
                startActivity(intent);
            }
        });

    }
}
