package com.rontejada91.cardgamehelper;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class CounterActivity extends Activity implements OnClickListener {
	// Fields
	private ImageButton bClear;
	private ImageButton bTools;
	private ImageButton bScores;
	private MediaPlayer aRestart;
	private MediaPlayer aCounter;
	private MediaPlayer aMenu;
	private AssetFileDescriptor afd;
	private AudioManager audio;
	private final int MAX_COUNT = 999999;
	private boolean musicOn;
	private Intent gameType; // Which game was selected? Magic? Yugioh? Etc
	private ImageButton[] ibSubtract; // All sub buttons
	private ImageButton[] ibAdd; // All add buttons
	private EditText[] etCounter; // All edit text counters
	private final int MAX_COUNTER_FIELDS = 12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter);
		
		setIds();
		setButtonListeners();
		setExtras();
		
		if (musicOn)
			setSoundSetup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.counter, menu);
		return true;
	}
	
	// Controls and adjusts the volume
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (musicOn) {
		    switch (keyCode) {
		    case KeyEvent.KEYCODE_VOLUME_UP:
		        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
		                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		        return true;
		    case KeyEvent.KEYCODE_VOLUME_DOWN:
		        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
		                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		        return true;
		    default:
		        return super.onKeyDown(keyCode, event);
		    }
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		
		// Provides the view id, will be used to differentiate between addition and subtraction buttons
		String idClicked = getResources().getResourceEntryName(v.getId());
		
		// Just the number of idClicked
		// This replaces anything that isnt a number with ""
		String idClickedNumber = idClicked.replaceAll("[^0-9]", "");
		
		// Simple parsing, we subtract 1 in the process to account for it's use in the array
		int idNumber = Integer.parseInt(idClickedNumber) - 1;
		
		// TODO - idNumber can only be assigned single digits, must accommodate for names
		// ending in multiple digits
		
		// If the view is a subtract button
		if (idClicked.contains("ibSub")) {

			// Set to zero if field is empty
			if (checkForEmpty(etCounter[idNumber])) {
				etCounter[idNumber].setText(Integer.toString(0));
			}
			// If counter not at 0, subtract 1
			else if (!checkForZero(etCounter[idNumber])) {
				if (musicOn)
					aCounter.start();
				
				etCounter[idNumber].setText(
						Integer.toString(
								Integer.parseInt(
										etCounter[idNumber].getText().toString()) - 1));
			}
			
		} 
		
		// If the view is an add button
		if (idClicked.contains("ibAdd")) {	
			// Set to 1 if field is empty
			if (checkForEmpty(etCounter[idNumber])) {
				if(musicOn)
					aCounter.start();
				
				etCounter[idNumber].setText(Integer.toString(1));
			}
			// If counter not at max, add 1
			else if (!checkForMax(etCounter[idNumber])) {
				if(musicOn)
					aCounter.start();
				
				etCounter[idNumber].setText(
						Integer.toString(
								Integer.parseInt(
										etCounter[idNumber].getText().toString()) + 1));
			}
		}
		
		// Menu buttons
		switch (v.getId()) {
		case R.id.ibClear:
			if(musicOn)
				aRestart.start();
			resetFields();
			break;
		case R.id.ibTools:
			if(musicOn)
				aMenu.start();
			Intent toolsIntent = new Intent(this, ToolsActivity.class);
			toolsIntent.putExtra("music", musicOn);
			toolsIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(toolsIntent);
			break;
		case R.id.ibScores:
			if(musicOn)
				aMenu.start();
			Intent mainIntent = new Intent(this, MainActivity.class);
			mainIntent.putExtra("music", musicOn);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(mainIntent);
			break;
		}
		
	}
	
	public boolean checkForEmpty(EditText et) {
		return et.getText().toString().matches("");
	}

	public boolean checkForZero(EditText et) {
		return et.getText().toString().matches("0");
	}
	
	public boolean checkForMax(EditText et) {
		return et.getText().toString().matches(Integer.toString(MAX_COUNT));
	}
	
	public void setIds() {
	
		// Declare how many of each view we have
		ibSubtract = new ImageButton[MAX_COUNTER_FIELDS];
		ibAdd = new ImageButton[MAX_COUNTER_FIELDS];
		etCounter = new EditText[MAX_COUNTER_FIELDS];
		
		// Assign all variables in their arrays to views found by their id's
		for (int i = 0; i < MAX_COUNTER_FIELDS; i++) {
			int resSubID = getResources().getIdentifier("ibSub" + (i + 1), "id", getPackageName());
			Log.d("resSubID", String.valueOf(resSubID));
			int resAddID = getResources().getIdentifier("ibAdd" + (i + 1), "id", getPackageName());
			Log.d("resAddID", String.valueOf(resAddID));
			int resCounterID = getResources().getIdentifier("etCounter" + (i + 1), "id", getPackageName());
			Log.d("resCounterID", String.valueOf(resCounterID));
			
			ibSubtract[i] = (ImageButton)findViewById(resSubID);
			Log.d("ibSubtract[" + i + "]", String.valueOf(ibSubtract[i].getId()));
			ibAdd[i] = (ImageButton)findViewById(resAddID);
			Log.d("ibAdd[" + i + "]", String.valueOf(ibAdd[i].getId()));
			etCounter[i] = (EditText)findViewById(resCounterID);
			Log.d("etCounter[" + i + "]", String.valueOf(etCounter[i].getId()));
		}
		
		bClear = (ImageButton)findViewById(R.id.ibClear);
		
		bTools = (ImageButton)findViewById(R.id.ibTools);
		
		bScores = (ImageButton)findViewById(R.id.ibScores);
		
	}
	
	public void setButtonListeners() {
		// Adding listeners to the buttons
		for (int i = 0; i < MAX_COUNTER_FIELDS; i++) {
			ibSubtract[i].setOnClickListener(this);
			ibAdd[i].setOnClickListener(this);
		}
		  
		bClear.setOnClickListener(this);
		bTools.setOnClickListener(this);
		bScores.setOnClickListener(this);

	}
	
	public void setExtras() {
		// Grabs the intent
		gameType = getIntent();
		
		// Set music on or off
		musicOn = gameType.getBooleanExtra("music", true);
		
	}
	
	public void setSoundSetup() {
		// Initializing volume and power control and variables
		audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		aRestart = new MediaPlayer();
		aCounter = new MediaPlayer();
		aMenu = new MediaPlayer();
		
		try {
			afd = getAssets().openFd("restart.ogg");
			aRestart.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aRestart.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "restart.ogg" + " for playback.", e);
		}
		
		try {
			afd = getAssets().openFd("counter.ogg");
			aCounter.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aCounter.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "counter.ogg" + " for playback.", e);
		}			
		
		try {
			afd = getAssets().openFd("menu.ogg");
			aMenu.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aMenu.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "menu.ogg" + " for playback.", e);
		}
	}

	public void resetFields() {
		// Restore all counter fields with a 0
		for (int i = 0; i < MAX_COUNTER_FIELDS; i++) {
			etCounter[i].setText("0");
		}
	}
	
}