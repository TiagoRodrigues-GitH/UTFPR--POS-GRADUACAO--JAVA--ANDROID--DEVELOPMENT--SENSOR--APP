package com.example.estudoemonitoramentodesensores.ui.annotation;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.utils.UtilsLocalDateTime;

import java.util.List;

/**
 * Class: AnnotationListAdapter
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-18
 */
public class AnnotationListAdapter extends RecyclerView.Adapter<AnnotationListAdapter.AnnotationHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private OnCreateContextMenu onCreateContextMenu;
    private OnContextMenuClickListener onContextMenuClickListener;

    private Context context;
    private List<AnnotationEntity> listAnnotationEntity;

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    interface OnCreateContextMenu {
        void onCreateContextMenu(ContextMenu menu,
                                 View v,
                                 ContextMenu.ContextMenuInfo menuInfo,
                                 int position,
                                 MenuItem.OnMenuItemClickListener menuItemClickListener);
    }

    interface OnContextMenuClickListener {
        boolean onContextMenuItemClick(MenuItem menuItem, int position);
    }

    public class AnnotationHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                                                                             View.OnLongClickListener,
                                                                             View.OnCreateContextMenuListener {
        public TextView textViewCreatedAt;
        public TextView textViewTextValue;


        public AnnotationHolder(@NonNull View itemView) {
            super(itemView);

            textViewCreatedAt = itemView.findViewById(R.id.textViewCreatedAt);
            textViewTextValue = itemView.findViewById(R.id.textViewTextValue);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (onCreateContextMenu != null) {
                onCreateContextMenu.onCreateContextMenu(menu,
                        v,
                        menuInfo,
                        getAdapterPosition(),
                        onMenuItemClickListener);
            }
        }

        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                if (onContextMenuClickListener != null) {
                    onContextMenuClickListener.onContextMenuItemClick(item, getAdapterPosition());
                    return true;
                }
                return false;
            }
        };
    }

    public AnnotationListAdapter(Context context, List<AnnotationEntity> listAnnotationEntity) {
        this.context = context;
        this.listAnnotationEntity = listAnnotationEntity;

    }

    @NonNull
    @Override
    public AnnotationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.line_list_annotations, parent, false);

        return new AnnotationHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnotationHolder holder, int position) {

        AnnotationEntity annotationEntity = listAnnotationEntity.get(position);

        holder.textViewCreatedAt.setText(UtilsLocalDateTime.formatLocalDateTime(annotationEntity.getCreatedAt()));
        holder.textViewTextValue.setText(annotationEntity.getText());


    }



    @Override
    public int getItemCount() {
        return listAnnotationEntity.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnCreateContextMenu getOnCreateContextMenu() {
        return onCreateContextMenu;
    }

    public void setOnCreateContextMenu(OnCreateContextMenu onCreateContextMenu) {
        this.onCreateContextMenu = onCreateContextMenu;
    }

    public OnContextMenuClickListener getOnContextMenuClickListener() {
        return onContextMenuClickListener;
    }

    public void setOnContextMenuClickListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.onContextMenuClickListener = onContextMenuClickListener;
    }
}