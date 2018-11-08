package com.example.hk.transport.Utilities.APIs;

import com.example.hk.transport.Utilities.Pojos.ForgetPasswordPojo;
import com.example.hk.transport.Utilities.Pojos.LoginPojo;
import com.example.hk.transport.Utilities.Pojos.ModulePojo;
import com.example.hk.transport.Utilities.Pojos.ProfilePojo;
import com.example.hk.transport.Utilities.Pojos.ResetPasswordPojo;
import com.example.hk.transport.Utilities.Pojos.SignUpPojo;
import com.example.hk.transport.Utilities.Pojos.UpdateProfilePojo;
import com.example.hk.transport.Utilities.Pojos.UploadFilePojo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

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

    @GET("/api/Modules/GetModule")
    Call<ModulePojo> getModule(@Query("Weight") int weight, @Query("Dimension") int dimension);

    @FormUrlEncoded
    @POST("/api/Users/ResetPassword")
    Call<ResetPasswordPojo> resetPassword(@Field("UserId") String userId, @Field("NewPass") String newPassword,@Field("OldPass") String oldPassword);

    @FormUrlEncoded
    @POST("/api/Users/UpdateProfile")
    Call<UpdateProfilePojo> updateProfile(@Field("UserId") String userId, @Field("ProfileImage") String profileImg, @Field("EmailAddress") String email, @Field("LastName") String lastName, @Field("FirstName") String firstName);

    @Multipart
    @POST("/api/Users/GetImagePath")
    Call<UploadFilePojo> uploadFile(@Part MultipartBody.Part Image);
}
