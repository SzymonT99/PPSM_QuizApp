package com.ppsm.quiz_app.ui.rate_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ppsm.quiz_app.R;

public class RateFragment extends Fragment {

    private RateViewModel rateViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rateViewModel =
                ViewModelProviders.of(this).get(RateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rate, container, false);
        /*final TextView textView = root.findViewById(R.id.text_slideshow);
        statsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
