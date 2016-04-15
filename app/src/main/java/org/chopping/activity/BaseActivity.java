package org.chopping.activity;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.chopping.org.chopping.R;

/**
 * Created by crisyelpegaso on 28/07/2015.
 */
public class BaseActivity extends Activity {

    protected static final String ENDPOINT = "http://192.168.1.103:9200/chopping";
    protected static final String SEARCH = "/_search";
    protected static final String GMAPS_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    protected static final String LOCATION = "location=";
    protected static final String GMAPS_SEARCH_ENDING = "&radius=500&sensor=true";
    protected static final String GMAP_API_KEY = "key=AIzaSyDxjkG4no7TcP-lEVUC8752V-DFJuJNjAA";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
