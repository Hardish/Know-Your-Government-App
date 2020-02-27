package com.example.government;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView displaytextView;
    private ArrayList<Government> governmentList = new ArrayList<>();
    private boolean checknet;
    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private LocationManager locationManager;
    private Criteria criteria;
    private RecyclerView recyclerView;
    private GovernmentAdapter governmentAdapter;

    //set image
    //private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        governmentAdapter = new GovernmentAdapter(governmentList, this);
        recyclerView.setAdapter(governmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaytextView = (TextView) findViewById(R.id.displaytxt);
        checknet =  doNetCheck();
        if(checknet == false)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.setMessage("No Network Connection");
            builder.setTitle("Data cannot be download/loaded without an internet connections");
            AlertDialog dialog1 = builder.create();
            dialog1.show();
        }
        else
        {

            //set location
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



            criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            //criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            //criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_LOCATION_REQUEST_CODE_ID);
            } else {
                setLocation();
                }

        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
                return;
            }
        }
        //no permission

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: " + " ************");
        outState.putString("Location", displaytextView.getText().toString());
        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: " + " ************");
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey("Location"))
        {
           // setLocation();
            displaytextView.setText(savedInstanceState.getString("Location"));
            new GovernmentInfoDownloader(MainActivity.this).execute(displaytextView.getText().toString());

        }

    }
    @SuppressLint("MissingPermission")
    private void setLocation()
    {
        String bestProvider = locationManager.getBestProvider(criteria, true);
        //((TextView) findViewById(R.id.displaytxt)).setText(bestProvider);

        Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null) {

           /* ((TextView) findViewById(R.id.locText)).setText(
                    String.format(Locale.getDefault(),
                            "%.4f, %.4f", currentLocation.getLatitude(), currentLocation.getLongitude()));*/

            doLatLon(currentLocation.getLatitude(), currentLocation.getLongitude());
            //take city/state or zip and call GovernmentInfoDownloaded



        } else {
            //can set alert box on no access granted
            //((TextView) findViewById(R.id.locText)).setText(R.string.no_locs);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);



            builder.setMessage("Location is not enable");
            builder.setTitle("Enable Location");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void doLatLon(double lat,double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses;
            addresses = geocoder.getFromLocation(lat, lon, 10);
            String state = addresses.get(0).getAdminArea();
            String city = addresses.get(0).getLocality();
            String postalcode = addresses.get(0).getPostalCode();

            Log.d(TAG, "doLatLon: "+addresses);
            Log.d(TAG, "doLatLon: state and city"+state+city+postalcode);

            new GovernmentInfoDownloader(this).execute(postalcode);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /* private void makeList() {
        for (int i = 0; i < 300; i++) {
            Government a = new Government("title"+i,"name"+i,"Republic");
            governmentList.add(a);
        }
    }*/

    private boolean doNetCheck() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.about:

                    checknet = doNetCheck();
                    if(checknet == false)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        builder.setMessage("No Internet");
                        builder.setTitle("No Internet connection to add stock");

                        AlertDialog dialog1 = builder.create();
                        dialog1.show();

                    }
                    else
                    {
                        Intent intent_about = new Intent(this,about.class);
                        // startActivityForResult(intent_add_notes,CODE_FOR_ADD_ACTIVITY);
                        startActivity(intent_about);
                    }
                    break;

                case  R.id.search:
                    checknet = doNetCheck();
                    if(checknet == false)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        builder.setMessage("No Internet");
                        builder.setTitle("No Internet connection to add stock");

                        AlertDialog dialog1 = builder.create();
                        dialog1.show();

                    }
                    else
                    {
                        Log.d(TAG, "onOptionsItemSelected: search pressed");
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        // Creae an edittext and set it to be the builder's view
                        final EditText et = new EditText(this);
                        et.setInputType(InputType.TYPE_CLASS_TEXT);
                        et.setGravity(Gravity.CENTER_HORIZONTAL);
                        builder.setView(et);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
try{



        displaytextView.setText(et.getText().toString());
        new GovernmentInfoDownloader(MainActivity.this).execute(displaytextView.getText().toString());


} catch (Exception e) {
    e.printStackTrace();
}


                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
                                displaytextView.setText(" ");
                            }
                        });
                        builder.setMessage("Enter City,State or Zip code:");
                        //   builder.setTitle("Enter City,State or Zip code:");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    }

                default:
                   // return super.onOptionsItemSelected(item);
                         break;
            }
        return true;
    }


    @Override
    public void onClick(View v)
    {
        int pos = recyclerView.getChildLayoutPosition(v);
        Government go = governmentList.get(pos);
        Intent intent = new Intent(MainActivity.this, government_detail_info.class);
        intent.putExtra(Government.class.getName(),go);
        startActivity(intent);
    }


    public void addData(ArrayList<Government> tempGovt, String displayFinaltext) {
        governmentList.clear();
        displaytextView.setText(displayFinaltext);
        governmentList.addAll(tempGovt);
        governmentAdapter.notifyDataSetChanged();

    }
}
