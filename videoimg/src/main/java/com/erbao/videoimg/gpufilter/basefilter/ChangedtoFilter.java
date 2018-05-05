package com.erbao.videoimg.gpufilter.basefilter;

import android.opengl.GLES20;

import com.erbao.videoimg.gpufilter.helper.MagicFilterType;
import com.erbao.videoimg.utils.EasyGlUtils;


/**
 * Created by asus on 2018/3/13.
 */

public class ChangedtoFilter {
    private MagicFilterType[] types = new MagicFilterType[]{
            MagicFilterType.NONE,
            MagicFilterType.WARM,
            MagicFilterType.ANTIQUE,
            MagicFilterType.INKWELL,
            MagicFilterType.BRANNAN,
            MagicFilterType.N1977,
            MagicFilterType.FREUD,
            MagicFilterType.HEFE,
            MagicFilterType.HUDSON,
            MagicFilterType.NASHVILLE,
            MagicFilterType.COOL
    };


    private GPUImageFilter curFilter;
    private int width, height;
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[1];
    private int curIndex = 0;
    GPUImageFilter filters;


    public ChangedtoFilter() {
        initFilter();

    }

    private void initFilter() {
        curFilter = getFilter(getCurIndex());

    }

    private GPUImageFilter getFilter(GPUImageFilter index) {
        GPUImageFilter filter = filters;
//        GPUImageFilter filter = MagicFilterFactory.initFilters(types[index]);
        if (filter == null) {
            filter = new GPUImageFilter();
        }
        return filter;
    }

    public void init() {
        curFilter.init();
    }

    private GPUImageFilter getCurIndex() {
        return filters;
    }

    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glGenFramebuffers(1, fFrame, 0);
        EasyGlUtils.genTexturesWithParameter(1, fTexture, 0, GLES20.GL_RGBA, width, height);
        onFilterSizeChanged(width, height);
    }

    private void onFilterSizeChanged(int width, int height) {
        curFilter.onInputSizeChanged(width, height);
        curFilter.onDisplaySizeChanged(width, height);

    }

    public int getOutputTexture() {
        return fTexture[0];
    }

    public void onDrawFrame(int textureId) {
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        curFilter.onDrawFrame(textureId);

        GLES20.glViewport(0, 0, width, height);
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(0, 0, width, height);
        curFilter.onDrawFrame(textureId);
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);

        curFilter = getFilter(getCurIndex());
        curFilter.init();
        curFilter.onDisplaySizeChanged(width, height);
        curFilter.onInputSizeChanged(width, height);
        EasyGlUtils.unBindFrameBuffer();
    }

    public void destroy() {
        curFilter.destroy();
    }

    public void setGPUImageFilter(GPUImageFilter getFilter) {
        filters = getFilter;

    }

}
