package com.invincible.offlinedictionary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.EditText;

import com.invincible.offlinedictionary.R;

public class SearchEditText extends EditText {
	private Paint	mPaint;
	private int		mUnderlineHeight;
	private Path	mPath;

	public SearchEditText(Context context) {
		super(context);
		init();
	}

	public SearchEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * Init the members of text view
	 */
	private void init() {
		mUnderlineHeight = getResources().getDimensionPixelSize(R.dimen.tab_underline);
		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(android.graphics.Paint.Style.STROKE);
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(mUnderlineHeight >> 1);
		mPath = new Path();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int y = getHeight() - (mUnderlineHeight);
		int stroke = mUnderlineHeight >> 1;
		mPath.moveTo(0, y - (stroke << 1));
		mPath.lineTo(0 + (stroke << 1), y);
		mPath.lineTo(getWidth() - (stroke << 1), y);
		mPath.lineTo(getWidth(), y - (stroke << 1));
		canvas.drawPath(mPath, mPaint);
	}

}
