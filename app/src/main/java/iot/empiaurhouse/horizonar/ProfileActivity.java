package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.util.ResourceBundle.clearCache;

public class ProfileActivity extends RuntimePermissionsActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 20;


    private static int RESULT_LOAD_IMAGE = 1;

    private static final String IMAGE_DIRECTORY = "/horizonAR";


    private static final int REQUEST_TAKE_PHOTO = 2;

    private TypeWriterTextView typeWriterTextView;
    SharedPreferences horizonPreferences;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhone;
    private EditText profileNameEdit;
    private EditText profileEmailEdit;
    private EditText profilePhoneEdit;
    private Toolbar profileToolbar;
    SharedPreferences.Editor horizonAR_IO;


    private ImageView closeProfileButton;
    private TextView profileNameText;
    private TextView profilePhoneText;
    private TextView profileEmailText;
    private CardView Username;
    private CardView UserPhone;
    private CardView UserEmail;
    private Button profileUpdate;
    private Bitmap selectedProfileImage;
    private String imageFilePath;
    private String cameraFilePath;
    private String base64String;





    private String Name;
    private String Phone;
    private String Email;



    private ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        typeWriterTextView = findViewById(R.id.myProfile);
        typeWriterText(getString(R.string.menu_myProfile),190,typeWriterTextView);
        statusBarColor(R.color.colorAccent);
        horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES",0);
        profileToolbar = findViewById(R.id.profileToolbar);
        profileName = findViewById(R.id.profileUserName);
        profileEmail = findViewById(R.id.profileUserEmail);
        profilePhone = findViewById(R.id.profileUserPhone);
        profileImage = findViewById(R.id.profileUserImage);
        profileNameEdit = findViewById(R.id.profileUserNameEdit);
        profilePhoneEdit = findViewById(R.id.profileUserPhoneEdit);
        profileEmailEdit = findViewById(R.id.profileUserEmailEdit);
        profileNameText = findViewById(R.id.profileUserNameText);
        profilePhoneText = findViewById(R.id.profileUserPhoneText);
        profileEmailText = findViewById(R.id.profileUserEmailText);
        Username = findViewById(R.id.cardviewUserName);
        UserPhone = findViewById(R.id.cardviewUserPhone);
        UserEmail = findViewById(R.id.cardviewUserEmail);
        profileUpdate = findViewById(R.id.profileUpdateButton);
        closeProfileButton = findViewById(R.id.closeProfile);

        this.profileRender();
        this.profileUIEventHandler();
        this.permitCheck();






    }



    private void profileEditor(){

        profileNameText.setVisibility(View.GONE);
        profileNameEdit.setVisibility(View.VISIBLE);
        profilePhoneText.setVisibility(View.GONE);
        profilePhoneEdit.setVisibility(View.VISIBLE);
        profileEmailText.setVisibility(View.GONE);
        profileEmailEdit.setVisibility(View.VISIBLE);
        profileUpdate.setVisibility(View.VISIBLE);

    }



    private void profileRender(){

        Name = horizonPreferences.getString("displayName",null);
        Phone = horizonPreferences.getString("mobileNumber",null);
        Email = horizonPreferences.getString("email",null);
        imageFilePath = horizonPreferences.getString("imageFilePath",null);
        cameraFilePath = horizonPreferences.getString("cameraFilePath",null);
        base64String = horizonPreferences.getString("base64String",null);
        if (base64String != null) {
            Bitmap profileBitmap = ImageUtil.convert(Objects.requireNonNull(base64String));

            if(profileBitmap != null) {
                profileImage.setImageBitmap(profileBitmap);

            }

        }

        profileName.setText(Name);
        profileEmail.setText(Email);
        profilePhone.setText(Phone);
        profileNameText.setText(Name);
        profilePhoneText.setText(Phone);
        profileEmailText.setText(Email);
        profileNameEdit.setText(Name);
        profilePhoneEdit.setText(Phone);
        profileEmailEdit.setText(Email);






    }


    private void typeWriterText(String myProfileText, Integer charDelay, TypeWriterTextView myprofileTextView){
        myprofileTextView.setCharacterDelay(charDelay);
        myprofileTextView.displayTextWithAnimation(myProfileText);

    }




    @Override
    protected void onResume() {
        super.onResume();
        this.profileRender();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



    private void profileUIEventHandler(){

        Username.setOnClickListener(view -> {

            this.profileEditor();
        });

        UserPhone.setOnClickListener(view -> {

            this.profileEditor();
        });

        UserEmail.setOnClickListener(view -> {

            this.profileEditor();
        });

        profileUpdate.setOnClickListener(view -> {

            this.profileSetter();
            finish();
            //submit info to loc store & online repo
            //then close view
        });


        closeProfileButton.setOnClickListener(view -> {

            this.exitProfilePrompt();
            //confirm profile changes
        });


        profileImage.setOnClickListener(view -> {


            selectImage(ProfileActivity.this);
            //confirm profile changes
        });


    }


    private void exitProfilePrompt(){

        if(profileUpdate.getVisibility() == View.VISIBLE) {

            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
            alertDialog.setTitle("Discard Changes");
            alertDialog.setIcon(R.drawable.horizonapplogo);
            alertDialog.setMessage(Name + ", would you like to discard changes to your AR Profile?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    (dialog, which) -> {
                        finish();

                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
        else {

            finish();

        }


    }



    private void profileSetter(){

        Name = profileNameEdit.getText().toString();
        Phone = profilePhoneEdit.getText().toString();
        Email = profileEmailEdit.getText().toString();
        SharedPreferences.Editor horizonAR_IO = horizonPreferences.edit();
        horizonAR_IO.putString("displayName", Name);
        horizonAR_IO.putString("mobileNumber", Phone);
        horizonAR_IO.putString("email", Email);


        horizonAR_IO.apply();


        //include profile PUT method for Circe API



    }







    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_TAKE_PHOTO);
    }



    private void chooseImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }



    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo","Choose from Gallery","Cancel" };

        AlertDialog.Builder selectBuilder = new AlertDialog.Builder(context);
        selectBuilder.setTitle("Select AR Avatar");
        selectBuilder.setIcon(R.drawable.ic_profile_red_50dp);

        selectBuilder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                this.takePhotoFromCamera();
            }
            else if (options[item].equals("Choose from Gallery")) {
               this.chooseImage();

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        selectBuilder.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(Objects.requireNonNull(selectedImage),
                    filePathColumn, null, null, null);
            Objects.requireNonNull(cursor).moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            selectedProfileImage = BitmapFactory.decodeFile(picturePath);
            base64String = ImageUtil.convert(selectedProfileImage);
            horizonAR_IO = horizonPreferences.edit();
            horizonAR_IO.putString("base64String", base64String);
            horizonAR_IO.apply();
            profileImage.setImageBitmap(selectedProfileImage);
            profileUpdate.setVisibility(View.VISIBLE);

            //CIRCE API Webservice image upload

        }

        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && null != data) {
            Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            base64String = ImageUtil.convert(selectedProfileImage);
            horizonAR_IO = horizonPreferences.edit();
            horizonAR_IO.putString("base64String", base64String);
            horizonAR_IO.apply();
            profileImage.setImageBitmap(thumbnail);
            saveImage(Objects.requireNonNull(thumbnail));
            profileUpdate.setVisibility(View.VISIBLE);

            //CIRCE API Webservice image upload



        }

    }



    private void statusBarColor(int colorInt){

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,colorInt));

    }










    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {

            if(profileUpdate.getVisibility() == View.VISIBLE){

                this.exitProfilePrompt();

            }
            else {

                this.moveTaskToBack(true);
                }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onPermissionsGranted(final int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();

    }



    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File horizonARDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!horizonARDirectory.exists()) {
            horizonARDirectory.mkdirs();
        }

        try {
            File f = new File(horizonARDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());
            cameraFilePath = f.getAbsolutePath();
            horizonAR_IO = horizonPreferences.edit();
            horizonAR_IO.putString("cameraFilePath", cameraFilePath);
            horizonAR_IO.apply();



            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }




    private void permitCheck(){

        final Handler handler = new Handler();
        handler.postDelayed(() -> ProfileActivity.super.requestAppPermissions(new
                        String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS), 4000);


    }












}
