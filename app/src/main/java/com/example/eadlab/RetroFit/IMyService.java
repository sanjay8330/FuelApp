package com.example.eadlab.RetroFit;

//import java.util.Observable;
import io.reactivex.Observable;
import io.reactivex.observables.*;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {
    @POST("api/Shed")
    @FormUrlEncoded
    Observable<String> addshed(@Field("shedName") String shedName, @Field("location") String location );

//    @POST("register")
//    @FormUrlEncoded
//    Observable<String> registerUsers(@Field("email") String email,
//                                     @Field("name") String name);
}
