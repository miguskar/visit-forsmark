package se.forsmark.visit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalenderActivity extends Activity {

	private int curMonth;
	private int curYear;
	private TextView tvMonth;
	private TextView tvYear;

	// STATES
	// N = next month
	// P = previous month
	// F = full
	// E = empty
	// S = seats!??!

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calenderview);
		Initialize(savedInstanceState);
	}

	@Override
	protected void onResume() {

		super.onResume();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, curYear);
		c.set(Calendar.MONTH, curMonth);
		c.set(Calendar.WEEK_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_WEEK, 2);
		setCalendar(c);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("YEAR", curYear);
		outState.putInt("MONTH", curMonth);
		super.onSaveInstanceState(outState);
	}

	public void Initialize(Bundle savedInstanceState) {

		// Set init values of the Calendar
		Calendar c = Calendar.getInstance();
		if (savedInstanceState == null) {
			curMonth = c.get(Calendar.MONTH);
			curYear = c.get(Calendar.YEAR);
		} else {
			curYear = savedInstanceState.getInt("YEAR");
			curMonth = savedInstanceState.getInt("MONTH");
			c.set(Calendar.YEAR, curYear);
			c.set(Calendar.MONTH, curMonth);
		}
		c.set(Calendar.WEEK_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_WEEK, 2);
		// Set visibility and initialize textviews and buttons
		tvMonth = (TextView) findViewById(R.id.border_title);
		tvYear = (TextView) findViewById(R.id.border_small);
		Button bb = (Button) findViewById(R.id.button_back_top);
		Button bn = (Button) findViewById(R.id.button_next_top);
		tvYear.setVisibility(View.VISIBLE);
		bn.setVisibility(View.VISIBLE);
		bb.setVisibility(View.VISIBLE);

		// Initialize calendar
		setCalendar(c);
	}

	public void setCalendar(Calendar c) {
		String[] dateInfo = getDateInfo();
		if (dateInfo[0].equals("NOCONNECTION")) {
			Toast.makeText(getApplicationContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
			dateInfo = null;
		} else if (dateInfo[0].equals("NORESULT")) {
			Toast.makeText(getApplicationContext(), R.string.noResultDatabase, Toast.LENGTH_SHORT).show();
			dateInfo = null;
		}
		LinearLayout cal = (LinearLayout) findViewById(R.id.calendarTable);
		LinearLayout row;
		Button dateCell;
		TextView restCell;

		String[] days = getResources().getStringArray(R.array.calDaysStringsSwe);
		String[] month = getResources().getStringArray(R.array.calMonthStringsSwe);

		tvMonth.setText(month[curMonth]);
		tvYear.setText(String.valueOf(curYear));

		row = (LinearLayout) findViewById(R.id.weekDays);

		for (int k = 0; k < 8; k++) {
			restCell = (TextView) row.getChildAt(k);
			restCell.setText(days[k]);
		}
		String info;
		for (int i = 0; i < 6; i++) {
			row = (LinearLayout) cal.getChildAt(i);

			for (int k = 0; k < 8; k++) {
				if (k > 0) {
					dateCell = (Button) row.getChildAt(k);
					if (c.get(Calendar.MONTH) != curMonth) {
						dateCell.setBackgroundResource(R.drawable.cell_next_month);
						dateCell.setTextColor(Color.GRAY);

						if (c.get(Calendar.MONTH) < curMonth) {
							if (curYear == c.get(Calendar.YEAR))
								dateCell.setTag("P");
							else
								dateCell.setTag("N");
						} else if (c.get(Calendar.MONTH) > curMonth)
							if (curYear == c.get(Calendar.YEAR))
								dateCell.setTag("N");
							else
								dateCell.setTag("P");
					} else {
						dateCell.setTextColor(Color.BLACK);
						if (dateInfo != null) {
							info = dateInfo[c.get(Calendar.DATE) - 1];
							if (info.equals("E")) {
								dateCell.setBackgroundResource(R.drawable.cell_empty);
							} else if (info.equals("F")) {
								dateCell.setBackgroundResource(R.drawable.cell_full);
							} else if (info.equals("S")) {
								dateCell.setBackgroundResource(R.drawable.cell_normal);
							}
							dateCell.setTag(info);
						} else {
							dateCell.setBackgroundResource(R.drawable.cell_empty);
							dateCell.setTag("E");
						}
					}

					dateCell.setText(String.valueOf(c.get(Calendar.DATE)));
					if (c.get(Calendar.MONTH) == Calendar.FEBRUARY && curYear % 4 != 0 && c.get(Calendar.DATE) == 28) {
						c.add(Calendar.DATE, +2);
						if (c.get(Calendar.DATE) == 2)
							c.add(Calendar.DATE, -1);
					} else {
						c.add(Calendar.DATE, +1);
					}
				} else {
					restCell = (TextView) row.getChildAt(k);
					restCell.setText(String.valueOf(c.get(Calendar.WEEK_OF_YEAR)));
				}
			}
		}
	}

	public void onDateClick(View v) {
		String tag = (String) v.getTag();
		if (tag.equals("N")) {
			topNextButton(null);
		} else if (tag.equals("P")) {
			topBackButton(null);
		} else if (tag.equals("F") || tag.equals("S")) {
			Intent dayView = new Intent(getApplicationContext(), DayActivity.class);
			TextView tv = (TextView) v;
			dayView.putExtra("DATE", Integer.parseInt(tv.getText().toString()));
			dayView.putExtra("MONTH", curMonth);
			dayView.putExtra("YEAR", curYear);
			startActivity(dayView);
		}
	}

	public void topBackButton(View v) {
		Calendar c = Calendar.getInstance();
		--curMonth;
		if (curMonth < 0) {
			curMonth = 11;
			--curYear;
		}
		c.set(Calendar.YEAR, curYear);
		c.set(Calendar.MONTH, curMonth);
		c.set(Calendar.WEEK_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_WEEK, 2);
		setCalendar(c);
	}

	public void topNextButton(View v) {
		Calendar c = Calendar.getInstance();
		++curMonth;
		if (curMonth > 11) {
			curMonth = 0;
			++curYear;

		}
		c.set(Calendar.YEAR, curYear);
		c.set(Calendar.MONTH, curMonth);
		c.set(Calendar.WEEK_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_WEEK, 2);
		setCalendar(c);
	}

	public void bottomBackClick(View v) {
		finish();
	}

	public String[] getDateInfo() {
		String[] tmp = { "NOCONNECTION" };
		if (isNetworkConnected()) {
			String result = "";
			InputStream is = null;
			// http post
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(getResources().getString(R.string.httpRequestUrl));

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("case", "getMonthInfo"));
				pairs.add(new BasicNameValuePair("month", "" + (curMonth + 1)));
				pairs.add(new BasicNameValuePair("year", "" + curYear));
				httppost.setEntity(new UrlEncodedFormEntity(pairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			// convert response to string
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
				result = result.replace("\"", "");

				tmp = result.split(",");
			} catch (StringIndexOutOfBoundsException e) {
				tmp[0] = "NORESULT";
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
		}
		return tmp;
	}

	private boolean isNetworkConnected() {
		getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}
}