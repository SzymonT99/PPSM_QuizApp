package com.ppsm.quiz_app.http;

import com.ppsm.quiz_app.model.CreateUserDto;
import com.ppsm.quiz_app.model.UserAutorizationDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceholderAPI {

    @POST("quiz/create-user")
    Call<Void> registerUser(@Body CreateUserDto user);

    @POST("quiz/user/login")
    Call<String> getUser(@Body UserAutorizationDto user);
}
