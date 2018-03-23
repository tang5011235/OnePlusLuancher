package test.oneplusluancher;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/3/20.
 */

public class Luangcher extends LinearLayout {

    private static final String TAG = "Luangcher";
    private ViewDragHelper mViewDragHelper;
    private int mHeight;
    private int mCenterHeight;
    private int mOrginalTop;
    private int mWidth;

    public Luangcher(Context context) {
        this(context, null);
    }

    public Luangcher(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mCenterHeight = mHeight / 2;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            ViewGroup.LayoutParams lp = childView.getLayoutParams();
            int selfWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int selfWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int selfHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int selfHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            switch (lp.width) {
                case ViewGroup.LayoutParams.MATCH_PARENT:
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWith, MeasureSpec.EXACTLY);
                    } else {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                case ViewGroup.LayoutParams.WRAP_CONTENT:
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWith, MeasureSpec.AT_MOST);
                    } else {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                default:
                    childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
                    break;
            }
            usedWith += lp.width;
        }*/

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View bottomChildeView = getChildAt(getChildCount() - 1);
        ViewGroup.LayoutParams lp = bottomChildeView.getLayoutParams();
        int childHeightSpec;
        switch (lp.height) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.AT_MOST);
                break;
            default:
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
                break;
        }
        bottomChildeView.measure(widthMeasureSpec, childHeightSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mOrginalTop = getChildAt(getChildCount() - 1).getTop();
    }

    private boolean canDrag = true;

    public Luangcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == getChildAt(getChildCount() - 1);
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }


            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.d(TAG, "onViewReleased: \n"
                        + "xvel: " + xvel
                        + "yvel" + yvel);
                int top = releasedChild.getTop();
                if (top < mCenterHeight) {
                    mViewDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
                } else {
                    mViewDragHelper.smoothSlideViewTo(releasedChild, 0, mOrginalTop);
                }
                invalidate();
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
              /*  Log.d(TAG, "clampViewPositionVertical: \n"
                        + "top:" + top
                        + "\n"
                        + "dy" + dy);*/
                return top > mOrginalTop ? mOrginalTop : top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return 0;
            }
        });
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    private float downx;
    private float downy;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float offsetx = 0;
        float offsety = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downx = ev.getX();
                downy = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetx = Math.abs(downx - ev.getX());
                offsety = Math.abs(downy - ev.getY());
                break;
        }

        if (offsetx < 20 && offsety < 20) {
            return false;
        } else {
            return mViewDragHelper.shouldInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
