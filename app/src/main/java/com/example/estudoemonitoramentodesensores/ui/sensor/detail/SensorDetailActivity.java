package com.example.estudoemonitoramentodesensores.ui.sensor.detail;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.model.SensorUsage;
import com.example.estudoemonitoramentodesensores.ui.annotation.AnnotationListActivity;
import com.example.estudoemonitoramentodesensores.ui.sensor.list.SensorListActivity;
import com.example.estudoemonitoramentodesensores.utils.UtilsAlert;
import com.example.estudoemonitoramentodesensores.utils.UtilsLocalDate;
import com.google.android.material.snackbar.Snackbar;
import java.time.LocalDate;

public class SensorDetailActivity extends AppCompatActivity {

    public static final String KEY_MODO        = "MODO";
    public static final String KEY_ID          = "ID";
    public static final String KEY_SUGERIR_TIPO = "SUGERIR_CLASSE";
    public static final String KEY_ULTIMA_TIPO  = "ULTIMA_CLASSE";
    public static final int    MODO_NOVO   = 0;
    public static final int    MODO_EDITAR = 1;

    private EditText    editNome, editSensorId, editData;
    private CheckBox    checkStatus;
    private RadioGroup  rgUso;
    private RadioButton rbIndustrial, rbResidencial, rbAutomotivo, rbCidades,
            rbEnergia, rbVarejo, rbAgricultura, rbOutros;
    private Spinner     spinnerTipo;
    private Button      btnAnnotations;

    private SensorDetailViewModel viewModel;
    private int          modo;
    private SensorEntity sensorOriginal;
    private boolean      formLoaded   = false;
    private boolean      sugerirTipo  = false;
    private int          ultimoTipo   = 0;
    private LocalDate    dataMedicao;
    private int          anosParaTras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_sensor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editNome        = findViewById(R.id.editTextSensorNome);
        editSensorId    = findViewById(R.id.editTextSensorId);
        editData        = findViewById(R.id.editTextDataPrimeiraMedicao);
        rgUso           = findViewById(R.id.radioGroupSensorUso);
        spinnerTipo     = findViewById(R.id.spinnerSensorClasse);
        checkStatus     = findViewById(R.id.checkBoxSensorStatus);
        btnAnnotations  = findViewById(R.id.buttonAnnotations);
        rbIndustrial    = findViewById(R.id.radioButtonSensorUsoIndustrial);
        rbResidencial   = findViewById(R.id.radioButtonUsoResidencial);
        rbAutomotivo    = findViewById(R.id.radioButtonUsoAutomotivo);
        rbCidades       = findViewById(R.id.radioButtonCidadesInteligentes);
        rbEnergia       = findViewById(R.id.radioButtonUsoSetorEnergia);
        rbVarejo        = findViewById(R.id.radioButtonSensorUsoVarejo);
        rbAgricultura   = findViewById(R.id.radioButtonSensorUsoAgricultura);
        rbOutros        = findViewById(R.id.radioButtonSensorUsoOutros);

        editData.setFocusable(false);
        editData.setOnClickListener(v -> mostrarDatePicker());

        lerPreferencias();
        anosParaTras = getResources().getInteger(R.integer.anos_para_tras);
        dataMedicao  = LocalDate.now().minusYears(anosParaTras);

        viewModel = new ViewModelProvider(this).get(SensorDetailViewModel.class);

        // Finish with OK when save completes
        viewModel.getSavedSensorId().observe(this, id -> {
            if (id == null) return;
            salvarUltimoTipo(spinnerTipo.getSelectedItemPosition());
            setResult(RESULT_OK, new Intent().putExtra(KEY_ID, id));
            finish();
        });

