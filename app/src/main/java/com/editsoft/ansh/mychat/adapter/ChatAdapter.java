package com.editsoft.ansh.mychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.activity.ImagePreview;
import com.editsoft.ansh.mychat.model.ChatInfo;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.editsoft.ansh.mychat.utility.PreferenceHelper;
import com.editsoft.ansh.mychat.utility.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SurvivoR on 7/10/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context mContext;
    private ArrayList<ChatInfo> chatInfos = new ArrayList<>();


    public ChatAdapter(Context mContext, ArrayList<ChatInfo> chatInfos) {
        this.mContext = mContext;
        this.chatInfos = chatInfos;
    }

    public void setChatInfos(ArrayList<ChatInfo> chatInfos) {
        this.chatInfos = new ArrayList<>();
        this.chatInfos = chatInfos;
        notifyDataSetChanged();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        final ChatInfo chatInfo = chatInfos.get(position);
        if (chatInfo.getCardType().equalsIgnoreCase(ConstantKey.ImageMSG)) {
            if (chatInfo.getMobileNo().equalsIgnoreCase(PreferenceHelper.getMobileNo())) {
                holder.ivRight.setVisibility(View.VISIBLE);
                holder.ivLeft.setVisibility(View.GONE);
                holder.tvLeft.setVisibility(View.GONE);
                holder.tvRight.setVisibility(View.GONE);
                Picasso.with(mContext)
                        .load(chatInfo.getImageUrl())
                        .into(holder.ivRight);
                holder.ivRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, ImagePreview.class).putExtra(ConstantKey.ImageUrl, chatInfo.getImageUrl()));
                    }
                });
            } else {
                holder.ivLeft.setVisibility(View.VISIBLE);
                holder.ivRight.setVisibility(View.GONE);
                holder.tvLeft.setVisibility(View.GONE);
                holder.tvRight.setVisibility(View.GONE);
                Picasso.with(mContext)
                        .load(chatInfo.getImageUrl())
                        .into(holder.ivLeft);

                holder.ivLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, ImagePreview.class).putExtra(ConstantKey.ImageUrl, chatInfo.getImageUrl()));
                    }
                });
            }
        } else {
            if (chatInfo.getMobileNo().equalsIgnoreCase(PreferenceHelper.getMobileNo())) {
                holder.tvRight.setVisibility(View.VISIBLE);
                holder.tvRight.setText(chatInfo.getMsg());
                holder.tvLeft.setVisibility(View.GONE);
                holder.ivRight.setVisibility(View.GONE);
                holder.ivLeft.setVisibility(View.GONE);
            } else {
                holder.tvLeft.setVisibility(View.VISIBLE);
                holder.tvLeft.setText(chatInfo.getMsg());
                holder.tvRight.setVisibility(View.GONE);
                holder.ivRight.setVisibility(View.GONE);
                holder.ivLeft.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatInfos.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLeft;
        private TextView tvRight;
        private ImageView ivLeft;
        private ImageView ivRight;

        private ChatViewHolder(View itemView) {
            super(itemView);
            tvLeft = (TextView) itemView.findViewById(R.id.tv_left);
            tvRight = (TextView) itemView.findViewById(R.id.tv_right);
            ivLeft = (ImageView) itemView.findViewById(R.id.iv_left);
            ivRight = (ImageView) itemView.findViewById(R.id.iv_right);
        }
    }
}
