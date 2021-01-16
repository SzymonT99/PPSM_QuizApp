package com.ppsm.quiz_app.ui.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ppsm.quiz_app.MainActivity;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.UserAutorizationDto;
import com.ppsm.quiz_app.ui.admin.AdminActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private JsonPlaceholderAPI jsonPlaceholderAPI;
    private EditText loginText, passwordText;
    private TextView warningText, internetWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final Button registerButton = findViewById(R.id.register_btn);
        final Button loginButton = findViewById(R.id.login_btn);

        loginText = findViewById(R.id.input_login);
        passwordText = findViewById(R.id.input_password);
        warningText = findViewById(R.id.warning);
        internetWarning = findViewById(R.id.internet_warning);

        loginText.setText(getLogin());          // zapamiętywanie nazwy użytkownika w panelu logowania

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if(!isNetworkConnected()) {
                                    internetWarning.setVisibility(View.VISIBLE);
                                    loginText.setEnabled(false);
                                    passwordText.setEnabled(false);
                                    registerButton.setEnabled(false);
                                    loginButton.setEnabled(false);
                                }
                                else {
                                    internetWarning.setVisibility(View.GONE);
                                    loginText.setEnabled(true);
                                    passwordText.setEnabled(true);
                                    registerButton.setEnabled(true);
                                    loginButton.setEnabled(true);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        };
        thread.start();
    }

    @SuppressLint("SetTextI18n")
    public void loginUser(View view){

        if (!loginText.getText().toString().equals("") && !passwordText.getText().toString().equals("")) {

            UserAutorizationDto userAutorization = new UserAutorizationDto(loginText.getText().toString(), passwordText.getText().toString());

            Call<String> call = jsonPlaceholderAPI.getUserRole(userAutorization);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String currentLogin = loginText.getText().toString();
                    if (response.code() == 200) {
                        String role = response.body();
                        if (role.equals("ROLE_USER")){
                            saveData(currentLogin);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("LOGIN", currentLogin);
                            startActivity(intent);
                        }

                        if (role.equals("ROLE_ADMIN")){
                            saveData(currentLogin);
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }
                        return;
                    }
                    if (response.code() == 401) {
                        warningText.setText("Niepoprawny login lub hasło");
                        return;
                    }
                    if (response.code() == 403) {
                        warningText.setText("Konto zablokowane");
                        return;
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    System.out.println("failure");
                    System.out.println(t.getMessage());
                }
            });
        }
        else {
            warningText.setText("Należy uzupełnić wszystkie pola");
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "");
    }

    public void saveData(String output) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LOGIN", output);
        editor.apply();
    }
}
