package se.forsmark.visit;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CalenderActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calenderview);

		TableLayout cal = (TableLayout) findViewById(R.id.calendarTable);
		TableRow row;
		TextView cell;

		Calendar c = Calendar.getInstance();
		c.set(Calendar.WEEK_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_WEEK, 2);

		String append;

		for (int i = 1; i < 7; i++) {
			row = (TableRow) cal.getChildAt(i);

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
