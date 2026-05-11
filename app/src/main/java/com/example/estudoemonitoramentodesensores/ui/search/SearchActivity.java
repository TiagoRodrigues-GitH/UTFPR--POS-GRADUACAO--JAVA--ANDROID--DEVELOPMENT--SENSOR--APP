package com.example.estudoemonitoramentodesensores.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.*;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;
import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.ui.sensor.detail.SensorDetailActivity;
import com.example.estudoemonitoramentodesensores.ui.sensor.list.SensorListAdapter;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel   viewModel;
    private SensorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.search_sensors);

        RecyclerView rv = findViewById(R.id.recyclerViewSearch);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        adapter = new SensorListAdapter(this, new ArrayList<>());
        adapter.setOnItemClickListener((v, pos) -> {
            long id = viewModel.getSensorIdAt(pos);
            Intent i = new Intent(this, SensorDetailActivity.class);
            i.putExtra(SensorDetailActivity.KEY_MODO, SensorDetailActivity.MODO_EDITAR);
            i.putExtra(SensorDetailActivity.KEY_ID, id);
            startActivity(i);
        });

        rv.setAdapter(adapter);

        viewModel.getAnnotationCounts().observe(this, counts ->{
            if(adapter != null){
                adapter.updateAnnotationCounts(counts);
            }
        });

        viewModel.getSearchResults().observe(this, results -> adapter.updateList(results));

        EditText searchInput = findViewById(R.id.editTextSearch);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                viewModel.search(s.toString()); }
        });

    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }


}
