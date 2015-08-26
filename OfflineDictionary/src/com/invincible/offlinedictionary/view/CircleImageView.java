package com.invincible.offlinedictionary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.invincible.offlinedictionary.R;

public class CircleImageView extends ImageView {

	private int		mRadius;
	private Paint	mPaint;

	public CircleImageView(Context context) {
		super(context);
		init();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("NewApi")
	public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	/**
	 * Method to init
	 */
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setColor(getResources().getColor(R.color.action_bar_back));
		mPaint.setAlpha(150);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w > h) {
			mRadius = (int) ((float) h * 0.35F);
		} else {
			mRadius = (int) ((float) w * 0.35F);
		}
		int padSub = (int) ((float) mRadius * 0.7F);
		int yP = (h >> 1) - padSub;
		int xP = (w >> 1) - padSub;
		setPadding(xP, yP, xP, yP);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mRadius, mPaint);
		super.onDraw(canvas);
	}
}
