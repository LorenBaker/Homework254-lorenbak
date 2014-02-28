package com.lbconsulting.homework254_lorenbak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

	private Paint mBackgroundPaint = new Paint();
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private DrawingBox mRedBox;
	private DrawingBox mBlueBox;

	private boolean isRedBoxMoving = false;
	private boolean isBlueBoxMoving = false;

	public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawingView(Context context) {
		super(context);
	}

	public void setRedDrawingBox(DrawingBox box) {
		mRedBox = box;
	}

	public int getRedBoxX() {
		return mRedBox.getBoxX();
	}

	public int getRedBoxY() {
		return mRedBox.getBoxY();
	}

	public void setBlueDrawingBox(DrawingBox box) {
		mBlueBox = box;
	}

	public int getBlueBoxX() {
		return mBlueBox.getBoxX();
	}

	public int getBlueBoxY() {
		return mBlueBox.getBoxY();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//MyLog.d("DrawingView", "onDraw");
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			MyLog.d("DrawingView", "ACTION_DOWN");
			isRedBoxMoving = mRedBox.isMoving(event.getX(), event.getY());
			if (isRedBoxMoving) {
				MyLog.d("DrawingView", "onTouchEvent; RED_BOX action_down; X:" + event.getX() + " Y:" + event.getY());
			} else {
				isBlueBoxMoving = mBlueBox.isMoving(event.getX(), event.getY());
				if (isBlueBoxMoving) {
					MyLog.d("DrawingView",
							"onTouchEvent; BLUE_BOX action_down; X:" + event.getX() + " Y:" + event.getY());
				}
			}

			return true;

		case MotionEvent.ACTION_UP:
			MyLog.d("DrawingView", "ACTION_UP");
			isRedBoxMoving = false;
			isBlueBoxMoving = false;
			return false;

		case MotionEvent.ACTION_MOVE:

			if (isRedBoxMoving) {
				//MyLog.w("DrawingView", "onTouchEvent; RED_BOX action_move; X:" + event.getX() + " Y:" + event.getY());
				int historySize = event.getHistorySize();
				if (historySize > 0) {
					for (int i = 0; i < historySize; i++) {
						addRedBoxPoint(event.getHistoricalX(i), event.getHistoricalY(i));
					}
				}

				addRedBoxPoint(event.getX(), event.getY());
				this.invalidate();
			}

			if (isBlueBoxMoving) {
				//MyLog.w("DrawingView", "onTouchEvent; BKUE_BOX action_move; X:" + event.getX() + " Y:" + event.getY());
				int historySize = event.getHistorySize();
				if (historySize > 0) {
					for (int i = 0; i < historySize; i++) {
						addBlueBoxPoint(event.getHistoricalX(i), event.getHistoricalY(i));
					}
				}

				addBlueBoxPoint(event.getX(), event.getY());
				this.invalidate();
			}

			// Return true because we are handling the event
			return true;
		default:
			break;
		}

		return false;
	}

	private void addRedBoxPoint(float x, float y) {

		if (mRedBox.getPrevBoxRect().left > -1) {
			mCanvas.drawRect(mRedBox.getPrevBoxRect(), mBackgroundPaint);

		}
		mRedBox.setBoxRect(x, y);
		mCanvas.drawRect(mRedBox.getBoxRect(), mRedBox.getFillPaint());
		mRedBox.setPrevBoxRect(x, y);
	}

	private void addBlueBoxPoint(float x, float y) {

		if (mBlueBox.getPrevBoxRect().left > -1) {
			mCanvas.drawRect(mBlueBox.getPrevBoxRect(), mBackgroundPaint);

		}
		mBlueBox.setBoxRect(x, y);
		mCanvas.drawRect(mBlueBox.getBoxRect(), mBlueBox.getFillPaint());
		mBlueBox.setPrevBoxRect(x, y);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		MyLog.d("DrawingView", "onSizeChanged; w:" + w + " h:" + h);

		Rect worldRect = new Rect();
		worldRect.left = 0;
		worldRect.top = 0;
		worldRect.right = w;
		worldRect.bottom = h;

		mRedBox.setWorldRect(worldRect);
		mBlueBox.setWorldRect(worldRect);
		//mRedBox.setBoxRect(0, 0);

		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setColor(Color.BLACK);

		// Initialize our interim storage
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

	}

	/*	public void setRedDrawingBoxPosition(float x, float y) {
			// TODO Auto-generated method stub
			mRedBox.setBoxRect(x, y);
			invalidate();
		}*/

	@Override
	protected void onAttachedToWindow() {
		MyLog.d("DrawingView", "onAttachedToWindow");
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		MyLog.d("DrawingView", "onCreateDrawableState");
		// TODO Auto-generated method stub
		return super.onCreateDrawableState(extraSpace);
	}

	@Override
	protected void onDetachedFromWindow() {
		MyLog.d("DrawingView", "onDetachedFromWindow");
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}

	@Override
	protected void onFinishInflate() {
		MyLog.d("DrawingView", "onFinishInflate");
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		MyLog.d("DrawingView", "onLayout");
		// TODO Auto-generated method stub
		addRedBoxPoint(0, 0);
		addBlueBoxPoint(2000, 2000);
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		MyLog.d("DrawingView", "onRestoreInstanceState");
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		MyLog.d("DrawingView", "onSaveInstanceState");
		// TODO Auto-generated method stub
		return super.onSaveInstanceState();
	}

	@Override
	protected boolean onSetAlpha(int alpha) {
		MyLog.d("DrawingView", "onSetAlpha");
		// TODO Auto-generated method stub
		return super.onSetAlpha(alpha);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		MyLog.d("DrawingView", "onWindowVisibilityChanged");
		// TODO Auto-generated method stub
		super.onWindowVisibilityChanged(visibility);
	}

}
