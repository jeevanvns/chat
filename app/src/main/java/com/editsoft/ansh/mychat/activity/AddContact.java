package com.editsoft.ansh.mychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.model.ContactInfo;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContact extends BaseActivity {

    private EditText etName;
    private EditText etMobileNo;
    private Button btnAddContact;
    private DatabaseReference reference;
    private DatabaseReference userContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_contact;
    }

    @Override
    public boolean showToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        etName = (EditText) findViewById(R.id.et_name);
        etMobileNo = (EditText) findViewById(R.id.et_mobile_no);
        btnAddContact = (Button) findViewById(R.id.btn_add_user);
    }

    @Override
    protected void initListener() {
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    showLoadingDialog();
                    String chatId = "";
                    int i = PreferenceHelper.getMobileNo().compareTo(etMobileNo.getText().toString());
                    if (i < 0) {
                        chatId = PreferenceHelper.getMobileNo() + etMobileNo.getText().toString();
                    } else {
                        chatId = etMobileNo.getText().toString() + PreferenceHelper.getMobileNo();
                    }
                    userContactList.child(chatId).setValue(new ContactInfo(chatId, etName.getText().toString(), etMobileNo.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoadingDialog();
                            if (task.isComplete()) {
                                finish();
                                startActivity(new Intent(AddContact.this, MainActivity.class));
                            } else {
                                showToast("Error on Add Contact");
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

    private void initFireBase() {
        reference = FirebaseDatabase.getInstance().getReference();
        userContactList = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.USER_CONTACT_LIST).child(String.valueOf(PreferenceHelper.getMobileNo()));
    }


    @Override
    public String setToolbarName() {
        return getResources().getString(R.string.text_toolbar_add_contact);
    }
}
