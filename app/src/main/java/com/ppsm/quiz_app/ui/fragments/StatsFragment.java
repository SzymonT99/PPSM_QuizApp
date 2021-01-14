package com.ppsm.quiz_app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.Statistics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class StatsFragment extends Fragment {

    private TextView nickText, numberGamesText, correctAnswText, incorrectAnswText, addedQuestionsText, currentRankText;
    private JsonPlaceholderAPI jsonPlaceholderAPI;
    private Statistics stats;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stats, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Statystyki");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        nickText = root.findViewById(R.id.user_name_text);
        numberGamesText = root.findViewById(R.id.stats_game_total_value);
        correctAnswText = root.findViewById(R.id.stats_correct_answers_value);
        incorrectAnswText = root.findViewById(R.id.stats_incorrect_answers_value);
        addedQuestionsText = root.findViewById(R.id.stats_question_added_value);
        currentRankText = root.findViewById(R.id.stats_current_rank_value);

        nickText.setText(getLogin());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        showRanking();

        return root;
    }

    private String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }

    public void showRanking() {

        Call<Statistics> call = jsonPlaceholderAPI.getStats(getLogin());
        call.enqueue(new Callback<Statistics>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Server Code: " + response.code());
                }
                System.out.println("Server Code: " + response.code());
                stats = response.body();

                numberGamesText.setText(stats.getGamesNumber().toString());
                correctAnswText.setText(stats.getCorrectAnswer().toString());
                incorrectAnswText.setText(stats.getIncorrectAnswer().toString());
                addedQuestionsText.setText(stats.getAddedQuestions().toString());
                currentRankText.setText(stats.getCurrentRank().toString());

            }

            @Override
            public void onFailure(Call<Statistics> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());

            }
        });


    }
}
