package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class DayActivity extends Activity{
	
	private TextView tvDate;
	private TextView tvYear;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dayview); // CHANGE
		Bundle extras = getIntent().getExtras();
		Initialize(extras);
	}
	
	public void Initialize(Bundle extras) {
		String day = extras.getString("DATE");
		int month = extras.getInt("MONTH");
		int year = extras.getInt("YEAR");
		String[] mon = this.getResources().getStringArray(R.array.calMonthStringsSwe);
		
		tvDate = (TextView) findViewById(R.id.border_title);
		tvYear = (TextView) findViewById(R.id.border_small);
		tvDate.setText(day + " " + mon[month]);
		tvYear.setVisibility(View.VISIBLE);
		tvYear.setText(String.valueOf(year));
		
		Button bb = (Button) findViewById(R.id.button_back_top);
		Button bn = (Button) findViewById(R.id.button_next_top);
		bn.setVisibility(View.VISIBLE);
		bb.setVisibility(View.VISIBLE);
	}
	
	public void topBackButton(View v) {
		
	}

	public void topNextButton(View v) {
		
	}
}
