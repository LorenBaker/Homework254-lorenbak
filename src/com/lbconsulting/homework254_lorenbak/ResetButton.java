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
	private float textMarginValue = (float) 0.15;

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
		float textWidth = width * (1 - textMarginValue);
		int textSize = determineMaxTextSize(btnText, textWidth);
		getTextPaint().setTextSize(textSize);
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

	/**
	 * Retrieve the maximum text size to fit in a given width.
	 * 
	 * @param str
	 *            (String): Text to check for size.
	 * @param maxWidth
	 *            (float): Maximum allowed width.
	 * @return (int): The desired text size.
	 */
	private int determineMaxTextSize(String str, float maxWidth)
	{
		int size = 0;
		Paint paint = new Paint();

		do {
			paint.setTextSize(++size);
		} while (paint.measureText(str) < maxWidth);

		return size;
	} //End getMaxTextSize()

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
	}

	public float getTextStartX() {
		float width = btnWidth * textMarginValue / 2;
		return btnRect.left + width;
	}

	public float getTextStartY() {
		return btnRect.bottom - btnHeight / 4;
	}

}
