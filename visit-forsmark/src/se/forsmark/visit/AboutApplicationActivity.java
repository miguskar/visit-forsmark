package se.forsmark.visit;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AboutApplicationActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutapplicationview);

		/** Called when the activity is first created. */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapplicationview);

		TabHost tabHost = getTabHost();

		TabSpec calspec = tabHost.newTabSpec(getString(R.string.theCalendar));
		calspec.setIndicator(getString(R.string.theCalendar));
		Intent CalendarIntent = new Intent(getApplicationContext(), AboutApplicationCalActivity.class);
		calspec.setContent(CalendarIntent);
		
		TabSpec faqspec = tabHost.newTabSpec(getString(R.string.FAQ));
		faqspec.setIndicator(getString(R.string.FAQ));
		Intent FAQIntent = new Intent(getApplicationContext(), AboutApplicationFAQ.class);
		faqspec.setContent(FAQIntent);

		TabSpec dayspec = tabHost.newTabSpec(getString(R.string.theDayview));
		 dayspec.setIndicator(getString(R.string.theDayview));
		Intent DayIntent = new Intent(getApplicationContext(), AboutApplicationDayActivity.class);
		dayspec.setContent(DayIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(calspec);
		tabHost.addTab(dayspec);
		tabHost.addTab(faqspec);

		Initialize(savedInstanceState);
	}

	private void Initialize(Bundle savedInstanceState) {
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.aboutApplication));
	}

	public void bottomBackClick(View v) {
		this.finish();
	}

}
