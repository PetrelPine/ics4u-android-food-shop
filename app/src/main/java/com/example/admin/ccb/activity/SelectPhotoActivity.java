package com.example.admin.ccb.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.admin.ccb.R;
import com.example.admin.ccb.adapter.SelectPhotoAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import www.ccb.com.common.base.BaseActivity;


public class SelectPhotoActivity extends BaseActivity {

    private SelectPhotoAdapter spAp;
    private List<LocalMedia> selectList;
    private RecyclerView rv;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_selece_photo;
    }

    @Override
    protected void initView() {
        UpTitle(null);
        rv = findViewById(R.id.rvSelectPhoto);
        rv.setLayoutManager(new GridLayoutManager(mContext, 3));
        spAp = new SelectPhotoAdapter(R.layout.item_pic);
        rv.setAdapter(spAp);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initList() {
        findViewById(R.id.tvSelectPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
    }

    private void selectPhoto() {
        // Here's an example: Don't write the API you don't need
        PictureSelector.create(SelectPhotoActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(9)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .previewVideo(true)
                .enablePreviewAudio(true)
                .isCamera(true)
                .imageFormat(PictureMimeType.PNG)
                .isZoomAnim(true)
                .sizeMultiplier(0.5f)
                .setOutputCameraPath("/CustomPath")
                .enableCrop(true)
                .compress(true)
               // .glideOverride()
               // .withAspectRatio(1,1)
                .hideBottomControls(true)
                .isGif(true)
               // .compressSavePath(getPath())
                .freeStyleCropEnabled(true)
                .circleDimmedLayer(false)
                .showCropFrame(true)
                .showCropGrid(false)
                .openClickSound(false)
                .selectionMedia(selectList)
                .previewEggs(false)
                .cropCompressQuality(90)
                .minimumCompressSize(100)
                .synOrAsy(true)
               // .cropWH()
                .rotateEnabled(true)
                .scaleEnabled(true)
               // .videoQuality()
                .videoMaxSecond(15)
                .videoMinSecond(10)
               // .recordVideoSecond()
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // Image selection results callback
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        Log.i("LocalMedia", selectList.get(i).getPath());
                    }
                    spAp.setNewData(selectList);
                    // For example, LocalMedia returns three paths
                    // 1.media.getPath(); For the original path
                    // 2.media.getCutPath(); IsCut () is the trimmed path, and you need to check media.iscut (). Whether it is true
                    // 3.media.getCompressPath(); If path isCompressed, determine media.iscompressed (). Whether it is true
                    // If clipped and compressed, take the compression path, because clipped first and then compressed
                    break;
            }
        }
    }

}
