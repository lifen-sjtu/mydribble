package com.example.jlwang.mydribble.view.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jlwang on 10/17/17.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int dimensionPixelSize;
    public SpaceItemDecoration(int dimensionPixelSize) {
        this.dimensionPixelSize = dimensionPixelSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = dimensionPixelSize;
        outRect.right = dimensionPixelSize;
        outRect.bottom = dimensionPixelSize;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = dimensionPixelSize;
        }
    }
}
