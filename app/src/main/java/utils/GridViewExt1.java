package utils;

/*
 * Copyright (C) 2019 Dennis Lang (landenlabs@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.landenlabs.all_colormatrix.R;

/**
 * Custom GridView which automatically provides dividers.
 * @noinspection ConstantValue
 */
public class GridViewExt1 extends GridView {

    final Paint paint = new Paint();

    private static final int BEG = 1;
    private static final int MID = 2;
    private static final int END = 4;

    // Vertical dividers
    private final int vDividers = MID;
    private float vDividerWidthPx = 1;
    private float vOffsetTopPx = 10;

    // Horizonatal dividers
    private float hDividerWidthPx = 1;

    public GridViewExt1(Context context) {
        super(context);
        init();
    }

    public GridViewExt1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GridViewExt1(Context context, AttributeSet attributes) {
        super(context, attributes);
        init();
    }

    private void init() {
        // TODO - get these properties from View attributes.
        paint.setColor(Color.WHITE);
        vDividerWidthPx = getResources().getDimension(R.dimen.grid_v_divider);
        hDividerWidthPx = getResources().getDimension(R.dimen.grid_h_divider);
        vOffsetTopPx = getResources().getDimension(R.dimen.grid_v_divider_pad_top);
    }

    // HACK - to fix wrap_content on height.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        final int columns = getNumColumns();
        final int count = getChildCount();
        for (int idx = 0; idx < count; idx++) {
            View child = getChildAt(idx);
            int top = child.getTop();
            int bottom = child.getBottom();
            int left = child.getLeft();
            int right = child.getRight();

            paint.setStrokeWidth(hDividerWidthPx);
            int hOffset = 0;
            canvas.drawLine(left + hOffset, bottom, right - hOffset, bottom, paint);

            paint.setStrokeWidth(vDividerWidthPx);
            int col = idx % columns;
            if ((vDividers & BEG) == BEG && col == 0) {
                float vColumn = left - vDividerWidthPx/2;
                canvas.drawLine(vColumn, top+vOffsetTopPx, vColumn , bottom, paint);
            }
            if ((vDividers & MID) == MID) {
                float vColumn = right - vDividerWidthPx/2;
                if (col <  columns-1) {
                    canvas.drawLine(vColumn, top+vOffsetTopPx, vColumn , bottom, paint);
                }
            }
            if ((vDividers & END) == END && col == columns-1) {
                float vColumn = right - vDividerWidthPx/2;
                canvas.drawLine(vColumn, top+vOffsetTopPx, vColumn , bottom, paint);
            }
        }
        super.dispatchDraw(canvas);
    }
}
