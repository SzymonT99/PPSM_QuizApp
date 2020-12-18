package com.ppsm.quiz_app.http;

import com.ppsm.quiz_app.model.CreateUserDto;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.model.UserAutorizationDto;
import com.ppsm.quiz_app.model.UserResultDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceholderAPI {

    @POST("quiz/create-user")
    Call<Void> registerUser(@Body CreateUserDto user);

    @POST("quiz/user/login")
    Call<String> getUser(@Body UserAutorizationDto user);

    @GET("quiz/questions/{active}")
    Call<List<Question>> getQuestions(@Path("active") boolean isActive);

    @POST("quiz/save-result")
    Call<Void> saveResult(@Body UserResultDto result);

}
