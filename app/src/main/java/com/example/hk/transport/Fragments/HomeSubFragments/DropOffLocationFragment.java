package com.example.hk.transport.Fragments.HomeSubFragments;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import com.example.hk.transport.Utilities.Common;
import com.example.hk.transport.Utilities.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DropOffLocationFragment extends Fragment {

    public static DropOffLocationFragment dropOffLocationFragment;
    MapView mMapView;
    private GoogleMap googleMap;
    Button startBtn;
    LinearLayout lastPannel,lastUpperPannel,topPanel,bikeLL,truckLL,rikshawLL,carLL;
    ImageView carIV,carLineIV,rikshawIV,rikshawLineIV,bikeIV,bikeLineIV,truckIV,truckLineIV;
    Marker marker;
    RelativeLayout dropOffLocationRL;
    TextView pickUpKnownLocationTV,pickUpaddressTV,dropOffAddressTV;
    public static boolean fromDropOff = false;

        public static DropOffLocationFragment getInstance() {
            if (dropOffLocationFragment == null)
                return new DropOffLocationFragment();
            else
                return dropOffLocationFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_drop_off_location, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            mMapView = view.findViewById(R.id.mapView);
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
            dropOffLocationRL = view.findViewById(R.id.dropOffLocationRL);
            pickUpKnownLocationTV = view.findViewById(R.id.pickUpKnownLocationTV);
            pickUpaddressTV = view.findViewById(R.id.pickUpaddressTV);
            dropOffAddressTV = view.findViewById(R.id.dropOffAddressTV);

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

                    if(fromDropOff)
                    {
                        marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Common.pickUpLatitude, Common.pickUpLongitude)).title("PickUp Location").snippet(Common.pickUpKnownLocation+"\n"+Common.pickUpAddress));
                        marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Common.dropOffLatitude, Common.dropOffLongitude)).title("DropOff Location").snippet(Common.dropOffAddress));
                        pickUpaddressTV.setText(Common.pickUpAddress);
                        pickUpKnownLocationTV.setText(Common.pickUpKnownLocation);
                        dropOffAddressTV.setText(Common.dropOffAddress);
                        ArrayList<LatLng> latLngArrayList = new ArrayList<>();
                        latLngArrayList.add(new LatLng(Common.pickUpLatitude, Common.pickUpLongitude));
                        latLngArrayList.add(new LatLng(Common.dropOffLatitude, Common.dropOffLongitude));
                        String url = getDirectionsUrl(new LatLng(Common.pickUpLatitude, Common.pickUpLongitude), new LatLng(Common.dropOffLatitude, Common.dropOffLongitude));
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
                        centerIncidentRouteOnMap(latLngArrayList);
                    }
                    else
                    {
                        marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Common.pickUpLatitude, Common.pickUpLongitude)).title("PickUp Location").snippet(Common.pickUpKnownLocation+"\n"+Common.pickUpAddress));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Common.pickUpLatitude, Common.pickUpLongitude)).zoom(16).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        pickUpaddressTV.setText(Common.pickUpAddress);
                        pickUpKnownLocationTV.setText(Common.pickUpKnownLocation);
                    }
                }
            });

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Common.dropOffLatitude == 0.0 || Common.dropOffLatitude == 0)
                    {
                        Toast.makeText(getActivity(),"Select DropOff Location",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ((MasterActivity)getActivity()).changeFragmentWithBack(GoBookingFragment.getInstance(),7);
                    }
                }
            });

            dropOffLocationRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MasterActivity)getActivity()).changeFragmentWithBack(EnterDropOffLocationFragment.getInstance(),10);
                }
            });

            ((MasterActivity)getActivity()).changeTitle("DROPOFF LOCATION");

            getModule();

            bikeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0 ; i < Common.modulePojo.getData().size() ; i++)
                    {
                        if(Common.modulePojo.getData().get(i).getModuleName().equals("Bike"))
                        {
                            if(Common.modulePojo.getData().get(i).getIsEnable())
                            {
                                bikeIV.setImageResource(android.R.color.transparent);
                                bikeIV.setBackground(getActivity().getResources().getDrawable(R.drawable.bike));
                                bikeLineIV.setVisibility(View.VISIBLE);
                                Common.selectedModuleId = Common.modulePojo.getData().get(i).getModuleId();

                                carLineIV.setVisibility(View.GONE);
                                rikshawLineIV.setVisibility(View.GONE);
                                truckLineIV.setVisibility(View.GONE);

                                for(int j = 0 ; j < Common.modulePojo.getData().size() ; j++)
                                {
                                    if(Common.modulePojo.getData().get(j).getModuleName().equals("Rikshaw"))
                                    {
                                        rikshawIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(rikshawIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Car"))
                                    {
                                        carIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(carIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Truck"))
                                    {
                                        truckIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(truckIV);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"This Module is nor Enable",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            rikshawLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0 ; i < Common.modulePojo.getData().size() ; i++)
                    {
                        if(Common.modulePojo.getData().get(i).getModuleName().equals("Rikshaw"))
                        {
                            if(Common.modulePojo.getData().get(i).getIsEnable())
                            {
                                rikshawIV.setImageResource(android.R.color.transparent);
                                rikshawIV.setBackground(getActivity().getResources().getDrawable(R.drawable.rikhshaw));
                                rikshawLineIV.setVisibility(View.VISIBLE);
                                Common.selectedModuleId = Common.modulePojo.getData().get(i).getModuleId();

                                carLineIV.setVisibility(View.GONE);
                                bikeLineIV.setVisibility(View.GONE);
                                truckLineIV.setVisibility(View.GONE);

                                for(int j = 0 ; j < Common.modulePojo.getData().size() ; j++)
                                {
                                    if(Common.modulePojo.getData().get(j).getModuleName().equals("Bike"))
                                    {
                                        bikeIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(bikeIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Car"))
                                    {
                                        carIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(carIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Truck"))
                                    {
                                        truckIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(truckIV);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"This Module is nor Enable",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            carLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0 ; i < Common.modulePojo.getData().size() ; i++)
                    {
                        if(Common.modulePojo.getData().get(i).getModuleName().equals("Car"))
                        {
                            if(Common.modulePojo.getData().get(i).getIsEnable())
                            {
                                carIV.setImageResource(android.R.color.transparent);
                                carIV.setBackground(getActivity().getResources().getDrawable(R.drawable.car));
                                carLineIV.setVisibility(View.VISIBLE);
                                Common.selectedModuleId = Common.modulePojo.getData().get(i).getModuleId();

                                rikshawLineIV.setVisibility(View.GONE);
                                bikeLineIV.setVisibility(View.GONE);
                                truckLineIV.setVisibility(View.GONE);

                                for(int j = 0 ; j < Common.modulePojo.getData().size() ; j++)
                                {
                                    if(Common.modulePojo.getData().get(j).getModuleName().equals("Bike"))
                                    {
                                        bikeIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(bikeIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Rikshaw"))
                                    {
                                        rikshawIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(rikshawIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Truck"))
                                    {
                                        truckIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(truckIV);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"This Module is nor Enable",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            truckLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0 ; i < Common.modulePojo.getData().size() ; i++)
                    {
                        if(Common.modulePojo.getData().get(i).getModuleName().equals("Truck"))
                        {
                            if(Common.modulePojo.getData().get(i).getIsEnable())
                            {
                                truckIV.setImageResource(android.R.color.transparent);
                                truckIV.setBackground(getActivity().getResources().getDrawable(R.drawable.truck));
                                truckLineIV.setVisibility(View.VISIBLE);
                                Common.selectedModuleId = Common.modulePojo.getData().get(i).getModuleId();

                                rikshawLineIV.setVisibility(View.GONE);
                                bikeLineIV.setVisibility(View.GONE);
                                carLineIV.setVisibility(View.GONE);

                                for(int j = 0 ; j < Common.modulePojo.getData().size() ; j++)
                                {
                                    if(Common.modulePojo.getData().get(j).getModuleName().equals("Bike"))
                                    {
                                        bikeIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(bikeIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Rikshaw"))
                                    {
                                        rikshawIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(rikshawIV);
                                    }
                                    else if(Common.modulePojo.getData().get(j).getModuleName().equals("Car"))
                                    {
                                        carIV.setImageResource(android.R.color.transparent);
                                        Picasso.get().load(Common.modulePojo.getData().get(j).getModuleImage()).into(carIV);
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"This Module is nor Enable",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            getModuleAlreadySelected();
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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null)
                googleMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode+"&key=AIzaSyAnCt46qn6t7tl4Y71FImJtnzo-yWfLBOg";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void centerIncidentRouteOnMap(List<LatLng> copiedPoints) {
        double minLat = Integer.MAX_VALUE;
        double maxLat = Integer.MIN_VALUE;
        double minLon = Integer.MAX_VALUE;
        double maxLon = Integer.MIN_VALUE;
        for (LatLng point : copiedPoints) {
            maxLat = Math.max(point.latitude, maxLat);
            minLat = Math.min(point.latitude, minLat);
            maxLon = Math.max(point.longitude, maxLon);
            minLon = Math.min(point.longitude, minLon);
        }
        final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(maxLat, maxLon)).include(new LatLng(minLat, minLon)).build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    private void getModule()
    {
        if(Common.modulePojo.getData() != null && Common.modulePojo.getData().size() > 0)
            for(int i = 0 ; i < Common.modulePojo.getData().size() ; i++)
            {
                if(Common.modulePojo.getData().get(i).getModuleName().equals("Bike"))
                {
                    bikeLL.setVisibility(View.VISIBLE);
                    Picasso.get().load(Common.modulePojo.getData().get(i).getModuleImage()).into(bikeIV);
                }
                else if(Common.modulePojo.getData().get(i).getModuleName().equals("Rikshaw"))
                {
                    rikshawLL.setVisibility(View.VISIBLE);
                    Picasso.get().load(Common.modulePojo.getData().get(i).getModuleImage()).into(rikshawIV);
                }
                else if(Common.modulePojo.getData().get(i).getModuleName().equals("Car"))
                {
                    carLL.setVisibility(View.VISIBLE);
                    Picasso.get().load(Common.modulePojo.getData().get(i).getModuleImage()).into(carIV);
                }
                else if(Common.modulePojo.getData().get(i).getModuleName().equals("Truck"))
                {
                    truckLL.setVisibility(View.VISIBLE);
                    Picasso.get().load(Common.modulePojo.getData().get(i).getModuleImage()).into(truckIV);
                }
            }
    }

    private void getModuleAlreadySelected()
    {
        if(Common.selectedModuleId != 0)
        {
            for(int i = 0 ; i < Common.modulePojo.getData().size() ; i++)
            {
                if(Common.modulePojo.getData().get(i).getModuleId().equals(Common.selectedModuleId))
                {
                    if(Common.modulePojo.getData().get(i).getModuleName().equals("Bike"))
                    {
                        bikeLineIV.setImageResource(android.R.color.transparent);
                        bikeIV.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bike));
                        bikeLineIV.setVisibility(View.VISIBLE);
                    }
                    else if(Common.modulePojo.getData().get(i).getModuleName().equals("Rikshaw"))
                    {
                        rikshawIV.setImageResource(android.R.color.transparent);
                        rikshawIV.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.rikhshaw));
                        rikshawLineIV.setVisibility(View.VISIBLE);
                    }
                    else if(Common.modulePojo.getData().get(i).getModuleName().equals("Car"))
                    {
                        carIV.setImageResource(android.R.color.transparent);
                        carIV.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.car));
                        carLineIV.setVisibility(View.VISIBLE);
                    }
                    else if(Common.modulePojo.getData().get(i).getModuleName().equals("Truck"))
                    {
                        truckIV.setImageResource(android.R.color.transparent);
                        truckIV.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.truck));
                        truckLineIV.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}
