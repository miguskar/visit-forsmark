package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutApplicationCalActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapplicationcalview);

		// Får Scrollvyn att scrolla upp, focus på första elementet i vyn
		TextView v = (TextView) findViewById(R.id.aboutCalendar);
		v.setFocusableInTouchMode(true);
		v.requestFocus();

	}

}
