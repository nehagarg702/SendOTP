/**********
 * The purpose of this fragment is to show the list of contacts in begining.
 * The list of contact is fetched from FakeJsonData class by using Json object and showed in listview.
 * On clicking any listitem, firstly check the length of number. if number length is more than 10 then go to next activity Send Message
 */

package com.example.dell.sendotp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends Fragment {
    //List Declarations
    List<String> Phoneno=new ArrayList<String>();
    List<String> Name=new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.contacts1, container, false);
        try {
            // Create the root JSONObject from the JSON string.
            JSONObject jsonRootObject = new JSONObject(new FakeJsonData().fakeJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("Contacts");

            //Iterate the jsonArray and add the info of JSONObjects into lists
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Name.add(jsonObject.optString("Fname").toString()+" "+jsonObject.optString("Lname").toString());
                Phoneno.add(jsonObject.optString("PhoneNo").toString());
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    // Creating List contain contacs infornation
        ListView listview=(ListView)view.findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, Name);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           // on click of list item go to next activity by using intent.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                if(Phoneno.get(position).toString().length()<10)
                {
                    Toast.makeText(view.getContext(), "Invalid Nummber", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(getContext(), SendMessage.class);
                    i.putExtra("Name", Name.get(position).toString());
                    i.putExtra("Number", Phoneno.get(position).toString());
                    startActivity(i);
                }
            }
        });
        return view;
    }
}
