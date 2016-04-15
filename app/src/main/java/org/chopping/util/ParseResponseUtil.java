package org.chopping.util;

import org.chopping.model.Store;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OE on 28/07/2015.
 */
public class ParseResponseUtil {

    public static String convertConcertsInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        try {
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static ArrayList<Store> parseStoresFromGMapResponse(String jsonResponse){
        ArrayList<Store> stores = new ArrayList<Store>();
        try{
            JSONObject response = new JSONObject(jsonResponse);
            JSONArray results = response.getJSONArray("results");
            for(int i=0; i<results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
              //  Double rating = result.getDouble("rating");
              //  if(rating != null && rating > 3) {
                    Store store = new Store();
                    store.setLatitude(result.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                    store.setLongitude(result.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                    store.setName(result.getString("name"));
                    store.setPicture(result.getString("icon"));
                    stores.add(store);
                //}
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return stores;
    }
    public static ArrayList<Store> parseStoresFromESResponse(String jsonResponse){
        ArrayList<Store> stores = new ArrayList<Store>();
        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONObject hitsObject = response.getJSONObject("hits");
            JSONArray hits = hitsObject.getJSONArray("hits");
            for(int i=0; i<hits.length(); i++){
                JSONObject hit = hits.getJSONObject(i);
                JSONObject sourceStore = hit.getJSONObject("_source");

                Store store = new Store();
                store.setId(hit.getString("_id"));
                store.setName(sourceStore.getString("name"));
                store.setLatitude(sourceStore.getDouble("lat"));
                store.setLongitude(sourceStore.getDouble("long"));
                stores.add(store);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stores;
    }
}
