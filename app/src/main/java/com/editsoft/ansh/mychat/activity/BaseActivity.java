package com.editsoft.ansh.mychat.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.utility.FileHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public abstract class BaseActivity extends AppCompatActivity {


    private static final int SELECT_FILE = 101;
    private ProgressDialog mProgressDialog;
    private boolean doubleBackToExitPressedOnce = false;
    private static final int REQUEST_CAMERA = 100;
    private File rawFile;
    private String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_base);

    }

    protected void initToolbar(boolean status) {
        if (status) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(setToolbarName());
        }
    }

    public void showToast(String s) {
        Toast.makeText(BaseActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public boolean showToolbar() {
        return false;
    }

    public void init() {
        initToolbar(showToolbar());
        initView();
        initListener();
        bindDataWithUi();
    }

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void bindDataWithUi();


    /**
     * show Progress Dialog
     */
    public void showLoadingDialog() {
        if (isDestroyingActivity()) return;
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(this, R.style.DialogTheme);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.dialog_progress);
        }
        mProgressDialog.show();
    }


    /**
     * @return boolean value
     * return true if Progress Dialog show else return false
     */
    public boolean isShowingProgressDialog() {
        return !(mProgressDialog == null) && mProgressDialog.isShowing();
    }


    /***
     * hide the Progress Dialog
     */
    public void hideLoadingDialog() {
        if (isDestroyingActivity())
            return;
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


    /**
     * check the base Activity is destroy or not
     * if activity destroy return true else return false
     *
     * @return
     */
    public boolean isDestroyingActivity() {
        return isFinishing() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed();
    }


    /**
     * hide the keyboard
     */
    public void hideSoftInputKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * this function check the internet connection and return the boolean value
     *
     * @return true and false value
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void addFragment(Fragment fragment, int containerId) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerId, fragment)
                .commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction replace = getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment);
        if (addToBackStack) {
            replace.addToBackStack(fragment.getClass().getName());
        }
        replace.commitAllowingStateLoss();
    }


    public String setToolbarName() {
        return getResources().getString(R.string.app_name);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
 /*       if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Touch again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);*/

        super.onBackPressed();
    }


    public boolean hasPermission(String[] permission) {
        for (String s : permission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(s)) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean checkPermission(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, permissionCode);
            hasPermission(permission);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK && null != data) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            onCameraResult(image);
        }

        if (requestCode == SELECT_FILE && data != null) {
            rawFile = new File(FileHelper.getPath(BaseActivity.this, data.getData()));
            uploadImage(rawFile);
        }
    }


    private void uploadImage(File rawFile) {
        showLoadingDialog();
        Uri file = Uri.fromFile(rawFile);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mychat-7f241.appspot.com");
        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: " + exception.getMessage());
                showToast("Error on Add Data");
                hideLoadingDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                hideLoadingDialog();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                onGalleryResult(downloadUrl);
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void onCameraResult(Bitmap path) {
    }


    protected void onGalleryResult(Uri uri) {
    }

    public void imagePicker() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }


    public void clickImage() {
        String[] cameraPermission = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (hasPermission(cameraPermission)) {
            imagePicker();
        } else {
            checkPermission(cameraPermission, REQUEST_CAMERA);
        }
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    public void openGallery() {
        String[] openGallery = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (hasPermission(openGallery)) {
            galleryIntent();
        } else {
            checkPermission(openGallery, SELECT_FILE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    imagePicker();
                }
                break;

            case SELECT_FILE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openGallery();
                }
                break;
        }
    }
}