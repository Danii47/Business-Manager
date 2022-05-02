package com.example.proyectotfgjavierlahoz.actividades.fragmentos.departamentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectotfgjavierlahoz.databinding.FragmentDepartamentosBinding;


public class DepartamentosFragment extends Fragment {

    private FragmentDepartamentosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DepartamentosViewModel slideshowViewModel =
                new ViewModelProvider(this).get(DepartamentosViewModel.class);

        binding = FragmentDepartamentosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}