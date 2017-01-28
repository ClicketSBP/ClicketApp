package be.nielsbril.clicket.app.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ClicketService {

    //Register
    @FormUrlEncoded
    @POST("register")
    Call<JsonObject> register(@Field("email") String email, @Field("lastname") String lastname, @Field("firstname") String firstname, @Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Field("email") String email, @Field("password") String password);

}