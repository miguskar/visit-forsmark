package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutApplicationDayActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapplicationdayview);

		// F�r Scrollvyn att scrolla upp, focus p� f�rsta elementet i vyn
		TextView v = (TextView) findViewById(R.id.aboutCalendar);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
	}

}
