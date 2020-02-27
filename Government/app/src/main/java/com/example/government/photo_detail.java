package com.example.government;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class photo_detail extends AppCompatActivity {

    private TextView locationTextView;
    private TextView officenameTextView;
    private TextView personNameTextView;
    private TextView partyNameTextView;
    private ImageView personImageView;
    private ImageView partySymbolImageView;
    private boolean checknet;
    private Government government;
    private String imageUrl;
    private static final String msURL_repu = "https://www.gop.com/";
    private static final String msURL_demo = "https://democrats.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ConstraintLayout currentlauout = (ConstraintLayout) findViewById(R.id.photodetaillayout);
        Intent intent_zoom = getIntent();
        checknet =  doNetCheck();
        if (intent_zoom.hasExtra(Government.class.getName())) {

            government = (Government) intent_zoom.getSerializableExtra(Government.class.getName());

            locationTextView = findViewById(R.id.locationpdtxt);
            locationTextView.setText(government.getDisplayFinaltext());

            officenameTextView = findViewById(R.id.titledptxt);
            officenameTextView.setText(government.getTitle());

            personNameTextView = findViewById(R.id.personNamepdtxt);
            personNameTextView.setText(government.getName());


            loadBackground(government.getParty(), currentlauout);

            personImageView = findViewById(R.id.personImageViewpd);
            imageUrl = government.getPimageurl();
            loadRemotePersonImage(imageUrl);

            partySymbolImageView = findViewById(R.id.partyimageviewdp);
            //check for the party and set appropriate image
            loadPartImage(government.getParty());

        }


    }

    public void clickLogo(View V)
    {
        checknet = doNetCheck();
        if(checknet == true)
        {
            if(government.getParty().toUpperCase().contains("DEMO"))
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(msURL_demo));
                startActivity(i);
            }
            else if(government.getParty().toUpperCase().contains("REPU"))
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(msURL_repu));
                startActivity(i);
            }
            else
            {

            }
        }

    }
    private void loadPartImage(String party)
    {
        if (party.isEmpty()) {
            partySymbolImageView.setVisibility(View.INVISIBLE);
        } else if (party.toUpperCase().contains("REPU")) {
            partySymbolImageView.setImageResource(R.drawable.rep_logo);
        } else if (party.toUpperCase().contains("DEMO")) {
            partySymbolImageView.setImageResource(R.drawable.dem_logo);
        }
    }

    private void loadRemotePersonImage(String imageUrl)
    {
        //Log.d(TAG, "loadImage: " + imageUrl);
        checknet = doNetCheck();
        Picasso picasso = new Picasso.Builder(this).build();
        picasso.isLoggingEnabled();
        if(checknet!=true & imageUrl.length()>1)
        {
            String uri = "@drawable/brokenimage";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());

            Drawable res = getResources().getDrawable(imageResource);
            personImageView.setImageDrawable(res);
        }
         else {
            picasso.load(imageUrl)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(personImageView);
        }

    }

    private void loadBackground(String party, ConstraintLayout currentlauout)
    {
        if (party.toUpperCase().contains("REPU")) {
            currentlauout.setBackgroundColor(Color.RED);
        } else if (party.toUpperCase().contains("DEMO")) {
            currentlauout.setBackgroundColor(Color.BLUE);
        } else {
            currentlauout.setBackgroundColor(Color.BLACK);
        }
    }

    private boolean doNetCheck()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;

        } else {
            return false;
        }
    }
}
