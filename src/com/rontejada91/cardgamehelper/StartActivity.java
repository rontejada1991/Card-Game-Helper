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
import android.widget.ImageButton;

public class StartActivity extends Activity implements OnClickListener {
	// Fields
	private ImageButton ibYugioh;
	private ImageButton ibMTG;
	private ImageButton ibPokemon;
	private ImageButton ibNaruto;
	private ImageButton ibOther;
	private ImageButton ibMusic;
	private boolean musicOn;
	private MediaPlayer aOption;
	private AssetFileDescriptor afd;
	private AudioManager audio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		setIds();
		setButtonListeners();
		
		// App starts with music set to on
		musicOn = true;
		
		if (musicOn)
			setSoundSetup();
		
		//setAds();
		//setTestAds();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
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
		// Will pass a code that will let MainActivity know what life points to set
		switch (v.getId()) {
			case R.id.ibYugioh:
				if (musicOn)
					aOption.start();
				Intent yugioh = new Intent(this, MainActivity.class);
				yugioh.putExtra("value", 8000);
				yugioh.putExtra("music", musicOn);
				startActivity(yugioh);
				break;
			case R.id.ibMagic:
				if (musicOn)
					aOption.start();
				Intent magic = new Intent(this, MainActivity.class);
				magic.putExtra("value", 20);
				magic.putExtra("music", musicOn);
				startActivity(magic);
				break;
			case R.id.ibPokemon:
				if (musicOn)
					aOption.start();
				Intent pokemon = new Intent(this, ToolsActivity.class);
				pokemon.putExtra("music", musicOn);
				startActivity(pokemon);
				break;
			case R.id.ibNaruto:
				if (musicOn)
					aOption.start();
				Intent naruto = new Intent(this, CounterActivity.class);
				naruto.putExtra("music", musicOn);
				startActivity(naruto);
				break;
			case R.id.ibOther:
				if (musicOn)
					aOption.start();
				Intent other = new Intent(this, MainActivity.class);
				other.putExtra("music", musicOn);
				startActivity(other);
				break;
			case R.id.ibMusic:
				if (musicOn) {
					aOption.start();
					// Change image and turn music off
					ibMusic.setBackgroundResource(R.drawable.music_off);
					musicOn = false;
				}
				else  {
					// change image and turn off music
					ibMusic.setBackgroundResource(R.drawable.music_on);
					musicOn = true;
					if (musicOn)
						aOption.start();
				}		
		}
	}

	public void setIds() {
		// Assign xml attributes to the buttons
		ibYugioh = (ImageButton)findViewById(R.id.ibYugioh);
		ibMTG = (ImageButton)findViewById(R.id.ibMagic);
		ibPokemon = (ImageButton)findViewById(R.id.ibPokemon);
		ibNaruto = (ImageButton)findViewById(R.id.ibNaruto);
		ibOther = (ImageButton)findViewById(R.id.ibOther);
		ibMusic = (ImageButton)findViewById(R.id.ibMusic);
	}
	
	public void setButtonListeners() {
		// Set listeners to the buttons
		ibYugioh.setOnClickListener(this);
		ibMTG.setOnClickListener(this);
		ibPokemon.setOnClickListener(this);
		ibNaruto.setOnClickListener(this);
		ibOther.setOnClickListener(this);
		ibMusic.setOnClickListener(this);
	}
	
	public void setSoundSetup() {
		audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		// Initializing variables and assigning audio to the media players
		aOption = new MediaPlayer();
		
		try {
			afd = getAssets().openFd("menu.ogg");
            aOption.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
			aOption.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "menu.ogg" + " for playback.", e);
		}
	}

}