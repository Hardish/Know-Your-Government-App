package com.example.government;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class government_detail_info extends AppCompatActivity {

    private TextView locationTextView;
    private TextView officenameTextView;
    private TextView personNameTextView;
    private TextView partyNameTextView;
    private ImageView personImageView;
    private ImageView partySymbolImageView;
    private TextView addressTextView;
    private TextView addtxt;
    private TextView phoneNumberTextView;
    private TextView photxt;
    private TextView emailAddressTextView;
    private TextView emaxt;
    private TextView webtxt;
    private TextView weburlTextView;
    private ImageView googleImageView;
    private ImageView facebookImageView;
    private ImageView twitterImageView;
    private ImageView youtubeImageView;
    private List<String> listchannels = new ArrayList<String>();
    private static final String TAG = "government_detail_info";
    private String imageUrl;
    private String sampleChannelName;
    private String sampleChannelId;
    private boolean checknet;
    private String googleId;
    private String facebookId;
    private String twitterId;
    private String youtubeId;
    private Government government;
    private static final String msURL_repu = "https://www.gop.com/";
    private static final String msURL_demo = "https://democrats.org/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_detail_info);
        ConstraintLayout currentlauout = (ConstraintLayout) findViewById(R.id.detailLayout);

        Intent intent = getIntent();
        checknet =  doNetCheck();

            if (intent.hasExtra(Government.class.getName())) {
                government = (Government) intent.getSerializableExtra(Government.class.getName());

                locationTextView = findViewById(R.id.locationtxt);
                locationTextView.setText(government.getDisplayFinaltext());

                officenameTextView = findViewById(R.id.officenametxt);
                officenameTextView.setText(government.getTitle());

                personNameTextView = findViewById(R.id.personNametxt);
                personNameTextView.setText(government.getName());

                partyNameTextView = findViewById(R.id.partytxt);
                loadBackground(government.getParty(), currentlauout);
                partyNameTextView.setText("("+government.getParty()+")");


                personImageView = findViewById(R.id.personimage);
                imageUrl = government.getPimageurl();
                loadRemotePersonImage(imageUrl);

                partySymbolImageView = findViewById(R.id.partysymbolimage);
                //check for the party and set appropriate image
                loadPartImage(government.getParty());

                addressTextView = findViewById(R.id.addresstxtview);
                addtxt = findViewById(R.id.addtxt);
                if (government.getAddress().length() == 1) {
                    addressTextView.setVisibility(View.GONE);
                    addtxt.setVisibility(View.GONE);
                } else {
                    addressTextView.setText(government.getAddress());
                    addressTextView.setPaintFlags(addressTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                }


                phoneNumberTextView = findViewById(R.id.phonetxt);
                photxt = findViewById(R.id.photxt);
                if (government.getPhoneNumber().length() == 1) {
                    phoneNumberTextView.setVisibility(View.GONE);
                    photxt.setVisibility(View.GONE);
                } else {
                    phoneNumberTextView.setText(government.getPhoneNumber());
                    phoneNumberTextView.setTextColor(Color.WHITE);

                    if(checknet == true)
                    {
                        Linkify.addLinks(phoneNumberTextView, Linkify.ALL);
                    }
                    else
                    {
                        phoneNumberTextView.setPaintFlags(phoneNumberTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }

                }


                emailAddressTextView = findViewById(R.id.emailtxt);
                emaxt = findViewById(R.id.emaxt);
                Log.d(TAG, "onCreate: " + government.getEmail().length());
                Log.d(TAG, "onCreate: " + government.getEmail().toString());
                if (government.getEmail().length() == 1) {
                    emailAddressTextView.setVisibility(View.GONE);
                    emaxt.setVisibility(View.GONE);
                } else {
                    emailAddressTextView.setText(government.getEmail());

                    if(checknet == true)
                    {
                        Linkify.addLinks(emailAddressTextView, Linkify.ALL);
                    }
                    else
                    {
                        emailAddressTextView.setPaintFlags(emailAddressTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }

                }

                weburlTextView = findViewById(R.id.weburltxt);
                webtxt = findViewById(R.id.webtxt);
                if (government.getWebsite().length() == 1) {
                    webtxt.setVisibility(View.GONE);
                    weburlTextView.setVisibility(View.GONE);
                } else {
                    weburlTextView.setText(government.getWebsite());

                    if(checknet == true)
                    {
                        Linkify.addLinks(weburlTextView, Linkify.ALL);
                    }
                    else
                    {
                        weburlTextView.setPaintFlags(weburlTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }

                }


                googleImageView = findViewById(R.id.googleimage);
                facebookImageView = findViewById(R.id.fbimage);
                twitterImageView = findViewById(R.id.twitterimage);
                youtubeImageView = findViewById(R.id.youtubeimage);

                listchannels = government.getListchannels();

                loadChannelImage(listchannels);

            }
    }



    public void clickMap(View v) {
        if(checknet == true)
        {
            String address = addressTextView.getText().toString();

            Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

            Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
            }
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

    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadBackground(String party, ConstraintLayout currentlauout) {

        if (party.toUpperCase().contains("REPU")) {
            currentlauout.setBackgroundColor(Color.RED);
        } else if (party.toUpperCase().contains("DEMO")) {
            currentlauout.setBackgroundColor(Color.BLUE);


        } else {
            currentlauout.setBackgroundColor(Color.BLACK);
        }
    }

    private void loadChannelImage(List<String> listchannels) {
        googleImageView.setVisibility(View.GONE);
        facebookImageView.setVisibility(View.GONE);
        twitterImageView.setVisibility(View.GONE);
        youtubeImageView.setVisibility(View.GONE);

        for (int i = 0; i < listchannels.size(); i++) {
            Log.d(TAG, "loadChannelImage: " + listchannels.get(i).toString());
            String[] channelnameAndid = listchannels.get(i).split(",");
            sampleChannelName = channelnameAndid[0];
            sampleChannelId = channelnameAndid[1];

            Log.d(TAG, "loadChannelImage: " + sampleChannelName + sampleChannelId);

            if (sampleChannelName.equalsIgnoreCase("GooglePlus")) {
                googleImageView.setVisibility(View.VISIBLE);
                googleId = sampleChannelId;

            }
            if (sampleChannelName.equalsIgnoreCase("Facebook")) {
                facebookImageView.setVisibility(View.VISIBLE);
                facebookId = sampleChannelId;
            }

            if (sampleChannelName.equalsIgnoreCase("Twitter")) {
                twitterImageView.setVisibility(View.VISIBLE);
                twitterId = sampleChannelId;
            }

            if (sampleChannelName.equalsIgnoreCase("YouTube")) {
                youtubeImageView.setVisibility(View.VISIBLE);
                youtubeId = sampleChannelId;
            }
        }
    }

    private void loadPartImage(String party) {
        if (party.isEmpty()) {
            partySymbolImageView.setVisibility(View.INVISIBLE);
        } else if (party.toUpperCase().contains("REPU")) {
            partySymbolImageView.setImageResource(R.drawable.rep_logo);
        } else if (party.toUpperCase().contains("DEMO")) {
            partySymbolImageView.setImageResource(R.drawable.dem_logo);
        }


    }

    private void loadRemotePersonImage(String imageUrl) {

        Log.d(TAG, "loadImage: " + imageUrl);
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
        else if (imageUrl.length() == 1) {
            String uri = "@drawable/missing";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            personImageView.setImageDrawable(res);

        } else {
            picasso.load(imageUrl)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(personImageView);
        }

    }

    public void googlePlusClicked(View v)
    {
        checknet = doNetCheck();
        if(checknet == true)
        {
            String name = sampleChannelId;
            Intent intent_channel = null;
            try {
                intent_channel = new Intent(Intent.ACTION_VIEW);
                intent_channel.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
                intent_channel.putExtra("customAppUri", name);
                startActivity(intent_channel);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://plus.google.com/" + name)));
            }
        }
    }
    public void facebookClicked(View v)
    {
        checknet = doNetCheck();
        if(checknet == true)
        {
            String FACEBOOK_URL = "https://www.facebook.com/" + facebookId;
            String urlToUse;

            PackageManager packageManager = getPackageManager();
            try {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850)
                { //newer versions of fb app
                    urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                }
                else
                { //older versions of fb app
                    urlToUse = "fb://page/" + facebookId;
                }
            }
            catch (PackageManager.NameNotFoundException e)
            {
                urlToUse = FACEBOOK_URL; //normal web url
            }
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(urlToUse));
            startActivity(facebookIntent);
        }
    }


    public void twitterClicked(View v)
    {
        checknet = doNetCheck();
        if(checknet == true)
        {
            Intent twitterintent = null;
            String name = twitterId;
            try {
                // get the Twitter app if possible
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                twitterintent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                twitterintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            catch (Exception e) {
                // no Twitter app, revert to browser
                twitterintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
            }
            startActivity(twitterintent);
        }
    }



    public void youTubeClicked(View v)
    {
        checknet = doNetCheck();
        if(checknet == true)
        {
            String name = youtubeId;
            Intent youTubeintent = null;
            try {
                youTubeintent = new Intent(Intent.ACTION_VIEW);
                youTubeintent.setPackage("com.google.android.youtube");
                youTubeintent.setData(Uri.parse("https://www.youtube.com/" + name));
                startActivity(youTubeintent);
            } catch (ActivityNotFoundException e)
            {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/" + name)));
            }
        }
    }

    public void zoomImage(View v)
    {
        if(imageUrl.length() == 1)
        {

        }
        else {
            Intent intent_zoom = new Intent(government_detail_info.this, photo_detail.class);
            intent_zoom.putExtra(Government.class.getName(),government);
            startActivity(intent_zoom);
        }

    }

}