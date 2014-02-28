package com.lbconsulting.homework254_lorenbak;

import android.graphics.Paint;
import android.graphics.Rect;

public class DrawingBox {
	private Paint mFillPaint = new Paint();
	private Paint mStrokePaint = new Paint();
	private Rect mBoxRect;
	private Rect mPrevBoxRect;
	private int mBoxSize;
	private Rect mWorldRect = new Rect();

	public DrawingBox(int pxBoxSize, int fillColor, int strokeColor) {
		setBoxSize(pxBoxSize);

		getFillPaint().setStyle(Paint.Style.FILL);
		getFillPaint().setColor(fillColor);

		getStrokePaint().setStyle(Paint.Style.STROKE);
		getStrokePaint().setColor(strokeColor);

		mPrevBoxRect = new Rect();
		mPrevBoxRect.left = -9999;
	}

	public Rect getBoxRect() {
		return mBoxRect;
	}

	public void setBoxRect(float x, float y) {
		this.mBoxRect = populateBoxRect(x, y, mBoxSize);
	}

	public Paint getFillPaint() {
		return mFillPaint;
	}

	/*	private void setFillPaint(Paint fillPaint) {
			this.mFillPaint = fillPaint;
		}*/

	public Paint getStrokePaint() {
		return mStrokePaint;
	}

	/*	private void setStrokePaint(Paint strokePaint) {
			this.mStrokePaint = strokePaint;
		}*/

	public Rect getPrevBoxRect() {
		return mPrevBoxRect;
	}

	public void setPrevBoxRect(float x, float y) {
		this.mPrevBoxRect = populateBoxRect(x, y, mBoxSize);
	}

	public int getBoxSize() {
		return mBoxSize;
	}

	public void setBoxSize(int pxBoxSize) {
		this.mBoxSize = pxBoxSize;
	}

	public void setWorldRect(Rect worldRect) {
		mWorldRect = worldRect;
	}

	private Rect populateBoxRect(float x, float y, int boxSize) {

		Rect resultRect = new Rect();
		float halfBoxSize = boxSize / 2;
		float left = x - halfBoxSize;
		float top = y - halfBoxSize;
		float right = x + halfBoxSize;
		float bottom = y + halfBoxSize;

		if (left < mWorldRect.left) {
			left = mWorldRect.left;
			right = mWorldRect.left + boxSize;
		}
		if (top < mWorldRect.top) {
			top = mWorldRect.top;
			bottom = mWorldRect.top + boxSize;
		}

		if (right > mWorldRect.right) {
			right = mWorldRect.right;
			left = mWorldRect.right - boxSize;
		}

		if (bottom > mWorldRect.bottom) {
			bottom = mWorldRect.bottom;
			top = mWorldRect.bottom - boxSize;
		}

		resultRect.left = Math.round(left);
		resultRect.top = Math.round(top);
		resultRect.right = Math.round(right);
		resultRect.bottom = Math.round(bottom);

		return resultRect;
	}

	public boolean isMoving(float x, float y) {
		int posX = Math.round(x);
		int posY = Math.round(y);
		boolean result = false;
		if (posX >= mBoxRect.left && posX <= mBoxRect.right) {
			if (posY >= mBoxRect.top && posY <= mBoxRect.bottom) {
				result = true;
			}
		}
		return result;
	}

	public int getBoxX() {
		return (mBoxRect.right - mBoxRect.left) / 2;
	}

	public int getBoxY() {
		return (mBoxRect.bottom - mBoxRect.top) / 2;
	}

}
