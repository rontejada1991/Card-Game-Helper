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

public class MainActivity extends Activity implements OnClickListener {
	// Fields
	private static final int MAX_VALUE = 999999;
	private static final int MIN_VALUE = 0;
	private ImageButton ibPlayer;
	private ImageButton ibScoreSub;
	private ImageButton ibScoreAdd;
	private ImageButton bReset;
	private ImageButton bTools;
	private ImageButton bCounters;
	private EditText etPlayer1Score;
	private EditText etPlayer2Score;
	private EditText etScore;
	private MediaPlayer aDmg;
	private MediaPlayer aHeal;
	private MediaPlayer aWin;
	private MediaPlayer aRestart;
	private MediaPlayer aPlayer;
	private MediaPlayer aMenu;
	private AssetFileDescriptor afd;
	private AudioManager audio;
	private Intent gameType;
	private int newScore;
	private int oldModifier;
	private boolean player;
	private boolean musicOn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setIds();
		setButtonListener();		
		setExtras();
				
		if (musicOn)
			setSoundSetup();
		
		//setAds();
		//setTestAds();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	// Once ibScoreSub or ibScoreAdd are clicked, the onClick method will determine which was pressed and based on that it will add or subtract
	// the value selected by the user from the selected player score. It will check whether it is the player or the opponent that is selected
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibScoreSub:
			if (player) {
				if (!etPlayer1Score.getText().toString().isEmpty() && 
						!etScore.getText().toString().isEmpty() &&
						!etScore.getText().toString().contentEquals("0"))
					dealDamage(etPlayer1Score, Integer.parseInt(etScore.getText().toString()));
				else if (!etPlayer1Score.getText().toString().isEmpty() &&
						oldModifier != 0)
					dealDamage(etPlayer1Score, oldModifier);
			}
			else {
				if (!etPlayer2Score.getText().toString().isEmpty() && 
						!etScore.getText().toString().isEmpty() &&
						!etScore.getText().toString().contentEquals(String.valueOf(MIN_VALUE)))
					dealDamage(etPlayer2Score, Integer.parseInt(etScore.getText().toString()));
				else if (!etPlayer2Score.getText().toString().isEmpty() &&
						oldModifier != 0)
					dealDamage(etPlayer2Score, oldModifier);
			}
			break;
		case R.id.ibScoreAdd:
			if (player) {
				if (!etPlayer1Score.getText().toString().isEmpty() && 
						!etScore.getText().toString().isEmpty() &&
						!etScore.getText().toString().contentEquals(String.valueOf(MIN_VALUE)))
					healDamage(etPlayer1Score, Integer.parseInt(etScore.getText().toString()));
				else if (!etPlayer1Score.getText().toString().isEmpty() &&
						oldModifier != 0)
					healDamage(etPlayer1Score, oldModifier);
			}
			else {
				if (!etPlayer2Score.getText().toString().isEmpty() && 
						!etScore.getText().toString().isEmpty() &&
						!etScore.getText().toString().contentEquals(String.valueOf(MIN_VALUE)))
					healDamage(etPlayer2Score, Integer.parseInt(etScore.getText().toString()));
				else if (!etPlayer2Score.getText().toString().isEmpty() &&
						oldModifier != 0)
					healDamage(etPlayer2Score, oldModifier);
			}			
			break;
		case R.id.ibRestart:
			// Set all the edit text back to nothing and the toggle button back to false
			etPlayer1Score.getText().clear(); 
			etPlayer2Score.getText().clear(); 
			etScore.getText().clear();
			
			// Setting modifier again since it is a new game
			etScore.setHint("Modifier");
			
			// setting oldModifier to 0 to stop it from remembering what value it had from last game
			oldModifier = 0;
			
			// Again sets the life based on game chosen
			setExtras();
			
