package com.example.hk.transport.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hk.transport.Adapters.MyPackageAdapter;
import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;

import java.util.ArrayList;
import java.util.List;

public class MyPackageFragment extends Fragment {

    public static MyPackageFragment myPackageFragment;

    TextView scheduleTV,historyTV;
    ImageView lineOneIV,lineTwoIV;
    RecyclerView recyclerView;

    List<String> stringList;
    MyPackageAdapter myPackageAdapter;

    public static MyPackageFragment getInstance()
    {
        if(myPackageFragment == null)
            return new MyPackageFragment();
        else
            return myPackageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_package, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MasterActivity)getActivity()).changeTitle("MY PACKAGE");

        scheduleTV = view.findViewById(R.id.scheduleTV);

        historyTV = view.findViewById(R.id.historyTV);

        lineOneIV = view.findViewById(R.id.lineOneIV);

        lineTwoIV = view.findViewById(R.id.lineTwoIV);

        recyclerView = view.findViewById(R.id.rv);

        scheduleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineOneIV.setVisibility(View.VISIBLE);
                lineTwoIV.setVisibility(View.INVISIBLE);
            }
        });

        historyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lineOneIV.setVisibility(View.INVISIBLE);
                lineTwoIV.setVisibility(View.VISIBLE);
            }
        });

        stringList = new ArrayList<String>();
        for(int a = 0 ; a < 15 ; a++)
        {
            stringList.add(""+a);
        }
        myPackageAdapter = new MyPackageAdapter(stringList);
        recyclerView.setAdapter(myPackageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
