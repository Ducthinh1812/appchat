package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appchat.Adapter.UserAdapter;
import com.example.appchat.Model.Chat;
import com.example.appchat.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListChatActivity extends AppCompatActivity {
    RecyclerView rclfriend;
    String voley;
    ArrayList<Chat> list;
    UserAdapter friendadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);
        rclfriend=findViewById(R.id.rclfriend);
        Intent intent=getIntent();
        voley=intent.getStringExtra("data");
        Log.e("aaaa",voley);
        getdata();
        rclfriend.setHasFixedSize(true);
        rclfriend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();
        friendadapter = new UserAdapter(getApplicationContext(), list,true);
        rclfriend.setAdapter(friendadapter);
    }

    private void getdata() {
        final ProgressDialog progressDialog = new ProgressDialog(ListChatActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(ListChatActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://appsellrice.000webhostapp.com/appchat/getlistchat.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    Log.e("ssss",response);
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (result.equals("thanh cong")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            list.add(new Chat(
                                    object.getString("Sender"),
                                    object.getString("Receiver"),
                                    object.getString("Message"),
                                    object.getString("isseen")
                            ));
                            Log.e("ssssss",list.toString());
                            friendadapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy sản nào phù hợp.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ListChatActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",voley);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}