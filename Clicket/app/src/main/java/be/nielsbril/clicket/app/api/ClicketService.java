package be.nielsbril.clicket.app.api;

import com.google.gson.JsonObject;

import be.nielsbril.clicket.app.helpers.UserResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

public interface ClicketService {

    //Auth
    @FormUrlEncoded
    @POST("register")
    Call<JsonObject> register(@Field("email") String email, @Field("name") String name, @Field("firstname") String firstname, @Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Field("email") String email, @Field("password") String password);

    //User
    @GET("api/users")
    Observable<UserResult> user(@Header("Authorization") String token);

    @PUT("api/users")
    Call<JsonObject> editUser(@Field("email") String email, @Field("name") String name, @Field("firstname") String firstname, @Field("phone") String phone, @Field("invoice_amount") String invoiceAmount, @Header("Authorization") String token);

}