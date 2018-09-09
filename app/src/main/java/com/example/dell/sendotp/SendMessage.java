/******
 * This activity show the details of Contact Selected and on press of send message button,
 * nex activity open which show the random generate OTP and we have to press send for sending OTP.
 */
package com.example.dell.sendotp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SendMessage extends AppCompatActivity {
    String Name, Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Name=getIntent().getStringExtra("Name");
        Number=getIntent().getStringExtra("Number");
        TextView tv=(TextView)findViewById(R.id.textView);
        TextView tv1=(TextView)findViewById(R.id.textView1);
        tv.setText("Name: "+Name);
        tv1.setText("Number: "+Number);
        Button send=(Button)findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),OTP.class);
                intent.putExtra("Number",Number);
                intent.putExtra("Name",Name);
                startActivity(intent);
            }
        });
    }

}
