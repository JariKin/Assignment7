package com.example.assignment7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/*
En saanut toimimaan kokonaan

Ymmärrän (ehkä) idean, että pitäisi tehdä uusi kutsu file url:iin ja
jotenkin saada tämä sieltä ulos esim Picassoon mutta omat taidot ei riitä
 */

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue = null;
    private TextView owner;
    private TextView license;
    private ImageView imageView;
    String fileurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.image);
        owner = findViewById(R.id.owner);
        license = findViewById(R.id.license);
        setSupportActionBar(toolbar);

        // Button to the toolbar
        Button button = new Button(this);
        Toolbar.LayoutParams l1 = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        l1.gravity = Gravity.START;
        button.setLayoutParams(l1);
        button.setText("Loremflicker's");
        toolbar.addView(button);
        int buttonID = 1;
        button.setId(buttonID);

        // EditText to the toolbar
        final EditText text = new EditText(this);
        Toolbar.LayoutParams l2 = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        l2.gravity = Gravity.CENTER;
        text.setLayoutParams(l2);
        toolbar.addView(text);
        int txtID = 2;
        text.setId(txtID);

        button = findViewById(buttonID);

        //queue = Volley.newRequestQueue(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {

                    // terminate app if edittext is empty
                    if(text.getText().toString().matches("")){
                        finish();
                        System.exit(0);
                    }

                    String url = "https://loremflickr.com/json/g/320/240/" + text.getText().toString() + "/all";
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        license.setText(response.getString("license"));
                                        owner.setText(response.getString("owner"));
                                        fileurl = response.getString("file");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                /*
                                Jotain Picasso kokeilua, ei toimi

                                Picasso.with(this).load(fileurl).into(imageView);
                                Picasso.with(getApplicationContext()).load(fileurl[0]).into(imageView);
                                 */



                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    MySingleton.getInstance(MainActivity.this).addToRequestque(request);

                } else {
                    Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        });
    }



    // function to check network
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
