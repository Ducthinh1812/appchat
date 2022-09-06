package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appchat.Adapter.MessageAdapter;
import com.example.appchat.Adapter.UserAdapter;
import com.example.appchat.Model.Chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    TextView username;
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;
    String send, receiver, str_send;
    Intent intent;
    SwipeRefreshLayout swipeRefreshLayout;
    Chat chat;
    boolean data;
    MesseageViewModel messeageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();
        send = intent.getStringExtra("send");
        receiver = intent.getStringExtra("receiver");
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.refresh);
        //
        messeageViewModel = new ViewModelProvider(this).get(MesseageViewModel.class);
        messeageViewModel.getChatMutableLiveData().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                readMessage();
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                messageAdapter = new MessageAdapter(getApplicationContext(), mchat);
                recyclerView.setAdapter(messageAdapter);
            }
        });
        //
        swipeRefreshLayout.setOnRefreshListener(() -> {
            mchat.clear();
            recyclerView.setAdapter(messageAdapter);
            recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
            swipeRefreshLayout.setRefreshing(false);
        });
        //
//
//        messageAdapter = new MessageAdapter(getApplicationContext(), mchat);
//        recyclerView.setAdapter(messageAdapter);
        //
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_send = text_send.getText().toString().trim();
                if (str_send.equalsIgnoreCase("")) {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage();
                }
                text_send.setText("");
            }

        });
    }


    private void sendMessage() {
        String urlUpdateSp = "https://appsellrice.000webhostapp.com/appchat/sender.php";
        RequestQueue requestQueue = Volley.newRequestQueue(MessageActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdateSp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Thêm Thành Công")) {
                    Toast.makeText(MessageActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    messeageViewModel.addmess(chat);
                    recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                } else {
                    Toast.makeText(MessageActivity.this, " Thêm không Thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MessageActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("send", send);
                params.put("receiver", receiver);
                params.put("message", str_send);
                params.put("isseen", "true");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void readMessage() {
        mchat = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(MessageActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://appsellrice.000webhostapp.com/appchat/getMessage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (result.equals("thanh cong")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            mchat.add(new Chat(
                                    object.getString("Sender"),
                                    object.getString("Receiver"),
                                    object.getString("Message"),
                                    object.getString("isseen")
                            ));
                            messageAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                            chat = mchat.get(i);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy sản nào phù hợp." + response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MessageActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("send", send);
                params.put("receiver", receiver);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("aaaaaaaaa", "sssssssssss1");
//        mchat.clear();
//        readMessage();
//        mchat.add(new Chat(send,receiver,str_send,"true"));
//        recyclerView.setAdapter(messageAdapter);
        Log.e("aaaaaaaaa", "sssssssssss");
    }
}