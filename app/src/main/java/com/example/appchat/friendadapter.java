package com.example.appchat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class friendadapter extends RecyclerView.Adapter<friendadapter.friendHolder> {

    Context context;
    ArrayList<messent> ArrayList;
    String urlSp = "https://website1812.000webhostapp.com/Coffee/Sanpham.php";
    public friendadapter(Context context, ArrayList<messent> ArrayList) {
        this.context = context;
        this.ArrayList = ArrayList;
    }

    @NonNull
    @Override
    public friendadapter.friendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfiend , parent, false);
        return new friendadapter.friendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull friendadapter.friendHolder holder, @SuppressLint("RecyclerView") int position) {
        messent mes =ArrayList.get(position);

        holder.tvname.setText(mes.getFriend());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("Please Wait..");
                progressDialog.show();
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSp, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
//                        Intent intent = new Intent(context, ChiTietSanPham.class);
//                        intent.putExtra("data",SanPhamArrayList.get(position));
//                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("TenSP",mes.getName());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
                notifyItemChanged(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return ArrayList.size();
    }

    public class friendHolder extends RecyclerView.ViewHolder {
        TextView tvname;
        CardView cardView;
        public friendHolder(@NonNull View itemView) {
            super(itemView);
            cardView= itemView.findViewById(R.id.CarviewSP);
            tvname = (TextView) itemView.findViewById(R.id.tvname);

        }
    }

}
