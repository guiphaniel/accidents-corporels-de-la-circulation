package com.example.projet.ui.list;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.example.projet.databinding.FragmentListBinding;
import com.example.projet.model.Accident;
import com.example.projet.model.SharedModel;
import com.example.projet.ui.AccidentAdapter;
import com.example.projet.ui.accident_details.AccidentDetailsFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private ListViewModel listViewModel;
    private SharedModel sharedModel;
    private RecyclerView rV;
    private SwipeRefreshLayout swipeRefresh;
    private String geofilter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listViewModel.getAccidents().setValue(new ArrayList< Accident >());
                ((MainActivity) requireActivity()).updateLocation();
            }
        });

        rV = binding.recyclerView;
        initScrollListeners();

        AccidentAdapter accidentAdapter = new AccidentAdapter() {
            @Override
            public void onClick(int position) {
                Accident accident = listViewModel.getAccidents().getValue().get(position);

                // Create new fragment
                AccidentDetailsFragment accidentDetailsFragment = AccidentDetailsFragment.newInstance(accident);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                Fragment listFragment = getParentFragmentManager().getFragments().get(0);

                transaction.hide(listFragment);
                transaction.add(((ViewGroup)getView().getParent()).getId(), accidentDetailsFragment, "details");

                // Commit the transaction
                transaction.commit();
            }
        };
        rV.setAdapter(accidentAdapter);

        //init models observers
        listViewModel.getAccidents().observe(getViewLifecycleOwner(), accidents -> {
            accidentAdapter.setAccidents(accidents);
        });

        sharedModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            if(!checkForInternetConnection())
                return;

            if(location != null)
                geofilter = "&geofilter.distance=" + location.getLatitude() + "%2C" + location.getLongitude() + "%2C" + sharedModel.getRadius().getValue();
            else
                geofilter = "";

            listViewModel.loadAccidents("https://data.opendatasoft.com//api/records/1.0/search/?dataset=accidents-corporels-de-la-circulation-millesime%40public&q=&start=&facet=Num_Acc&facet=jour&facet=mois&facet=an&facet=lum&facet=adr&facet=dep&facet=atm&facet=col&facet=lat&facet=long&facet=surf&facet=catv&facet=obs&facet=obsm&facet=grav" + geofilter);
            swipeRefresh.setRefreshing(false);
        });

        // init accidents
        if(listViewModel.getAccidents().getValue().isEmpty()){
            ((MainActivity) requireActivity()).updateLocation();
        }

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
                if(!checkForInternetConnection())
                    return;
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                listViewModel.loadAccidents("https://data.opendatasoft.com//api/records/1.0/search/?dataset=accidents-corporels-de-la-circulation-millesime%40public&q=&start=" + 10*page + "&facet=Num_Acc&facet=jour&facet=mois&facet=an&facet=lum&facet=adr&facet=dep&facet=atm&facet=col&facet=lat&facet=long&facet=surf&facet=catv&facet=obs&facet=obsm&facet=grav" + geofilter);
            }
        };

        rV.addOnScrollListener(scrollListener);
    }

    private boolean checkForInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        Snackbar.make(getView(), "Vous n'êtes pas connecté à internet.", Snackbar.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}