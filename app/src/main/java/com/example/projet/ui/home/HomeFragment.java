package com.example.projet.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projet.MainActivity;
import com.example.projet.databinding.FragmentHomeBinding;
import com.example.projet.model.Accident;
import com.example.projet.model.SharedModel;
import com.example.projet.ui.AccidentAdapter;
import com.example.projet.ui.accident_details.AccidentDetailsFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private SharedModel sharedModel;
    private RecyclerView rV;
    private SwipeRefreshLayout swipeRefresh;
    private String geofilter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) requireActivity()).updateLocation();
            }
        });

        rV = binding.recyclerView;
        initScrollListeners();

        AccidentAdapter accidentAdapter = new AccidentAdapter() {
            @Override
            public void onClick(int position) {
                Accident accident = homeViewModel.getAccidents().getValue().get(position);

                // Create new fragment and transaction
                AccidentDetailsFragment accidentDetailsFragment = AccidentDetailsFragment.newInstance(accident);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(((ViewGroup)getView().getParent()).getId(), accidentDetailsFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        };
        rV.setAdapter(accidentAdapter);

        //init models observers
        homeViewModel.getAccidents().observe(getViewLifecycleOwner(), accidents -> {
            accidentAdapter.setAccidents(accidents);
        });

        sharedModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            if(location != null)
                geofilter = "&geofilter.distance=" + location.getLatitude() + "%2C" + location.getLongitude() + "%2C" + sharedModel.getRadius().getValue();
            else
                geofilter = "";

            homeViewModel.getAccidents().setValue(new ArrayList< Accident >());
            homeViewModel.loadAccidents("https://data.opendatasoft.com//api/records/1.0/search/?dataset=accidents-corporels-de-la-circulation-millesime%40public&q=&start=&facet=Num_Acc&facet=jour&facet=mois&facet=an&facet=lum&facet=adr&facet=dep&facet=atm&facet=col&facet=lat&facet=long&facet=surf&facet=catv&facet=obs&facet=obsm&facet=grav" + geofilter);
            swipeRefresh.setRefreshing(false);
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
                homeViewModel.loadAccidents("https://data.opendatasoft.com//api/records/1.0/search/?dataset=accidents-corporels-de-la-circulation-millesime%40public&q=&start=" + 10*page + "&facet=Num_Acc&facet=jour&facet=mois&facet=an&facet=lum&facet=adr&facet=dep&facet=atm&facet=col&facet=lat&facet=long&facet=surf&facet=catv&facet=obs&facet=obsm&facet=grav" + geofilter);
            }
        };

        rV.addOnScrollListener(scrollListener);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity) requireActivity()).updateLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}