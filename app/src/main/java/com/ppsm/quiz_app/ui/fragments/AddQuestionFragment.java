package com.ppsm.quiz_app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.CreateQuestionDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class AddQuestionFragment extends Fragment {

    private EditText answerA, answerB, answerC, answerD, questionContent, seconds;
    private JsonPlaceholderAPI jsonPlaceholderAPI;
    private RadioGroup radioGroup;
    private Button saveQuestioButton;
    private RadioButton radioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_question, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Dodaj pytanie");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        questionContent = root.findViewById(R.id.question_content_value);
        answerA = root.findViewById(R.id.input_answer_A);
        answerB = root.findViewById(R.id.input_answer_B);
        answerC = root.findViewById(R.id.input_answer_C);
        answerD = root.findViewById(R.id.input_answer_D);
        seconds = root.findViewById(R.id.time_value);

        radioGroup = root.findViewById(R.id.radioGroup);
        saveQuestioButton = root.findViewById(R.id.save_question_btn);

        saveQuestioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendQuestion(v);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.101:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        return root;
    }

    public void sendQuestion(View view) {
        if (!answerA.getText().toString().equals("") && !answerB.getText().toString().equals("") &&
                !answerC.getText().toString().equals("") && !answerD.getText().toString().equals("") &&
                !questionContent.getText().toString().equals("") && !seconds.getText().toString().equals("")) {


            int selectedID = radioGroup.getCheckedRadioButtonId();
            radioButton =  getActivity().findViewById(selectedID);
            int correctNumber;
            if (radioButton.getText().equals("A")) correctNumber = 1;
            if (radioButton.getText().equals("B")) correctNumber = 2;
            if (radioButton.getText().equals("C")) correctNumber = 3;
            else correctNumber = 4;

            CreateQuestionDto createQuestionDto = new CreateQuestionDto(getLogin(), questionContent.getText().toString(),
                    answerA.getText().toString(), answerB.getText().toString(), answerC.getText().toString(),
                    answerD.getText().toString(), Integer.parseInt(seconds.getText().toString()), correctNumber);


            Call<Void> call = jsonPlaceholderAPI.addQuestion(createQuestionDto);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Server Code: " + response.code());
                    }
                    Toast toast = Toast.makeText(getContext(), "Wysłano pytanie", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    questionContent.setText("");
                    answerA.setText("");
                    answerB.setText("");
                    answerC.setText("");
                    answerD.setText("");
                    seconds.setText("");

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("failure");
                    System.out.println(t.getMessage());
                }
            });

        }
        else {
            Toast.makeText(getContext(), "Należy uzupełnić wszystkie pola", Toast.LENGTH_SHORT).show();
        }
    }


    private String getLogin() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sharedPreferences.getString("LOGIN", "None");
    }

}
