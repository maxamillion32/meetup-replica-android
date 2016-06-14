package eg.edu.guc.android.meetup.util;


import java.util.List;

import eg.edu.guc.android.meetup.model.Product;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.model.UserOld;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface PublicApiRoutes {
    @POST("/login/create")
    @FormUrlEncoded
    void login(@Field("login[email]") String email,
               @Field("login[password]") String password,
               Callback<User> callback);

    @GET("/login/facebook/{uid}/{f_name}/{l_name}")
    void facebookLogin(@Path("uid") String uid,
                       @Path("f_name") String fName,
                       @Path("l_name") String lName,
                       Callback<User> callback);

    // =================================================
    @GET("/products")
    void getProducts(Callback<List<Product>> callback);
    // =================================================


    // SIGN UP
    @POST("/users")
    @FormUrlEncoded
    void signUp(@Field("user[f_name]") String fName,
                @Field("user[l_name]") String lName,
                @Field("user[email]") String email,
                @Field("user[password]") String password,
                @Field("user[location]") String location,
                @Field("user[day]") int day,
                @Field("user[month]") int month,
                @Field("user[year]") int year,
                @Field("user[profile_picture]") String profile_picture,
                @Field("user[gender]") String gender,
                Callback<User> callback);
}
