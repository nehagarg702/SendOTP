/*****
 * This fragment display the list of OTP send.
 * The list of data is fetched from the database.
 * If there is no data then it show NO OTP IS SENT
 */
package com.example.dell.sendotp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Messages extends Fragment {
    SaveData data;
    SQLiteDatabase db;
    List<String> Item=new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.messages1, container, false);
        //Sqlite Database Connectivity
            data = new SaveData(view.getContext(), "MyDB.db", null, 1);
            db = data.getReadableDatabase();
            String columns[] = new String[]{"name", "date", "time", "combinetime", "timeinm", "phone", "otp"};
            Cursor c = db.query("SaveData", columns, null, null, null, null, "timeinm Desc");
            while (c.moveToNext()) {
                //Fetching data from database and adding it into list Item
                Item.add("Name: " + c.getString(0) + " Number: " + c.getString(5) + " OTP: " + c.getString(6) + " Time: " + c.getString(3));
            }
            if(c.getCount()==0)
            {
                TextView tv=(TextView)view.findViewById(R.id.textView);
                tv.setText("No OTP is Sent");
            }
            else{
                //Displaying List Data
            ListView listview = (ListView) view.findViewById(R.id.listView);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, Item);
            listview.setAdapter(adapter);
       }
        return view;
    }
}
