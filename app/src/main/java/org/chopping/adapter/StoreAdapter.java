package org.chopping.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chopping.org.chopping.R;

import org.chopping.model.Store;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by OE on 30/07/2015.
 */
public class StoreAdapter extends BaseAdapter {

    private ArrayList<Store> stores;
    private LayoutInflater inflater = null;

    public StoreAdapter(Context context, ArrayList<Store> stores)
    {
        super();
        this.stores = stores;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Store getItem(int position) {
        return stores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_store, parent, false);
        }

        Store store = getItem(position);

        if (store != null) {
            TextView storeName = (TextView) view.findViewById(R.id.store_name);

            if (storeName != null) {
                storeName.setText(store.getName());
            }

            new DownloadImageTask((ImageView) view.findViewById(R.id.store_pic))
                    .execute(store.getPicture());


        }

        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

