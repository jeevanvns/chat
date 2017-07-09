package com.editsoft.ansh.mychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.model.UserInfo;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends BaseActivity {
    private EditText etName;
    private EditText etMobileNo;
    private Button btnRegister;

    private DatabaseReference reference;
    private DatabaseReference userDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_up;
    }

    private void initFireBase() {
        reference = FirebaseDatabase.getInstance().getReference();
        userDataSource = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.USER_DETAILS);
    }

    @Override
    protected void initListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    showLoadingDialog();
                    userDataSource.child(etMobileNo.getText().toString()).setValue(new UserInfo(etName.getText().toString(), etMobileNo.getText().toString(), "")).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoadingDialog();
                            if (task.isComplete()) {
                                finish();
                                PreferenceHelper.setIsLogin(true);
                                PreferenceHelper.setMobileNo(etMobileNo.getText().toString());
                                PreferenceHelper.setName(etName.getText().toString());
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            } else {
                                showToast("Error on SignUp");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void bindDataWithUi() {
        initFireBase();

    }

    private boolean validate() {
        if (etMobileNo.getText().length() < 10) {
            showToast("Enter valid Mobile No");
            return false;
        } else if (etName.getText().length() < 2) {
            showToast("Enter valid Name");
        }
        return true;
    }

    @Override
    protected void initView() {
        etName = (EditText) findViewById(R.id.et_name);
        etMobileNo = (EditText) findViewById(R.id.et_mobile_no);
        btnRegister = (Button) findViewById(R.id.btn_register);
    }
}
