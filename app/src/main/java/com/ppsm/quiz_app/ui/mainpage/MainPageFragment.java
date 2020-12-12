package com.ppsm.quiz_app.ui.mainpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ppsm.quiz_app.R;

public class MainPageFragment extends Fragment {

    private MainPageViewModel mainPageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainPageViewModel =
                ViewModelProviders.of(this).get(MainPageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mainpage, container, false);
        Button log_out = root.findViewById(R.id.LogOut_Btn);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_mainpage_to_nav_home);
            }
        });
        return root;
    }
}
