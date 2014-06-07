package fr.utc.payutc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import android.view.Menu;


import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button connectingbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		connectingbutton = (Button) findViewById(R.id.connect);
		connectingbutton.setOnClickListener(new View.OnClickListener() {
	  			
	    	@Override
	    	public void onClick(View v) {
	    		Intent intent = new Intent(MainActivity.this, TabBar.class);
	    		startActivity(intent);
	    	}
	    	
		});
	}
}
	
