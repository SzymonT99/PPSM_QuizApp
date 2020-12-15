package com.ppsm.quiz_app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.ppsm.quiz_app.MainActivity;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;

import static java.security.AccessController.getContext;

public class MainPageFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mainpage, container, false);
        Button log_out = root.findViewById(R.id.log_out_btn);
        TextView userNameText = root.findViewById(R.id.text_user);

        Intent intent = getActivity().getIntent();
        String userName = intent.getStringExtra("LOGIN");
        userNameText.setText(userName);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_nav_mainpage_to_nav_home);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
