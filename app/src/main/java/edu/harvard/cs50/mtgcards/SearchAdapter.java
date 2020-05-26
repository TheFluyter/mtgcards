package edu.harvard.cs50.mtgcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchAdapter extends AppCompatActivity {

    private TextView textView;
    private Intent intent;
    private String searchedText;
    private String url;
    private String name;
    private RequestQueue que;
    private ImageView cardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_adapter);

        textView = findViewById(R.id.cardName);
        cardImage = findViewById(R.id.cardImage);
        que = Volley.newRequestQueue(this);

        loadCard();
    }

    public void loadCard() {
        textView.setText("");
        getUrl();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the name of the card
                            name = "Card name: " + response.getString("name");
                            textView.setText(name);

                            // Get the normal sized image of the card
                            JSONObject imageEntries = response.getJSONObject("image_uris");
                            String imageUrl = imageEntries.getString("normal");
                            Picasso.get().load(imageUrl).into(cardImage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Sorry, we can't find a card named " + searchedText);
            }
        });

        que.add(request);
    }

    public void getUrl() {

        intent = getIntent();
        searchedText = intent.getStringExtra(MainActivity.USER_SEARCH);
        searchedText = searchedText.replace(" ", "+");
        url = "https://api.scryfall.com/cards/named?fuzzy=" + searchedText;
    }
}