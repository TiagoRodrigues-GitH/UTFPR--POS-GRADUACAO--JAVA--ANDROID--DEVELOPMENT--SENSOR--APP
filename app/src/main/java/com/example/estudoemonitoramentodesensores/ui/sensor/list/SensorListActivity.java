package com.example.estudoemonitoramentodesensores.ui.sensor.list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.ui.info.InfoAppActivity;
import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.data.AppDatabase;
import com.example.estudoemonitoramentodesensores.ui.search.SearchActivity;
import com.example.estudoemonitoramentodesensores.ui.sensor.detail.SensorDetailActivity;
import com.example.estudoemonitoramentodesensores.utils.UtilsAlert;
import com.google.android.material.snackbar.Snackbar;
import android.net.Uri;
import androidx.core.content.FileProvider;

import java.io.File;

import com.example.estudoemonitoramentodesensores.utils.PDFExporter;
import com.example.estudoemonitoramentodesensores.utils.CsvExporter;
import com.example.estudoemonitoramentodesensores.utils.JsonExporter;
import java.util.Collections;
import java.util.List;

public class SensorListActivity extends AppCompatActivity {

    public static final String ARQUIVO_PREFERENCIAS       = "com.example.estudoemonitoramentodesensores.PREFERENCIAS";
    public static final String KEY_ORDENCACAO_ASCENDENTE  = "ORDENCAO_ASCENDENTE";
    private static final boolean PADRAO_ORDENACAO         = true;

