package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class AttendantsDialogActivity extends Activity {

	private int maxSeats;
	private EditText ed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attendatsdialog);

		Initialize(getIntent().getExtras());
	}

	public void Initialize(Bundle extras) {
		int date = extras.getInt("DATE");
		int month = extras.getInt("MONTH");
		maxSeats = extras.getInt("SEATS");
		String start = extras.getString("START");
		String end = extras.getString("END");

		ed = (EditText) findViewById(R.id.numAtt);
		ed.setFilters(new InputFilter[] {new InputFilterMinMax(0, maxSeats)});
		TextView tvDate = (TextView) findViewById(R.id.border_title);
		TextView tvTime = (TextView) findViewById(R.id.border_small);
		tvTime.setVisibility(View.VISIBLE);

		String[] mon = this.getResources().getStringArray(
				R.array.calMonthStringsSwe);
		tvDate.setText(this.getString(R.string.dialogThe) + " " + date + " "
				+ mon[month]);
		tvTime.setText(start + " - " + end);
	}

	public class InputFilterMinMax implements InputFilter {
		 
		private int min, max;
	 
		public InputFilterMinMax(int min, int max) {
			this.min = min;
			this.max = max;
		}
	 
		public InputFilterMinMax(String min, String max) {
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {	
			try {
				Log.v("dest", dest.toString());
				Log.v("sourse", source.toString());
				int input = Integer.parseInt(dest.toString() + source.toString());
				Log.v("inpyt", ""+input);
				if (isInRange(min, max, input))
					return null;
				input = Integer.parseInt(source.toString() + dest.toString());
				if (isInRange(min, max, input))
					return null;
			} catch (NumberFormatException nfe) { }	
			
			return "";
		}
	 
		private boolean isInRange(int a, int b, int c) {
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	};

	public void decreaseAtt(View v) {
		String s = ed.getText().toString();
		int i;
		if (!s.equals("") && (i = Integer.parseInt(s)) > 0) {
			ed.setText(String.valueOf(--i));
		}
	}

	public void increaseAtt(View v) {
		String s = ed.getText().toString();
		int i;
		if (!s.equals("") && (i = Integer.parseInt(s))< maxSeats) {
			ed.setText(String.valueOf(++i));
		}
	}

	public void cancelButton(View v) {
		finish();
	}

	public void nextButton(View v) {

	}
}
