package com.ppsm.quiz_app.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.ui.AdminPanelAdapter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity implements AdminPanelAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private AdminPanelAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JsonPlaceholderAPI jsonPlaceholderAPI;
    private List<Question> questionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mRecyclerView = findViewById(R.id.recycler_view_admin);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        getUsersQuestions();
    }

    private void getUsersQuestions() {

        Call<List<Question>> call = jsonPlaceholderAPI.getQuestions(false);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Server Code: " + response.code());
                }

                questionsList = response.body();
                mAdapter = new AdminPanelAdapter(questionsList, AdminActivity.this);
                System.out.println("uuu"+ mAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        System.out.println("xdddd");
        Question currentQuestion = questionsList.get(position);
        Intent intent = new Intent(AdminActivity.this, VerifyQuestionActivity.class);
        intent.putExtra("CURRENT_QUESTION", currentQuestion);
        startActivity(intent);
    }
}
