package com.ppsm.quiz_app.ui.admin_panel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ppsm.quiz_app.R;

public class AdminPanelFragment extends Fragment {

    private AdminPanelViewModel adminPanelViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminPanelViewModel =
                ViewModelProviders.of(this).get(AdminPanelViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_panel, container, false);
        return root;
    }
}
