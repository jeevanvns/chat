package com.editsoft.ansh.mychat.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.utility.ConstantKey;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagePreview extends BaseActivity {
    private ImageView imageView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initView() {
        imageView = (ImageView) findViewById(R.id.activity_image_preview);
        String imageUrl = getIntent().getStringExtra(ConstantKey.ImageUrl);
        Picasso.with(ImagePreview.this)
                .load(imageUrl)
                .into(imageView);
        mAttacher = new PhotoViewAttacher(imageView);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void bindDataWithUi() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
