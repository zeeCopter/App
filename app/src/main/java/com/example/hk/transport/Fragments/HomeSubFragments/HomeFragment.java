package com.example.hk.transport.Fragments.HomeSubFragments;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;
import com.example.hk.transport.Utilities.APIs.API;
import com.example.hk.transport.Utilities.Common;
import com.example.hk.transport.Utilities.Pojos.ModulePojo;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public static HomeFragment homeFragment;

    MapView mMapView;
    private GoogleMap googleMap;
    Button startBtn;
    LinearLayout lastPannel,lastUpperPannel,topPanel,bikeLL,truckLL,rikshawLL,carLL;
    ImageView carIV,carLineIV,rikshawIV,rikshawLineIV,bikeIV,bikeLineIV,truckIV,truckLineIV;
    Marker marker;
    RelativeLayout pickUpLocationRL;
    TextView addressTV,knownLocationTV;
    public static boolean fromEnterPickUp = false;

    public static int state = 0;
    //state is 0 before ride
    //state is 1 after ride

    public static HomeFragment getInstance() {
        if (homeFragment == null)
            return new HomeFragment();
        else
            return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://mooncable.hopto.org:8034/");
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String s = args[0].toString();
            try {
                JSONObject json = new JSONObject(s);
                double latitude;
                double longitude;
                //marker.remove();
                latitude = json.getDouble("latitude");
                longitude = json.getDouble("longitude");
                final LatLng loc = new LatLng(latitude, longitude);
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        addMarkerOnMap(loc);
                    }
                });
            } catch (Exception e) {
                return;
            }

        }
    };

    private void addMarkerOnMap(LatLng latLng)
    {
        marker.remove();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker Title").snippet("Marker Description"));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.mapView);
        knownLocationTV = view.findViewById(R.id.knownLocationTV);
        addressTV = view.findViewById(R.id.addressTV);
        lastPannel = view.findViewById(R.id.lastPannel);
        lastUpperPannel = view.findViewById(R.id.lastUpperPannel);
        topPanel = view.findViewById(R.id.topPanel);
        startBtn = view.findViewById(R.id.startBtn);
        bikeLL = view.findViewById(R.id.bikeLL);
        truckLL = view.findViewById(R.id.truckLL);
        rikshawLL = view.findViewById(R.id.rikshawLL);
        carLL = view.findViewById(R.id.carLL);
        carIV = view.findViewById(R.id.carIV);
        carLineIV = view.findViewById(R.id.carLineIV);
        rikshawIV = view.findViewById(R.id.rikshawIV);
        rikshawLineIV = view.findViewById(R.id.rikshawLineIV);
        bikeIV = view.findViewById(R.id.bikeIV);
        bikeLineIV = view.findViewById(R.id.bikeLineIV);
        truckIV = view.findViewById(R.id.truckIV);
        truckLineIV = view.findViewById(R.id.truckLineIV);
        pickUpLocationRL = view.findViewById(R.id.pickUpLocationRL);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(24.926294, 67.022095);
                //marker = googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if(state == 1)
                {
                    mSocket.on("myChat", onNewMessage);
                    mSocket.connect();
                }
                else if(fromEnterPickUp)
                {
                    LatLng pickUpLocation = new LatLng(Common.pickUpLatitude, Common.pickUpLongitude);
                    cameraPosition = new CameraPosition.Builder().target(pickUpLocation).zoom(16).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    googleMap.addMarker(new MarkerOptions().position(pickUpLocation).title(Common.pickUpAddress+" (PICKUP LOCATION)"));
                    addressTV.setText(Common.pickUpAddress);
                    knownLocationTV.setText(Common.pickUpKnownLocation);
                }
                else
                {
                    getCurrentLocation();
                }
            }
        });

        ((MasterActivity)getActivity()).changeTitle("PICKUP LOCATION");

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MasterActivity)getActivity()).changeFragmentWithBack(DropOffLocationFragment.getInstance(),9);
            }
        });

        pickUpLocationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MasterActivity)getActivity()).changeFragmentWithBack(EnterPickUpLocationFragment.getInstance(),8);
            }
        });

        if(state == 0)
        {
            lastPannel.setVisibility(View.GONE);
            lastUpperPannel.setVisibility(View.VISIBLE);
        }
        else
        {
            lastPannel.setVisibility(View.VISIBLE);
            lastUpperPannel.setVisibility(View.GONE);
            topPanel.setVisibility(View.GONE);
        }

        getModule();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        mSocket.disconnect();
        mSocket.off("myChat", onNewMessage);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void getModule()
    {
        Common.showProgressDialog(getActivity());
        API.getWebServices().getModule(4,22).enqueue(new Callback<ModulePojo>() {
            @Override
            public void onResponse(Call<ModulePojo> call, Response<ModulePojo> response) {
                Common.dismissProgressDialog();
                if(response.body().getStatus())
                {
                    Common.modulePojo = response.body();
                    if(response.body().getData() != null && response.body().getData().size() > 0)
                    for(int i = 0 ; i < response.body().getData().size() ; i++)
                    {
                        if(response.body().getData().get(i).getModuleName().equals("Bike"))
                        {
                            bikeLL.setVisibility(View.VISIBLE);
                            Picasso.get().load(response.body().getData().get(i).getModuleImage()).into(bikeIV);
                        }
                        else if(response.body().getData().get(i).getModuleName().equals("Rikshaw"))
                        {
                            rikshawLL.setVisibility(View.VISIBLE);
                            Picasso.get().load(response.body().getData().get(i).getModuleImage()).into(rikshawIV);
                        }
                        else if(response.body().getData().get(i).getModuleName().equals("Car"))
                        {
                            carLL.setVisibility(View.VISIBLE);
                            Picasso.get().load(response.body().getData().get(i).getModuleImage()).into(carIV);
                        }
                        else if(response.body().getData().get(i).getModuleName().equals("Truck"))
                        {
                            truckLL.setVisibility(View.VISIBLE);
                            Picasso.get().load(response.body().getData().get(i).getModuleImage()).into(truckIV);
                        }
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModulePojo> call, Throwable t) {
                Common.dismissProgressDialog();
                Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocation()
    {
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance);

        SmartLocation.with(getActivity()).location()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        if(!fromEnterPickUp)
                        {
                            Common.pickUpLongitude = location.getLongitude();
                            Common.pickUpLatitude = location.getLatitude();
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(16).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            getAddress(location.getLatitude(),location.getLongitude());
                        }
                    }
                });
    }

    private void getAddress(double latitude,double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressTV.setText(addresses.get(0).getAddressLine(0));
            knownLocationTV.setText(addresses.get(0).getFeatureName());
            Common.pickUpKnownLocation = addresses.get(0).getFeatureName();
            Common.pickUpAddress = addresses.get(0).getAddressLine(0);
            /*String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
