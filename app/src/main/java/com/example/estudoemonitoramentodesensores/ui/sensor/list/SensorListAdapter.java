package com.example.estudoemonitoramentodesensores.ui.sensor.list;

import android.content.Context;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.model.SensorUsage;
import com.example.estudoemonitoramentodesensores.utils.UtilsLocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class: SensorListAdapter
 * Project: Estudo e Monitoramento de Sensores
 *
 * Description: Adapter para RecyclerView que exibe lista de sensores com
 *              suas respectivas anotações. Suporta clique e clique longo.
 *
 * @author Tiago Rodrigues
 * @version 1.2
 * @since 2026-04-05
 */
public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.SensoresHolder> {

    public interface OnItemClickListener     { void onItemClick(View v, int pos); }
    public interface OnItemLongClickListener { boolean onItemLongClick(View v, int pos); }

    private OnItemClickListener     onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private final Context           context;
    private List<SensorEntity>      listaSensores;
    private final String[]          sensorTipo;

    // Mapa para armazenar as anotações por sensor ID
    private Map<Long, List<AnnotationEntity>> annotationsMap = new HashMap<>();
    // Mapa para armazenar a contagem de anotações por sensor ID
    private Map<Long, Integer> countsMap = new HashMap<>();

    public class SensoresHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView tvNome, tvId, tvData, tvStatus, tvTipo, tvUso, tvNoAnnotations;
        LinearLayout layoutAnotacoes;

        SensoresHolder(@NonNull View v) {
            super(v);
            tvNome          = v.findViewById(R.id.textViewValorSensorNome);
            tvId            = v.findViewById(R.id.textViewValorSensorId);
            tvData          = v.findViewById(R.id.textViewValorDataPrimeiraMedicao);
            tvStatus        = v.findViewById(R.id.textViewValorSensorIsActive);
            tvTipo          = v.findViewById(R.id.textViewValorSensorTipo);
            tvUso           = v.findViewById(R.id.textViewValorSensorUso);
            tvNoAnnotations = v.findViewById(R.id.textViewNoAnnotations);
            layoutAnotacoes = v.findViewById(R.id.layoutContainerAnotacoes);
            
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override public void onClick(View v) {
            if (onItemClickListener != null) onItemClickListener.onItemClick(v, getAdapterPosition()); }
        @Override public boolean onLongClick(View v) {
            return onItemLongClickListener != null
                    && onItemLongClickListener.onItemLongClick(v, getAdapterPosition()); }
    }

    public SensorListAdapter(Context context, List<SensorEntity> list) {
        this.context       = context;
        this.listaSensores = list;
        this.sensorTipo    = context.getResources().getStringArray(R.array.sensoresTypo);
    }

    // Atualiza lista de sensores
    public void updateList(List<SensorEntity> newList) {
        this.listaSensores = newList;
        notifyDataSetChanged();
    }

    // Atualiza o mapa de anotações
    public void updateAnnotations(Map<Long, List<AnnotationEntity>> annotations) {
        this.annotationsMap = annotations;
        notifyDataSetChanged();
    }

    // Atualiza o mapa de contagem de anotações
    public void updateAnnotationCounts(Map<Long, Integer> counts) {
        this.countsMap = counts;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public SensoresHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SensoresHolder(LayoutInflater.from(context)
                .inflate(R.layout.line_list_sensors, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SensoresHolder h, int position) {
        SensorEntity s = listaSensores.get(position);
        h.tvNome.setText(s.getSensorName());
        h.tvId.setText(s.getSensorId());
        h.tvData.setText(UtilsLocalDate.formatLocalDate(s.getFirstMeasureDate()));
        h.tvStatus.setText(context.getString(R.string.active).equals(s.getSensorStatus())
                ? R.string.possui_modulo : R.string.nao_possui_modulo);

        if (s.getSensorType() >= 0 && s.getSensorType() < sensorTipo.length)
            h.tvTipo.setText(sensorTipo[s.getSensorType()]);

        SensorUsage u = s.getSensorUsage();
        if (u != null) switch (u) {
            case INDUSTRIAL:    h.tvUso.setText(R.string.radioButtonSensorUsoIndustrial); break;
            case RESIDENTIAL:   h.tvUso.setText(R.string.radioButtonSensorUsoResidencial); break;
            case AUTOMOTIVE:    h.tvUso.setText(R.string.radioButtonSensorUsoAutomotivo); break;
            case SMART_CITIES:  h.tvUso.setText(R.string.radioButtonSensorUsoCidadesInteligentes); break;
            case ENERGY_SECTOR: h.tvUso.setText(R.string.radioButtonUsoSensorEnergia); break;
            case RETAIL:        h.tvUso.setText(R.string.radioButtonSensorVarejo); break;
            case AGRICULTURE:   h.tvUso.setText(R.string.radioButtonUsoAgricultura); break;
            case OTHERS:        h.tvUso.setText(R.string.radioButtonSensorUsoOutros); break;
        }

        // Exibe as anotações
        h.layoutAnotacoes.removeAllViews();
        List<AnnotationEntity> annotations = annotationsMap.get(s.getId());
        
        if (annotations != null && !annotations.isEmpty()) {
            h.tvNoAnnotations.setVisibility(View.GONE);
            h.layoutAnotacoes.setVisibility(View.VISIBLE);
            
            for (AnnotationEntity note : annotations) {
                TextView tvNote = new TextView(context);
                tvNote.setText(note.getText());
                tvNote.setBackgroundResource(R.drawable.bg_note_item);
                
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 8); // Espaço entre as notas
                tvNote.setLayoutParams(params);
                
                h.layoutAnotacoes.addView(tvNote);
            }
        } else {
            h.layoutAnotacoes.setVisibility(View.GONE);
            h.tvNoAnnotations.setVisibility(View.VISIBLE);
            
            Integer count = countsMap.get(s.getId());
            if (count != null && count > 0) {
                h.tvNoAnnotations.setText(context.getString(R.string.annotation_count, count));
            } else {
                h.tvNoAnnotations.setText(R.string.no_annotations);
            }
        }
    }

    @Override public int getItemCount() { return listaSensores != null ? listaSensores.size() : 0; }
    public void setOnItemClickListener(OnItemClickListener l)     { onItemClickListener = l; }
    public void setOnItemLongClickListener(OnItemLongClickListener l) { onItemLongClickListener = l; }
}