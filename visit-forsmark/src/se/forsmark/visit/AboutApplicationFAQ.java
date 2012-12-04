package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutApplicationFAQ extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapplicationfaq);
		
		//Får Scrollvyn att scrolla upp, focus på första elementet i vyn
		TextView v = (TextView) findViewById(R.id.q1);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
	}
	
}
