package com.example.projet.ui.accident_details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projet.databinding.FragmentNotificationsBinding;
import com.example.projet.model.Accident;

public class AccidentDetailsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public static AccidentDetailsFragment newInstance(Accident accident) {
        AccidentDetailsFragment accidentDetailsFragment = new AccidentDetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable("accident", accident);
        accidentDetailsFragment.setArguments(args);

        return accidentDetailsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        Accident accident = (Accident) getArguments().getSerializable("accident");
        Log.d("accident", accident.toString());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}