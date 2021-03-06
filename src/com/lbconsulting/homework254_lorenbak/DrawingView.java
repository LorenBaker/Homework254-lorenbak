package com.lbconsulting.homework254_lorenbak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * The appliction's custom view Handles all canvas drawing
 * 
 * @author Loren
 * 
 */
public class DrawingView extends View {

	// create the view's objects and variables
	private Canvas mCanvas;
	private Bitmap mBitmap;

	private DrawingBox mRedBox;
	private DrawingBox mBlueBox;
	private ResetButton mResetButton;

	private Paint mBackgroundPaint = new Paint();

	private boolean isRedBoxMoving = false;
	private boolean isBlueBoxMoving = false;
	private boolean isResetButtonTouched = false;

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

	// box and reset button getters and setters
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

	public void setResetButton(ResetButton resetButton) {
		mResetButton = resetButton;
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
			// Local broadcast intent. Used to play action down sound.
			Intent intent = new Intent(MainActivity.ACTION_DOWN_BROADCAST_KEY);

			// Determine view object has been selected. 
			// Play action down sound if a box is selected.
			if (mRedBox.isMoving(event.getX(), event.getY())) {
				isRedBoxMoving = true;
				LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
				MyLog.d("DrawingView", "onTouchEvent; RED_BOX action_down; X:" + event.getX() + " Y:" + event.getY());

			} else if (mBlueBox.isMoving(event.getX(), event.getY())) {
				isBlueBoxMoving = true;
				LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
				MyLog.d("DrawingView",
						"onTouchEvent; BLUE_BOX action_down; X:" + event.getX() + " Y:" + event.getY());

			} else if (mResetButton.isResetButtonTouched(event.getX(), event.getY())) {
				isResetButtonTouched = true;
				Intent resetBtnIntent = new Intent(MainActivity.ACTION_RESET_DOWN_BROADCAST_KEY);
				LocalBroadcastManager.getInstance(getContext()).sendBroadcast(resetBtnIntent);
				MyLog.d("DrawingView", "onTouchEvent; RESET action_down; X:" + event.getX() + " Y:" + event.getY());
				// reset the box location
				addRedBoxPoint(0, 0); // upper left
				addBlueBoxPoint(999999, 999999); // lower right
				this.invalidate();
			}

			onTouchEventHandled = true;
			break;

		case MotionEvent.ACTION_UP:
			// Local broadcast intent. Used to play action up sound.
			intent = new Intent(MainActivity.ACTION_UP_BROADCAST_KEY);

			// Determine which view object has been released.
			// Play action up sound if a box is released.
			if (isRedBoxMoving) {
				isRedBoxMoving = false;
				LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
				MyLog.d("DrawingView", "onTouchEvent; RED_BOX action_up; X:" + event.getX() + " Y:" + event.getY());
				addRedBoxPoint(mRedBox.getBoxCenterX(), mRedBox.getBoxCenterY());

			} else if (isBlueBoxMoving) {
				isBlueBoxMoving = false;
				LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
				MyLog.d("DrawingView", "onTouchEvent; BLUE_BOX action_up; X:" + event.getX() + " Y:" + event.getY());
				addBlueBoxPoint(mBlueBox.getBoxCenterX(), mBlueBox.getBoxCenterY());

			} else if (isResetButtonTouched) {
				isResetButtonTouched = false;
				MyLog.d("DrawingView", "onTouchEvent; RESET action_up; X:" + event.getX() + " Y:" + event.getY());
			}
			onTouchEventHandled = true;
			this.invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			// Move the selected box
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

			} else if (isBlueBoxMoving) {
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

		if (isResetButtonTouched) {
			mCanvas.drawRect(mResetButton.getResetButtonRect(), mResetButton.getFillPaint_pressed());
		} else {
			mCanvas.drawRect(mResetButton.getResetButtonRect(), mResetButton.getFillPaint_default());
		}
		mCanvas.drawText(mResetButton.getText(), mResetButton.getTextStartX(), mResetButton.getTextStartY(),
				mResetButton.getTextPaint());
		mCanvas.drawRect(mResetButton.getResetButtonRect(), mResetButton.getStrokePaint());
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

		if (isResetButtonTouched) {
			mCanvas.drawRect(mResetButton.getResetButtonRect(), mResetButton.getFillPaint_pressed());
		} else {
			mCanvas.drawRect(mResetButton.getResetButtonRect(), mResetButton.getFillPaint_default());
		}
		mCanvas.drawText(mResetButton.getText(), mResetButton.getTextStartX(), mResetButton.getTextStartY(),
				mResetButton.getTextPaint());
		mCanvas.drawRect(mResetButton.getResetButtonRect(), mResetButton.getStrokePaint());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		MyLog.d("DrawingView", "onSizeChanged; w:" + w + " h:" + h);

		RectF worldRect = new RectF();
		worldRect.set(0, 0, w, h);

		mRedBox.setWorldRect(worldRect);
		mBlueBox.setWorldRect(worldRect);
		mResetButton.setWorldRect(worldRect);

		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setColor(Color.BLACK);

		// Initialize our interim storage
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		MyLog.d("DrawingView", "onLayout");
		// set show the initial box in their proper locations
		addRedBoxPoint(mInitialRedBoxCenterX, mInitialRedBoxCenterY);
		addBlueBoxPoint(mInitialBlueBoxCenterX, mInitialBlueBoxCenterY);
		super.onLayout(changed, left, top, right, bottom);
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