        // Show error from background save
        viewModel.getSaveError().observe(this, resId -> {
            if (resId != null) UtilsAlert.mostrarAviso(this, resId);
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        modo = bundle.getInt(KEY_MODO);

        if (modo == MODO_NOVO) {
            setTitle(R.string.novo_sensor);
            if (sugerirTipo) spinnerTipo.setSelection(ultimoTipo);
            btnAnnotations.setVisibility(View.INVISIBLE);
        } else {
            setTitle(R.string.editar_sensor);
            long id = bundle.getLong(KEY_ID);
            viewModel.loadSensor(id);
            viewModel.loadAnnotationCount(id);

            viewModel.getSensor().observe(this, s -> {
                if (s != null && !formLoaded) {
                    formLoaded = true;
                    sensorOriginal = s;
                    popularFormulario(s);
                }
            });

            viewModel.getAnnotationCount().observe(this, count -> {
                if (count != null)
                    btnAnnotations.setText(getString(R.string.annotations, count));
            });
        }
    }

    private void popularFormulario(SensorEntity s) {
        editNome.setText(s.getSensorName());
        editSensorId.setText(s.getSensorId());
        if (s.getFirstMeasureDate() != null) dataMedicao = s.getFirstMeasureDate();
        editData.setText(UtilsLocalDate.formatLocalDate(dataMedicao));
        checkStatus.setChecked(getString(R.string.active).equals(s.getSensorStatus()));
        spinnerTipo.setSelection(s.getSensorType());

        SensorUsage u = s.getSensorUsage();
        if      (u == SensorUsage.INDUSTRIAL)    rbIndustrial.setChecked(true);
        else if (u == SensorUsage.RESIDENTIAL)   rbResidencial.setChecked(true);
        else if (u == SensorUsage.AUTOMOTIVE)    rbAutomotivo.setChecked(true);
        else if (u == SensorUsage.SMART_CITIES)  rbCidades.setChecked(true);
        else if (u == SensorUsage.ENERGY_SECTOR) rbEnergia.setChecked(true);
        else if (u == SensorUsage.RETAIL)        rbVarejo.setChecked(true);
        else if (u == SensorUsage.AGRICULTURE)   rbAgricultura.setChecked(true);
        else if (u == SensorUsage.OTHERS)        rbOutros.setChecked(true);

        editNome.requestFocus();
        editNome.setSelection(editNome.length());
    }

    private void mostrarDatePicker() {
        new DatePickerDialog(this, R.style.SpinnerDatePickerDialogTheme,
                (v, y, m, d) -> {
                    dataMedicao = LocalDate.of(y, m + 1, d);
                    editData.setText(UtilsLocalDate.formatLocalDate(dataMedicao));
                },
                dataMedicao.getYear(), dataMedicao.getMonthValue() - 1, dataMedicao.getDayOfMonth())
        {{  getDatePicker().setMaxDate(UtilsLocalDate.toMillissegundos(LocalDate.now())); }}
                .show();
    }

    public void limparSensorCampos() {
        final String nome = editNome.getText().toString();
        final String sid  = editSensorId.getText().toString();
        final LocalDate dataAntes = dataMedicao;
        final int       usoId     = rgUso.getCheckedRadioButtonId();
        final int       tipo      = spinnerTipo.getSelectedItemPosition();
        final boolean   status    = checkStatus.isChecked();
        final ScrollView sv       = findViewById(R.id.main);

        editNome.setText(null); editSensorId.setText(null); editData.setText(null);
        dataMedicao = LocalDate.now(); rgUso.clearCheck();
        spinnerTipo.setSelection(0); checkStatus.setChecked(false);

        Snackbar.make(sv, R.string.entradas_foram_apagadas, Snackbar.LENGTH_LONG)
                .setAction(R.string.desfazer, v -> {
                    editNome.setText(nome); editSensorId.setText(sid);
                    dataMedicao = dataAntes;
                    editData.setText(UtilsLocalDate.formatLocalDate(dataMedicao));
                    spinnerTipo.setSelection(tipo);
                    if (usoId != -1) rgUso.check(usoId);
                    checkStatus.setChecked(status);
                }).show();
    }

    public void salvarSensorCampos() {
        String nome = editNome.getText().toString().trim();
        if (nome.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.por_favor_adicionar_o_nome_do_sensor);
            editNome.requestFocus(); return; }
        String sid = editSensorId.getText().toString().trim();
        if (sid.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.por_favor_adicionar_o_identificador_do_sensor);
            editSensorId.requestFocus(); return; }
        if (editData.getText().toString().trim().isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.the_data_of_first_measure_cannot_be_empty); return; }
        int age = UtilsLocalDate.diferencaEmAnosParaHoje(dataMedicao);
        if (age < 0 || age > 70) {
            UtilsAlert.mostrarAviso(this, R.string.idade_do_sensor_alert); return; }
        SensorUsage uso = getSelectedUsage();
        if (uso == null) {
            UtilsAlert.mostrarAviso(this, R.string.por_favor_preencher_o_campo_de_uso_do_sensor); return; }
        if (spinnerTipo.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            UtilsAlert.mostrarAviso(this, R.string.o_spinner_nao_possui_valores); return; }

        String status = checkStatus.isChecked() ? getString(R.string.active) : getString(R.string.inactive);
        SensorEntity sensor = new SensorEntity(nome, sid,
                spinnerTipo.getSelectedItemPosition(), status, uso, dataMedicao);

        if (sensor.equals(sensorOriginal)) { setResult(RESULT_CANCELED); finish(); return; }

        if (modo == MODO_NOVO)  { viewModel.saveNew(sensor); }
        else { sensor.setId(sensorOriginal.getId()); viewModel.saveEdit(sensor); }
        // finish() triggered by getSavedSensorId() observer
    }

    private SensorUsage getSelectedUsage() {
        int id = rgUso.getCheckedRadioButtonId();
        if (id == R.id.radioButtonSensorUsoIndustrial)  return SensorUsage.INDUSTRIAL;
        if (id == R.id.radioButtonUsoResidencial)       return SensorUsage.RESIDENTIAL;
        if (id == R.id.radioButtonUsoAutomotivo)        return SensorUsage.AUTOMOTIVE;
        if (id == R.id.radioButtonCidadesInteligentes)  return SensorUsage.SMART_CITIES;
        if (id == R.id.radioButtonUsoSetorEnergia)      return SensorUsage.ENERGY_SECTOR;
        if (id == R.id.radioButtonSensorUsoVarejo)      return SensorUsage.RETAIL;
        if (id == R.id.radioButtonSensorUsoAgricultura) return SensorUsage.AGRICULTURE;
        if (id == R.id.radioButtonSensorUsoOutros)      return SensorUsage.OTHERS;
        return null; }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_opcoes, menu); return true; }
    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menuItemSugerirClasseSensor).setChecked(sugerirTipo); return true; }
    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)                  { finish(); return true; }
        if (id == R.id.itemMenuSalvar)                { salvarSensorCampos(); return true; }
        if (id == R.id.menuItemLimpar)                { limparSensorCampos(); return true; }
        if (id == R.id.menuItemSugerirClasseSensor)   {
            boolean val = !item.isChecked();
            salvarSugerirTipo(val); item.setChecked(val);
            if (sugerirTipo) spinnerTipo.setSelection(ultimoTipo); return true; }
        return super.onOptionsItemSelected(item); }

    private void lerPreferencias() {
        SharedPreferences p = getSharedPreferences(SensorListActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        sugerirTipo = p.getBoolean(KEY_SUGERIR_TIPO, false);
        ultimoTipo  = p.getInt(KEY_ULTIMA_TIPO, 0); }
    private void salvarSugerirTipo(boolean v) {
        getSharedPreferences(SensorListActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_SUGERIR_TIPO, v).apply(); sugerirTipo = v; }
    private void salvarUltimoTipo(int v) {
        getSharedPreferences(SensorListActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE)
                .edit().putInt(KEY_ULTIMA_TIPO, v).apply(); }

    private final ActivityResultLauncher<Intent> launcherAnnotations =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    r -> { /* annotation count LiveData auto-updates */ });

    public void openAnnotation(View view) {
        if (sensorOriginal == null) return;
        Intent i = new Intent(this, AnnotationListActivity.class);
        i.putExtra(AnnotationListActivity.KEY_ID_SENSOR,   sensorOriginal.getId());
        i.putExtra(AnnotationListActivity.KEY_SENSOR_NAME, sensorOriginal.getSensorName());
        launcherAnnotations.launch(i);
    }
}