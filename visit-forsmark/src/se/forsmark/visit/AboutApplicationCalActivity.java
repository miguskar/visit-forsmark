package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutApplicationCalActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapplicationcalview);

		// F�r Scrollvyn att scrolla upp, focus p� f�rsta elementet i vyn
		TextView v = (TextView) findViewById(R.id.aboutCalendar);
		v.setFocusableInTouchMode(true);
		v.requestFocus();

	}

}
