package com.lbconsulting.homework254_lorenbak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

	private static String TAG = "DrawingView";

	// PHASE 1 Create some needed variables
	private Paint mCirclePaint = new Paint();
	private Paint mBackgroundPaint = new Paint();
	//private volatile ArrayList<Point> mPoints = new ArrayList<Point>();

	// PHASE 2 Create some more needed variables
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private float mPrevX;
	private float mPrevY;

	public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawingView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.d(TAG, "onDraw");

		// PHASE 1 Get the Points to draw and draw them to the Canvas
		/*		if (mPoints.size() > 0) {
					for (Point p : mPoints) {
						// What impact does the following have?
						Log.d("!!!", "p: " + p.toString());
						canvas.drawCircle(p.x, p.y, p.size * 50.0f, mCirclePaint);
					}
				}*/
		// PHASE 2 Draw our composed result
		canvas.drawBitmap(mBitmap, 0, 0, null);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "ACTION_DOWN");
			return true;

		case MotionEvent.ACTION_UP:
			Log.d(TAG, "ACTION_UP");
			return false;

		case MotionEvent.ACTION_MOVE:
			//Log.i(TAG, "ACTION_MOVE");

			// PHASE 1 What could we do to improve the quality?
			int historySize = event.getHistorySize();
			if (historySize > 0) {
				for (int i = 0; i < historySize; i++) {
					addPoint(
							event.getHistoricalX(i),
							event.getHistoricalY(i),
							event.getHistoricalPressure(i),
							event.getHistoricalSize(i));
				}
			}

			// PHASE 1 + 2 Add events as Points
			addPoint(event.getX(), event.getY(), event.getPressure(), event.getSize());
			Log.e("TAG", "ACTION_MOVE; X:" + event.getX() + " Y:" + event.getY() + " pres:" + event.getPressure()
					+ " size:" + event.getSize());

			// PHASE 1 + 2 How do we get ourselves to call onDraw()?
			this.invalidate();
			// Return true because we are handling the event
			return true;
		}

		return false;
	}

	private void addPoint(float x, float y, float pressure, float size) {

		// PHASE 1 Add to our Points collection
		//mPoints.add(new Point(x, y, pressure, size));

		// PHASE 2 Determine an appropriate radius
		float radius = 60;
		/*if (pressure != 1.0f) {
			mCirclePaint.setAlpha(Math.round(pressure * 255));
			radius = pressure > 1.0f ? 1.0f : pressure * 100.0f;
		} else {
			radius = size * 50.0f;
		}
		*/
		// PHASE 2 Do our drawing
		if (mPrevX > -1) {
			mCanvas.drawCircle(mPrevX, mPrevY, radius, mBackgroundPaint);
		}
		mCanvas.drawCircle(x, y, radius, mCirclePaint);
		mPrevX = x;
		mPrevY = y;

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d(TAG, "onSizeChanged; w:" + w + " h:" + h);

		// PHASE 1 + 2 : Initialize the Paint value
		mCirclePaint.setStyle(Paint.Style.FILL);
		mCirclePaint.setColor(Color.RED);

		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setColor(Color.BLACK);
		mPrevX = -1;
		mPrevY = -1;

		// PHASE 2 : Initialize our interim storage
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

}
