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

public class UserChatListAdapter extends RecyclerView.Adapter<UserChatListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ContactInfo> infos = new ArrayList<>();

    public UserChatListAdapter(Context mContext, ArrayList<ContactInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    public void setInfos(ArrayList<ContactInfo> infos) {
        this.infos.clear();
        this.infos = infos;
        notifyDataSetChanged();
    }

    public void putInfos(ContactInfo infos) {
        this.infos.add(infos);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ContactInfo contactInfo = infos.get(position);
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
        return infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvMobileNo;
        private LinearLayout llContactRow;

        private ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvMobileNo = (TextView) itemView.findViewById(R.id.tv_mobile_no);
            llContactRow = (LinearLayout) itemView.findViewById(R.id.ll_contact_row);
        }
    }
}
