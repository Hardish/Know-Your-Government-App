package com.example.government;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class about extends AppCompatActivity {
    private static final String TAG = "about";
    private static final String msURL = "https://developers.google.com/civic-information";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView = (TextView) findViewById(R.id.googletxt);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    public void callInforAPI(View view)
    {
        Log.d(TAG, "callInforAPI: Calling about method");
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(msURL));
        startActivity(i);

    }
}
