package com.editsoft.ansh.mychat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.activity.BaseActivity;
import com.editsoft.ansh.mychat.adapter.UserChatListAdapter;
import com.editsoft.ansh.mychat.model.ContactInfo;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatList extends Fragment {
    private RecyclerView rvChatList;
    private DatabaseReference reference;
    private DatabaseReference userChatList;
    private ArrayList<ContactInfo> infos = new ArrayList<>();
    private UserChatListAdapter listAdapter;


    public ChatList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        initView(view);
        initFireBase();
        getContactList();
        initListener();
        return view;
    }

    private void initListener() {
        infos = new ArrayList<>();
        ((BaseActivity) getActivity()).showLoadingDialog();
        userChatList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((BaseActivity) getActivity()).hideLoadingDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    infos.add(snapshot.getValue(ContactInfo.class));
                }
                listAdapter.setInfos(infos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getContactList() {
    }

    private void initFireBase() {
        reference = FirebaseDatabase.getInstance().getReference();
        userChatList = reference.child(ConstantKey.MY_CHAT).child(ConstantKey.TEMP_CONTACT_LIST).child(String.valueOf(PreferenceHelper.getMobileNo()));
    }

    private void initView(View view) {
        rvChatList = (RecyclerView) view.findViewById(R.id.rv_chat_list);
        rvChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new UserChatListAdapter(getContext(), infos);
        rvChatList.setAdapter(listAdapter);
    }

}
