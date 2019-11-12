package iot.empiaurhouse.horizonar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class PurviewFragment extends Fragment {


    public PurviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purview, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // handle your fragment events here

    }

}
