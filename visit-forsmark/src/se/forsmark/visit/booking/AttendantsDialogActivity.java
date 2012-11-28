package se.forsmark.visit.booking;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import se.forsmark.visit.R;
import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AttendantsDialogActivity extends Activity {
	private static int TERMS_ACTIVITY = 1337;
	private int maxSeats, id;
	private EditText ed;
	private String DATE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attendatsdialog);
		int curseats = 1;
		if (savedInstanceState != null) {
			curseats = savedInstanceState.getInt("CURSEATS");
		}
		Initialize(getIntent().getExtras(), curseats);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		String s = ed.getText().toString();
		int i = 1;
		if (!s.equals("")) {
			i = Integer.parseInt(s);
		}
		outState.putInt("CURSEATS", i);
		super.onSaveInstanceState(outState);
	}

	public void Initialize(Bundle extras, int curseats) {
		id = extras.getInt("ID");
		int date = extras.getInt("DATE");
		int month = extras.getInt("MONTH");
		int year = extras.getInt("YEAR");
		maxSeats = extras.getInt("SEATS");
		String start = extras.getString("START");
		String end = extras.getString("END");

		ed = (EditText) findViewById(R.id.numAtt);
		ed.setFilters(new InputFilter[] { new InputFilterMinMax(0, maxSeats) });
		TextView tvDate = (TextView) findViewById(R.id.border_title);
		TextView tvTime = (TextView) findViewById(R.id.border_small);
		tvTime.setVisibility(View.VISIBLE);

		String[] mon = getResources().getStringArray(R.array.calMonthStringsSwe);
		tvDate.setText(String.format(getString(R.string.dateFormat), date, mon[month]));
		tvTime.setText(start + " - " + end);
		
		String formatDate = "";
		if (date < 10)
			formatDate = "0";
		
		formatDate += date;
		
		String formatMonth = "";
		if (month < 10)
			formatMonth= "0";
		
		formatMonth += month;
		
		DATE = String.format("%s-%s-%s %s %s", year, formatMonth, formatDate, start, end);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TERMS_ACTIVITY) {
			if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
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
				int input = Integer.parseInt(dest.toString() + source.toString());
				if (isInRange(min, max, input))
					return null;
				input = Integer.parseInt(source.toString() + dest.toString());
				if (isInRange(min, max, input))
					return null;
			} catch (NumberFormatException nfe) {
			}

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
		if (!s.equals("") && (i = Integer.parseInt(s)) < maxSeats) {
			ed.setText(String.valueOf(++i));
		}
	}

	public void cancelButton(View v) {
		finish();
	}

	public void nextButton(View v) {
		String s = ed.getText().toString();
		int seats;
		if (!s.equals("") && (seats = Integer.parseInt(s)) > 0) {
			if (seats <= maxSeats) {
				String resKey = preBook(seats);
				Log.e("reservationKey", resKey);
				if (resKey.equals("NOCONNECTION")) {
					Toast.makeText(getApplicationContext(), R.string.noInternet, Toast.LENGTH_LONG).show();
				} else if (resKey.equals("NORESULT")) {
					Toast.makeText(getApplicationContext(), R.string.noResultDatabase, Toast.LENGTH_LONG).show();
				} else {
					startContactActivity(resKey, seats);
				}
			} else {
				Log.e("toManySeats", seats + "");
				Toast.makeText(getApplicationContext(), getString(R.string.ToManySeats), Toast.LENGTH_LONG).show();
			}
		} else {
			Log.e("wrongValue", s + "");
			Toast.makeText(getApplicationContext(), getString(R.string.WrongIntValue), Toast.LENGTH_LONG).show();
		}
	}

	// TODO TOASTA
	public void startContactActivity(String message, int seats) {
		if (!message.contains("fail")) {
			DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
			db.open();
			db.addBooking(message, DATE);
			db.close();
			Intent terms = new Intent(getApplicationContext(), TermsActivity.class);
			terms.putExtra("eventId", id);
			terms.putExtra("bookingId", message);
			terms.putExtra("seats", seats);
			startActivityForResult(terms, TERMS_ACTIVITY);
		} else {
			Log.e("PHP-FAIL: ", message);
		}
	}

	public String preBook(int seats) {
		String result = "NOCONNECTION";
		InputStream is = null;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(this.getString(R.string.httpRequestUrl));

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();

			pairs.add(new BasicNameValuePair("case", "preBook"));
			pairs.add(new BasicNameValuePair("id", "" + id));
			pairs.add(new BasicNameValuePair("seats", "" + seats));

			httppost.setEntity(new UrlEncodedFormEntity(pairs));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
			result = result.substring(1, result.length() - 2);
		} catch (StringIndexOutOfBoundsException e) {
			result = "NORESULT";
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		return result;
	}
}
