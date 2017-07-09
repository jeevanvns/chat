package com.editsoft.ansh.mychat.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.adapter.ChatAdapter;
import com.editsoft.ansh.mychat.callback.DefaultCallback;
import com.editsoft.ansh.mychat.model.ChatInfo;
import com.editsoft.ansh.mychat.model.ContactInfo;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.editsoft.ansh.mychat.utility.FileHelper;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class ChatActivity extends BaseActivity {
    private static final int SELECT_FILE = 100;
    private static final int WRITE_EXTERNAL_STORAGE = 101;
    private static final String TAG = ChatActivity.class.getSimpleName();
    private RecyclerView rvChat;
    private String chatId;
    private ContactInfo otherUserInfo;
    private ImageView ivSendButton;
    private EditText etMessage;
    private DatabaseReference reference;
    private DatabaseReference userChatRoom;
    private DatabaseReference userDataSource;
    private DatabaseReference tempContactList;
    private ArrayList<ChatInfo> chatInfos = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private File rawFile;
    private DefaultCallback defaultCallback;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    private void initFireBase() {
        otherUserInfo = getIntent().getParcelableExtra(ConstantKey.OTHER_USER_INFO);
        chatId = otherUserInfo.getChatId();
        reference = FirebaseDatabase.getInstance().getReference();
        userChatRoom = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.CHAT_ROOM).child(otherUserInfo.getChatId());
        userDataSource = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.USER_CONTACT_LIST);
        tempContactList = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.TEMP_CONTACT_LIST);


    }

    @Override
    public boolean showToolbar() {
        return true;
    }

    @Override
    protected void initListener() {
        userChatRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatInfos = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    chatInfos.add(snapshot.getValue(ChatInfo.class));
                }
                chatAdapter.setChatInfos(chatInfos);
                if (chatInfos.size() > 2) {
                    rvChat.smoothScrollToPosition(chatInfos.size() - 1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().length() != 0) {
                    sendMessage();
                }
            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ivSendButton.setEnabled(false);
//                mSendButton.setImageResource(R.drawable.ic_send);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMessage.getText().length() != 0) {
                    ivSendButton.setEnabled(true);
//                    mSendButton.setImageResource(R.drawable.ic_send);
                } else {
                    ivSendButton.setEnabled(false);
//                    mSendButton.setImageResource(R.drawable.ic_logo_white);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void bindDataWithUi() {

    }

    private void sendMessage() {
        Long tsLong = System.currentTimeMillis() / 1000;
        ChatInfo chatInfo = new ChatInfo(tsLong.toString(), PreferenceHelper.getName(), PreferenceHelper.getMobileNo(), etMessage.getText().toString(), ConstantKey.NormaMSG, "");
        userChatRoom.child(tsLong.toString()).setValue(chatInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    etMessage.setText("");
                    initUser();
                } else {
                    showToast("Error");
                }
            }
        });

    }


    private void sendImageMsg() {
        int permissionCamera = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA);
        int permissionGallery = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCamera != PackageManager.PERMISSION_GRANTED && permissionGallery != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        } else {
            galleryIntent();
        }
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void initUser() {
        userDataSource.child(otherUserInfo.getMobileNo()).child(chatId).setValue(new ContactInfo(chatId, PreferenceHelper.getName(), PreferenceHelper.getMobileNo()));
        userDataSource.child(PreferenceHelper.getMobileNo()).child(chatId).setValue(new ContactInfo(chatId, otherUserInfo.getName(), otherUserInfo.getMobileNo()));
        //for chat list
        tempContactList.child(otherUserInfo.getMobileNo()).child(chatId).setValue(new ContactInfo(chatId, PreferenceHelper.getName(), PreferenceHelper.getMobileNo()));
        tempContactList.child(PreferenceHelper.getMobileNo()).child(chatId).setValue(new ContactInfo(chatId, otherUserInfo.getName(), otherUserInfo.getMobileNo()));

    }

    @Override
    protected void initView() {
        initFireBase();
        rvChat = (RecyclerView) findViewById(R.id.rv_chat);
        rvChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        chatAdapter = new ChatAdapter(ChatActivity.this, chatInfos);
        rvChat.setAdapter(chatAdapter);
        etMessage = (EditText) findViewById(R.id.et_message);
        ivSendButton = (ImageView) findViewById(R.id.iv_send);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                rawFile = new File(FileHelper.getPath(ChatActivity.this, data.getData()));
                uploadImage(rawFile);
            }
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
                Long tsLong = System.currentTimeMillis() / 1000;
                ChatInfo chatInfo = new ChatInfo(tsLong.toString(), PreferenceHelper.getName(), PreferenceHelper.getMobileNo(), etMessage.getText().toString(), ConstantKey.ImageMSG, String.valueOf(downloadUrl));
                userChatRoom.child(tsLong.toString()).setValue(chatInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            etMessage.setText("");
                            initUser();
                        } else {
                            showToast("Error");
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_profile:
                sendImageMsg();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }
}
