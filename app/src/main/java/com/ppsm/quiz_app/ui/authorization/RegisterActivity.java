package com.ppsm.quiz_app.ui.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.CreateUserDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private JsonPlaceholderAPI jsonPlaceholderAPI;
    EditText emailText, loginText, passwordText, repeatedPasswordText;
    TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        emailText = findViewById(R.id.input_email);
        loginText = findViewById(R.id.input_login);
        passwordText = findViewById(R.id.input_password);
        repeatedPasswordText = findViewById(R.id.input_repeated_password);
        warningText = findViewById(R.id.warning);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);
    }

    @SuppressLint("SetTextI18n")
    public void registerUser(final View view) {

        if (!emailText.getText().toString().equals("") && !loginText.getText().toString().equals("")
                && !passwordText.getText().toString().equals("") && !repeatedPasswordText.getText().toString().equals("")) {

            if (passwordText.getText().toString().equals(repeatedPasswordText.getText().toString())) {
                CreateUserDto user = new CreateUserDto(loginText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString());

                Call<Void> call = jsonPlaceholderAPI.registerUser(user);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if(response.code() == 201){
                            Toast toast = Toast.makeText(getApplicationContext(), "Pomyślna rejestracja", Toast.LENGTH_SHORT);
                            toast.show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            return;
                        }
                        else if (response.code() == 403){
                            warningText.setText("Istnieje już użytkownik o takiej nazwie");
                            return;
                        }
                        else {      // BAD_REQUEST
                            warningText.setText("Nie poprawny email lub hasło (wymagane min. 10 znaków)");
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("failure");
                        System.out.println(t.getMessage());
                    }
                });
            }
            else {
                warningText.setText("Podane hasła są różne");
            }
        }
        else {
            warningText.setText("Należy uzupełnić wszystkie pola");
        }
    }
}
