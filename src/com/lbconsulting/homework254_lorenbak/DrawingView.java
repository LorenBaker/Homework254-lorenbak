package com.lbconsulting.homework254_lorenbak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

	private float mInitialRedBoxCenterX;
	private float mInitialRedBoxCenterY;

	private float mInitialBlueBoxCenterX;
	private float mInitialBlueBoxCenterY;

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

	public RectF getRedBoxRect() {
		return mRedBox.getBoxRect();
	}

	public void setBlueDrawingBox(DrawingBox box) {
		mBlueBox = box;
	}

	public RectF getBlueBoxRect() {
		return mBlueBox.getBoxRect();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//MyLog.d("DrawingView", "onDraw");
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean onTouchEventHandled = false;
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

			onTouchEventHandled = true;
			break;

		case MotionEvent.ACTION_UP:
			MyLog.d("DrawingView", "ACTION_UP");

			if (isRedBoxMoving) {
				isRedBoxMoving = false;
				MyLog.d("DrawingView", "onTouchEvent; RED_BOX action_up; X:" + event.getX() + " Y:" + event.getY());
				addRedBoxPoint(mRedBox.getBoxCenterX(), mRedBox.getBoxCenterY());
			}

			if (isBlueBoxMoving) {
				isBlueBoxMoving = false;
				MyLog.d("DrawingView", "onTouchEvent; BLUE_BOX action_up; X:" + event.getX() + " Y:" + event.getY());
				addBlueBoxPoint(mBlueBox.getBoxCenterX(), mBlueBox.getBoxCenterY());
			}
			onTouchEventHandled = true;
			break;

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
			onTouchEventHandled = true;
			break;

		default:
			break;
		}

		return onTouchEventHandled;
	}

	private void addRedBoxPoint(float x, float y) {

		if (mRedBox.getPrevBoxRect().left > -1) {
			// paint the previous red box location with the background color
			mCanvas.drawRect(mRedBox.getPrevBoxRect(), mBackgroundPaint);
			// draw the blue box if the red box intersect with the blue box
			if (mRedBox.getPrevBoxRect().intersect(mBlueBox.getBoxRect())) {
				mCanvas.drawRect(mBlueBox.getBoxRect(), mBlueBox.getFillPaint());
			}
		}

		// set the new red box location and draw it
		mRedBox.setBoxRect(x, y);
		mCanvas.drawRect(mRedBox.getBoxRect(), mRedBox.getFillPaint());

		if (isRedBoxMoving) {
			// draw a stroke around the red box
			mCanvas.drawRect(mRedBox.getBoxStrokeRect(), mRedBox.getStrokePaint());
		}
		mRedBox.setPrevBoxRect(x, y);
	}

	private void addBlueBoxPoint(float x, float y) {

		if (mBlueBox.getPrevBoxRect().left > -1) {
			// paint the previous blue box location with the background color
			mCanvas.drawRect(mBlueBox.getPrevBoxRect(), mBackgroundPaint);
			// draw the red box if the blue box intersect with the red box
			if (mBlueBox.getPrevBoxRect().intersect(mRedBox.getBoxRect())) {
				mCanvas.drawRect(mRedBox.getBoxRect(), mRedBox.getFillPaint());
			}
		}

		// set the new blue box location and draw it
		mBlueBox.setBoxRect(x, y);
		mCanvas.drawRect(mBlueBox.getBoxRect(), mBlueBox.getFillPaint());
		if (isBlueBoxMoving) {
			// draw a stroke around the blue box
			mCanvas.drawRect(mBlueBox.getBoxStrokeRect(), mRedBox.getStrokePaint());
		}
		mBlueBox.setPrevBoxRect(x, y);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		MyLog.d("DrawingView", "onSizeChanged; w:" + w + " h:" + h);

		RectF worldRect = new RectF();
		worldRect.set(0, 0, w, h);

		mRedBox.setWorldRect(worldRect);
		mBlueBox.setWorldRect(worldRect);

		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setColor(Color.BLACK);

		// Initialize our interim storage
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

	}

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
		addRedBoxPoint(mInitialRedBoxCenterX, mInitialRedBoxCenterY);
		addBlueBoxPoint(mInitialBlueBoxCenterX, mInitialBlueBoxCenterY);
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

	public void InitializeRedBoxLocation(float redBoxCenterX, float redBoxCenterY) {
		if (redBoxCenterX < 0) {
			mInitialRedBoxCenterX = 0;
			mInitialRedBoxCenterY = 0;
		} else {
			mInitialRedBoxCenterX = redBoxCenterX;
			mInitialRedBoxCenterY = redBoxCenterY;
		}
	}

	public void InitializeBlueBoxLocation(float blueBoxCenterX, float blueBoxCenterY) {
		if (blueBoxCenterX < 0) {
			mInitialBlueBoxCenterX = 999999;
			mInitialBlueBoxCenterY = 999999;
		} else {
			mInitialBlueBoxCenterX = blueBoxCenterX;
			mInitialBlueBoxCenterY = blueBoxCenterY;
		}
	}

}
