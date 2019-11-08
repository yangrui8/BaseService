package com.gcstorage.reportservice;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gcstorage.reportservice.style.CustomImgPickerPresenter;
import com.gcstorage.reportservice.style.RedBookCropPresenter;
import com.gcstorage.reportservice.style.WXImgPickerPresenter;
import com.yrbase.adapter.CommonAdapter;
import com.yrbase.adapter.base.ViewHolder;
import com.yrbase.baseactivity.BaseActivity;
import com.yrbase.imagepicker.ImagePicker;
import com.yrbase.imagepicker.bean.CropConfig;
import com.yrbase.imagepicker.bean.ImageItem;
import com.yrbase.imagepicker.bean.MimeType;
import com.yrbase.imagepicker.bean.PickerError;
import com.yrbase.imagepicker.builder.MultiPickerBuilder;
import com.yrbase.imagepicker.data.OnImagePickCompleteListener;
import com.yrbase.imagepicker.data.OnImagePickCompleteListener2;
import com.yrbase.imagepicker.presenter.IMultiPickerBindPresenter;
import com.yrbase.luban.CompressionPredicate;
import com.yrbase.luban.Luban;
import com.yrbase.luban.OnCompressListener;
import com.yrbase.luban.OnRenameListener;
import com.yrbase.response.RetrofitServiceManager;
import com.yrbase.utils.LogUtil;
import com.yrbase.utils.OnPerfectClickListener;
import com.yrbase.utils.ViewUtil;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImagePickerActivity extends BaseActivity<TestPresenter.Presenter> implements TestPresenter.View {
    private ArrayList<ImageItem> picList = new ArrayList<>();
    private RadioButton mRbRedBook;
    private RadioButton mRbWeChat;
    private RadioButton mRbCustom;
    private RadioGroup mRgStyle;
    private CheckBox mCbJPEG;
    private CheckBox mCbPNG;
    private CheckBox mCbGIF;
    private CheckBox mCbBMP;
    private CheckBox mCbWEBP;
    private CheckBox mCbMPEG;
    private CheckBox mCbMP4;
    private CheckBox mCbAVI;
    private CheckBox mCbMKV;
    private CheckBox mCbWEBM;
    private CheckBox mCbTS;
    private CheckBox mCbQUICKTIME;
    private CheckBox mCbTHREEGPP;
    private CheckBox mCbShowCamera;
    private CheckBox mCbClosePreview;
    private CheckBox mCbPreviewCanEdit;
    private CheckBox mCbVideoSingle;
    private CheckBox mCbImageOrVideo;
    private RadioButton mRbNew;
    private RadioButton mRbShield;
    private RadioButton mRbSave;
    private RadioButton mRbMulti;
    private RadioButton mRbCrop;
    private RadioButton mRbTakePhoto;
    private RadioButton mRbTakePhotoAndCrop;
    private RadioButton mRbSingle;
    private CheckBox mCbCircle;
    private TextView mCropX;
    private SeekBar mXSeekBar;
    private TextView mCropY;
    private SeekBar mYSeekBar;
    private TextView mCropMargin;
    private SeekBar mMarginSeekBar;
    private LinearLayout mCropSetLayout;
    private RadioGroup mRgNextPickType;
    private RadioGroup mRgOpenType;
    private RadioButton mRbAutoCrop;
    private RadioGroup mRgOpenType2;
    private GridLayout mGridLayout;
    private CheckBox mCbGap;
    private CheckBox mCbGapBackground;

    private int maxCount = 9;

    private RecyclerView recyclerView;
    private CommonAdapter<File> fileCommonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitServiceManager.ENTERPORT = "http://120.79.194.253:8768";

        getToolBarX().setCenterText("多文件上传").setRightText("发布朋友圈").setRightTextOnClickListener(new OnPerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //先压缩

                //picList


                upLoadPicture(picList);


            }


        });


        initView();
        picList.clear();
        refreshGridLayout();
    }

    @Override
    public void initPresenter() {
        mPresenter.setView(this);
    }

    private void initView() {
        mRbRedBook = findViewById(R.id.rb_redBook);
        mRbWeChat = findViewById(R.id.rb_weChat);
        mRbCustom = findViewById(R.id.rb_custom);
        mRgStyle = findViewById(R.id.rg_style);
        mCbJPEG = findViewById(R.id.cb_JPEG);
        mCbPNG = findViewById(R.id.cb_PNG);
        mCbGIF = findViewById(R.id.cb_GIF);
        mCbBMP = findViewById(R.id.cb_BMP);
        mCbWEBP = findViewById(R.id.cb_WEBP);
        mCbMPEG = findViewById(R.id.cb_MPEG);
        mCbMP4 = findViewById(R.id.cb_MP4);
        mCbAVI = findViewById(R.id.cb_AVI);
        mCbMKV = findViewById(R.id.cb_MKV);
        mCbWEBM = findViewById(R.id.cb_WEBM);
        mCbTS = findViewById(R.id.cb_TS);
        mCbQUICKTIME = findViewById(R.id.cb_QUICKTIME);
        mCbTHREEGPP = findViewById(R.id.cb_THREEGPP);
        mCbShowCamera = findViewById(R.id.cb_showCamera);
        mCbClosePreview = findViewById(R.id.cb_closePreview);
        mCbPreviewCanEdit = findViewById(R.id.cb_previewCanEdit);
        mCbVideoSingle = findViewById(R.id.cb_videoSingle);
        mCbImageOrVideo = findViewById(R.id.cb_imageOrVideo);
        mRbNew = findViewById(R.id.rb_new);
        mRbShield = findViewById(R.id.rb_shield);
        mRbSave = findViewById(R.id.rb_save);
        mRbMulti = findViewById(R.id.rb_multi);
        mRbCrop = findViewById(R.id.rb_crop);
        mRbSingle = findViewById(R.id.rb_single);
        mRbTakePhoto = findViewById(R.id.rb_takePhoto);
        mRbTakePhotoAndCrop = findViewById(R.id.rb_takePhotoAndCrop);
        mCbCircle = findViewById(R.id.cb_circle);
        mCropX = findViewById(R.id.mCropX);
        mXSeekBar = findViewById(R.id.mXSeekBar);
        mCropY = findViewById(R.id.mCropY);
        mYSeekBar = findViewById(R.id.mYSeekBar);
        mCropMargin = findViewById(R.id.mCropMargin);
        mMarginSeekBar = findViewById(R.id.mMarginSeekBar);
        mCropSetLayout = findViewById(R.id.mCropSetLayout);
        mGridLayout = findViewById(R.id.gridLayout);
        mRgNextPickType = findViewById(R.id.rg_nextPickType);
        mRgOpenType = findViewById(R.id.rg_openType);
        mRbAutoCrop = findViewById(R.id.rb_autoCrop);
        mRgOpenType2 = findViewById(R.id.rg_openType2);
        mRgStyle.setOnCheckedChangeListener(listener);
        mRbMulti.setOnCheckedChangeListener(listener4);
        mRbCrop.setOnCheckedChangeListener(listener4);
        mRbAutoCrop.setOnCheckedChangeListener(listener4);
        mRbTakePhoto.setOnCheckedChangeListener(listener4);
        mRbTakePhotoAndCrop.setOnCheckedChangeListener(listener4);
        mRbSingle.setOnCheckedChangeListener(listener4);
        mXSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        mYSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        mMarginSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        mCbGap = findViewById(R.id.cb_gap);
        mCbGapBackground = findViewById(R.id.cb_gapBackground);

        recyclerView = findViewById(R.id.recycler_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileCommonAdapter = new CommonAdapter<File>(this, R.layout.item_file_image, null) {
            @Override
            protected void convert(ViewHolder holder, File file, int position) {
                Glide.with(ImagePickerActivity.this).load(file).into((ImageView) holder.getView(R.id.image));
            }
        };
        recyclerView.setAdapter(fileCommonAdapter);

    }

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == mRbRedBook.getId()) {
                mCbClosePreview.setEnabled(false);
                mCbImageOrVideo.setEnabled(false);
                mRbNew.setEnabled(false);
                mRbShield.setEnabled(false);
                mRbSave.setEnabled(false);
                mRbCrop.setEnabled(false);
                mCbVideoSingle.setEnabled(true);
                mRbMulti.setChecked(true);
            } else if (checkedId == mRbWeChat.getId()) {
                mCbClosePreview.setEnabled(true);
                mCbImageOrVideo.setEnabled(true);
                mRgNextPickType.setEnabled(true);
                mRbCrop.setEnabled(true);
                mCbVideoSingle.setEnabled(true);
                mRbMulti.setChecked(true);
                mRbNew.setEnabled(true);
                mRbShield.setEnabled(true);
                mRbSave.setEnabled(true);
            } else if (checkedId == mRbCustom.getId()) {
                mCbClosePreview.setEnabled(false);
                mCbVideoSingle.setEnabled(false);
                mCbImageOrVideo.setEnabled(true);
                mRgNextPickType.setEnabled(true);
                mRbCrop.setEnabled(true);
                mRbMulti.setChecked(true);
                mRbNew.setEnabled(true);
                mRbShield.setEnabled(true);
                mRbSave.setEnabled(true);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener listener4 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (buttonView.getParent() == mRgOpenType) {
                    mRgOpenType2.clearCheck();
                } else {
                    mRgOpenType.clearCheck();
                }

                if (buttonView == mRbTakePhotoAndCrop || buttonView == mRbAutoCrop || buttonView == mRbCrop) {
                    mCropSetLayout.setVisibility(View.VISIBLE);
                } else {
                    mCropSetLayout.setVisibility(View.GONE);
                }
            }
        }
    };

    @SuppressLint("DefaultLocale")
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar == mXSeekBar) {
                mCropX.setText(String.format("cropX: %d", progress));
            } else if (seekBar == mYSeekBar) {
                mCropY.setText(String.format("cropY: %d", progress));
            } else if (seekBar == mMarginSeekBar) {
                mCropMargin.setText(String.format("剪裁框间距: %d", progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void redBookPick(int count) {
        ImagePicker.withCrop(new RedBookCropPresenter())//设置presenter
                .setMaxCount(count)//设置选择数量
                .showCamera(mCbShowCamera.isChecked())//设置显示拍照
                .setColumnCount(4)//设置列数
                .mimeType(getMimeTypes())//设置需要加载的文件类型
                // .filterMimeType(MimeType.GIF)//设置需要过滤掉的文件类型
                .setFirstImageItem(picList.size() > 0 ? picList.get(0) : null)//设置上一次选中的图片
                // .setFirstImageUrl(null)//设置上一次选中的图片地址
                .setVideoSinglePick(mCbVideoSingle.isChecked())//设置视频单选
                .setCropPicSaveFilePath(ImagePicker.cropPicSaveFilePath)
                .setMaxVideoDuration(120000L)//设置可选区的最大视频时长
                .setMinVideoDuration(5000L)
                .pick(this, new OnImagePickCompleteListener2() {
                    @Override
                    public void onPickFailed(PickerError error) {
                        ViewUtil.Toast(error.getMessage());
                    }

                    @Override
                    public void onImagePickComplete(ArrayList<ImageItem> items) {
                        //图片剪裁回调，主线程
                        //注意：剪裁回调里的ImageItem中getCropUrl()才是剪裁过后的图片地址
                        for (ImageItem item : items) {
                            if (item.getCropUrl() != null && item.getCropUrl().length() > 0) {
                                item.path = item.getCropUrl();
                            }
                        }
                        picList.addAll(items);
                        refreshGridLayout();
                    }
                });
    }

    private void pick(int count) {
        IMultiPickerBindPresenter presenter = mRbWeChat.isChecked() ? new WXImgPickerPresenter() : new CustomImgPickerPresenter();
        ImagePicker.withMulti(presenter)//指定presenter
                .setMaxCount(count)//设置选择的最大数
                .setColumnCount(4)//设置列数
                .mimeType(getMimeTypes())//设置要加载的文件类型，可指定单一类型
                // .filterMimeType(MimeType.GIF)//设置需要过滤掉加载的文件类型
                .showCamera(mCbShowCamera.isChecked())//显示拍照
                .setPreview(!mCbClosePreview.isChecked())//是否开启预览
                .setVideoSinglePick(mCbVideoSingle.isChecked())//设置视频单选
                .setSinglePickImageOrVideoType(mCbImageOrVideo.isChecked())//设置图片和视频单一类型选择
                .setMaxVideoDuration(120000L)//设置视频可选取的最大时长
                .setMinVideoDuration(5000L)
                .setLastImageList(mRbSave.isChecked() ? picList : null)//设置上一次操作的图片列表，下次选择时默认恢复上一次选择的状态
                .setShieldList(mRbShield.isChecked() ? picList : null)//设置需要屏蔽掉的图片列表，下次选择时已屏蔽的文件不可选择
                .pick(this, new OnImagePickCompleteListener() {
                    @Override
                    public void onImagePickComplete(ArrayList<ImageItem> items) {
                        //图片选择回调，主线程
                        if (mRbSave.isChecked()) {
                            picList.clear();
                        }
                        picList.addAll(items);
                        refreshGridLayout();
                    }
                });
    }

    private void preview(int pos) {
        IMultiPickerBindPresenter presenter = mRbWeChat.isChecked() ? new WXImgPickerPresenter() : new CustomImgPickerPresenter();
        if (mCbPreviewCanEdit.isChecked()) {
            //开启编辑预览
            ImagePicker.preview(this, presenter, picList, pos, new OnImagePickCompleteListener() {
                @Override
                public void onImagePickComplete(ArrayList<ImageItem> items) {
                    //图片编辑回调，主线程
                    picList.clear();
                    picList.addAll(items);
                    refreshGridLayout();
                }
            });
        } else {
            //开启普通预览
            ImagePicker.preview(this, presenter, picList, pos, null);
        }
    }

    private void crop() {
        IMultiPickerBindPresenter presenter = mRbWeChat.isChecked() ? new WXImgPickerPresenter() : new CustomImgPickerPresenter();
        MultiPickerBuilder builder = ImagePicker.withMulti(presenter)//指定presenter
                .setColumnCount(4)//设置列数
                .mimeType(getMimeTypes())//设置要加载的文件类型，可指定单一类型
                // .filterMimeType(MimeType.GIF)//设置需要过滤掉加载的文件类型
                .showCamera(mCbShowCamera.isChecked())//显示拍照
                .cropRectMinMargin(mMarginSeekBar.getProgress())
                .cropStyle(mCbGap.isChecked() ? CropConfig.STYLE_GAP : CropConfig.STYLE_FILL)
                .cropGapBackgroundColor(mCbGapBackground.isChecked() ? Color.TRANSPARENT : Color.RED)
                .cropSaveFilePath(ImagePicker.cropPicSaveFilePath)
                .setCropRatio(mXSeekBar.getProgress(), mYSeekBar.getProgress());
        if (mCbCircle.isChecked()) {
            builder.cropAsCircle();
        }
        builder.crop(this, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                //图片选择回调，主线程
                picList.addAll(items);
                refreshGridLayout();
            }
        });
    }

    private void takePhoto() {
        ImagePicker.takePhoto(this, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                picList.addAll(items);
                refreshGridLayout();
            }
        });
    }

    private void takePhotoAndCrop() {
        //配置剪裁属性
        CropConfig cropConfig = new CropConfig();
        cropConfig.setCropRatio(mXSeekBar.getProgress(), mYSeekBar.getProgress());//设置剪裁比例
        cropConfig.setCropRectMargin(mMarginSeekBar.getProgress());//设置剪裁框间距，单位px
        cropConfig.setCropSaveFilePath(ImagePicker.cropPicSaveFilePath);
        cropConfig.setCircle(mCbCircle.isChecked());//是否圆形剪裁
        cropConfig.setCropStyle(mCbGap.isChecked() ? CropConfig.STYLE_GAP : CropConfig.STYLE_FILL);
        cropConfig.setCropGapBackgroundColor(mCbGapBackground.isChecked() ? Color.TRANSPARENT : Color.RED);
        ImagePicker.takePhotoAndCrop(this, new WXImgPickerPresenter(), cropConfig, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                //剪裁回调，主线程
                picList.addAll(items);
                refreshGridLayout();
            }
        });
    }

    private void autoCrop() {
        if (picList.size() == 0) {
            Toast.makeText(this, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        //配置剪裁属性
        CropConfig cropConfig = new CropConfig();
        cropConfig.setCropRatio(mXSeekBar.getProgress(), mYSeekBar.getProgress());//设置剪裁比例
        cropConfig.setCropRectMargin(mMarginSeekBar.getProgress());//设置剪裁框间距，单位px
        cropConfig.setCropSaveFilePath(ImagePicker.cropPicSaveFilePath);
        cropConfig.setCircle(mCbCircle.isChecked());//是否圆形剪裁
        cropConfig.setCropStyle(mCbGap.isChecked() ? CropConfig.STYLE_GAP : CropConfig.STYLE_FILL);
        cropConfig.setCropGapBackgroundColor(mCbGapBackground.isChecked() ? Color.TRANSPARENT : Color.RED);
        ImagePicker.crop(this, new WXImgPickerPresenter(), cropConfig, picList.get(0).path, new OnImagePickCompleteListener2() {
            @Override
            public void onPickFailed(PickerError error) {
               ViewUtil.Toast(error.getMessage());
            }

            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                //剪裁回调，主线程
                picList.addAll(items);
                refreshGridLayout();
            }
        });
    }

    private void startPick() {
//        ImagePicker.provideMediaSets(this, MimeType.ofAll(), new MediaSetsDataSource.MediaSetProvider() {
//            @Override
//            public void providerMediaSets(ArrayList<ImageSet> imageSets) {
//                Log.e("startPick", "providerMediaSets: " + imageSets.size());
//            }
//        });

//        ImagePicker.provideAllMediaItems(this, getMimeTypes(), new MediaItemsDataSource.MediaItemProvider() {
//            @Override
//            public void providerMediaItems(ArrayList<ImageItem> imageItems, ImageSet allVideoSet) {
//                Log.e("startPick", "providerMediaSets: " + imageItems.size());
//            }
//        });

        if (mRbCrop.isChecked()) {
            crop();
        } else if (mRbTakePhoto.isChecked()) {
            takePhoto();
        } else if (mRbAutoCrop.isChecked()) {
            autoCrop();
        } else if (mRbTakePhotoAndCrop.isChecked()) {
            takePhotoAndCrop();
        } else if (mRbSingle.isChecked()) {
            if (mRbRedBook.isChecked()) {
                redBookPick(1);
            } else {
                pick(1);
            }
        } else if (mRbRedBook.isChecked()) {
            redBookPick(maxCount - picList.size());
        } else if (mRbWeChat.isChecked()) {
            pick(maxCount - picList.size());
        } else if (mRbCustom.isChecked()) {
            pick(maxCount - picList.size());
        }
    }


    /**
     * 刷新图片显示
     */
    private void refreshGridLayout() {
        mGridLayout.setVisibility(View.VISIBLE);
        mGridLayout.removeAllViews();
        int num = picList.size();
        final int picSize = (getScreenWidth() - dp(20)) / 4;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(picSize, picSize);
        if (num >= maxCount) {
            mGridLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < num; i++) {
                RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.a_layout_pic_select, null);
                view.setLayoutParams(params);
                view.setPadding(dp(5), dp(5), dp(5), dp(5));
                setPicItemClick(view, i);
                mGridLayout.addView(view);
            }
        } else {
            mGridLayout.setVisibility(View.VISIBLE);
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.add_pic));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(dp(5), dp(5), dp(5), dp(5));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPick();
                }
            });
            for (int i = 0; i < num; i++) {
                RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.a_layout_pic_select, null);
                view.setLayoutParams(params);
                view.setPadding(dp(5), dp(5), dp(5), dp(5));
                setPicItemClick(view, i);
                mGridLayout.addView(view);
            }
            mGridLayout.addView(imageView);
        }
    }

    public void setPicItemClick(RelativeLayout layout, final int pos) {
        ImageView iv_pic = (ImageView) layout.getChildAt(0);
        ImageView iv_close = (ImageView) layout.getChildAt(1);
        Glide.with(this).load(picList.get(pos).path).into(iv_pic);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picList.remove(pos);
                refreshGridLayout();
            }
        });
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview(pos);
            }
        });
    }

    public int dp(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, this.getResources().getDisplayMetrics());
    }

    /**
     * 获得屏幕宽度
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private Set<MimeType> getMimeTypes() {
        Set<MimeType> mimeTypes = new HashSet<>();
        if (mCbJPEG.isChecked()) {
            mimeTypes.add(MimeType.JPEG);
        }
        if (mCbPNG.isChecked()) {
            mimeTypes.add(MimeType.PNG);
        }
        if (mCbGIF.isChecked()) {
            mimeTypes.add(MimeType.GIF);
        }
        if (mCbBMP.isChecked()) {
            mimeTypes.add(MimeType.BMP);
        }
        if (mCbWEBP.isChecked()) {
            mimeTypes.add(MimeType.WEBP);
        }
        if (mCbMPEG.isChecked()) {
            mimeTypes.add(MimeType.MPEG);
        }
        if (mCbMP4.isChecked()) {
            mimeTypes.add(MimeType.MP4);
        }
        if (mCbAVI.isChecked()) {
            mimeTypes.add(MimeType.AVI);
        }
        if (mCbMKV.isChecked()) {
            mimeTypes.add(MimeType.MKV);
        }
        if (mCbWEBM.isChecked()) {
            mimeTypes.add(MimeType.WEBM);
        }
        if (mCbTS.isChecked()) {
            mimeTypes.add(MimeType.TS);
        }
        if (mCbQUICKTIME.isChecked()) {
            mimeTypes.add(MimeType.QUICKTIME);
        }
        if (mCbTHREEGPP.isChecked()) {
            mimeTypes.add(MimeType.THREEGPP);
        }
        return mimeTypes;
    }


    /*************/


    private void upLoadPicture(ArrayList<ImageItem> picList) {
        if (ViewUtil.isListEmpty(picList)) {
            ViewUtil.Toast("请先选中图片");
            return;
        }

        List<File> Files = new ArrayList<>();

        for (ImageItem imageItem : picList) {
            Files.add(new File(imageItem.getPath()));
        }


        withLs(Files);

    }

    private <T> void withLs(final List<T> photos) {
        Luban.with(this)
                .load(photos)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .setFocusAlpha(false)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        try {
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(filePath.getBytes());
                            return new BigInteger(1, md.digest()).toString(32);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onProgress(File file, int position) {
                        //
                    }

                    @Override
                    public void onSuccessAll(List file) {
                        fileCommonAdapter.addAll(file);
                        mPresenter.uoLoadImage(file);
                    }


                    @Override
                    public void onError(Throwable e) {
                        ViewUtil.Toast("图片压缩失败");
                    }


                }).launch();
    }


    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    @Override
    public void onSaveSuccess(List<UrlBean> data) {

        ViewUtil.Toast("图片上传成功");

        LogUtil.yangRui().e(data.toString());

        fileCommonAdapter.clear();

    }

    @Override
    public void onError(String str) {

        ViewUtil.Toast("图片上传失败");
        LogUtil.yangRui().e(str);
        fileCommonAdapter.clear();
    }


    /*************/
    /*************/


}
