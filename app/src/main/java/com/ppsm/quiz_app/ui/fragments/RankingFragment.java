package com.ppsm.quiz_app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.QuizResult;
import com.ppsm.quiz_app.ui.RankingAdapter;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;
import java.util.ArrayList;

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
    private Handler handler = new Handler();
    private Runnable runnable;
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

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pull_to_refresh_ranking);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        showRanking();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkConnected()) {
                    showRanking();
                    Toast.makeText(getContext(), "Zaaktualizowano!", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {                 // odświeżanie przez przeciągnięcie
                        pullToRefresh.setRefreshing(false);
                    }
                }, 500);
            }
        });

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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
            }
        });

    }
    public String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }

    public void automaticRefresh(){
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                if (isNetworkConnected()) {
                    handler.postDelayed(runnable, 30000);
                }
                else {
                    handler.removeCallbacks(runnable);      // zatrzymanie automatycznego odświeżania
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
                }
                showRanking();
            }
        }, 30000);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();
        automaticRefresh();             // automatyczne odświeżanie co 30s.
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

}
