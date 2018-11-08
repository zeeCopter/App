package com.example.hk.transport;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hk.transport.Utilities.APIs.API;
import com.example.hk.transport.Utilities.BitmapUtils;
import com.example.hk.transport.Utilities.Common;
import com.example.hk.transport.Utilities.Pojos.LoginPojo;
import com.example.hk.transport.Utilities.Pojos.ProfilePojo;
import com.example.hk.transport.Utilities.Pojos.ResetPasswordPojo;
import com.example.hk.transport.Utilities.Pojos.UpdateProfilePojo;
import com.example.hk.transport.Utilities.Pojos.UploadFilePojo;
import com.example.hk.transport.Utilities.SharePreferencesUtility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    ImageView backIV;
    EditText lastNameET,firstNameET,numberET,passwordET,emailET;
    CircleImageView profileIV;
    Button updateBtn;
    Dialog dialog;
    String image = "";
    int REQUEST_IMAGE_CAPTURE = 1;
    int REQUEST_GALLERY_PICK = 2;
    String mCurrentPhotoPath,compressImage;
    File photoFile;
    Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backIV = findViewById(R.id.backIV);
        lastNameET = findViewById(R.id.lastNameET);
        firstNameET = findViewById(R.id.firstNameET);
        numberET = findViewById(R.id.numberET);
        passwordET = findViewById(R.id.passwordET);
        emailET = findViewById(R.id.emailET);
        profileIV = findViewById(R.id.profile_image);
        updateBtn = findViewById(R.id.updateBtn);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionDialog();
            }
        });

        getProfile();
    }

    private void getProfile()
    {
        Common.showProgressDialog(EditProfileActivity.this);
        API.getWebServices().getProfile(Common.loginPojo.getUserId()).enqueue(new Callback<ProfilePojo>() {
            @Override
            public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                Common.dismissProgressDialog();
                if(response.body().getStatus())
                {
                    firstNameET.setText(response.body().getFirstName());
                    lastNameET.setText(response.body().getLastName());
                    numberET.setText(response.body().getMobileNumber().substring(3));
                    emailET.setText(response.body().getEmailAddress());
                    Picasso.get().load(response.body().getImageUrl()).into(profileIV);
                    LoginPojo loginPojo = new SharePreferencesUtility(EditProfileActivity.this).getLoginModel();
                    loginPojo.setFirstName(response.body().getFirstName());
                    loginPojo.setLastName(response.body().getLastName());
                    loginPojo.setEmailAddress(response.body().getEmailAddress());
                    loginPojo.setImageUrl(response.body().getImageUrl());
                    new SharePreferencesUtility(EditProfileActivity.this).saveLoginModel(loginPojo);
                }
                else
                {
                    Toast.makeText(EditProfileActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilePojo> call, Throwable t) {
                Common.dismissProgressDialog();
                Toast.makeText(EditProfileActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile()
    {
        if(firstNameET.getText().toString().equals(""))
        {
            Toast.makeText(EditProfileActivity.this,"Enter First Name",Toast.LENGTH_SHORT).show();
        }
        else if(lastNameET.getText().toString().equals(""))
        {
            Toast.makeText(EditProfileActivity.this,"Enter Last Name",Toast.LENGTH_SHORT).show();
        }
        else if(emailET.getText().toString().equals(""))
        {
            Toast.makeText(EditProfileActivity.this,"Enter Email Address",Toast.LENGTH_SHORT).show();
        }
        else if(!Common.isValidEmailAddress(emailET.getText().toString()))
        {
            Toast.makeText(EditProfileActivity.this,"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String imageToSend;
            if(!image.equals(""))
            {
                imageToSend = image;
            }
            else
            {
                String[] separated = Common.loginPojo.getImageUrl().split("/");
                imageToSend = separated[separated.length-1];
            }
            Common.showProgressDialog(EditProfileActivity.this);
            API.getWebServices().updateProfile(Common.loginPojo.getUserId(),
                    imageToSend,
                    emailET.getText().toString(),
                    lastNameET.getText().toString(),
                    firstNameET.getText().toString()).enqueue(new Callback<UpdateProfilePojo>() {
                @Override
                public void onResponse(Call<UpdateProfilePojo> call, Response<UpdateProfilePojo> response) {
                    Common.dismissProgressDialog();
                    if(response.body().getStatus())
                    {
                        Common.loginPojo.setFirstName(response.body().getFirstName());
                        Common.loginPojo.setLastName(response.body().getLastName());
                        Common.loginPojo.setEmailAddress(response.body().getEmailAddress());
                        Common.loginPojo.setImageUrl(response.body().getImageUrl());
                        finish();
                    }
                    else
                    {
                        Toast.makeText(EditProfileActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfilePojo> call, Throwable t) {
                    Common.dismissProgressDialog();
                    Toast.makeText(EditProfileActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void selectOptionDialog()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_option);

        final Button openCameraBtn,pickFromGalleryBtn;
        openCameraBtn = dialog.findViewById(R.id.openCameraBtn);
        pickFromGalleryBtn = dialog.findViewById(R.id.pickFromGalleryBtn);

        openCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                dialog.dismiss();
            }
        });

        pickFromGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_PICK);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                if(photoFile != null && mCurrentPhotoPath != null)
                {
                    compressImage = BitmapUtils.compressImage(mCurrentPhotoPath,EditProfileActivity.this);
                    myBitmap = BitmapFactory.decodeFile(compressImage);
                }
            }
            else if(requestCode == REQUEST_GALLERY_PICK && resultCode == RESULT_OK)
            {
                compressImage = BitmapUtils.compressImage(BitmapUtils.getPathFromUri(this,data.getData()),EditProfileActivity.this);
                myBitmap = BitmapFactory.decodeFile(compressImage);
            }

            profileIV.setImageBitmap(myBitmap);

            File f = new File(compressImage);

            String mimeType= URLConnection.guessContentTypeFromName(f.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType),f);
            MultipartBody.Part body = MultipartBody.Part.createFormData("Image", f.getName(), requestFile);

            Common.showProgressDialog(EditProfileActivity.this);
            API.getWebServices().uploadFile(body).enqueue(new Callback<UploadFilePojo>() {
                @Override
                public void onResponse(Call<UploadFilePojo> call, Response<UploadFilePojo> response) {
                    Common.dismissProgressDialog();
                    if(response.body().getStatus())
                    {
                        image = response.body().getData();
                    }
                }

                @Override
                public void onFailure(Call<UploadFilePojo> call, Throwable t) {
                    Common.dismissProgressDialog();
                    Toast.makeText(EditProfileActivity.this,"Check Internet Connection",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.hk.transport.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

}
