package com.editsoft.ansh.mychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.activity.ChatActivity;
import com.editsoft.ansh.mychat.model.ContactInfo;
import com.editsoft.ansh.mychat.utility.ConstantKey;

import java.util.ArrayList;

/**
 * Created by SurvivoR on 7/10/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context mContext;
    private ArrayList<ContactInfo> contactInfos = new ArrayList<>();

    public ContactAdapter(Context mContext, ArrayList<ContactInfo> contactInfos) {
        this.mContext = mContext;
        this.contactInfos = contactInfos;
    }

    public void addContactInfos(ContactInfo contactInfos) {
        this.contactInfos.add(contactInfos);
        notifyDataSetChanged();
    }

    public void setContactInfos(ArrayList<ContactInfo> contactInfos) {
        this.contactInfos.clear();
        this.contactInfos = contactInfos;
        notifyDataSetChanged();
    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, int position) {
        final ContactInfo contactInfo = contactInfos.get(position);
        holder.tvName.setText(contactInfo.getName());
        holder.tvMobileNo.setText(contactInfo.getMobileNo());
        holder.llContactRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(ConstantKey.OTHER_USER_INFO, contactInfo);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return contactInfos.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvMobileNo;
        private LinearLayout llContactRow;

        private ContactViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvMobileNo = (TextView) itemView.findViewById(R.id.tv_mobile_no);
            llContactRow = (LinearLayout) itemView.findViewById(R.id.ll_contact_row);
        }
    }
}
