package com.lbconsulting.homework254_lorenbak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BoxView extends View {
	private Paint mFillPaint;
	private Paint mStrokePaint;
	private Bitmap mBoxRed;
	private Bitmap mBoxBlue;
	private Canvas mCanvas;

	public BoxView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initializeBoxView();
	}

	public BoxView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initializeBoxView();
	}

	public BoxView(Context context) {
		super(context);
		initializeBoxView();
	}

	private void initializeBoxView() {
		// TODO Auto-generated method stub
		MyLog.i("BoxView", "initializeBoxView");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		MyLog.i("BoxView", "onDraw");
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		MyLog.i("BoxView", "onSizeChanged");

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		MyLog.i("BoxView", "onTouchEvent");

		return super.onTouchEvent(event);
	}

}
