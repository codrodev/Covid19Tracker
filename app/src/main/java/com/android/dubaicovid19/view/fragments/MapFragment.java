package com.android.dubaicovid19.view.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.CovidCenters;
import com.android.dubaicovid19.data.model.NearByInfectedUser;
import com.android.dubaicovid19.databinding.FragmentMapBinding;
import com.android.dubaicovid19.view.viewModels.InfectedNearByViewModel;
import com.android.dubaicovid19.view.viewModels.MapViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    MapViewModel model;
    FragmentMapBinding binding;
    private View mRootView;
    private GoogleMap myGoogleMap;
    private ArrayList<LatLng>listLatLng;
    HashMap<Marker,NearByInfectedUser> hashMapMarker = new HashMap<Marker,NearByInfectedUser>();
    private MapView mapView;

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        binding.setVmFragmentMap(model);
        mRootView = binding.getRoot();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        binding.fabSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    void LoadingGoogleMap(List<NearByInfectedUser > lstInfectedUser)
    {
        if (myGoogleMap != null)
        {
            myGoogleMap.clear();
            myGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            myGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            myGoogleMap.setMyLocationEnabled(true);
            myGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            if(lstInfectedUser.size() > 0)
            {
                try
                {
                    listLatLng=new ArrayList<LatLng>();
                    for (int i = 0; i < lstInfectedUser.size(); i++)
                    {
                        NearByInfectedUser bean= lstInfectedUser.get(i);
                        if(bean.getLat() > 0 && bean.getLon() >0)
                        {
                            double lat= bean.getLat();
                            double lon= bean.getLon();

                            Marker marker = myGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat,lon))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            hashMapMarker.put(marker,bean);

                            //Set Zoom Level of Map pin
                            LatLng object=new LatLng(lat, lon);
                            listLatLng.add(object);
                        }
                    }
                    SetZoomlevel(listLatLng);
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }

                myGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker position) {
                        NearByInfectedUser bean = hashMapMarker.get(position);
                        //Toast.makeText(getActivity(), bean.getName(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        else
        {
            Toast.makeText(getActivity(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }


    public void  SetZoomlevel(ArrayList<LatLng> listLatLng)
    {
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(listLatLng.get(0));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        myGoogleMap.moveCamera(center);
        myGoogleMap.animateCamera(zoom);
        //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(0), 14));
        /*if (listLatLng != null && listLatLng.size() == 1)
        {
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(0), 10));
        }
        else if (listLatLng != null && listLatLng.size() > 1)
        {
            final Builder builder = LatLngBounds.builder();
            for (int i = 0; i < listLatLng.size(); i++)
            {
                builder.include(listLatLng.get(i));
            }

            final ViewTreeObserver treeObserver = binding.rlMapLayout.getViewTreeObserver();
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout()
                {
                    if(myGoogleMap != null){
                        myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), mapView.getWidth()
                                , mapView.getHeight(), 80));
                        binding.rlMapLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });

        }*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myGoogleMap = googleMap;
        if(InfectedNearByViewModel.lstNearByInfectedUser != null && InfectedNearByViewModel.lstNearByInfectedUser.size() > 0){
            LoadingGoogleMap(InfectedNearByViewModel.lstNearByInfectedUser);
        }
    }
}
