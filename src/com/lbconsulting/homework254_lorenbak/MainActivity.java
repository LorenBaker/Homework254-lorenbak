package com.lbconsulting.homework254_lorenbak;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View view = findViewById(R.id.mainView);
		if (view != null) {
			view.setBackgroundColor(Color.BLACK);
		}
	}

}
