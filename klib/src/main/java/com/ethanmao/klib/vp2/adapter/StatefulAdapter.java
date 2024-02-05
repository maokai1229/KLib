package com.ethanmao.klib.vp2.adapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;

public interface StatefulAdapter {
    /** Saves adapter state */
    @NonNull
    Parcelable saveState();

    /** Restores adapter state */
    void restoreState(@NonNull Parcelable savedState);
}