package com.ppsm.quiz_app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.model.RatesDto;
import com.ppsm.quiz_app.model.UserRateDto;

import java.util.List;

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

        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("1");
                addRate(getLogin(), true);
            }
        });

        badButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( "2");
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

        getRates();

        return root;
    }

    private void getRates() {

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

    private String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }

    private void addRate(String userName, Boolean isPositiveOpinion) {

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
            }
        });
    }

}
