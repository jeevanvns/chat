package com.editsoft.ansh.mychat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.activity.BaseActivity;
import com.editsoft.ansh.mychat.adapter.ContactAdapter;
import com.editsoft.ansh.mychat.model.ContactInfo;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatContact extends Fragment {

    private String TAG = ChatContact.class.getSimpleName();
    private RecyclerView rvContactList;
    private Context mContext;
    private ArrayList<ContactInfo> contactInfos = new ArrayList<>();
    private DatabaseReference reference;
    private DatabaseReference userContactList;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_contact, container, false);
        mContext = inflater.getContext();
        initView(view);
        initFireBase();
        getContactList();
        initListener();
        return view;
    }

    private void initListener() {
        ((BaseActivity) getActivity()).showLoadingDialog();
        userContactList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((BaseActivity) getActivity()).hideLoadingDialog();
                contactInfos = new ArrayList<ContactInfo>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    contactInfos.add(snapshot.getValue(ContactInfo.class));
                }
                contactAdapter.setContactInfos(contactInfos);
                Log.e(TAG, "onDataChange:SIze is " + contactInfos.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initFireBase() {
        reference = FirebaseDatabase.getInstance().getReference();
        userContactList = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.USER_CONTACT_LIST).child(String.valueOf(PreferenceHelper.getMobileNo()));
    }

    private void getContactList() {

    }

    private void initView(View view) {
        rvContactList = (RecyclerView) view.findViewById(R.id.rv_contact);
        rvContactList.setLayoutManager(new LinearLayoutManager(getContext()));
        contactAdapter = new ContactAdapter(mContext, contactInfos);
        rvContactList.setAdapter(contactAdapter);
    }


}
