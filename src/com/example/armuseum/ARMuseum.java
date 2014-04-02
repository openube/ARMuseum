package com.example.armuseum;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ARMuseum extends Activity implements OnClickListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	private Button scanBtn;
	private TextView formatTxt, contentTxt;
	
	private ImageView mSplashScreen = null;
	
	private ScheduledExecutorService scheduleTaskExecutor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_armuseum);

		scanBtn = (Button) findViewById(R.id.scan_button);
		formatTxt = (TextView) findViewById(R.id.scan_format);
		contentTxt = (TextView) findViewById(R.id.scan_content);
		
		mSplashScreen = (ImageView)findViewById(R.id.imageSplash);
		
		scheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
		scheduleTaskExecutor.schedule(new DismissSplash(this), 5, TimeUnit.SECONDS);
	}

	class DismissSplash implements Runnable
	{
		private final ARMuseum mMainActivity;

		public DismissSplash(ARMuseum mainActivity)
		{
			mMainActivity = mainActivity;
		}

		public void run()
		{
			mMainActivity.runOnUiThread(new DoDismissSplash());
		}
		
		class DoDismissSplash implements Runnable
		{
			public void run()
			{
				mMainActivity.onSplashDone();
			}
		}
	}
	
	public void onSplashDone()
	{
		mSplashScreen.setVisibility(View.GONE);
		
		// scanBtn.setOnClickListener(this);
				IntentIntegrator scanIntegrator = new IntentIntegrator(this);
				scanIntegrator.initiateScan();
	}

	 

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanningResult != null) {
			// we have a result
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();

			formatTxt.setText("FORMAT: " + scanFormat);
			contentTxt.setText("CONTENT: " + scanContent);

		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.scan_button) {
			// scan
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		}

	}

}
