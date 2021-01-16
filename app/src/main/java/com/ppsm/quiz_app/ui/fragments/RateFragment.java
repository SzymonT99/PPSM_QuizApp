package com.ppsm.quiz_app.ui.fragments;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.RatesDto;
import com.ppsm.quiz_app.model.UserRateDto;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RateFragment extends Fragment {

    private Button goodButton, badButton;
    private TextView goodRatesText, badRatesText;
    private JsonPlaceholderAPI jsonPlaceholderAPI;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rate, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Ocena aplikacji");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        goodButton = root.findViewById(R.id.rate_well_btn);
        badButton = root.findViewById(R.id.rate_worse_btn);

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pull_to_refresh_rate);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkConnected()) {
                    getRates();
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

        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRate(getLogin(), true);
            }
        });

        badButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRate(getLogin(), false);
            }
        });

        goodRatesText = root.findViewById(R.id.good_count);
        badRatesText = root.findViewById(R.id.bad_count);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        if (isNetworkConnected()) {
            getRates();
        }
        else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
        }


        return root;
    }

    public void getRates() {

        Call<RatesDto> call = jsonPlaceholderAPI.getAllRates();
        call.enqueue(new Callback<RatesDto>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<RatesDto> call, Response<RatesDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Server Code: " + response.code());
                }
                RatesDto rates = response.body();

                goodRatesText.setText(rates.getGoodRates().toString());
                badRatesText.setText(rates.getBadRates().toString());
            }

            @Override
            public void onFailure(Call<RatesDto> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());

            }
        });
    }

    public String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }

    public void addRate(String userName, Boolean isPositiveOpinion) {

        UserRateDto userRate = new UserRateDto(userName, isPositiveOpinion);

        Call<Boolean> call = jsonPlaceholderAPI.addRate(userRate);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Server Code: " + response.code());
                }
                Boolean stateResponse = response.body();
                if (!stateResponse){
                    Toast.makeText(getContext(), "Dokonano już oceny!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Dziękujemy za ocenę!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
