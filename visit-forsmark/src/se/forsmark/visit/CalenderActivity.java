package se.forsmark.visit;

import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalenderActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calenderview);
		
		//View v = View.inflate(this, R.layout.calenderview, null);
		//RelativeLayout l = (RelativeLayout) findViewById(R.id.activity_container);
		//l.addView(v);

		LinearLayout cal = (LinearLayout) findViewById(R.id.calendarTable);
		LinearLayout row;
		TextView cell;

		Calendar c = Calendar.getInstance();
		c.set(Calendar.WEEK_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_WEEK, 2);
		
		String[] s = this.getResources().getStringArray(R.array.calStringsSwe);
		row = (LinearLayout) cal.getChildAt(0);
		
		for (int k = 0; k < 8; k++) {
			cell = (TextView) row.getChildAt(k);
			cell.setText(s[k]);
		}
		
		for (int i = 1; i < 7; i++) {
			row = (LinearLayout) cal.getChildAt(i);

			for (int k = 0; k < 8; k++) {
				cell = (TextView) row.getChildAt(k);

				if (k > 0) {
					cell.setText(String.valueOf(c.get(Calendar.DATE)));
					c.add(Calendar.DATE, +1);
				} else {
					cell.setText(String.valueOf(c.get(Calendar.WEEK_OF_YEAR)));
				}
			}
		}
	}
}
