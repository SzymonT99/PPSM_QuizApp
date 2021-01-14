package com.ppsm.quiz_app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.ppsm.quiz_app.MainActivity;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.ChangedUserLoginDto;
import com.ppsm.quiz_app.model.ChangedUserPasswordDto;
import com.ppsm.quiz_app.model.DeleteUserDto;
import com.ppsm.quiz_app.model.UserAutorizationDto;
import com.ppsm.quiz_app.ui.admin.AdminActivity;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class AccountFragment extends Fragment {

    private EditText changePasswordNew, changePasswordOld, changeLoginNew, changeLoginAuthorize, deleteAccountPassword;
    private TextView warningChangePassword, warningChangeLogin, warningDeleteAccount;
    private Button changePasswordButton, changeLoginButton, deleteAccountButton;
    private JsonPlaceholderAPI jsonPlaceholderAPI;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Ustawienia konta");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        changePasswordNew = root.findViewById(R.id.input_new_passwd);
        changePasswordOld = root.findViewById(R.id.input_old_passwd);
        changeLoginNew = root.findViewById(R.id.input_new_login);
        changeLoginAuthorize = root.findViewById(R.id.input_password_new_login);
        deleteAccountPassword = root.findViewById(R.id.delete_confirm_passwd);

        changePasswordButton = root.findViewById(R.id.confirm_passwd_change_btn);
        changeLoginButton = root.findViewById(R.id.confirm_login_change_btn);
        deleteAccountButton = root.findViewById(R.id.confirm_delete_acc_btn);

        warningChangePassword = root.findViewById(R.id.warning1);
        warningChangeLogin = root.findViewById(R.id.warning2);
        warningDeleteAccount = root.findViewById(R.id.warning3);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        changeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLogin();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

        return root;
    }


    @SuppressLint("SetTextI18n")
    private void changePassword() {

        if (!changePasswordNew.getText().toString().equals("") && !changePasswordOld.getText().toString().equals("")){

            ChangedUserPasswordDto changedUserPassword = new ChangedUserPasswordDto(getLogin(),
                    changePasswordOld.getText().toString(), changePasswordNew.getText().toString());

            Call<Void> call = jsonPlaceholderAPI.changePassword(changedUserPassword);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast toast = Toast.makeText(getContext(), "Zmieniono hasło", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        changePasswordNew.setText("");
                        changePasswordOld.setText("");
                        warningChangePassword.setText("");
                        return;
                    }
                    if (response.code() == 401) {
                        warningChangePassword.setText("Niepoprawne hasło");
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
            warningChangePassword.setText("Należy uzupełnić wszystkie pola");
        }
    }

    @SuppressLint("SetTextI18n")
    private void deleteAccount() {
        if (!deleteAccountPassword.getText().toString().equals("")){

            DeleteUserDto deleteUserDto = new DeleteUserDto(getLogin(), deleteAccountPassword.getText().toString());

            Call<Void> call = jsonPlaceholderAPI.deleteUser(deleteUserDto);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        Toast toast = Toast.makeText(getContext(), "Usunięto konto", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        deleteAccountPassword.setText("");
                        warningDeleteAccount.setText("");

                        return;
                    }
                    if (response.code() == 401) {
                        warningDeleteAccount.setText("Niepoprawne hasło");
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
            warningDeleteAccount.setText("Należy uzupełnić pole");
        }
    }

    @SuppressLint("SetTextI18n")
    private void changeLogin() {

        if (!changeLoginNew.getText().toString().equals("") && !changeLoginAuthorize.getText().toString().equals("")){

            ChangedUserLoginDto changedUserLogin = new ChangedUserLoginDto(getLogin(),
                    changeLoginNew.getText().toString(), changeLoginAuthorize.getText().toString());

            Call<Void> call = jsonPlaceholderAPI.changeLogin(changedUserLogin);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast toast = Toast.makeText(getContext(), "Zmieniono login", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        saveData(changeLoginNew.getText().toString());
                        changeLoginNew.setText("");
                        changeLoginAuthorize.setText("");
                        warningChangeLogin.setText("");
                        return;
                    }
                    if (response.code() == 401) {
                        warningChangeLogin.setText("Niepoprawne hasło");
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
            warningChangeLogin.setText("Należy uzupełnić wszystkie pola");
        }
    }

    private String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }

    private void saveData(String output) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LOGIN", output);
        editor.apply();
    }


}
