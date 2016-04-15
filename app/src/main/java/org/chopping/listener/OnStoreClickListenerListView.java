package org.chopping.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.chopping.org.chopping.R;

import org.chopping.activity.MapActivity;
import org.chopping.activity.StoreResultsActivity;
import org.chopping.adapter.StoreAdapter;
import org.chopping.model.Store;

import java.util.ArrayList;

/**
 * Created by OE on 30/07/R2015.
 */
public class OnStoreClickListenerListView implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();
        StoreAdapter adapter = (StoreAdapter) parent.getAdapter();
        Store storeSelected = adapter.getItem(position);
        TextView textViewItem = ((TextView) view.findViewById(R.id.store_name));
        // get the clicked item name
        // just toast it
        Toast.makeText(context, "Item: " + storeSelected.getName(), Toast.LENGTH_SHORT).show();
        loadMapForStore(parent, storeSelected);
    }

    private void loadMapForStore(View view, Store store){
        Intent intent = new Intent(view.getContext(), MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ArrayList<Store> stores = new ArrayList<Store>();
        stores.add(store);
        intent.putParcelableArrayListExtra("stores", stores);
        view.getContext().startActivity(intent);
    }
}
