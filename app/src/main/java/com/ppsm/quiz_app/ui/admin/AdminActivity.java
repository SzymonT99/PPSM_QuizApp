package com.ppsm.quiz_app.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ppsm.quiz_app.MainActivity;
import com.ppsm.quiz_app.R;
import com.ppsm.quiz_app.http.JsonPlaceholderAPI;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.ui.AdminPanelAdapter;
import com.ppsm.quiz_app.ui.SwipeToDeleteCallback;
import com.ppsm.quiz_app.ui.authorization.LoginActivity;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class AdminActivity extends AppCompatActivity implements AdminPanelAdapter.OnItemClickListener {

    static boolean active=true;
    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    private RecyclerView mRecyclerView;
    private AdminPanelAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private JsonPlaceholderAPI jsonPlaceholderAPI;
    private List<Question> questionsList;
    private LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        mRecyclerView = findViewById(R.id.recycler_view_admin);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        enableSwipeToDeleteAndUndo();

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://192.168.1.102:8080/")
                .baseUrl("http://192.168.1.115:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        // Refresh  the layout
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getUsersQuestions();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                if(active) {
                    getUsersQuestions();
                    handler.postDelayed(this, 30000);  //30 000 =5 minut;
                }
            }
        }, 6000);
        getUsersQuestions();
    }

    private void getUsersQuestions() {
            if(checkInternetConnection()) {
                Call<List<Question>> call = jsonPlaceholderAPI.getQuestions(false);
                call.enqueue(new Callback<List<Question>>() {
                    @Override
                    public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                        if (!response.isSuccessful()) {
                            System.out.println("Server Code: " + response.code());
                        }

                        questionsList = response.body();
                        mAdapter = new AdminPanelAdapter(questionsList, AdminActivity.this);
                        System.out.println("uuu" + mAdapter);
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
                Toast.makeText(getApplicationContext(),"Pobrano dane",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Brak Internetu!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
    }


    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Question item = mAdapter.getQuestionsList().get(position);

                deleteQuestion(questionsList.get(position).getId());
                mAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(swipeRefreshLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(item, position);
                        mRecyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);
    }

    private void deleteQuestion(Long questionId) {
        Call<Question> call = jsonPlaceholderAPI.deleteQuestion(questionId);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.code() == 200) {
                    //Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
                    //startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(), "Usunięto pytanie", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        System.out.println("xdddd");
        Question currentQuestion = questionsList.get(position);
        Intent intent = new Intent(AdminActivity.this, VerifyQuestionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("CURRENT_QUESTION", currentQuestion);
        startActivity(intent);
    }

    public void logOut(View view) {
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
