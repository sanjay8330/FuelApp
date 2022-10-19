package com.example.eadlab.RetroFit;

//import java.util.Observable;
import com.example.eadlab.Model.ShedModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observables.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IMyService {

    @GET("/api/Shed")//Check
    Call<List<ShedModel>> getAllSheds();


    @POST("/api/Shed")
    Call<ShedModel> addFuelStation(@Body ShedModel shedModel);

    @PUT
    @FormUrlEncoded
    Call<ShedModel> updateFuelStation(@Field("shedName") String shedName, @Field("location") String location);

}
