/******
 * This is the activity where OTP is send by using MSG91 API.
 * On pressing of butten send data is send and the details of OTP message is also added into database
 * Rest api is used to set connection and json is used to read the data from url
 */
package com.example.dell.sendotp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class OTP extends AppCompatActivity {
    int n;
    Date currentTime;
    SaveData data;
    String Name, Number,url,Message,Date,Time;
    //API_Key
    public static final String API_SID = "233727AtCVTkHsG5b8181f0";
    private BottomSheetBehavior mBottomSheetBehavior;
    private Button sendMessage;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
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
        currentTime = Calendar.getInstance().getTime();
        Date= String.valueOf(currentTime.getDate())+" "+String.valueOf(currentTime.getMonth())+" "+String.valueOf(currentTime.getYear());
        Time= String.valueOf(currentTime.getHours())+":"+String.valueOf(currentTime.getMinutes())+":"+String.valueOf(currentTime.getSeconds());
        data=new SaveData(this, "MyDB.db", null, 1);
        Name=getIntent().getStringExtra("Name");
        Number=getIntent().getStringExtra("Number");
        //Generate Random SIX-DIGIT OTP
        Random rnd = new Random();
        n = 100000 + rnd.nextInt(900000);
        TextView tv=(TextView)findViewById(R.id.textView);
        Message="Hi. Your OTP is: "+n;
        tv.setText(Message);
        sendMessage=(Button)findViewById(R.id.button);
        //OTP API
        url="http://control.msg91.com/api/sendotp.php?authkey="+API_SID+"&message="+Message+"&sender=KISSAN&mobile=91"+Number+"&otp="+n;
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternetConenction())
                    new FetchApi().execute(url);
            }
        });
    }

    //Checking Network Connectivity
    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Check Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    // Sending OTP and Adding data into Database
    private class FetchApi extends AsyncTask<String, String, String> {
        private HttpURLConnection connection;
        private BufferedReader reader;
        File path = new File(android.os.Environment.getExternalStorageDirectory(), "Message.json");
        fileoperation fileOpreation=new fileoperation(getApplicationContext(),path);

        @Override
        protected String doInBackground(String... params) {
            try {
                URL myUrl = new URL(params[0]);
                connection = (HttpURLConnection) myUrl.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //  dialog.hide();
            } catch (IOException e) {
                e.printStackTrace();
                //dialog.hide();
            } finally {
                if (connection != null) {
                    connection.disconnect();

                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (EOFException e){

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), "OTP sent successfully ", Toast.LENGTH_SHORT).show();
                db=data.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put("name", Name);
                cv.put("time",Time);
                cv.put("date",Date);
                cv.put("combinetime",currentTime.toString());
                cv.put("timeinm", System.currentTimeMillis());
                cv.put("otp",n);
                cv.put("phone",Number);
                db.insert("SaveData", null, cv);
                String jsonString=fileOpreation.read();
                boolean writeJson=false;
                // if Message sent successfully data store in Json file..........................

                if(fileOpreation.isFileExist()){
                    writeJson=fileOpreation.write(Name, System.currentTimeMillis()+"",n+"",1);


                } else {
                    writeJson=fileOpreation.create(Name, System.currentTimeMillis()+"",n+"",1);

                }

                if(writeJson){
                    sendMessage.setEnabled(false);
                    // close dailog sheet.... and progress..............
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheetBehavior.setPeekHeight(0);

                }
            }
        }

    }

}
