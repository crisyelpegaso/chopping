package org.chopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chopping.org.chopping.R;

import java.util.HashMap;


public class SearchActivity extends BaseActivity {

    EditText searchInput;
    HashMap<String, String> queries = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Spinner spinner = (Spinner) findViewById(R.id.search_options);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        queries.put("Indumentaria", "types=clothing_store&keyword=woman");
        queries.put("Zapatos", "types=shoe_store");
        queries.put("Maquillaje", "types=store&keyword=make-up");
        queries.put("Deportes", "types=clothing_store&keyword=sports");
        queries.put("Shopping", "types=shopping_mall");

    }

    public void searchStores(View view) {

        Intent intent = new Intent(this, StoreResultsActivity.class);
        Spinner categories = (Spinner) this.findViewById(R.id.search_options);
        String keyword = String.valueOf(((EditText)this.findViewById(R.id.search_keyword)).getText());
        //Spinner categories = (Spinner)view.findViewById(R.id.search_options);
        String category = categories.getSelectedItem().toString();
        intent.putExtra("keyword", keyword);
        intent.putExtra("query", queries.get(category));
        startActivity(intent);

    }

}
