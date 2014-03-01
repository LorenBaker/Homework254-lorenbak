package com.lbconsulting.homework254_lorenbak;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * A class to hold the reset button info
 * 
 * @author Loren
 * 
 */
public class ResetButton {

	private Paint mFillPaint_default = new Paint();
	private Paint mFillPaint_focused = new Paint();
	private Paint mFillPaint_pressed = new Paint();
	private Paint mStrokePaint = new Paint();
	private Paint mTextPaint = new Paint();

	private String btnText = "Reset";

	private float btnHeight = 0;
	private float btnWidth = 0;
	private float btnMargin = 0;
	private RectF mWorldRect;

	private RectF btnRect = new RectF();

	ResetButton(float height, float width, float margin, int defalultColor, int focusedColor, int pressedColor,
			float strokeSize, int strokeColor) {
		this.btnHeight = height;
		this.btnWidth = width;
		this.btnMargin = margin;

		getFillPaint_default().setStyle(Paint.Style.FILL);
		getFillPaint_default().setColor(defalultColor);

		getFillPaint_focused().setStyle(Paint.Style.FILL);
		getFillPaint_focused().setColor(focusedColor);

		getFillPaint_pressed().setStyle(Paint.Style.FILL);
		getFillPaint_pressed().setColor(pressedColor);

		getStrokePaint().setStyle(Paint.Style.STROKE);
		getStrokePaint().setColor(strokeColor);
		getStrokePaint().setStrokeWidth(strokeSize);

		getTextPaint().setStyle(Paint.Style.FILL);
		getTextPaint().setColor(Color.BLACK);
		getTextPaint().setAntiAlias(true);
		getTextPaint().setTextSize(50);
	}

	public String getText() {
		return btnText;
	}

	public RectF getResetButtonRect() {
		return btnRect;
	}

	public void setWorldRect(RectF worldRect) {
		this.mWorldRect = worldRect;

		float left = btnMargin;
		float top = mWorldRect.bottom - btnHeight - btnMargin;
		float right = btnWidth + btnMargin;
		float bottom = mWorldRect.bottom - btnMargin;

		btnRect.set(left, top, right, bottom);
	}

	public Paint getFillPaint_default() {
		return mFillPaint_default;
	}

	public Paint getFillPaint_focused() {
		return mFillPaint_focused;
	}

	public Paint getFillPaint_pressed() {
		return mFillPaint_pressed;
	}

	public Paint getStrokePaint() {
		return mStrokePaint;
	}

	public Paint getTextPaint() {
		return mTextPaint;
	}

	public boolean isResetButtonTouched(float x, float y) {
		return btnRect.contains(x, y);
		/*		boolean result = false;
				if (x >= btnRect.left && x <= btnRect.right) {
					if (y >= btnRect.top && y <= btnRect.bottom) {
						result = true;
					}
				}
				return result;*/
	}

	public float getTextStaryX() {
		return btnRect.left + btnHeight / 4;
	}

	public float getTextStartY() {
		return btnRect.bottom - btnHeight / 4;
	}

}
