package com.editsoft.ansh.mychat.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.model.UserInfo;
import com.editsoft.ansh.mychat.utility.CircleTransform;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends BaseActivity {

    private ImageView ivProfileImage;
    private DatabaseReference reference;
    private DatabaseReference userDataSource;
    private UserInfo userInfo;
    private TextView etName;
    private TextView tvMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_profile;
    }

    @Override
    protected void initView() {
        initFirebase();
        ivProfileImage = (ImageView) findViewById(R.id.iv_profile);
        etName = (TextView) findViewById(R.id.et_name);
        tvMobileNo = (TextView) findViewById(R.id.tv_mobile_no);

    }

    private void initFirebase() {
        reference = FirebaseDatabase.getInstance().getReference();
        userDataSource = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.USER_DETAILS).child(PreferenceHelper.getMobileNo());
    }

    @Override
    protected void initListener() {
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        userDataSource.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                userInfo = dataSnapshot.getValue(UserInfo.class);
                bindDataWithUi();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    protected void bindDataWithUi() {
        if (userInfo != null) {
            Picasso.with(UpdateProfile.this)
                    .load(userInfo.getImageUrl())
                    .placeholder(R.drawable.user_placeholder)
                    .transform(new CircleTransform())
                    .error(R.drawable.user_placeholder)
                    .into(ivProfileImage);

            etName.setText(userInfo.getName());
            tvMobileNo.setText(userInfo.getMobileNo());
        }
    }

    @Override
    protected void onGalleryResult(Uri uri) {
        userDataSource.setValue(new UserInfo(PreferenceHelper.getName(), PreferenceHelper.getMobileNo(), String.valueOf(uri))).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideLoadingDialog();
                if (task.isComplete()) {
                    showToast("Profile Pic Update");
                } else {
                    showToast("Error on SignUp");
                }
            }
        });
    }
}
