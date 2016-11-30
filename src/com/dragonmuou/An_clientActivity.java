package com.dragonmuou;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class An_clientActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Handler x = new Handler();
        x.postDelayed(new Runnable(){   
            public void run() {   
                Intent i = new Intent(An_clientActivity.this, Main_face.class);  
                startActivity(i);   
                An_clientActivity.this.finish();    
            }   
        }, 10); 
    }
}