			if(musicOn)
				aRestart.start();
			break;
		case R.id.ibTools:
			if(musicOn)
				aMenu.start();
			// Go to the tools activity
			Intent toolsIntent = new Intent(MainActivity.this, ToolsActivity.class);
			toolsIntent.putExtra("music", musicOn);
			toolsIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(toolsIntent);
			break;
		case R.id.ibCounters:
			if(musicOn)
				aMenu.start();
			// Go to counters activity
			Intent countersIntent = new Intent(this, CounterActivity.class);
			countersIntent.putExtra("music", musicOn);
			countersIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(countersIntent);
			break;
		case R.id.ibPlayer:
			if  (musicOn) {
				aPlayer.start();
			}
			if (!player) {
				ibPlayer.setImageResource(R.drawable.left);
				player = true;
			}
			else {
				ibPlayer.setImageResource(R.drawable.right);
				player = false;
			}
			break;
		}	
	}
	
	public void setIds() {
		// Assigns variables to their views
		ibPlayer = (ImageButton)findViewById(R.id.ibPlayer);
		ibScoreAdd = (ImageButton)findViewById(R.id.ibScoreAdd);
		ibScoreSub = (ImageButton)findViewById(R.id.ibScoreSub);
		bReset = (ImageButton)findViewById(R.id.ibRestart);
		bTools = (ImageButton)findViewById(R.id.ibTools);
		bCounters = (ImageButton)findViewById(R.id.ibCounters);
		etPlayer1Score = (EditText)findViewById(R.id.etPlayer1Score);
		etPlayer2Score = (EditText)findViewById(R.id.etPlayer2Score);
		etScore = (EditText)findViewById(R.id.etScore);
	}
	
	public void setButtonListener() {
		// Makes the variable buttons click able
		ibScoreSub.setOnClickListener(this);
		ibScoreAdd.setOnClickListener(this);	
		bReset.setOnClickListener(this);
		bTools.setOnClickListener(this);
		bCounters.setOnClickListener(this);
		ibPlayer.setOnClickListener(this);
	}
	
	public void setExtras() {
		// Grabs the intent
		gameType = getIntent();
		
		// Set score (if any) based on game chosen
		if (gameType.hasExtra("value")) {
			etPlayer1Score.setText(String.valueOf(gameType.getExtras().getInt("value")));
			etPlayer2Score.setText(String.valueOf(gameType.getExtras().getInt("value")));
		}
		
		// Set music on or off
		musicOn = gameType.getBooleanExtra("music", true);
	}
	
	public void setSoundSetup() {
		audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		// Initializing variables and assigning audio to the media players
		aDmg = new MediaPlayer();
		aHeal = new MediaPlayer();
		aWin = new MediaPlayer();
		aRestart = new MediaPlayer();
		aMenu = new MediaPlayer();
		aPlayer = new MediaPlayer();
		
		try {
			afd = getAssets().openFd("damage.ogg");
            aDmg.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
			aDmg.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "damage.ogg" + " for playback.", e);
		}
		
		try {
			afd = getAssets().openFd("healing.ogg");
            aHeal.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aHeal.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "healing.ogg" + " for playback.", e);
		}
		
		try {
			afd = getAssets().openFd("win.ogg");
            aWin.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aWin.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "win.ogg" + " for playback.", e);
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
		
		try {
			afd = getAssets().openFd("counter.ogg");
			aPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aPlayer.prepare();
		} catch (IOException e) {
			Log.e("PlayAudio", "Could not open file " + "counter.ogg" + " for playback.", e);
		}
		
	}

	public void dealDamage(EditText etPlayerScore, int modifier) {
		newScore = Integer.parseInt(etPlayerScore.getText().toString()) - modifier;

		// If the score is the minimum value, as in, the game is over, end the if statement
		// If the new score is less than the min score, set the min score. If not, set the new score.
		if (!etPlayerScore.getText().toString().contentEquals(Integer.toString(MIN_VALUE))) {
			if (newScore <= MIN_VALUE) {
				etPlayerScore.setText(Integer.toString(MIN_VALUE));
				if(musicOn)
					aWin.start();
				}
			else {
				etPlayerScore.setText(Integer.toString(newScore)); 
				if(musicOn)
					aDmg.start();
			}
		}
		
		// empty the modifier
		etScore.getText().clear();
		
		// Set the hint to be the modifier we just calculated
		etScore.setHint(String.valueOf(modifier));
		
		// makes the oldModifier the modifier we just used, for later use
		oldModifier = modifier;
	}
	
	public void healDamage(EditText etPlayerScore, int modifier) {
		newScore = Integer.parseInt(etPlayerScore.getText().toString()) + modifier;
		
		// If the new score is greater than the max score, set the max score. If not, set the new score.
		if (newScore > MAX_VALUE) {
			etPlayerScore.setText(Integer.toString(MAX_VALUE));
		}
		else {
			etPlayerScore.setText(Integer.toString(newScore)); 
			if (musicOn)
				aHeal.start();		
		}
		
		// empty the modifier
		etScore.getText().clear();
		
		// Set the hint to be the modifier we just calculated
		etScore.setHint(String.valueOf(modifier));
		
		// makes the oldModifier the modifier we just used, for later use
		oldModifier = modifier;
	}
	
}