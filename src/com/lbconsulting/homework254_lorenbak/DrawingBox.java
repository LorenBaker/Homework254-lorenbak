package com.lbconsulting.homework254_lorenbak;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * A class to hold box info
 * 
 * @author Loren
 * 
 */
public class DrawingBox {
	private Paint mFillPaint = new Paint();
	private Paint mStrokePaint = new Paint();
	private RectF mBoxRect;
	private RectF mBoxStrokeRect;
	private RectF mPrevBoxRect;
	private float mBoxSize;
	private float mHalfBoxSize;
	private RectF mWorldRect = new RectF();
	private float mHalfStrokeSize;

	public DrawingBox(float pxBoxSize, int fillColor, int strokeColor, float pxStrokeSize) {
		setBoxSize(pxBoxSize);

		getFillPaint().setStyle(Paint.Style.FILL);
		getFillPaint().setColor(fillColor);

		getStrokePaint().setStyle(Paint.Style.STROKE);
		getStrokePaint().setColor(strokeColor);
		getStrokePaint().setStrokeWidth(pxStrokeSize);

		mPrevBoxRect = new RectF();
		mPrevBoxRect.left = -9999;

		mHalfStrokeSize = pxStrokeSize / 2;
	}

	public RectF getBoxRect() {
		return mBoxRect;
	}

	public void setBoxRect(float x, float y) {
		this.mBoxRect = populateBoxRect(x, y);
	}

	public Paint getFillPaint() {
		return mFillPaint;
	}

	public Paint getStrokePaint() {
		return mStrokePaint;
	}

	public RectF getPrevBoxRect() {
		return mPrevBoxRect;
	}

	public void setPrevBoxRect(float x, float y) {
		this.mPrevBoxRect = populateBoxRect(x, y);
	}

	public float getBoxSize() {
		return mBoxSize;
	}

	public void setBoxSize(float pxBoxSize) {
		this.mBoxSize = pxBoxSize;
		this.mHalfBoxSize = pxBoxSize / 2;
	}

	public void setWorldRect(RectF worldRect) {
		mWorldRect = worldRect;
	}

	/**
	 * 
	 * @param x
	 *            = horizontal box center
	 * @param y
	 *            = vertical box center
	 * @return the box rectangle
	 */
	private RectF populateBoxRect(float x, float y) {

		RectF resultRect = new RectF();

		float left = x - mHalfBoxSize;
		float top = y - mHalfBoxSize;
		float right = x + mHalfBoxSize;
		float bottom = y + mHalfBoxSize;

		// check if the box is outside of it's world ...
		// if so, restrict box to the world's boundaries
		if (left < mWorldRect.left) {
			left = mWorldRect.left;
			right = mWorldRect.left + mBoxSize;
		}
		if (top < mWorldRect.top) {
			top = mWorldRect.top;
			bottom = mWorldRect.top + mBoxSize;
		}

		if (right > mWorldRect.right) {
			right = mWorldRect.right;
			left = mWorldRect.right - mBoxSize;
		}

		if (bottom > mWorldRect.bottom) {
			bottom = mWorldRect.bottom;
			top = mWorldRect.bottom - mBoxSize;
		}

		resultRect.set(left, top, right, bottom);
		return resultRect;
	}

	/**
	 * 
	 * @param x
	 *            = touch horizontal location
	 * @param y
	 *            = touch vertical location
	 * @return true if x,y is contained within the box rectangle
	 */
	public boolean isMoving(float x, float y) {
		return mBoxRect.contains(x, y);
		/*		
				boolean result = false;
				if (x >= mBoxRect.left && x <= mBoxRect.right) {
					if (y >= mBoxRect.top && y <= mBoxRect.bottom) {
						result = true;
					}
				}
				return result;*/
	}

	public float getBoxCenterX() {
		return mBoxRect.centerX();
	}

	public float getBoxCenterY() {
		return mBoxRect.centerY();
	}

	public RectF getBoxStrokeRect() {
		mBoxStrokeRect = new RectF();
		// box stroke is located inside the box's boundaries
		float left = mBoxRect.left + mHalfStrokeSize;
		float top = mBoxRect.top + mHalfStrokeSize;
		float right = mBoxRect.right - mHalfStrokeSize;
		float bottom = mBoxRect.bottom - mHalfStrokeSize;
		mBoxStrokeRect.set(left, top, right, bottom);

		return mBoxStrokeRect;
	}

}
