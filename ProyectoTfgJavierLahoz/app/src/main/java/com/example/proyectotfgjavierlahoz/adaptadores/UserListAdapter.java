package com.example.proyectotfgjavierlahoz.adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    List<Empleado> usuarios;
    private View.OnClickListener listener;
    private View.OnLongClickListener longListener;

    public UserListAdapter(List<Empleado> usuarios){
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_usuarios, parent, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Empleado usuario = usuarios.get(position);

        if(usuario.getImagen() != null){
            holder.imgUsuario.setImageBitmap(usuario.getImagen());
        } else {
            holder.imgUsuario.setImageResource(R.drawable.user_logo);
        }

        holder.txvNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
        holder.txvEmail.setText(usuario.getCorreo());
        holder.txvMovil.setText(usuario.getMovil());
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public void filtrar(List<Empleado> lista){
        usuarios = lista;
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

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgUsuario;
        public TextView txvNombre;
        public TextView txvEmail;
        public TextView txvMovil;

        public UserViewHolder(View view) {
            super(view);
            imgUsuario = (ImageView) view.findViewById(R.id.imgEmpleado);
            txvNombre = (TextView) view.findViewById(R.id.txvNombre);
            txvEmail = (TextView) view.findViewById(R.id.txvEmail);
            txvMovil = (TextView) view.findViewById(R.id.txvMovil);
        }
    }
}
