package se.forsmark.visit.booking;

import java.io.BufferedReader;	
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
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
import se.forsmark.visit.R.array;
import se.forsmark.visit.R.id;
import se.forsmark.visit.R.layout;
import se.forsmark.visit.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DayActivity extends Activity {
	private TextView tvDate;
	private TextView tvYear;
	private int curMonth;
	private int curYear;
	private int curDate;
	private ArrayList<DayListItem> array;
	private DayListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dayview);
		Bundle extras = getIntent().getExtras();

		Initialize(extras, savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("YEAR", curYear);
		outState.putInt("MONTH", curMonth);
		outState.putInt("DATE", curDate);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		printEvents(0);
	}

	public void Initialize(Bundle extras, Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			curDate = extras.getInt("DATE");
			curMonth = extras.getInt("MONTH");
			curYear = extras.getInt("YEAR");
		} else {
			curDate = savedInstanceState.getInt("DATE");
			curMonth = savedInstanceState.getInt("MONTH");
			curYear = savedInstanceState.getInt("YEAR");
		}
		Button bb = (Button) findViewById(R.id.button_back_top);
		Button bn = (Button) findViewById(R.id.button_next_top);
		tvDate = (TextView) findViewById(R.id.border_title);
		tvYear = (TextView) findViewById(R.id.border_small);

		bn.setVisibility(View.VISIBLE);
		bb.setVisibility(View.VISIBLE);
		tvYear.setVisibility(View.VISIBLE);

		array = new ArrayList<DayListItem>();

		adapter = new DayListAdapter(this, R.id.dayItemList, array);

		ListView list = (ListView) findViewById(R.id.dayItemList);

		list.setAdapter(adapter);

		list.setOnItemClickListener(itemListener);

		printEvents(0);

	}

	OnItemClickListener itemListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			DayListItem day = array.get(pos);
			Date todayDate = new Date();
			int startHour = Integer.valueOf(day.getStart().substring(0, 2));
			int startMin = Integer.valueOf(day.getStart().substring(3, 5));
			Date theDate = new Date(curYear - 1900, curMonth, curDate, startHour, startMin);
			Log.v("ThedateDate", theDate.toString());
			Log.v("TodayDate", todayDate.toString());

			if (theDate.compareTo(todayDate) > 0) {
				if (!day.getSeats().equals(getString(R.string.noSeats))) {
					Intent dialog = new Intent(getApplicationContext(), AttendantsDialogActivity.class);
					dialog.putExtra("ID", day.getId());
					dialog.putExtra("DATE", curDate);
					dialog.putExtra("MONTH", curMonth);
					dialog.putExtra("YEAR", curYear);
					dialog.putExtra("START", day.getStart());
					dialog.putExtra("SEATS", Integer.parseInt(day.getSeats()));
					dialog.putExtra("END", day.getEnd());
					startActivity(dialog);
				}
			} else {
				Toast.makeText(getApplicationContext(), R.string.backInTime, Toast.LENGTH_LONG).show();
			}
		}
	};

	public void printEvents(int extra) {
		String[] events = getDateInfo(extra);
		String temp;

		array.clear();
		if (!events[0].equals("NOCONNECTION")) {
			if (!events[0].equals("NORESULT")) {
				if (events.length >= 5) {
					for (int i = 0; i < events.length; i = i + 5) {
						if (Integer.parseInt(events[i + 3]) < 1) {
							temp = getResources().getString(R.string.noSeats);
						} else {
							temp = events[i + 3];
						}
						array.add(new DayListItem(Integer.valueOf(events[i]), events[i + 2].substring(0, 5), temp,
								events[i + 4].substring(0, 5)));
					}

					adapter.notifyDataSetChanged();

					String[] mon = getResources().getStringArray(R.array.calMonthStringsSwe);
					curYear = Integer.parseInt(events[1].substring(0, 4));
					curMonth = Integer.parseInt(events[1].substring(5, 7)) - 1;
					curDate = Integer.parseInt(events[1].substring(8, 10));
					tvDate.setText(curDate + " " + mon[curMonth]);
					tvYear.setText(String.valueOf(curYear));
				} else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.noticeNoDayEvents),
							Toast.LENGTH_LONG).show();
					if (extra == 1) {
						int date = new Date(curYear, curMonth + 1, 0).getDate();
						++curDate;
						if (curDate > date) {
							++curMonth;
							curDate = 1;
							if (curMonth > 11) {
								curMonth = 0;
								++curYear;
							}
						}
					} else {
						--curDate;
						if (curDate < 1) {
							--curMonth;
							if (curMonth < 0) {
								curMonth = 11;
								--curYear;
							}
							curDate = new Date(curYear, curMonth + 1, 0).getDate();
						}
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), R.string.noResultDatabase, Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), R.string.noInternet, Toast.LENGTH_LONG).show();
		}
	}

	public void topBackButton(View v) {
		--curDate;

		if (curDate < 1) {
			--curMonth;
			if (curMonth < 0) {
				curMonth = 11;
				--curYear;
			}
			curDate = new Date(curYear, curMonth + 1, 0).getDate();
		}
		printEvents(1);
	}

	public void topNextButton(View v) {
		int temp = new Date(curYear, curMonth + 1, 0).getDate();
		++curDate;
		if (curDate > temp) {
			++curMonth;
			curDate = 1;
			if (curMonth > 11) {
				curMonth = 0;
				++curYear;
			}
		}
		printEvents(2);
	}

	public void bottomBackClick(View v) {
		finish();
	}

	public String[] getDateInfo(int extra) {
		String[] tmp = { "NOCONNECTION" };
		if (isNetworkConnected()) {
			String result = "";
			InputStream is = null;
			// http post
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(getResources().getString(R.string.httpRequestUrl));

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("case", "getDayInfo"));
				pairs.add(new BasicNameValuePair("extra", "" + extra));
				pairs.add(new BasicNameValuePair("date", "" + (curDate)));
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
				result = result.replace("[", "");
				result = result.replace("]", "");
				result = result.replace("\"", "");
				Log.v("r", "r: " + result);
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
