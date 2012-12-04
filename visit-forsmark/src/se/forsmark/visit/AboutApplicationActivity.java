package se.forsmark.visit;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class AboutApplicationActivity extends TabActivity {
	private TabHost tabHost;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutapplicationview);

		/** Called when the activity is first created. */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutapplicationview);

		tabHost = getTabHost();

		TabSpec calSpec = tabHost.newTabSpec(getString(R.string.theCalendar));
		calSpec.setIndicator(getString(R.string.theCalendar));
		Intent calendarIntent = new Intent(getApplicationContext(), AboutApplicationCalActivity.class);
		calSpec.setContent(calendarIntent);
		
		TabSpec faqSpec = tabHost.newTabSpec(getString(R.string.FAQ));
		faqSpec.setIndicator(getString(R.string.FAQ));
		Intent FAQIntent = new Intent(getApplicationContext(), AboutApplicationFAQ.class);
		faqSpec.setContent(FAQIntent);

		TabSpec daySpec = tabHost.newTabSpec(getString(R.string.theDayview));
		daySpec.setIndicator(getString(R.string.theDayview));
		Intent dayIntent = new Intent(getApplicationContext(), AboutApplicationDayActivity.class);
		daySpec.setContent(dayIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(calSpec);
		tabHost.addTab(daySpec);
		tabHost.addTab(faqSpec);
		
		setTabColor(tabHost);
		
		Initialize(savedInstanceState);
		
	}

	private void Initialize(Bundle savedInstanceState) {
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.aboutApplication));
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				setTabColor(tabHost);
			}
		});
	}
	
	public static void setTabColor(TabHost tabhost) {
	    for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
	    {
	    	tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.border);//unselected
	    }
	    tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.orange_border); // selected
	}

	public void bottomBackClick(View v) {
		this.finish();
	}

}
