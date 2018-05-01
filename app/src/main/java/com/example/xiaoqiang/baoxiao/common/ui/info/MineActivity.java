package com.example.xiaoqiang.baoxiao.common.ui.info;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaoqiang.baoxiao.R;
import com.example.xiaoqiang.baoxiao.common.base.MyBaseActivity;
import com.example.xiaoqiang.baoxiao.common.been.MyUser;
import com.example.xiaoqiang.baoxiao.common.controller.UpdataController;
import com.example.xiaoqiang.baoxiao.common.utils.StatusBarUtil;
import com.example.xiaoqiang.baoxiao.common.view.UpdataView;
import com.flyco.roundview.RoundTextView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineActivity extends MyBaseActivity implements UpdataView {
    private CircleImageView head;
    private RoundTextView roundTextView;
    private TextView nickname;

    private ArrayList<AlbumFile> mAlbumFiles;

    private UpdataController controller;

    private String url;

    MyUser user;

    @Override
    public Integer getViewId() {
        return R.layout.activity_mine;
    }

    @Override
    public void init() {
        user = BmobUser.getCurrentUser(MyUser.class);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.profile));

        controller = new UpdataController(this);

        head = findViewById(R.id.mine_head);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });

        roundTextView = findViewById(R.id.mine_edit_btn);
        roundTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineActivity.this, EditActivity.class));
            }
        });

        nickname = findViewById(R.id.mine_nickname);
        if (!TextUtils.isEmpty(user.getPhotoPath())) {
            Glide.with(this).load(user.getPhotoPath()).centerCrop().into(head);
        }
        if (!TextUtils.isEmpty(user.getNickName())) {
            nickname.setText(user.getNickName());
        }
    }

    void openAlbum() {

        Album.album(this)
                .multipleChoice()
                .selectCount(1)
                .camera(true)
                .columnCount(4)
                .checkedList(mAlbumFiles)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
//
                        uploadImg(result.get(0).getPath());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {

                    }
                })
                .start();

    }

    void uploadImg(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    MyUser user = BmobUser.getCurrentUser(MyUser.class);
                    user.setPhotoPath(bmobFile.getUrl());
                    url = bmobFile.getUrl();
                    controller.updataUser(user);
                } else {
                    Log.e("xiaoqiang", e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            user=BmobUser.getCurrentUser(MyUser.class);
            nickname.setText(user.getNickName());
        }
    }

    @Override
    public void onUpdataUserSuccess() {
        Glide.with(MineActivity.this).load(url).centerCrop().into(head);
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(Throwable throwable) {
        Log.e("xiaoqiang", throwable.getMessage());
    }
}
