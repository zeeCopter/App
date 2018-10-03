package com.example.hk.transport.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;

public class WalletFragment extends Fragment {

    public static WalletFragment walletFragment;

    public static WalletFragment getInstance()
    {
        if(walletFragment == null)
            return new WalletFragment();
        else
            return walletFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MasterActivity)getActivity()).changeTitle("Wallet");
    }
}