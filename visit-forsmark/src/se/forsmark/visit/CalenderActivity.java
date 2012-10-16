package se.forsmark.visit;

import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalenderActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calenderview);
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.WEEK_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_WEEK, 2);
		setCalender(c); // Initialize calendar
		
		Button bb = (Button) findViewById(R.id.button_back_top);
		Button bn  = (Button) findViewById(R.id.button_next_top);
		bn.setVisibility(View.VISIBLE);
		bb.setVisibility(View.VISIBLE);
	}

	public void setCalender(Calendar c) {

		LinearLayout cal = (LinearLayout) findViewById(R.id.calendarTable);
		LinearLayout row;
		Button dateCell;
		TextView restCell;

		String[] s = this.getResources().getStringArray(R.array.calStringsSwe);
		
		row = (LinearLayout) findViewById(R.id.weekDays);

		for (int k = 0; k < 8; k++) {
			restCell = (TextView) row.getChildAt(k);
			restCell.setText(s[k]);
		}

		for (int i = 0; i < 6; i++) {
			row = (LinearLayout) cal.getChildAt(i);

			for (int k = 0; k < 8; k++) {
				if (k > 0) {
					dateCell = (Button) row.getChildAt(k);
					dateCell.setText(String.valueOf(c.get(Calendar.DATE)));
					c.add(Calendar.DATE, +1);
				} else {
					restCell = (TextView) row.getChildAt(k);
					restCell.setText(String.valueOf(c
							.get(Calendar.WEEK_OF_YEAR)));
				}
			}
		}

	}
	
	public void onDateClick(View v){
		
	}
}
