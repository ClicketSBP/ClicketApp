package be.nielsbril.clicket.app.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

    @FormUrlEncoded
    @PUT("api/users")
    Call<JsonObject> editUser(@Field("email") String email, @Field("name") String name, @Field("firstname") String firstname, @Field("phone") String phone, @Field("invoice_amount") String invoiceAmount, @Header("Authorization") String token);

    //Cars
    @GET("api/cars/all")
    Observable<CarsResult> cars(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/car")
    Call<JsonObject> addCar(@Field("name") String name, @Field("license_plate") String licensePlate, @Field("default_car") String defaultCar, @Header("Authorization") String token);

    @FormUrlEncoded
    @PUT("api/car")
    Call<JsonObject> editCar(@Field("id") int id, @Field("name") String name, @Field("license_plate") String licensePlate, @Field("default_car") String defaultCar, @Header("Authorization") String token);

    @DELETE("api/car/{id}")
    Call<JsonObject> deleteCar(@Path("id") int id, @Header("Authorization") String token);

    //Sessions
    @FormUrlEncoded
    @POST("api/sessions/recent")
    Observable<SessionsResult> sessions(@Field("amount") int amount, @Header("Authorization") String token);

    @GET("api/session/active")
    Observable<SessionSingleResult> activeSession(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/session")
    Observable<SessionSingleResult> startSession(@Field("lat") String lat, @Field("lng") String lng, @Field("car_id") int carId, @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/session/active")
    Observable<SessionStopResult> stopSession(@Field("id") int id, @Header("Authorization") String token);

}