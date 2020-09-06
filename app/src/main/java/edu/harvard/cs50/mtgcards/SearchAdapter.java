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
import java.util.Arrays;
import java.util.List;

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
    private ArrayList<String> formats;
    private ArrayList<TextView> formatTextViewLegalities;
    private ArrayList<TextView> formatTextViewNames;
    private ArrayList<String> formatNames;

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
        createLegalityLists();

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
                            // TODO In the future we will create CARD en LEGALITY objects
                            JSONObject legalEntries = response.getJSONObject("legalities");

                            for (int i = 0; i < formats.size(); i++) {
                                String legalText = legalEntries.getString(formats.get(i));
                                setLegal(legalText, formatTextViewLegalities.get(i));
                                formatTextViewNames.get(i).setText(formatNames.get(i));
                            }

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

    private void createLegalityLists() {
        createFormatsList();
        createFormatTextViewLegalities();
        createTextViewNames();
        createFormatNames();
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

    private void createFormatsList() {
        List<String> formatsList = Arrays.asList("standard", "modern", "pauper", "commander");
        formats = new ArrayList<>();
        formats.addAll(formatsList);
    }

    private void createFormatTextViewLegalities() {
        List<TextView> formatTextViewLegalitiesList = Arrays.asList(legalStandardBox, legalModernBox, legalPauperBox, legalCommanderBox);
        formatTextViewLegalities = new ArrayList<>();
        formatTextViewLegalities.addAll(formatTextViewLegalitiesList);
    }

    private void createTextViewNames() {
        List<TextView> formatTextViewNameList = Arrays.asList(legalStandardText, legalModernText, legalPauperText, legalCommanderText);
        formatTextViewNames = new ArrayList<>();
        formatTextViewNames.addAll(formatTextViewNameList);
    }

    private void createFormatNames() {
        List<String> formatNameList = Arrays.asList("Standard", "Modern", "Pauper", "Commander");
        formatNames = new ArrayList<>();
        formatNames.addAll(formatNameList);
    }
}