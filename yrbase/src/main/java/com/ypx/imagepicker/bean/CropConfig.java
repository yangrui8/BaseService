package com.ypx.imagepicker.bean;

import android.graphics.Color;

import com.ypx.imagepicker.ImagePicker;

/**
 * Time: 2019/10/27 18:53
 * Author:ypx
 * Description: 单图剪裁配置类
 */
public class CropConfig extends BaseSelectConfig {
    //充满式剪裁
    public static final int STYLE_FILL = 1;
    //留白式剪裁
    public static final int STYLE_GAP = 2;
    private int cropRatioX = 1;
    private int cropRatioY = 1;
    private boolean isCircle = false;
    private int cropRectMargin = 0;
    private String cropSaveFilePath = ImagePicker.cropPicSaveFilePath;
    private int cropStyle = STYLE_FILL;
    private int cropGapBackgroundColor = Color.BLACK;

    public int getCropStyle() {
        return cropStyle;
    }

    public void setCropStyle(int cropStyle) {
        this.cropStyle = cropStyle;
    }

    public int getCropGapBackgroundColor() {
        return cropGapBackgroundColor;
    }

    public void setCropGapBackgroundColor(int cropGapBackgroundColor) {
        this.cropGapBackgroundColor = cropGapBackgroundColor;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }


    public int getCropRectMargin() {
        return cropRectMargin;
    }

    public void setCropRectMargin(int cropRectMargin) {
        this.cropRectMargin = cropRectMargin;
    }

    public String getCropSaveFilePath() {
        return cropSaveFilePath;
    }

    public void setCropSaveFilePath(String cropSaveFilePath) {
        this.cropSaveFilePath = cropSaveFilePath;
    }

    public int getCropRatioX() {
        return cropRatioX;
    }

    public void setCropRatio(int x, int y) {
        this.cropRatioX = x;
        this.cropRatioY = y;
    }

    public int getCropRatioY() {
        return cropRatioY;
    }

    public boolean isGap() {
        return cropStyle == STYLE_GAP;
    }

    public boolean isNeedPng() {
        return isCircle || getCropGapBackgroundColor() == Color.TRANSPARENT;
    }
}
