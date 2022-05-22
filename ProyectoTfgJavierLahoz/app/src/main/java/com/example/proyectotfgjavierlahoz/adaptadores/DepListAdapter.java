package com.example.proyectotfgjavierlahoz.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.modelos.Departamento;

import java.util.List;

public class DepListAdapter extends RecyclerView.Adapter<DepListAdapter.DepViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    List<Departamento> departamentos;
    private View.OnClickListener listener;
    private View.OnLongClickListener longListener;

    public DepListAdapter(List<Departamento> departamentos){
        this.departamentos = departamentos;
    }

    @NonNull
    @Override
    public DepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_departamentos, parent, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new DepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepViewHolder holder, int position) {
        Departamento departamento = departamentos.get(position);

       if(departamento.getImagen() != null){
            holder.imgDepartamento.setImageBitmap(departamento.getImagen());
        } else {
            holder.imgDepartamento.setImageResource(R.drawable.ic_outline_home_work_24);
        }

        holder.txvNombre.setText(departamento.getNombre());
        holder.txvEncargado.setText(departamento.getEncargado());
    }

    @Override
    public int getItemCount() {
        return departamentos.size();
    }

    public void filtrar(List<Departamento> lista){
        departamentos = lista;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longListener){
        this.longListener = longListener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(longListener != null){
            longListener.onLongClick(view);
        }
        return false;
    }

    public class DepViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgDepartamento;
        public TextView txvNombre;
        public TextView txvEncargado;

        public DepViewHolder(View view) {
            super(view);
            imgDepartamento = (ImageView) view.findViewById(R.id.imgEmpleado);
            txvNombre = (TextView) view.findViewById(R.id.txvDepartamento);
            txvEncargado = (TextView) view.findViewById(R.id.txvEncargado);
        }
    }
}
