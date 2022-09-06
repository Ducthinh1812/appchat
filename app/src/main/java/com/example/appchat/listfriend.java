package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class listfriend extends AppCompatActivity {
    RecyclerView rclfriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfriend);
        rclfriend=findViewById(R.id.rclfriend);
    }
}