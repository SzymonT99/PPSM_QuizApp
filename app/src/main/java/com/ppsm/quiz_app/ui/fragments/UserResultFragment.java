package com.ppsm.quiz_app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.UserResultDto;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class UserResultFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_result, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Wyniki Quizu");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        String userNick = getLogin();
        int correctAnswers = getArguments().getInt("correctAnswers");
        int totalQuestions = getArguments().getInt("totalQuestions");
        int points = getArguments().getInt("points");

        Button playAgainButton, checkRankingButton;
        playAgainButton = root.findViewById(R.id.play_again_btn);
        checkRankingButton = root.findViewById(R.id.check_ranking_btn);

        TextView userNickText, correctAnswText, maxQuestionsText, pointsText, dateText;
        userNickText = root.findViewById(R.id.user_nick_value);
        correctAnswText = root.findViewById(R.id.correct_answers_value);
        maxQuestionsText = root.findViewById(R.id.max_question_vaue);
        pointsText = root.findViewById(R.id.points_value);
        dateText = root.findViewById(R.id.date_vaue);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        String dateString = sdf.format(date);

        userNickText.setText(userNick);
        correctAnswText.setText(String.valueOf(correctAnswers));
        maxQuestionsText.setText(String.valueOf(totalQuestions));
        pointsText.setText(String.valueOf(points));
        dateText.setText(dateString);

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()){
                    Navigation.findNavController(v).navigate(R.id.action_nav_user_result_to_nav_quiz);
                }
                else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
                }

            }
        });

        checkRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    Navigation.findNavController(v).navigate(R.id.action_nav_user_result_to_nav_ranking);
                }
                else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
                }

            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        UserResultDto result = new UserResultDto(getLogin(), points, correctAnswers, 1);

        Call<Void> call = jsonPlaceholderAPI.saveResult(result);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Server Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    public String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
