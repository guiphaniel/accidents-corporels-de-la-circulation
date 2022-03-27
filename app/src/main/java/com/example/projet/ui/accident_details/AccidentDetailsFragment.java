package com.example.projet.ui.accident_details;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projet.R;
import com.example.projet.databinding.FragmentAccidentDetailsBinding;
import com.example.projet.model.Accident;

import java.lang.reflect.Field;
import java.util.Locale;

public class AccidentDetailsFragment extends Fragment {

    private FragmentAccidentDetailsBinding binding;

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

        binding = FragmentAccidentDetailsBinding.inflate(inflater, container, false);
        Accident accident = (Accident) getArguments().getSerializable("accident");

        Uri imgUri;
        String g = accident.grav;
        if(g.contains("Tué")) {
            imgUri=Uri.parse(String.valueOf("android.resource://com.example.projet/" + R.drawable.dead));
            binding.ivGrav2.setImageURI(imgUri);
        } else if(g.contains("Blessé")) {
            imgUri=Uri.parse("android.resource://com.example.projet/" + R.drawable.hurt);
            binding.ivGrav2.setImageURI(imgUri);
        } else {
            imgUri=Uri.parse("android.resource://com.example.projet/" + R.drawable.unhurt);
            binding.ivGrav2.setImageURI(imgUri);
        }

        binding.tvNumAcc.setText(accident.num_acc);
        binding.tvLat.setText(accident.getLat());
        binding.tvLon.setText(accident.getLon());
        binding.tvAdr.setText(accident.adr);
        binding.tvDate2.setText(String.format("%s-%s-%s", accident.jour, accident.mois, accident.an));
        binding.tvLum.setText(accident.lum);
        binding.tvAtm.setText(accident.atm);
        binding.tvSurf.setText(accident.surf);
        binding.tvCatv.setText(accident.catv);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void hide() {

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        Fragment sourceFragment = getParentFragmentManager().getFragments().get(0);

        transaction.remove(this);
        transaction.show(sourceFragment);

        // Commit the transaction
        transaction.commit();

    }
}