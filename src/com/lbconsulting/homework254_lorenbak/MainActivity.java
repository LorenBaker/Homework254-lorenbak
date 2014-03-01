package com.lbconsulting.homework254_lorenbak;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Window;

public class MainActivity extends Activity implements OnLoadCompleteListener {

	// projectView holds application's DrawingView custom control
	DrawingView projectView;

	// variables to hold sounds
	private int mBlop_ActionUp;
	private int mTick_ActionDown;
	private int mResetButton_ActionDown;
	private SoundPool mSoundPool;
	private HashMap<Integer, SoundResource> mSoundResources = new HashMap<Integer, SoundResource>();

	// local broadcast keys and receivers to play sounds
	public static final String ACTION_UP_BROADCAST_KEY = "action_up_key";
	public static final String ACTION_DOWN_BROADCAST_KEY = "action_down_key";
	public static final String ACTION_RESET_DOWN_BROADCAST_KEY = "action_reset_button_down_key";

	private BroadcastReceiver mActionUp;
	private BroadcastReceiver mActionDown;
	private BroadcastReceiver mResetButtonDown;

	@SuppressWarnings("resource")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i("MainACTIVITY", "onCreate");
		// Hide the Action bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Action up receiver. Plays a sound when a box is released.
		mActionUp = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				playActionUpSound();
			}
		};

		// Action down receiver. Plays a sound when a box is selected.
		mActionDown = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				playActionDownSound();
			}
		};

		// Action down receiver. Plays a sound when a reset button is selected.
		mResetButtonDown = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				playResetButtonDown();
			}
		};

		// Register local broadcast receivers.
		LocalBroadcastManager.getInstance(this).registerReceiver(mActionUp, new IntentFilter(ACTION_UP_BROADCAST_KEY));
		LocalBroadcastManager.getInstance(this).registerReceiver(mActionDown,
				new IntentFilter(ACTION_DOWN_BROADCAST_KEY));
		LocalBroadcastManager.getInstance(this).registerReceiver(mResetButtonDown,
				new IntentFilter(ACTION_RESET_DOWN_BROADCAST_KEY));

		// setup soundPool and read .mp3 sound files
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		mSoundPool.setOnLoadCompleteListener(this);

		AssetManager am = this.getAssets();
		AssetFileDescriptor afd;
		try {
			afd = am.openFd("Tick_ActionDown.mp3");
			mTick_ActionDown = mSoundPool.load(afd, 1);

			afd = am.openFd("Blop_ActionUp.mp3");
			mBlop_ActionUp = mSoundPool.load(afd, 1);

			afd = am.openFd("ButtonClick.mp3");
			mResetButton_ActionDown = mSoundPool.load(afd, 1);

			afd.close();

		} catch (IOException e) {
			MyLog.e("MainACTIVITY", "onCreate; IO exception loading sound pool .mp3 files.");
			e.printStackTrace();
		}

		// get the app's custom view
		projectView = (DrawingView) findViewById(R.id.mainView);
		if (projectView != null) {
			// Initialize the DrawingView
			projectView.setBackgroundColor(Color.BLACK);

			// convert box dp units to pixels
			float pxBoxSize = HW254Utilities.dpToPx(this, 60);
			float pxStrokeSize = HW254Utilities.dpToPx(this, 8);

			// create red and blue boxes
			DrawingBox BlueBox = new DrawingBox(pxBoxSize, Color.RED, Color.WHITE, pxStrokeSize);
			projectView.setRedDrawingBox(BlueBox);

			DrawingBox blueBox = new DrawingBox(pxBoxSize, Color.BLUE, Color.WHITE, pxStrokeSize);
			projectView.setBlueDrawingBox(blueBox);

			// create the reset button
			float pxResetButtonHeight = HW254Utilities.dpToPx(this, 30);
			float pxResetButtonWidth = HW254Utilities.dpToPx(this, 60);
			float pxResetButtonMargin = HW254Utilities.dpToPx(this, 30);
			float pxResetButtonStrokeSize = HW254Utilities.dpToPx(this, 1);

			ResetButton resetBtn = new ResetButton(pxResetButtonHeight, pxResetButtonWidth, pxResetButtonMargin,
					Color.LTGRAY, Color.GRAY, Color.LTGRAY, pxResetButtonStrokeSize, Color.WHITE);
			projectView.setResetButton(resetBtn);
		}
	}

	@Override
	protected void onResume() {
		MyLog.i("MainACTIVITY", "onResume");

		// get red and blue box locations and initialize 
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
		// Unregister receivers since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mActionUp);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mActionDown);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mResetButtonDown);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		MyLog.i("MainACTIVITY", "onPause");
		// save Blue and blue box locations
		SharedPreferences preferences = getSharedPreferences("HW254", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();

		applicationStates.putFloat("redBoxCenterX", projectView.getRedBoxRect().centerX());
		applicationStates.putFloat("redBoxCenterY", projectView.getRedBoxRect().centerY());

		applicationStates.putFloat("blueBoxCenterX", projectView.getBlueBoxRect().centerX());
		applicationStates.putFloat("blueBoxCenterY", projectView.getBlueBoxRect().centerY());

		applicationStates.commit();
		super.onPause();
	}

	/**
	 * A class to hold sound resource info
	 * 
	 * @author Loren
	 * 
	 */
	private class SoundResource {
		public int id;
		public boolean loaded;
		public float volume;

		public SoundResource(int id, boolean loaded, float volume) {
			this.id = id;
			this.loaded = loaded;
			this.volume = volume;
		}
	}

	/**
	 * This method plays a sound when a box is released
	 */
	private void playActionUpSound() {
		final SoundResource sr = mSoundResources.get(mBlop_ActionUp);
		if (sr == null) {
			MyLog.e("MainACTIVITY", "playActionUpSound() Cound not find SoundResource for ID: " + mBlop_ActionUp);
			return;
		}

		if (sr.loaded) {
			/*play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
						Parameters
							soundID	a soundID returned by the load() function
							leftVolume	left volume value (range = 0.0 to 1.0)
							rightVolume	right volume value (range = 0.0 to 1.0)
							priority	stream priority (0 = lowest priority)
							loop	loop mode (0 = no loop, -1 = loop forever)
							rate	playback rate (1.0 = normal playback, range 0.5 to 2.0)
						Returns
							non-zero streamID if successful, zero if failed*/
			if (mSoundPool.play(sr.id, 0.5f, 0.5f, 1, 0, 1f) == 0) {
				MyLog.e("MainACTIVITY", "playActionUpSound() FAILED for sound resource ID: " + sr.id);
			}
		}
	}

	/**
	 * This method plays a sound when a box is selected
	 */
	private void playActionDownSound() {
		final SoundResource sr = mSoundResources.get(mTick_ActionDown);
		if (sr == null) {
			MyLog.e("MainACTIVITY", "playActionDownSound() Cound not find SoundResource for ID: " + mTick_ActionDown);
			return;
		}

		if (sr.loaded) {
			if (mSoundPool.play(sr.id, 0.5f, 0.5f, 1, 0, 1f) == 0) {
				MyLog.e("MainACTIVITY", "playActionDownSound() FAILED for sound resource ID: " + sr.id);
			}
		}
	}

	/**
	 * This method plays a sound when the reset button selected
	 */
	private void playResetButtonDown() {
		final SoundResource sr = mSoundResources.get(mResetButton_ActionDown);
		if (sr == null) {
			MyLog.e("MainACTIVITY", "playResetButtonDown() Cound not find SoundResource for ID: "
					+ mResetButton_ActionDown);
			return;
		}

		if (sr.loaded) {
			if (mSoundPool.play(sr.id, 0.75f, 0.75f, 1, 0, 1f) == 0) {
				MyLog.e("MainACTIVITY", "playResetButtonDown() FAILED for sound resource ID: " + sr.id);
			}
		}
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		MyLog.i("MainACTIVITY", "SoundPool.OnLoadCompleteListener: onLoadComplete(); sampleId: " + sampleId
				+ " stati: " + status);
		mSoundResources.put(sampleId, new SoundResource(sampleId, true, 1f));
	}

}
