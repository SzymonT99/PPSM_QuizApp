package com.ppsm.quiz_app.http;

import com.ppsm.quiz_app.model.ChangedUserLoginDto;
import com.ppsm.quiz_app.model.ChangedUserPasswordDto;
import com.ppsm.quiz_app.model.CreateQuestionDto;
import com.ppsm.quiz_app.model.CreateUserDto;
import com.ppsm.quiz_app.model.DeleteUserDto;
import com.ppsm.quiz_app.model.Question;
import com.ppsm.quiz_app.model.QuizResult;
import com.ppsm.quiz_app.model.Statistics;
import com.ppsm.quiz_app.model.UserAutorizationDto;
import com.ppsm.quiz_app.model.UserResultDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceholderAPI {

    @POST("quiz/create-user")
    Call<Void> registerUser(@Body CreateUserDto user);

    @POST("quiz/user/login")
    Call<String> getUserRole(@Body UserAutorizationDto user);

    @GET("quiz/questions/{active}")
    Call<List<Question>> getQuestions(@Path("active") boolean isActive);

    @POST("quiz/save-result")
    Call<Void> saveResult(@Body UserResultDto result);

    @GET("quiz/ranking")
    Call<ArrayList<QuizResult>> getRanking();

    @GET("quiz/statistics/{nick}")
    Call<Statistics> getStats(@Path("nick") String nick);

    @PUT("quiz/user-update/password")
    Call<Void> changePassword(@Body ChangedUserPasswordDto changedUserPassword);

    @PUT("quiz/user-update/login")
    Call<Void> changeLogin(@Body ChangedUserLoginDto changedUserLogin);

    @HTTP(method = "DELETE", path = "/quiz/delete-user", hasBody = true)
    Call<Void> deleteUser(@Body DeleteUserDto deleteUser);

    @POST("quiz/add-question")
    Call<Void> addQuestion(@Body CreateQuestionDto question);

}
