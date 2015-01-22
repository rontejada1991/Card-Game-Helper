package com.rontejada91.cardgamehelper;

import java.io.IOException;
import java.util.Random;

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
import android.widget.ImageButton;
import android.widget.ImageView;

public class ToolsActivity extends Activity implements OnClickListener {
	// Fields
	private ImageButton ibScores;
	private ImageButton ibReset;
	private ImageButton ibCounters;
	private ImageButton ibFlipCoin;
	private ImageButton ibRollDie;
	private ImageView ivDie1;
	private ImageView ivDie2;
	private ImageView ivDie3;
	private ImageView ivDie4;
	private ImageView ivDie5;
	private ImageView ivDie6;
	private ImageView ivDie7;
	private ImageView ivCoin1;
	private ImageView ivCoin2;
	private ImageView ivCoin3;
	private ImageView ivCoin4;
	private ImageView ivCoin5;
	private ImageView ivCoin6;
	private ImageView ivCoin7;
	private ImageView[] dieFilledIn;
	private ImageView[] coinFilledIn;
	private Random rand;
	private MediaPlayer aCoin;
	private MediaPlayer aDice;
	private MediaPlayer aRestart;
	private MediaPlayer aMenu;
	private AssetFileDescriptor afd;
	private AudioManager audio;
	private final int MAX_COUNTER = 7; // Maximum number of coins/die that can be on the screen
	private int coinCounter; // How many coins have been flipped
	private int dieCounter; // How many die have been rolled
	private int roll; // What number was rolled
	private int flip; // What number was flipped
	boolean filledIn; // Was the coin/dice filled in
	private Intent gameType;
	private boolean musicOn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);

		setIds();
		setButtonListeners();
		setExtras();
		
		// Used for randomizing coin flips and die rolls
		rand = new Random();
		
		// Array of image view objects that will get filled in as coins are flipped and die rolled
		coinFilledIn = new ImageView[] { 
				ivCoin1, ivCoin2, ivCoin3, ivCoin4, ivCoin5, ivCoin6, ivCoin7
		};
		
		dieFilledIn = new ImageView[] { 
				ivDie1, ivDie2, ivDie3, ivDie4, ivDie5, ivDie6, ivDie7
		};
		
		if (musicOn)
			setSoundSetup();		
		
		//setAds();
		//setTestAds();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tools, menu);
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
		switch (v.getId()) {
		case R.id.ibScores:
			if(musicOn)
				aMenu.start();
			// Go to the scores activity
			Intent mainIntent = new Intent(ToolsActivity.this, MainActivity.class);
			mainIntent.putExtra("music", musicOn);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(mainIntent);
			break;
		case R.id.ibCounters:
			if(musicOn)
				aMenu.start();
			// Go to the counters activity
			Intent countersIntent = new Intent(this, CounterActivity.class);
			countersIntent.putExtra("music", musicOn);
			countersIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(countersIntent);
			break;
		case R.id.ibFlipCoin:
			if (musicOn)
				aCoin.start();
			flip = rand.nextInt(2); // Randomly selects 0 - 1
			
			if (coinCounter < MAX_COUNTER) {
				setCoin(flip);
				}
			else {
				// Reset the counter to 0
				coinCounter = 0;
				// Clears all the images
				for (int i = 0; i < MAX_COUNTER; i++) {
					coinFilledIn[i].setImageDrawable(null);
				}
				setCoin(flip);
			}
			break;
		case R.id.ibRollDie:
			if (musicOn)
				aDice.start();
			roll = rand.nextInt(6); // Randomly selects 0 - 5
			
			if (dieCounter < MAX_COUNTER) {
				setDie(roll);
			}
			else {
				// Reset the counter to 0
				dieCounter = 0;
				// Clears all the images
				for (int i = 0; i < MAX_COUNTER; i++) {
					dieFilledIn[i].setImageDrawable(null);
				}
				setDie(roll);
			}
			break;
		case R.id.ibClear:
			if (musicOn)
				aRestart.start();
			// Reset both counters
			coinCounter = 0;
			dieCounter = 0;
			// Clears all the images
			for (int i = 0; i < MAX_COUNTER; i++) {
				dieFilledIn[i].setImageDrawable(null);
				coinFilledIn[i].setImageDrawable(null);
			}
			break;
		}	
	}
	
	public void setIds() {
		ibScores = (ImageButton)findViewById(R.id.ibScores);
		ibReset = (ImageButton)findViewById(R.id.ibClear);
		ibCounters = (ImageButton)findViewById(R.id.ibCounters);
		ibFlipCoin = (ImageButton)findViewById(R.id.ibFlipCoin);
		ibRollDie = (ImageButton)findViewById(R.id.ibRollDie);
		ivDie1 = (ImageView)findViewById(R.id.ivDie1);
		ivDie2 = (ImageView)findViewById(R.id.ivDie2);
		ivDie3 = (ImageView)findViewById(R.id.ivDie3);
		ivDie4 = (ImageView)findViewById(R.id.ivDie4);
		ivDie5 = (ImageView)findViewById(R.id.ivDie5);
		ivDie6 = (ImageView)findViewById(R.id.ivDie6);
		ivDie7 = (ImageView)findViewById(R.id.ivDie7);
		ivCoin1 = (ImageView)findViewById(R.id.ivCoin1);
		ivCoin2 = (ImageView)findViewById(R.id.ivCoin2);
		ivCoin3 = (ImageView)findViewById(R.id.ivCoin3);
		ivCoin4 = (ImageView)findViewById(R.id.ivCoin4);
		ivCoin5 = (ImageView)findViewById(R.id.ivCoin5);
		ivCoin6 = (ImageView)findViewById(R.id.ivCoin6);
		ivCoin7 = (ImageView)findViewById(R.id.ivCoin7);
	}
	
	public void setButtonListeners() {
		ibScores.setOnClickListener(this);
		ibReset.setOnClickListener(this); 
		ibCounters.setOnClickListener(this);
		ibFlipCoin.setOnClickListener(this);
		ibRollDie.setOnClickListener(this);
	}
	
	public void setExtras() {
		// Grabs the intent
		gameType = getIntent();
		
		// Set music on or off
		musicOn = gameType.getBooleanExtra("music", true);
	}
	
	public void setSoundSetup() {
		// Initializing volume and power control
		audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		// Initializing vassigning audio to the media players
		aCoin = new MediaPlayer();
		aDice = new MediaPlayer();
		aRestart = new MediaPlayer();
		aMenu = new MediaPlayer();
		
		try {
			afd = getAssets().openFd("coinsound.mp3");
            aCoin.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aCoin.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "coinsound.mp3" + " for playback.", e);
		}
		
		try {
			afd = getAssets().openFd("dicesound.mp3");
            aDice.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aDice.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "dicesound.mp3" + " for playback.", e);
		}	
		
		try {
			afd = getAssets().openFd("restart.ogg");
			aRestart.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aRestart.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "restart.ogg" + " for playback.", e);
		}
		
		try {
			afd = getAssets().openFd("menu.ogg");
			aMenu.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aMenu.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "menu.ogg" + " for playback.", e);
		}
	}
	
	public void setCoin(int flip) {
		for (int i = 0; i < MAX_COUNTER; i++) {
			filledIn = false;
			// Checks if the image view has an image, if it does it will keep looping until it doesn't
			if (coinFilledIn[i].getDrawable() == null) {
				switch (flip) {
				case 0:
					coinFilledIn[i].setImageResource(R.drawable.heads);
					break;
				case 1:
					coinFilledIn[i].setImageResource(R.drawable.tails);
					break;
				}
				filledIn = true;
				coinCounter++;
			}
			// If we added a coin, exit the for loop
			if (filledIn)
				break;
		}
	}
	
	public void setDie(int roll) {
		for (int i = 0; i < MAX_COUNTER; i++) {
			filledIn = false;
			// Checks if the image view has an image, if it does it will loop again until it doesn't
			if (dieFilledIn[i].getDrawable() == null) {
				// Sets the empty image view according to the roll 1-6
				switch (roll) {
				case 0: 
					dieFilledIn[i].setImageResource(R.drawable.one);
					break;
				case 1: 
					dieFilledIn[i].setImageResource(R.drawable.two);
					break;
				case 2: 
					dieFilledIn[i].setImageResource(R.drawable.three);
					break;
				case 3: 
					dieFilledIn[i].setImageResource(R.drawable.four);
					break;
				case 4: 
					dieFilledIn[i].setImageResource(R.drawable.five);
					break;
				case 5:
					dieFilledIn[i].setImageResource(R.drawable.six);
					break;
				}
				filledIn = true;
				dieCounter++;
			}
			// If we added a die, exit the for loop
			if (filledIn)
				break;
		}
	}

}