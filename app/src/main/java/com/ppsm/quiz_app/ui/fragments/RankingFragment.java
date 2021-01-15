package com.ppsm.quiz_app.ui.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.model.QuizResult;
import com.ppsm.quiz_app.ui.RankingAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RankingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JsonPlaceholderAPI jsonPlaceholderAPI;
    private ArrayList<QuizResult> rankingList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ranking, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Ranking");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        mRecyclerView = root.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getContext());

//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.0.102:8080/")
                .baseUrl("http://192.168.1.115:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        showRanking();

        return root;
    }

    public void showRanking() {

        Call<ArrayList<QuizResult>> call = jsonPlaceholderAPI.getRanking();
        call.enqueue(new Callback<ArrayList<QuizResult>>() {
            @Override
            public void onResponse(Call<ArrayList<QuizResult>> call, Response<ArrayList<QuizResult>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Server Code: " + response.code());
                }
                rankingList = response.body();
                mAdapter = new RankingAdapter(rankingList, getLogin());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<ArrayList<QuizResult>> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
            }
        });

    }
    private String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }
}
