package com.ppsm.quiz_app.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizFragment extends Fragment {

    private JsonPlaceholderAPI jsonPlaceholderAPI;
    private Handler handler = new Handler();
    private Runnable runnable;
    private View viewG;
    private List<Question> questionsList;
    private Integer currentQuestionId;
    private Integer seconds;
    private Integer points;
    private TextView questionText, timeText, pointsText, roundText, categoryText;
    private Button answerAButton, answerBButton, answerCButton, answerDButton;
    private Button fiftyButton, rollButton, safetyButton;
    private Boolean isSafe = false;
    private int[] halfAnswers = null;
    private Integer correctAnswers = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewG = view;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mTitle.setText("Quiz");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        answerAButton = root.findViewById(R.id.answer_a_button);
        answerBButton = root.findViewById(R.id.answer_b_button);
        answerCButton = root.findViewById(R.id.answer_c_button);
        answerDButton = root.findViewById(R.id.answer_d_button);
        fiftyButton = root.findViewById(R.id.fifty_btn);
        rollButton = root.findViewById(R.id.roll_btn);
        safetyButton = root.findViewById(R.id.safety_btn);

        questionText = root.findViewById(R.id.question_content);
        timeText = root.findViewById(R.id.time_value);
        pointsText = root.findViewById(R.id.points_value);
        roundText = root.findViewById(R.id.round_text);
        categoryText = root.findViewById(R.id.category_text);

        answerAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    selectAnswer(0);
                }
                else {
                    backToLoginScreen();
                }
            }
        });
        answerBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    selectAnswer(1);
                }
                else {
                    backToLoginScreen();
                }
            }
        });
        answerCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    selectAnswer(2);
                }
                else {
                    backToLoginScreen();
                }
            }
        });
        answerDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    selectAnswer(3);
                }
                else {
                    backToLoginScreen();
                }
            }
        });

        fiftyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    int correctAnswerId = 0;

                    for (int i = 0; i < 4; i++) {
                        if (questionsList.get(currentQuestionId).getAnswers().get(i).isCorrect()) {
                            correctAnswerId = i;
                        }
                    }

                    int secondAnswerId = 0;
                    Random rand = new Random();
                    while (secondAnswerId == correctAnswerId) {
                        secondAnswerId = rand.nextInt(4);
                    }

                    int correctPosition = rand.nextInt(2);

                    halfAnswers = new int[2];

                    if (correctPosition == 0) {
                        answerAButton.setText(questionsList.get(currentQuestionId).getAnswers().get(correctAnswerId).getAnswer());
                        answerBButton.setText(questionsList.get(currentQuestionId).getAnswers().get(secondAnswerId).getAnswer());
                        halfAnswers[0] = correctAnswerId;
                        halfAnswers[1] = secondAnswerId;
                    } else {
                        answerBButton.setText(questionsList.get(currentQuestionId).getAnswers().get(correctAnswerId).getAnswer());
                        answerAButton.setText(questionsList.get(currentQuestionId).getAnswers().get(secondAnswerId).getAnswer());
                        halfAnswers[0] = secondAnswerId;
                        halfAnswers[1] = correctAnswerId;
                    }

                    answerCButton.setText("");
                    answerDButton.setText("");
                    answerCButton.setEnabled(false);
                    answerDButton.setEnabled(false);

                    fiftyButton.setEnabled(false);                  // koła ratunkowe
                }
                else {
                    backToLoginScreen();
                }
            }
        });

        safetyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    isSafe = true;
                    safetyButton.setEnabled(false);
                }
                else {
                    backToLoginScreen();
                }
            }
        });

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    goNextQuestion();
                    rollButton.setEnabled(false);
                }
                else {
                    backToLoginScreen();
                }
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        if (isNetworkConnected()) {
            getQuiz();
        }
        else {
            backToLoginScreen();
        }

        return root;
    }


    public void getQuiz() {

        Call<List<Question>> call = jsonPlaceholderAPI.getQuestions(true);
        call.enqueue(new Callback<List<Question>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Server Code: " + response.code());
                }

                questionsList = response.body();
                questionsList = randomizeQuestions(questionsList);          // losowanie kolejności pytań
                currentQuestionId = 0;
                seconds = questionsList.get(0).getSeconds();
                points = 0;
                pointsText.setText("0");
                timeText.setText(seconds.toString());
                roundText.setText("Runda: " + "1");
                questionText.setText(questionsList.get(0).getContent());
                categoryText.setText("Kategoria: " + questionsList.get(0).getCategory());
                answerAButton.setText(questionsList.get(0).getAnswers().get(0).getAnswer());
                answerBButton.setText(questionsList.get(0).getAnswers().get(1).getAnswer());
                answerCButton.setText(questionsList.get(0).getAnswers().get(2).getAnswer());
                answerDButton.setText(questionsList.get(0).getAnswers().get(3).getAnswer());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
            }
        });
    }

    public List<Question> randomizeQuestions(List<Question> unsortedQuestions) {

        List<Question> randomizeList = new ArrayList<>();

        int currentPointWeight = 1;
        List<Question> tmpList = new ArrayList<>();
        for (int i = 0; i < unsortedQuestions.size(); i++) {
            if (unsortedQuestions.get(i).getPoints() == currentPointWeight){
                tmpList.add(unsortedQuestions.get(i));
            }
            else {
                currentPointWeight++;
                Collections.shuffle(tmpList);
                for (Question question : tmpList) {
                    randomizeList.add(question);
                }
                tmpList.clear();
                if (i + 1 == unsortedQuestions.size()) {
                    randomizeList.add(unsortedQuestions.get(i));
                }
                else {
                    tmpList.add(unsortedQuestions.get(i));
                }
            }
        }

        return randomizeList;
    }

    public void selectAnswer(Integer id) {

        if (halfAnswers != null) {
            answerCButton.setEnabled(true);
            answerDButton.setEnabled(true);

            if (questionsList.get(currentQuestionId).getAnswers().get(halfAnswers[id]).isCorrect()) {
                points += questionsList.get(currentQuestionId).getPoints();             // poprawne wskazanie jednej z dwóch odp.
                correctAnswers++;
                halfAnswers = null;
                goNextQuestion();
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putInt("correctAnswers", correctAnswers);
                bundle.putInt("totalQuestions", questionsList.size());
                bundle.putInt("points", points);
                Navigation.findNavController(viewG).navigate(R.id.action_nav_quiz_to_nav_user_result, bundle);
            }

        }
        else {
            if (questionsList.get(currentQuestionId).getAnswers().get(id).isCorrect()) {
                points += questionsList.get(currentQuestionId).getPoints();
                correctAnswers++;
                goNextQuestion();
            } else {
                if (isSafe) {
                    goNextQuestion();
                    isSafe = false;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("correctAnswers", correctAnswers);
                    bundle.putInt("totalQuestions", questionsList.size());
                    bundle.putInt("points", points);
                    Navigation.findNavController(viewG).navigate(R.id.action_nav_quiz_to_nav_user_result, bundle);
                }
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, 1000);
                manageQuiz();
            }
        }, 1000);
    }


    @SuppressLint("SetTextI18n")
    public void manageQuiz() {

        if (!isNetworkConnected()) {
            handler.removeCallbacks(runnable);
            backToLoginScreen();
        }
        else {
            if (seconds > 0) {
                seconds--;
                timeText.setText(seconds.toString());
            }
            else {
                if (halfAnswers != null) {
                    answerCButton.setEnabled(true);
                    answerDButton.setEnabled(true);
                    halfAnswers = null;
                }

                if (isSafe) {
                    goNextQuestion();           // wykorzystanie koła ratunkowego - chroniącego przed podaniem błędnej odp.
                    isSafe = false;
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("correctAnswers", correctAnswers);
                    bundle.putInt("totalQuestions", questionsList.size());
                    bundle.putInt("points", points);
                    Navigation.findNavController(viewG).navigate(R.id.action_nav_quiz_to_nav_user_result, bundle);
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    public void goNextQuestion() {
        if (currentQuestionId + 1 != questionsList.size()) {

            currentQuestionId++;
            seconds = questionsList.get(currentQuestionId).getSeconds();
            timeText.setText(seconds.toString());
            pointsText.setText(points.toString());
            roundText.setText("Runda: " + (currentQuestionId + 1));
            questionText.setText(questionsList.get(currentQuestionId).getContent());
            categoryText.setText("Kategoria: " + questionsList.get(currentQuestionId).getCategory());
            answerAButton.setText(questionsList.get(currentQuestionId).getAnswers().get(0).getAnswer());
            answerBButton.setText(questionsList.get(currentQuestionId).getAnswers().get(1).getAnswer());
            answerCButton.setText(questionsList.get(currentQuestionId).getAnswers().get(2).getAnswer());
            answerDButton.setText(questionsList.get(currentQuestionId).getAnswers().get(3).getAnswer());
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("correctAnswers", correctAnswers);
            bundle.putInt("totalQuestions", questionsList.size());
            bundle.putInt("points", points);
            Navigation.findNavController(viewG).navigate(R.id.action_nav_quiz_to_nav_user_result, bundle);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    public void backToLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(getContext(), "Brak połączenia z Internetem", Toast.LENGTH_LONG).show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
