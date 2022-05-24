package com.example.proyectotfgjavierlahoz.adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.modelos.Fichaje;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class FichListAdapter extends RecyclerView.Adapter<FichListAdapter.FichViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    List<Fichaje> fichajes;
    private View.OnClickListener listener;
    private View.OnLongClickListener longListener;

    public FichListAdapter(List<Fichaje> fichajes){
        this.fichajes = fichajes;
    }

    @NonNull
    @Override
    public FichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_fichaje, parent, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new FichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FichViewHolder holder, int position) {
        Fichaje fichaje = fichajes.get(position);
        Date inicio = null, fin = null;
        if(fichaje.getFichFin()!=null){
            holder.txvDia.setText(fichaje.getFichDia() + " - " + fichaje.getFichFinDia());
            holder.txvEntradaSalida.setText(fichaje.getFichInicio() + " - " + fichaje.getFichFin());
        } else{
            holder.txvDia.setText(fichaje.getFichDia() + " - ");
            holder.txvEntradaSalida.setText(fichaje.getFichInicio() + " - ");
        }

        java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        try {
            inicio = df.parse(fichaje.getFichInicio());
            if(fichaje.getFichFin() != null){
                fin = df.parse(fichaje.getFichFin());

                long tiempoMS = fin.getTime() - inicio.getTime();

                Log.i("testHoras", String.valueOf(fin.getTime() + " " + inicio.getTime()));

                int tiempoSegundos = (int) (tiempoMS / 1000);
                int horas, minutos, segundos;
                horas = tiempoSegundos / 3600;
                tiempoSegundos = tiempoSegundos - (horas * 3600);
                minutos = tiempoSegundos / 60;
                tiempoSegundos = tiempoSegundos - (minutos * 60);
                segundos = tiempoSegundos;

                if(horas == 0 && minutos == 0){
                    holder.txvHoras.setText(segundos + " segundos");
                } else if (horas == 0){
                    holder.txvHoras.setText(minutos + " minutos " + segundos + " segundos");
                } else if (minutos == 0){
                    holder.txvHoras.setText(horas + " horas " + segundos + " segundos");
                } else {
                    holder.txvHoras.setText(horas + " horas " + minutos + " minutos " + segundos + " segundos");
                }
            } else {
                holder.txvHoras.setText("En curso");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return fichajes.size();
    }

    public void filtrar(List<Fichaje> lista){
        fichajes = lista;
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

    public class FichViewHolder extends RecyclerView.ViewHolder {

        public TextView txvDia;
        public TextView txvEntradaSalida;
        public TextView txvHoras;

        public FichViewHolder(View view) {
            super(view);
            txvDia = (TextView) view.findViewById(R.id.txvDia);
            txvEntradaSalida = (TextView) view.findViewById(R.id.txvEntradaSalida);
            txvHoras = (TextView) view.findViewById(R.id.txvHoras);
        }
    }
}