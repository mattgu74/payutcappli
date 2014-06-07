package fr.utc.payutc;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		
		TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
		TabSpec secondTabSpec = tabHost.newTabSpec("tid1");

		/** TabSpec setIndicator() is used to set name for the tab. */
		/** TabSpec setContent() is used to set content for a particular tab. */
		firstTabSpec.setIndicator("First Tab Name").setContent(new Intent(this, FirstTab.class));
		secondTabSpec.setIndicator("Second Tab Name").setContent(new Intent(this, SecondTab.class));

		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(firstTabSpec);
		tabHost.addTab(secondTabSpec);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
