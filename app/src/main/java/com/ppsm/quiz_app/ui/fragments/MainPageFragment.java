package com.ppsm.quiz_app.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;

import static android.content.Context.MODE_PRIVATE;


public class MainPageFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Strona Główna");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        View root = inflater.inflate(R.layout.fragment_mainpage, container, false);


        Button logOutButton = root.findViewById(R.id.log_out_btn);
        Button runQuizButton = root.findViewById(R.id.run_quiz_btn);
        Button showStatsButton = root.findViewById(R.id.show_stats_btn);
        Button showRankingButton = root.findViewById(R.id.show_ranking_btn);

        TextView userNameText = root.findViewById(R.id.text_user);

        Intent intent = getActivity().getIntent();
        final String userName = intent.getStringExtra("LOGIN");
        userNameText.setText(userName);

        saveData(userName);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        runQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_main_page_to_nav_quiz);
            }
        });

        showStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_main_page_to_nav_stats);
            }
        });

        showRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_main_page_to_nav_ranking);
            }
        });

        return root;
    }

    private void saveData(String output) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LOGIN", output);
        editor.apply();
    }

}
