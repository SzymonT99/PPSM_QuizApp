package com.ppsm.quiz_app.ui.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    private TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Button registerButton = findViewById(R.id.register_btn);

        loginText = findViewById(R.id.input_login);
        passwordText = findViewById(R.id.input_password);
        warningText = findViewById(R.id.warning);

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
    }

    @SuppressLint("SetTextI18n")
    public void loginUser(View view){

        if (!loginText.getText().toString().equals("") && !passwordText.getText().toString().equals("")) {

            UserAutorizationDto userAutorization = new UserAutorizationDto(loginText.getText().toString(), passwordText.getText().toString());

            Call<String> call = jsonPlaceholderAPI.getUserRole(userAutorization);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.code() == 200) {
                        String role = response.body();
                        if (role.equals("ROLE_USER")){
                            String currentLogin = loginText.getText().toString();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("LOGIN", currentLogin);
                            startActivity(intent);
                        }

                        if (role.equals("ROLE_ADMIN")){
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }
                        return;
                    }
                    if (response.code() == 401) {
                        warningText.setText("Nie poprawny login lub hasło");
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

}
