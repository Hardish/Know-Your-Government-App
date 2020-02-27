package com.example.government;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class GovernmentInfoDownloader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;

    private  String DATA_URL = "https://www.googleapis.com/civicinfo/v2";
    //  " https://cloud.iexapis.com/stable/stock/stock_symbol/quote?token=your_token ";
    /*https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCwd34TiF9qn4LROdEWiCxjMZK5BU3xsIE&address=60605*/
    private static final String TAG = "GovernmentInfoDownloade";
    private static final String yourAPIKey = "AIzaSyCwd34TiF9qn4LROdEWiCxjMZK5BU3xsIE";
    String data ="";
    private String zipcode;
    private ArrayList<Government> tempGovt = new ArrayList<>();
    private String displayFinaltext = "";


    public GovernmentInfoDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {

        //boolean fahrenheit = Boolean.parseBoolean(params[1]);

         zipcode = strings[0];


        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();
        buildURL.appendPath("representatives");
        buildURL.appendQueryParameter("key", yourAPIKey);
        buildURL.appendQueryParameter("address", zipcode);

        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
           parseJSON(data);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return displayFinaltext;
    }


    private ArrayList<Government> parseJSON(String s)
    {

        tempGovt.clear();
        try
        {
            JSONObject jObjMain = new JSONObject(data);
            JSONObject jObjMain1 = jObjMain.getJSONObject("normalizedInput");


            String city =  (String)jObjMain1.get("city");
            String state = (String)jObjMain1.get("state");
            String zip = (String)jObjMain1.get("zip");

           if(zip.isEmpty())
           {
               displayFinaltext = city +","+state;
           }
           else
           {
               displayFinaltext = city +","+state+","+zip;
           }

//***********************************************************************************************************************
/*
 The “offices” JSONArray started
 */
            StringBuilder offices = new StringBuilder();
            StringBuilder officesIndices = new StringBuilder();
            JSONObject json_officeIndicesAndOffices = new JSONObject();

            JSONArray ja_json_officeIndicesAndOffices = new JSONArray();

            JSONArray jBorders = jObjMain.getJSONArray("offices");
            for (int j = 0; j < jBorders.length(); j++) {
                JSONObject rec = (JSONObject) jBorders.get(j);
                String name = rec.getString("name");  //put in array list of Government

                offices.append(String.format(Locale.getDefault(), "%s ", name));
                JSONArray  officeindices = (JSONArray) rec.get("officialIndices");

                json_officeIndicesAndOffices.put(name,officeindices);
                ja_json_officeIndicesAndOffices.put(json_officeIndicesAndOffices);
            }

            JSONArray key = json_officeIndicesAndOffices.names();
            ArrayList<Integer> listdata = new ArrayList<Integer>();
            for(int i=0;i<key.length();++i)
            {
                String keys = key.getString(i);
                Log.d(TAG, "parseJSON: Keys value "+ json_officeIndicesAndOffices.getJSONArray(keys));
                Log.d(TAG, "parseJSON: Keys length "+ json_officeIndicesAndOffices.getJSONArray(keys).length());
                for(int j=0;j<json_officeIndicesAndOffices.getJSONArray(keys).length();j++)
                {
                    Log.d(TAG, "iterating "+ json_officeIndicesAndOffices.getJSONArray(keys).get(j));
                    listdata.add((Integer)json_officeIndicesAndOffices.getJSONArray(keys).get(j));
                }


            }
            Log.d(TAG, "parseJSON  officesIndices: "+json_officeIndicesAndOffices);
            Log.d(TAG, "parseJSON: json_officeIndicesAndOffices length"+json_officeIndicesAndOffices.length());
            Log.d(TAG, "parseJSON: "+offices);

            /*
            The “offices” JSONArray ended
            */
//***********************************************************************************************************************
            /*
            The “officials” JSONArray  Started from here
            */
            StringBuilder officials_name = new StringBuilder();

         //   StringBuilder officesIndices = new StringBuilder();
         //   JSONObject json_officeIndicesAndOffices = new JSONObject();

            JSONArray jofficialsdetail = jObjMain.getJSONArray("officials");
  /*          for (int j = 0; j < jofficialsdetail.length(); j++)
            {
                JSONObject rec = (JSONObject) jofficialsdetail.get(j);

                String name = rec.getString("name");
                officials_name.append(String.format(Locale.getDefault(), "%s ", name));


                final JSONArray  list_address;


                if(rec.has("address") == true)
                {
                    list_address  = (JSONArray) rec.get("address");
                    for(int add = 0;add<list_address.length();add++)
                    {
                        JSONObject temp = (JSONObject) list_address.get(add);
                        String line1 = temp.getString("line1");
                        String line2 = temp.getString("line2");
                        String line3 = temp.getString("line3");
                        String city1 = temp.getString("city");
                        String state1 = temp.getString("state");
                        String zip1 = temp.getString("zip");

                        if(line3.isEmpty())
                        {
                             address_final = line1 + ", " + line2+ ", " +city1+ ", " +state1+ ", " +zip1;
                          //  address_final = address_final + new_address;
                           // listaddress.add(name+"->"+address_final);
                            Log.d(TAG, "parseJSON: sample" + name +address_final);
                            //Log.d(TAG, "parseJSON:listaddress    "+ "  "+listaddress.size());
                            // finalAddress.append(address_final);
                        }
                        else
                        {
                             address_final = line1 + ", " + line2+ ", " + line3+ ", " +city1+ ", " +state1+ ", " +zip1;
                            listaddress.add(name+"->"+address_final);
                            Log.d(TAG, "parseJSON: sample" + name +address_final);
                            Log.d(TAG, "parseJSON:listaddress    "+ "  "+listaddress.size());
                            // finalAddress.append(address_final);
                        }
//String append
*//*                        if(line3.isEmpty())
                        {
                            if(address_final.isEmpty())
                            {
                                address_final = line1 + ", " + line2+ ", " +city1+ ", " +state1+ ", " +zip1;
                                finalAddress.append(address_final);
                            }
                            else
                            {

                                String new_address_final = line1 + ", " + line2+ ", " +city1+ ", " +state1+ ", " +zip1;
                                finalAddress.append(address_final);
                                finalAddress.append(new_address_final);

                            }
                        }
                        else
                        {
                            if(address_final.isEmpty())
                            {
                                address_final = line1 + ", " + line2+ ", " +city1+ ", " +state1+ ", " +zip1;
                                finalAddress.append(address_final);
                            }
                            else
                            {

                                String new_address_final = line1 + ", " + line2+ ", " + line3+ ", " +city1+ ", " +state1+ ", " +zip1;
                                finalAddress.append(address_final);
                                finalAddress.append(new_address_final);

                            }
                        }*//*
////String append end here
                    }
                }
                //address ends here

                if(rec.has("party") == true)
                {
                    String party = rec.getString("party");
                    Log.d(TAG, "parseJSON: Party" + party);
                }

                //checking for party

                final JSONArray  list_phone;
                //  String number = "312-744-5000";
                if(rec.has("phones") == true)
                {
                    list_phone  = (JSONArray) rec.get("phones");
                    //JSONObject temp = (JSONObject) list_phone.get(0);
                    String phoneNumber = list_phone.get(0).toString();
                    String first = phoneNumber.subSequence(1,4).toString();
                    String rest = phoneNumber.substring(6,phoneNumber.length());
                    finalPhoneNumber = first+"-"+rest;
                    Log.d(TAG, "parseJSON: listphone" + finalPhoneNumber);
                }
                //end of phone number


                final JSONArray  list_urls;
                if(rec.has("urls") == true)
                {
                    list_urls = (JSONArray) rec.get("urls");
                    finalWebUrl = list_urls.get(0).toString();
                    Log.d(TAG, "parseJSON URLS:" + finalWebUrl);
                }
                //urls end here

                final JSONArray list_emails;
                // String[] addresses = new String[]
                //                {"christopher.hield@gmail.com", "chield@iit.edu"};
                if(rec.has("emails") == true)
                {
                    list_emails = (JSONArray) rec.get("emails");
                    email = list_emails.get(0).toString();
                    Log.d(TAG, "parseJSON:Emails " + email);
                }
                //end of email



                if(rec.has("photoUrl") == true)
                {
                    iconUrl = rec.getString("photoUrl");
                    Log.d(TAG, "parseJSON: PhotoURl " + iconUrl );
                   *//* Picasso picasso = new Picasso.Builder(mainActivity).build();
                    picasso.load(iconUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);*//*
                }
                else
                    {
                        *//*Picasso picasso = new Picasso.Builder(mainActivity).build();
                        picasso.load(R.drawable.missing).into(imageView);*//*
                    }
                //photoUrl ends here

                final JSONArray list_channels;
                if(rec.has("channels") == true)
                {
                    list_channels =  (JSONArray) rec.get("channels");

                    for(int c = 0;c<list_channels.length();c++)
                    {
                       JSONObject channel = (JSONObject) list_channels.get(c);
                       String type = channel.getString("type");
                       String id = channel.getString("id");
                        listchannels.add(type +"," +id);
                        Log.d(TAG, "parseJSON: type and id" + type + id);
                    }

                }




            }*/

            /*
            The “officials” JSONArray  ended here
            */

            //create list of the person, fill the details and set into arraylist
            //jofficialsdetail for name and party --> json_officeIndicesAndOffices + officeindices
            JSONArray key2 = json_officeIndicesAndOffices.names();
          //  ArrayList<Integer> listdata2 = new ArrayList<Integer>();
            for(int i=0;i<key2.length();++i)
            {
                String keys = key2.getString(i);
               /* Log.d(TAG, "parseJSON: Keys value "+ json_officeIndicesAndOffices.getJSONArray(keys));
                Log.d(TAG, "parseJSON: Keys length "+ json_officeIndicesAndOffices.getJSONArray(keys).length());*/
                for(int j=0;j<json_officeIndicesAndOffices.getJSONArray(keys).length();j++)
                {
                    Log.d(TAG, "iterating "+ json_officeIndicesAndOffices.getJSONArray(keys).get(j));
                    int index = (Integer)json_officeIndicesAndOffices.getJSONArray(keys).get(j);
                    JSONObject objSample = (JSONObject) jofficialsdetail.get(index);
                    String partyName = objSample.getString("party");
                    String personName = objSample.getString("name");




                    String  iconUrl =" ";

                    if(objSample.has("photoUrl") == true)
                    {
                       iconUrl = objSample.getString("photoUrl");
                    }
                    else
                    {
                        iconUrl = " ";
                    }

                    String finalPhoneNumber = " ";
                    final JSONArray  list_phone;
                    //  String number = "312-744-5000";
                    if(objSample.has("phones") == true)
                    {
                        list_phone  = (JSONArray) objSample.get("phones");
                        //JSONObject temp = (JSONObject) list_phone.get(0);
                        String phoneNumber = list_phone.get(0).toString();
                        String first = "(" + phoneNumber.subSequence(1,4).toString() +")";
                        String rest = phoneNumber.substring(6,phoneNumber.length());
                        finalPhoneNumber = first+" "+rest;
                        Log.d(TAG, "parseJSON: listphone" + finalPhoneNumber);
                    }
                    else
                    {
                        finalPhoneNumber = " ";
                    }

                    final JSONArray list_emails;
                    String email = "";
                    // String[] addresses = new String[]
                    //                {"christopher.hield@gmail.com", "chield@iit.edu"};
                    if(objSample.has("emails") == true)
                    {
                        list_emails = (JSONArray) objSample.get("emails");
                        email = list_emails.get(0).toString();
                        Log.d(TAG, "parseJSON:Emails " + email);
                    }
                    else
                    {
                        email = " ";
                    }

                    String finalWebUrl = " ";
                    final JSONArray  list_urls;
                    if(objSample.has("urls") == true)
                    {
                        list_urls = (JSONArray) objSample.get("urls");
                        finalWebUrl = list_urls.get(0).toString();
                        Log.d(TAG, "parseJSON URLS:" + finalWebUrl);
                    }
                    else
                    {
                        finalWebUrl = " ";
                    }

                    List<String> listchannels = new ArrayList<String>();
                    final JSONArray list_channels;
                    if(objSample.has("channels") == true)
                    {
                        list_channels =  (JSONArray) objSample.get("channels");

                        for(int c = 0;c<list_channels.length();c++)
                        {
                            JSONObject channel = (JSONObject) list_channels.get(c);
                            String type = channel.getString("type");
                            String id = channel.getString("id");
                            listchannels.add(type +"," +id);
                            Log.d(TAG, "parseJSON: type and id" + type + id);
                        }

                    }

                    final JSONArray  list_address;
                    String address_final = " ";
                    //StringBuilder address = new StringBuilder();
                    if(objSample.has("address") == true)
                    {
                        list_address  = (JSONArray) objSample.get("address");
                       // for(int add = 0;add<list_address.length();add++)
                        //{
                            JSONObject temp = (JSONObject) list_address.get(0);
                            String line1 = temp.getString("line1");
                            String line2 = temp.getString("line2");
                            String line3 = temp.getString("line3");
                            String city1 = temp.getString("city");
                            String state1 = temp.getString("state");
                            String zip1 = temp.getString("zip");

                            if(line3.isEmpty())
                            {
                                    address_final = line1 + ", " + line2+ ", " +city1+ ", " +state1+ ", " +zip1;
                                   /* if(address.length() == 0)
                                    {
                                        address.append(address_final);
                                    }
                                    else
                                    {
                                        address.append("->"+address_final);
                                    }*/


                            }
                            else
                            {
                                    address_final = line1 + ", " + line2+ ", " + line3+ ", " +city1+ ", " +state1+ ", " +zip1;
                                    /* if(address.length() == 0)
                                     {
                                      address.append(address_final);
                                         }
                                      else
                                      {
                                      address.append("->"+address_final);
                                     }*/

                            }

                        //}
                    }




                    Government g = new Government(keys,personName,partyName,iconUrl,address_final,finalPhoneNumber,email,finalWebUrl,listchannels,displayFinaltext);
                    tempGovt.add(g);

                    //listdata.add((Integer)json_officeIndicesAndOffices.getJSONArray(keys).get(j));
                }


            }

            //create Governmet object


            //add Government object into tempGovt
            //tempGovt


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tempGovt;
    }

    @Override
    protected void onPostExecute(String s) {
       // Log.d(TAG, "onPostExecute: "+s);
        /*if(s.equalsIgnoreCase("Data Present"))
        {
            mainActivity.addData(tempGovt);
        }*/
        if(displayFinaltext.isEmpty())
        {

        }
        else
        {
            mainActivity.addData(tempGovt,displayFinaltext);

        }
    }
}
