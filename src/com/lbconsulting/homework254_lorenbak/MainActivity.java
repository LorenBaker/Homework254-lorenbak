package com.lbconsulting.homework254_lorenbak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends Activity {

	DrawingView projectView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i("MainACTIVITY", "onCreate");
		setContentView(R.layout.activity_main);
		projectView = (DrawingView) findViewById(R.id.mainView);
		if (projectView != null) {
			projectView.setBackgroundColor(Color.BLACK);
			int pxBoxSize = HW254Utilities.dpToPx(this, 60);

			DrawingBox redBox = new DrawingBox(pxBoxSize, Color.RED, Color.WHITE);
			projectView.setRedDrawingBox(redBox);

			DrawingBox blueBox = new DrawingBox(pxBoxSize, Color.BLUE, Color.WHITE);
			projectView.setBlueDrawingBox(blueBox);
		}
	}

	@Override
	protected void onResume() {
		MyLog.i("MainACTIVITY", "onResume");
		SharedPreferences storedStates = getSharedPreferences("HW254", MODE_PRIVATE);
		mActiveListID = storedStates.getLong("ActiveListID", -1);
		mActiveItemID = storedStates.getLong("ActiveItemID", -1);
		mActiveListPosition = storedStates.getInt("ActiveListPosition", -1);
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
		// save activity state
		SharedPreferences preferences = getSharedPreferences("HW254", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		applicationStates.putInt("redBoxX", projectView.getRedBoxX());
		applicationStates.putInt("redBoxY", projectView.getRedBoxY());

		applicationStates.putInt("redBoxX", projectView.getBlueBoxX());
		applicationStates.putInt("redBoxY", projectView.getBlueBoxY());

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
