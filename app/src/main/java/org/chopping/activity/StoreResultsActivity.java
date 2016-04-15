package org.chopping.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chopping.org.chopping.R;
import com.google.android.gms.maps.model.LatLng;

import org.chopping.adapter.StoreAdapter;
import org.chopping.listener.OnStoreClickListenerListView;
import org.chopping.model.Store;
import org.chopping.util.ParseResponseUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by crisyelpegaso on 28/07/2015.
 */
public class StoreResultsActivity extends BaseActivity{

    private static final String STORE_INDEX =  "/store";

    private TextView resultsResume;
    private ListView resultsListView;
    private ArrayList<Store> storeResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        resultsListView = (ListView) findViewById(R.id.results);
        resultsResume = (TextView) findViewById(R.id.results_resume);


        // ALERT MESSAGE
        Toast.makeText(getBaseContext(),
                "Searching ....", Toast.LENGTH_LONG)
                .show();

        //String URL = ENDPOINT + STORE_INDEX + SEARCH;


        LocationManager service = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        String URL = GMAPS_SEARCH + LOCATION + userLocation.latitude + "," + userLocation.longitude + GMAPS_SEARCH_ENDING ;
        String keyword = getIntent().getStringExtra("keyword");
        if( keyword.length() > 0 ){
            URL = URL + "&keyword=" + keyword;
        } else {
            URL = URL + "&" + getIntent().getStringExtra("query");
        }
        URL = URL +"&"+ GMAP_API_KEY;


        try {
            new GetStoresTask(this).execute(URL);

        } catch (Exception ex) {
            resultsResume.setText("Failed : " + ex.toString());
        }

    }

    public class GetStoresTask extends AsyncTask<String, Void, String> {

        private Context context;

        public GetStoresTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPostExecute(String result){

            StoreAdapter adapter = new StoreAdapter(getApplicationContext(), storeResults);
            if(storeResults.size() > 0){
                resultsResume.setText(storeResults.size() + " results found.");
            } else {
                resultsResume.setText(result);
            }

            resultsListView.setAdapter(adapter);

            resultsListView.setOnItemClickListener(new OnStoreClickListenerListView());
        }

        @Override
        protected String doInBackground(String... urls) {
            String urlString= (String)urls[0];
            InputStream inputStream = null;
            String resultResume = "No results found";

            try{

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");


                if(this.isOnline()) {
                    connection.connect();

                    //JSONObject jsonBody = createSearchJson();

                    //JSONObject jsonBody = createJsonFromGMap();

                    OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                    //wr.write(jsonBody.toString());
                    wr.close();

                    int HttpResult = 0;
                    try {
                        HttpResult = connection.getResponseCode();
                        if(HttpResult ==HttpURLConnection.HTTP_OK){
                            inputStream = connection.getInputStream();
                            if (inputStream == null) {
                                return resultResume;
                            }

                            String jsonStoresFromES = ParseResponseUtil.convertConcertsInputStreamToString(inputStream);
                            storeResults = ParseResponseUtil.parseStoresFromGMapResponse(jsonStoresFromES);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    resultResume = "You're offline!";
                }
            }catch(Exception e){
                resultResume = "Search failed =(";
            }

            return resultResume;
        }

        private JSONObject createSearchJson() throws JSONException {
            Intent intent = getIntent();
            String categorySelected = intent.getStringExtra("category");
            JSONObject match = new JSONObject();
            match.put("name", categorySelected);
            JSONObject query = new JSONObject();
            query.put("match", match);

            JSONObject has_parent = new JSONObject();
            has_parent.put("type", "category");
            has_parent.put("query", query);

            JSONObject mainQuery = new JSONObject();
            mainQuery.put("has_parent", has_parent);
            JSONObject parent = new JSONObject();
            parent.put("query", mainQuery);
            return parent;
        }

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }

    public void displayOnResultsOnMap(View view){
        Intent intent = new Intent(view.getContext(), MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra("stores", this.storeResults);
        view.getContext().startActivity(intent);
    }

}
