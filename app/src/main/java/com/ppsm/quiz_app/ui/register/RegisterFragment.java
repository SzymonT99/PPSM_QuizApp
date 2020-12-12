package com.ppsm.quiz_app.ui.register;

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

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                ViewModelProviders.of(this).get(RegisterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        Button register = root.findViewById(R.id.Register_Btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_register_to_nav_home);
            }
        });
        return root;
    }
}
