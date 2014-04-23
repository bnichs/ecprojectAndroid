package com.example.filereconciler;

import android.widget.EditText;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec504project.application.CoreReconciler;
import ec504project.application.CoreReconciler.Metrics;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String	thePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Button button = (Button) findViewById(R.id.choose_file_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(MainActivity.this,
						FileBrowseActivity.class);

				startActivityForResult(browserIntent, 1);

			}
		});

		Button button2 = (Button) findViewById(R.id.button_reconcile);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				String ip = ((EditText) findViewById(R.id.ipText1)).getText()
						.toString().trim();

				if (!validateIp(ip)) {
					System.out.println("Damn");

					Toast.makeText(MainActivity.this,
							"Please enter a valid Ip", Toast.LENGTH_SHORT)
							.show();

				}
				else if (thePath == null || !(new File(thePath).exists())) {
					Toast.makeText(MainActivity.this,
							"Please choose a file/directory",
							Toast.LENGTH_SHORT).show();
				}
				else {
					InetAddress ips;
					try {
						File fil = new File(thePath);
						ips = InetAddress.getByName(ip);
						
						try {
							Metrics m = (new ReconcileTask()).execute(fil,ips).get();
							String print = "Bandwidth used:\t" + m.bandwidth + " bytes\n"+
											"Time taken:\t"+m.timeInNs + " ns";
							Toast.makeText(MainActivity.this,
									print, Toast.LENGTH_SHORT)
									.show();
						}
						catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
											

						////make toast 

					}
					catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(MainActivity.this,
								"Please enter a valid Ip", Toast.LENGTH_SHORT)
								.show();

					}


					//reconcile shit 
				}
				/*
				 * Intent browserIntent = new Intent(MainActivity.this,
				 * FileBrowseActivity.class);
				 * startActivityForResult(browserIntent, 1);
				 */
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("filename");
				TextView tv = (TextView) findViewById(R.id.textView1);
				tv.setText(result);
				thePath=result;
			}
			if (resultCode == RESULT_CANCELED) {
				//THis should never happen 
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) { return true; }
		return super.onOptionsItemSelected(item);
	}

	public boolean validateIp(String ip) {
		String[] parts = ip.split("\\.");
		if (parts.length != 4) { return false; }
		int num = -1;
		for (String part : parts) {
			try {
				num = Integer.parseInt(part);
			}
			catch (NumberFormatException e) {
				return false;
			}
			if (num < 0 || num > 255) { return false; }
		}
		return true;

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
