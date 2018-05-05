package com.erbao.videoimg.interfaces;

import android.net.Uri;

public interface OnTrimVideoListener {

    void onStartTrim();

    void onFinishTrim(final Uri uri);

    void onCancel();
}