    private RecyclerView        recyclerViewSensores;
    private SensorListAdapter   sensorListAdapter;
    private SensorListViewModel viewModel;
    private MenuItem            menuItemOrdencao;
    private ActionMode          actionMode;
    private View                viewSelecionada;
    private Drawable            backgroundDrawable;
    private int                 posicaoSelecionada = -1;
    private boolean             ordenacaoAscendente = PADRAO_ORDENACAO;
    private List<SensorEntity>  listaSensores;

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override public boolean onCreateActionMode(ActionMode m, Menu menu) {
            m.getMenuInflater().inflate(R.menu.sensores_item_selecionado, menu); return true; }
        @Override public boolean onPrepareActionMode(ActionMode m, Menu menu) { return false; }
        @Override public boolean onActionItemClicked(ActionMode m, MenuItem item) {
            if (item.getItemId() == R.id.menuItemEditar)  { editarSensor();  return true; }
            if (item.getItemId() == R.id.menuItemExcluir) { excluirSensor(); return true; }
            return false; }
        @Override public void onDestroyActionMode(ActionMode m) {
            if (viewSelecionada != null) viewSelecionada.setBackground(backgroundDrawable);
            actionMode = null; viewSelecionada = null; backgroundDrawable = null;
            recyclerViewSensores.setEnabled(true); }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);
        setTitle(R.string.lista_de_sensores);

        recyclerViewSensores = findViewById(R.id.recyclerViewSensores);
        recyclerViewSensores.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSensores.setHasFixedSize(true);
        recyclerViewSensores.addItemDecoration(
                new DividerItemDecoration(this, LinearLayout.VERTICAL));

        lerPreferencias();

        viewModel = new ViewModelProvider(this).get(SensorListViewModel.class);
        viewModel.setSortAscending(ordenacaoAscendente);

        // Listening to Sensors List news
        viewModel.getSensors().observe(this, sensors -> {
            listaSensores = sensors;
            if (sensorListAdapter == null) {
                sensorListAdapter = new SensorListAdapter(this, listaSensores);
                sensorListAdapter.setOnItemClickListener((v, pos) -> {
                    posicaoSelecionada = pos; editarSensor(); });
                sensorListAdapter.setOnItemLongClickListener((v, pos) -> {
                    if (actionMode != null) return false;
                    posicaoSelecionada = pos; viewSelecionada = v;
                    backgroundDrawable = v.getBackground();
                    v.setBackgroundColor(getColor(R.color.corDeSelecao));
                    recyclerViewSensores.setEnabled(false);
                    actionMode = startSupportActionMode(actionModeCallback);
                    return true; });
                recyclerViewSensores.setAdapter(sensorListAdapter);

                // Initial data load for annotations
                if (viewModel.getAnnotationsGrouped().getValue() != null) {
                    sensorListAdapter.updateAnnotations(viewModel.getAnnotationsGrouped().getValue());
                }
            } else {
                sensorListAdapter.updateList(sensors);
            }
        });

        // Observe grouped annotations to display them as a list under each sensor
        viewModel.getAnnotationsGrouped().observe(this, annotations -> {
            if (sensorListAdapter != null) {
                sensorListAdapter.updateAnnotations(annotations);
            }
        });
    }

    // Launchers — LiveData auto-refreshes the list after insert/update/delete
    private final ActivityResultLauncher<Intent> launcherNovo =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), r -> {});

    private final ActivityResultLauncher<Intent> launcherEditar =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), r -> {
                posicaoSelecionada = -1;
                if (actionMode != null) actionMode.finish();
            });

    private void abrirNovoSensor() {
        Intent i = new Intent(this, SensorDetailActivity.class);
        i.putExtra(SensorDetailActivity.KEY_MODO, SensorDetailActivity.MODO_NOVO);
        launcherNovo.launch(i);
    }

    private void editarSensor() {
        SensorEntity s = listaSensores.get(posicaoSelecionada);
        Intent i = new Intent(this, SensorDetailActivity.class);
        i.putExtra(SensorDetailActivity.KEY_MODO, SensorDetailActivity.MODO_EDITAR);
        i.putExtra(SensorDetailActivity.KEY_ID, s.getId());
        launcherEditar.launch(i);
    }

    private void excluirSensor() {
        final SensorEntity sensor = listaSensores.get(posicaoSelecionada);
        UtilsAlert.confirmarAcao(this, getString(R.string.deseja_apagar, sensor.getSensorName()),
                (d, w) -> viewModel.delete(sensor, rows -> {
                    if (rows != 1) {
                        runOnUiThread(() -> UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_excluir));
                        return;
                    }
                    runOnUiThread(() -> {
                        if (actionMode != null) actionMode.finish();
                        Snackbar.make(recyclerViewSensores, R.string.sensor_excluido, Snackbar.LENGTH_LONG)
                                .setAction(R.string.desfazer, v -> viewModel.insert(sensor, null))
                                .show();
                    });
                }), null);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensores_opcoes, menu);
        menuItemOrdencao = menu.findItem(R.id.menuItemOrdenacao);
        return true; }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        atualizarIconeOrdencacao(); return true; }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuItemAdicionar)  { abrirNovoSensor(); return true; }
        if (id == R.id.menuItemSobre)      { startActivity(new Intent(this, InfoAppActivity.class)); return true; }
        if (id == R.id.menuItemOrdenacao)  {
            salvarPreferencia(!ordenacaoAscendente);
            viewModel.setSortAscending(ordenacaoAscendente);
            atualizarIconeOrdencacao(); return true; }
        if (id == R.id.menuItemRestaurar)  { confirmarRestaurarPadroes(); return true; }
        if (id == R.id.menuItemSearch)     { startActivity(new Intent(this, SearchActivity.class)); return true; }


        if (id == R.id.menuExportPdf) {

            if (listaSensores == null || listaSensores.isEmpty()) {
                Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show();
                return true;
            }
            File file = PDFExporter.exportSensors(this, listaSensores);

            openPdf(file);
            return true;}

        if (id == R.id.menuExportCsv) {

            if (listaSensores == null || listaSensores.isEmpty()) {
                Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show();
                return true;
            }
            File file = new File(getExternalFilesDir(null), "sensors.csv");
            CsvExporter.exportSensors(listaSensores, file);

            Toast.makeText(this, "CSV exported", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.menuExportJson) {

            if (listaSensores == null || listaSensores.isEmpty()) {
                Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show();
                return true;
            }
            File file = new File(getExternalFilesDir(null), "sensors.json");
            JsonExporter.exportSensors(listaSensores, file);

            Toast.makeText(this, "JSON exported", Toast.LENGTH_SHORT).show();
            return true;
        }
    return super.onOptionsItemSelected(item);
    }

    private void atualizarIconeOrdencacao() {
        if (menuItemOrdencao != null)
            menuItemOrdencao.setIcon(ordenacaoAscendente
                    ? R.drawable.ic_action_ascending_order
                    : R.drawable.ic_action_descending_order); }

    private void lerPreferencias() {
        SharedPreferences p = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        ordenacaoAscendente = p.getBoolean(KEY_ORDENCACAO_ASCENDENTE, PADRAO_ORDENACAO); }

    private void salvarPreferencia(boolean val) {
        getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_ORDENCACAO_ASCENDENTE, val).apply();
        ordenacaoAscendente = val; }

    private void confirmarRestaurarPadroes() {
        UtilsAlert.confirmarAcao(this, R.string.deseja_restaurar_padroes, (d, w) -> {
            getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE).edit().clear().apply();
            ordenacaoAscendente = PADRAO_ORDENACAO;
            viewModel.setSortAscending(ordenacaoAscendente);
            atualizarIconeOrdencacao();
            Toast.makeText(this,
                    R.string.as_configuracoes_voltaram_para_o_padrao_de_instalacao,
                    Toast.LENGTH_LONG).show();
        }, null); }

    private void openPdf(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "No PDF app found", Toast.LENGTH_LONG).show();
        }
    }
}