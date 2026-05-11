package com.example.estudoemonitoramentodesensores.ui.annotation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.utils.UtilsAlert;
import java.time.LocalDateTime;
import java.util.List;

public class AnnotationListActivity extends AppCompatActivity {

    public static final String KEY_ID_SENSOR   = "KEY_ID_SENSOR";
    public static final String KEY_SENSOR_NAME = "KEY_SENSOR_NAME";

    private RecyclerView          annotationsRecyclerView;
    private AnnotationListAdapter annotationListAdapter;
    private AnnotationViewModel   viewModel;
    private ActionMode            actionMode;
    private View                  viewSelecionada;
    private Drawable              backgroundDrawable;
    private List<AnnotationEntity> listAnnotationEntities;
    private long                  sensorId;
    private int                   posicaoSelecionada = -1;

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override public boolean onCreateActionMode(ActionMode m, Menu menu) {
            m.getMenuInflater().inflate(R.menu.sensores_item_selecionado, menu); return true; }
        @Override public boolean onPrepareActionMode(ActionMode m, Menu menu) { return false; }
        @Override public boolean onActionItemClicked(ActionMode m, MenuItem item) {
            if (item.getItemId() == R.id.menuItemEditar)  { updateAnnotation(); return true; }
            if (item.getItemId() == R.id.menuItemExcluir) { deleteAnnotation(); return true; }
            return false; }
        @Override public void onDestroyActionMode(ActionMode m) {
            if (viewSelecionada != null) viewSelecionada.setBackground(backgroundDrawable);
            actionMode = null; viewSelecionada = null; backgroundDrawable = null;
            annotationsRecyclerView.setEnabled(true); }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sensorId = bundle.getLong(KEY_ID_SENSOR);
            String sensorName = bundle.getString(KEY_SENSOR_NAME, "");
            setTitle(getString(R.string.annotations_from_sensor, sensorName));
        }

        annotationsRecyclerView = findViewById(R.id.annotationRecyclerView);
        annotationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        annotationsRecyclerView.setHasFixedSize(true);
        annotationsRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));

        viewModel = new ViewModelProvider(this).get(AnnotationViewModel.class);
        viewModel.loadForSensor(sensorId);

        viewModel.getAnnotations().observe(this, list -> {
            listAnnotationEntities = list;
            if (annotationListAdapter == null) {
                annotationListAdapter = new AnnotationListAdapter(this, listAnnotationEntities);
                annotationListAdapter.setOnItemClickListener((v, pos) -> {
                    posicaoSelecionada = pos; updateAnnotation(); });
                annotationListAdapter.setOnItemLongClickListener((v, pos) -> {
                    if (actionMode != null) return false;
                    posicaoSelecionada = pos; viewSelecionada = v;
                    backgroundDrawable = v.getBackground();
                    v.setBackgroundColor(getColor(R.color.corDeSelecao));
                    annotationsRecyclerView.setEnabled(false);
                    actionMode = startSupportActionMode(actionModeCallback);
                    return true; });
                annotationsRecyclerView.setAdapter(annotationListAdapter);
            } else {
                annotationListAdapter.notifyDataSetChanged();
            }
        });

        // Close ActionMode after delete/update
        viewModel.getOperationResult().observe(this, rows -> {
            if (rows != null && rows > 0) {
                posicaoSelecionada = -1;
                if (actionMode != null) actionMode.finish();
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.annotations_options, menu); return true; }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemAddNote) { newAnnotation(); return true; }
        if (item.getItemId() == android.R.id.home)    { finish(); return true; }
        return super.onOptionsItemSelected(item); }

    private void deleteAnnotation() {
        final AnnotationEntity e = listAnnotationEntities.get(posicaoSelecionada);
        UtilsAlert.confirmarAcao(this, R.string.do_you_want_to_delete_this_note,
                (d, w) -> viewModel.delete(e), null); }

    public void newAnnotation() {
        UtilsAlert.readText(this, R.string.new_note,
                R.layout.enter_annotation, R.id.editTextText, null, text -> {
                    if (text == null || text.trim().isEmpty()) {
                        UtilsAlert.mostrarAviso(this, R.string.the_text_cannot_be_empty); return; }
                    viewModel.insert(new AnnotationEntity(sensorId, LocalDateTime.now(), text.trim())); }); }

    private void updateAnnotation() {
        final AnnotationEntity e = listAnnotationEntities.get(posicaoSelecionada);
        UtilsAlert.readText(this, R.string.update_the_text,
                R.layout.enter_annotation, R.id.editTextText, e.getText(), text -> {
                    if (text == null || text.trim().isEmpty()) {
                        UtilsAlert.mostrarAviso(this, R.string.the_text_cannot_be_empty); return; }
                    if (!text.trim().equalsIgnoreCase(e.getText())) {
                        e.setText(text.trim()); viewModel.update(e); } }); }
}