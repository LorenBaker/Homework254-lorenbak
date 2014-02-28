package com.lbconsulting.homework254_lorenbak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {

	DrawingView projectView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i("MainACTIVITY", "onCreate");
		// Hide the Action bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		projectView = (DrawingView) findViewById(R.id.mainView);
		if (projectView != null) {
			projectView.setBackgroundColor(Color.BLACK);
			float pxBoxSize = HW254Utilities.dpToPx(this, 60);
			float pxStrokeSize = HW254Utilities.dpToPx(this, 8);

			DrawingBox BlueBox = new DrawingBox(pxBoxSize, Color.RED, Color.WHITE, pxStrokeSize);
			projectView.setRedDrawingBox(BlueBox);

			DrawingBox blueBox = new DrawingBox(pxBoxSize, Color.BLUE, Color.WHITE, pxStrokeSize);
			projectView.setBlueDrawingBox(blueBox);
		}
	}

	@Override
	protected void onResume() {
		MyLog.i("MainACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("HW254", MODE_PRIVATE);

		float redBoxCenterX = storedStates.getFloat("redBoxCenterX", -1);
		float redBoxCenterY = storedStates.getFloat("redBoxCenterY", -1);
		projectView.InitializeRedBoxLocation(redBoxCenterX, redBoxCenterY);

		float blueBoxCenterX = storedStates.getFloat("blueBoxCenterX", -1);
		float blueBoxCenterY = storedStates.getFloat("blueBoxCenterY", -1);
		projectView.InitializeBlueBoxLocation(blueBoxCenterX, blueBoxCenterY);

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		MyLog.i("MainACTIVITY", "onDestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		MyLog.i("MainACTIVITY", "onPause");
		// save Blue and blue box rectangles
		SharedPreferences preferences = getSharedPreferences("HW254", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		applicationStates.putFloat("redBoxCenterX", projectView.getRedBoxRect().centerX());
		applicationStates.putFloat("redBoxCenterY", projectView.getRedBoxRect().centerY());

		applicationStates.putFloat("blueBoxCenterX", projectView.getBlueBoxRect().centerX());
		applicationStates.putFloat("blueBoxCenterY", projectView.getBlueBoxRect().centerY());

		applicationStates.commit();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		MyLog.i("MainACTIVITY", "onRestart");
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		MyLog.i("MainACTIVITY", "onRestoreInstanceState");
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		MyLog.i("MainACTIVITY", "onSaveInstanceState");
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		MyLog.i("MainACTIVITY", "onStart");
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		MyLog.i("MainACTIVITY", "onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}

}
