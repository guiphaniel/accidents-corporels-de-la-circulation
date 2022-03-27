package com.example.projet.ui.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.projet.R;
import com.example.projet.databinding.FragmentMapBinding;
import com.example.projet.model.Accident;
import com.example.projet.model.Cluster;
import com.example.projet.model.SharedModel;
import com.example.projet.ui.accident_details.AccidentDetailsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;
    private SharedModel sharedModel;
    private HashMap<Marker, Accident> markers;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setOnMarkerClickListener(marker -> {
                Accident accident = markers.get(marker);

                if (accident == null)
                    return false;

                AccidentDetailsFragment accidentDetailsFragment = AccidentDetailsFragment.newInstance(accident);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                Fragment mapFragment = getParentFragmentManager().getFragments().get(0);

                transaction.hide(mapFragment);
                transaction.add(((ViewGroup)getView().getParent()).getId(), accidentDetailsFragment, "details");

                // Commit the transaction
                transaction.commit();

                return true;
            });

            sharedModel = new ViewModelProvider(requireActivity()).get(SharedModel.class);
            mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

            markers = new HashMap<>();
            float maxZoom = googleMap.getMaxZoomLevel() - googleMap.getMinZoomLevel();
            final int[] lastZoom = {-1};

            mapViewModel.getClusters().observe(getViewLifecycleOwner(), clusters -> {
                addMyPosMarker(googleMap);
                for(Cluster c : clusters) {
                    LatLng myPos = new LatLng(c.getLocation().latitude, c.getLocation().longitude);

                    BitmapDrawable bitmap = (BitmapDrawable)getResources().getDrawable(R.drawable.accident);
                    int width = 50;
                    int height = bitmap.getIntrinsicHeight() * width / bitmap.getIntrinsicWidth();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap.getBitmap(), width, height, false);

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(myPos).title(String.valueOf(c.getCount())).icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
                    markers.put(marker, null);
                }
            });

            mapViewModel.getAccidents().observe(getViewLifecycleOwner(), accidents -> {
                addMyPosMarker(googleMap);
                for(Accident a : accidents) {
                    if (a.getLat().equals("Inconnu(e)") || a.getLon().equals("Inconnu(e)"))
                        continue;

                    LatLng pos = new LatLng(Double.parseDouble(a.getLat()), Double.parseDouble(a.getLon()));

                    BitmapDrawable bitmap;
                    String g = a.grav;
                    if(g.contains("Tué")) {
                        bitmap = (BitmapDrawable)getResources().getDrawable(R.drawable.dead);
                    } else if(g.contains("Blessé")) {
                        bitmap = (BitmapDrawable)getResources().getDrawable(R.drawable.hurt);
                    } else {
                        bitmap = (BitmapDrawable)getResources().getDrawable(R.drawable.unhurt);
                    }
                    int width = 50;
                    int height = bitmap.getIntrinsicHeight() * width / bitmap.getIntrinsicWidth();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap.getBitmap(), width, height, false);

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(pos).title(a.num_acc).icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
                    markers.put(marker, a);
                }
            });


            googleMap.setOnCameraMoveListener(() -> {
                int currentZoom = (int) ((googleMap.getCameraPosition().zoom / maxZoom) * 100 / 10);
                if (lastZoom[0] != currentZoom) {
                    lastZoom[0] = currentZoom;
                    LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;

                    for(Marker m : markers.keySet()) {
                        m.remove();
                    }
                    markers.clear();
                    googleMap.clear();

                    String geofilterPolygon = "&geofilter.polygon=" + String.join(",", latToString(bounds.northeast.latitude, bounds.northeast.longitude),latToString(bounds.southwest.latitude, bounds.northeast.longitude),latToString(bounds.southwest.latitude,bounds.southwest.longitude), latToString(bounds.northeast.latitude,bounds.southwest.longitude));

                    if(currentZoom < 8) {
                        mapViewModel.getClusters().getValue().clear();
                        mapViewModel.loadClusters("https://public.opendatasoft.com/api/records/1.0/geocluster/?dataset=accidents-corporels-de-la-circulation-millesime&clusterdistance=" + 1000 / (1 + currentZoom * 10) + "&clusterprecision=" + currentZoom * 2 + geofilterPolygon);
                    } else {
                        mapViewModel.getAccidents().getValue().clear();
                        mapViewModel.loadAccidents("https://data.opendatasoft.com//api/records/1.0/search/?dataset=accidents-corporels-de-la-circulation-millesime%40public&q=&rows=-1&facet=Num_Acc&facet=jour&facet=mois&facet=an&facet=lum&facet=adr&facet=dep&facet=atm&facet=col&facet=lat&facet=long&facet=surf&facet=catv&facet=obs&facet=obsm&facet=grav" + geofilterPolygon);
                    }
                }
            });



            LatLng myPos = addMyPosMarker(googleMap);
            if (myPos != null)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        }

        private String latToString(double lat, double lon) {
            return "(" + lat + "," + lon + ")";
        }

        private LatLng addMyPosMarker(GoogleMap googleMap) {
            Location position = sharedModel.getLocation().getValue();
            LatLng myPos = new LatLng(position.getLatitude(), position.getLongitude());
            if(myPos == null)
                return null;

            BitmapDrawable bitmap = (BitmapDrawable)getResources().getDrawable(R.drawable.my_position);
            int width = 50;
            int height = bitmap.getIntrinsicHeight() * width / bitmap.getIntrinsicWidth();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap.getBitmap(), width, height, false);

            googleMap.addMarker(new MarkerOptions().position(myPos).title("Vous êtes ici").icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));

            return myPos;
        }
    };

    private FragmentMapBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textDashboard;
        mapViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}