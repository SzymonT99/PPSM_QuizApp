package com.ppsm.quiz_app.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.model.SpecifyQuestionDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyQuestionActivity extends AppCompatActivity {

    private Long questionId;
    private EditText modifyAnswerA, modifyAnswerB, modifyAnswerC, modifyAnswerD,
            modifyQuestionContent, modifySeconds, points, category;
    private TextView rowA, rowB, rowC, rowD, warning;
    private JsonPlaceholderAPI jsonPlaceholderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_question);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        rowA = findViewById(R.id.answer_a_vf);
        rowB = findViewById(R.id.answer_b_vf);
        rowC = findViewById(R.id.answer_c_vf);
        rowD = findViewById(R.id.answer_d_vf);
        warning = findViewById(R.id.warning_vf);

        modifyAnswerA = findViewById(R.id.input_answer_A_vf);
        modifyAnswerB = findViewById(R.id.input_answer_B_vf);
        modifyAnswerC = findViewById(R.id.input_answer_C_vf);
        modifyAnswerD = findViewById(R.id.input_answer_D_vf);
        modifyQuestionContent = findViewById(R.id.question_content_value_vf);
        modifySeconds = findViewById(R.id.time_value_vf);
        points = findViewById(R.id.points_value_vf);
        category = findViewById(R.id.category_value_vf);

        Intent intent = getIntent();
        Question receiveQuestion = (Question) intent.getSerializableExtra("CURRENT_QUESTION");

        questionId = receiveQuestion.getId();

        modifyQuestionContent.setText(receiveQuestion.getContent());
        modifyAnswerA.setText(receiveQuestion.getAnswers().get(0).getAnswer());
        modifyAnswerB.setText(receiveQuestion.getAnswers().get(1).getAnswer());
        modifyAnswerC.setText(receiveQuestion.getAnswers().get(2).getAnswer());
        modifyAnswerD.setText(receiveQuestion.getAnswers().get(3).getAnswer());
        modifySeconds.setText(receiveQuestion.getSeconds().toString());

        for (int i = 0; i < 4; i++){
            if (receiveQuestion.getAnswers().get(i).isCorrect()) {
                if (i == 0) {
                    rowA.setBackgroundResource(R.drawable.rounded);
                    modifyAnswerA.setBackgroundResource(R.drawable.create_answer_layout);
                }
                if (i == 1) {
                    rowB.setBackgroundResource(R.drawable.rounded);
                    modifyAnswerB.setBackgroundResource(R.drawable.create_answer_layout);
                }
                if (i == 2) {
                    rowC.setBackgroundResource(R.drawable.rounded);
                    modifyAnswerC.setBackgroundResource(R.drawable.create_answer_layout);
                }
                if (i == 3) {
                    rowD.setBackgroundResource(R.drawable.rounded);
                    modifyAnswerD.setBackgroundResource(R.drawable.create_answer_layout);
                }
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

    }

    @SuppressLint("SetTextI18n")
    private void acceptQuestion() {

        if (!modifyQuestionContent.getText().toString().equals("") && !modifyAnswerA.getText().toString().equals("") &&
                !modifyAnswerB.getText().toString().equals("") && !modifyAnswerC.getText().toString().equals("") &&
                !modifyAnswerD.getText().toString().equals("") && !modifySeconds.getText().toString().equals("") &&
                !points.getText().toString().equals("") && !category.getText().toString().equals("")) {
            System.out.println("xddd");

            SpecifyQuestionDto specifyQuestionDto = new SpecifyQuestionDto(modifyQuestionContent.getText().toString(),
                    modifyAnswerA.getText().toString(), modifyAnswerB.getText().toString(), modifyAnswerC.getText().toString(),
                    modifyAnswerD.getText().toString(), Integer.parseInt(modifySeconds.getText().toString()),
                    Integer.parseInt(points.getText().toString()), category.getText().toString());

            Call<Void> call = jsonPlaceholderAPI.acceptQuestion(questionId, specifyQuestionDto);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Intent intent = new Intent(VerifyQuestionActivity.this, AdminActivity.class);
                        startActivity(intent);
                        Toast toast = Toast.makeText(getApplicationContext(), "Zaakceptowano pytanie", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
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
            warning.setText("Pola nie mogą być puste");
        }
    }

    private void deleteQuestion() {
        Call<Question> call = jsonPlaceholderAPI.deleteQuestion(questionId);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(VerifyQuestionActivity.this, AdminActivity.class);
                    startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(), "Usunięto pytanie", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
            }
        });
    }


    public void confirmQuestion(View view) {
        acceptQuestion();
    }

    public void denyQuestion(View view) {
        deleteQuestion();
    }

    public void backToQuestions(View view) {
        Intent intent = new Intent(VerifyQuestionActivity.this, AdminActivity.class);
        startActivity(intent);
    }
}
