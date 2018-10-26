package com.example.hk.transport.Utilities.APIs;

import com.example.hk.transport.Utilities.Pojos.ForgetPasswordPojo;
import com.example.hk.transport.Utilities.Pojos.LoginPojo;
import com.example.hk.transport.Utilities.Pojos.ProfilePojo;
import com.example.hk.transport.Utilities.Pojos.ResetPasswordPojo;
import com.example.hk.transport.Utilities.Pojos.SignUpPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Interfaces {

    @FormUrlEncoded
    @POST("/api/Users/Login")
    Call<LoginPojo> login(@Field("MobileNo") String mobileNo, @Field("Pass") String password);

    @FormUrlEncoded
    @POST("/api/Users/SignupUser")
    Call<SignUpPojo> signUp(@Field("MobileNo") String mobileNo, @Field("Pass") String password, @Field("FirstName") String firstName, @Field("LastName") String lastName, @Field("EmailAddress") String email);

    @FormUrlEncoded
    @POST("/api/Users/ForgotPass")
    Call<ForgetPasswordPojo> forgetPassword(@Field("EmailAddress") String email);

    @FormUrlEncoded
    @POST("/api/Users/GetProfile")
    Call<ProfilePojo> getProfile(@Field("UserId") String userId);

    @FormUrlEncoded
    @POST("/api/Users/ResetPassword")
    Call<ResetPasswordPojo> resetPassword(@Field("UserId") String userId, @Field("NewPass") String newPassword,@Field("OldPass") String oldPassword);
}
