package edu.harvard.cs50.mtgcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchAdapter extends AppCompatActivity {

    private TextView errorMessage;
    private TextView legalities;
    private TextView legalStandardBox;
    private TextView legalStandardText;
    private TextView legalModernBox;
    private TextView legalModernText;
    private TextView legalPauperBox;
    private TextView legalPauperText;
    private TextView legalCommanderBox;
    private TextView legalCommanderText;
    private String searchedText;
    private String url;
    private RequestQueue que;
    private ImageView cardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_adapter);

        errorMessage = findViewById(R.id.errorMessage);
        legalities = findViewById(R.id.legalities);
        legalStandardBox = findViewById(R.id.legalStandardBox);
        legalStandardText = findViewById(R.id.legalStandardText);
        legalModernBox = findViewById(R.id.legalModernBox);
        legalModernText = findViewById(R.id.legalModernText);
        legalPauperBox = findViewById(R.id.legalPauperBox);
        legalPauperText = findViewById(R.id.legalPauperText);
        legalCommanderBox = findViewById(R.id.legalCommanderBox);
        legalCommanderText = findViewById(R.id.legalCommanderText);
        cardImage = findViewById(R.id.cardImage);
        que = Volley.newRequestQueue(this);

        loadCard();
    }

    public void loadCard() {
        errorMessage.setText("");
        legalities.setText("");
        legalStandardBox.setText("");
        legalStandardText.setText("");
        legalModernBox.setText("");
        legalModernText.setText("");
        legalPauperBox.setText("");
        legalPauperText.setText("");
        legalCommanderBox.setText("");
        legalCommanderText.setText("");

        getUrl();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Set the normal sized image of the card
                            JSONObject imageEntries = response.getJSONObject("image_uris");
                            String imageUrl = imageEntries.getString("normal");
                            Picasso.get().load(imageUrl).into(cardImage);

                            // Set legalities
                            JSONObject legalEntries = response.getJSONObject("legalities");

                            String legalText = legalEntries.getString("standard");
                            setLegal(legalText, legalStandardBox);
                            legalStandardText.setText(R.string.legalStandardText);

                            legalText = legalEntries.getString("modern");
                            setLegal(legalText, legalModernBox);
                            legalModernText.setText(R.string.legalModernText);

                            legalText = legalEntries.getString("pauper");
                            setLegal(legalText, legalPauperBox);
                            legalPauperText.setText(R.string.legalPauperText);

                            legalText = legalEntries.getString("commander");
                            setLegal(legalText, legalCommanderBox);
                            legalCommanderText.setText(R.string.legalCommanderText);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                errorMessage.setText("Sorry, we can't find a card named \"" + searchedText + "\"");
            }
        });

        que.add(request);
    }

    public void getUrl() {
        Intent intent = getIntent();
        searchedText = intent.getStringExtra(MainActivity.USER_SEARCH);
        searchedText = searchedText.replace(" ", "+");
        url = "https://api.scryfall.com/cards/named?fuzzy=" + searchedText;
    }

    public void setLegal(String legalInfo, TextView textView) {
        if (legalInfo.equals("legal")) {
            textView.setText("LEGAL");
            textView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.legalGreen));
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        } else {
            textView.setText("NOT LEGAL");
            textView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.notLegalRed));
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    }
}