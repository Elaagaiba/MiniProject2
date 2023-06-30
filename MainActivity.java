package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnPass;
    TextView tvQuote,tvAuthor;
    ImageView ivFavorite;
    ToggleButton tbPinUnpin;
    SharedPreferences sharedPreferences;
    boolean isFavorite = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPass = findViewById(R.id.btnPass);
        tbPinUnpin = findViewById(R.id.tbPinUnpin);
        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        ivFavorite = findViewById(R.id.ivFavorite);

        sharedPreferences = getSharedPreferences("Pined_Qiotes",MODE_PRIVATE);

        String quote = sharedPreferences.getString("quote",null);
        //getRandomQuotes();
        if(quote == null) {
            getRandomQuotes();


        }else{

            tvQuote.setText(sharedPreferences.getString("quote",null));
            tvAuthor.setText(sharedPreferences.getString("author",null));
            tbPinUnpin.setChecked(true);
            isFavorite = true;
        }

        tbPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(isChecked){
                    editor.putString("quote",tvQuote.getText().toString());
                    editor.putString("author",tvAuthor.getText().toString());
                }else{
                    editor.putString("quote",null);
                    editor.putString("author",null);
                }
                editor.commit();
        }
        });




        ivFavorite.setOnClickListener(v-> {
            if(isFavorite)
                ivFavorite.setImageResource(R.drawable.baseline_favorite_24);
            else
                ivFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
            isFavorite =!isFavorite;
        });

        btnPass.setOnClickListener(v-> {
            finish();
        });

    }

    private void getRandomQuotes() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        //region StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            tvQuote.setText(jsonObject.getString("quote"));
                            tvAuthor.setText(jsonObject.getString("author"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        }
                    },
                     new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         error.printStackTrace();
                         tvQuote.setText("That didn't work!");
                         tvAuthor.setText("!!!");
                     }
                });

        stringRequest.setTag("TAG");
        queue.add(stringRequest);
        //endregion
    }
}