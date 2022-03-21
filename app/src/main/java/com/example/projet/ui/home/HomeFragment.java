package com.example.projet.ui.home;

import android.os.Bundle;
import android.os.RecoverySystem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.databinding.FragmentHomeBinding;
import com.example.projet.ui.AccidentAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private RecyclerView rV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rV = binding.recyclerView;
        initScrollListeners();

        AccidentAdapter accidentAdapter = new AccidentAdapter();
        rV.setAdapter(accidentAdapter);

        homeViewModel.getAccidents().observe(getViewLifecycleOwner(), accidents -> {
            accidentAdapter.setAccidents(accidents);
        });
        return root;
    }

    private void initScrollListeners() {
        EndlessRecyclerViewScrollListener scrollListener;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        rV.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                homeViewModel.loadAccidents("https://data.opendatasoft.com//api/records/1.0/search/?dataset=accidents-corporels-de-la-circulation-millesime%40public&q=&start=" + 10*page + "&facet=Num_Acc&facet=jour&facet=mois&facet=an&facet=lum&facet=dep&facet=atm&facet=col&facet=lat&facet=long&facet=surf&facet=catv&facet=obs&facet=obsm&facet=grav");
            }
        };

        rV.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}