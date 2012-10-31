package se.forsmark.visit;

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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dayview);
		Bundle extras = getIntent().getExtras();

		Initialize(extras);
	}

	public void Initialize(Bundle extras) {
		curDate = extras.getInt("DATE");
		curMonth = extras.getInt("MONTH");
		curYear = extras.getInt("YEAR");

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
        	if(!day.getSeats().equals(getString(R.string.noSeats))){
        		Intent dialog = new Intent(v.getContext(), AttendantsDialogActivity.class);
				dialog.putExtra("DATE", curDate);
				dialog.putExtra("MONTH", curMonth);
				dialog.putExtra("START", day.getStart());
				dialog.putExtra("SEATS", Integer.parseInt(day.getSeats()));
				dialog.putExtra("END", day.getEnd());
				startActivity(dialog);
        	}
        	
        	//TODO RESARVERA PLATSER!!!
		}
    };

	public void printEvents(int extra) {
		String[] events = getDateInfo(extra);
		String temp;

		array.clear();
		if (events.length >= 4) {
			for (int i = 0; i < events.length; i = i + 4) {
				if (Integer.parseInt(events[i + 2]) < 1) {
					temp = this.getString(R.string.noSeats);
				} else {
					temp = events[i + 2];
				}
				array.add(new DayListItem(events[i + 1].substring(0,5), temp, events[i + 3].substring(0,5)));
			}

			adapter.notifyDataSetChanged();

			String[] mon = this.getResources().getStringArray(R.array.calMonthStringsSwe);
			curYear = Integer.parseInt(events[0].substring(0, 4));
			curMonth = Integer.parseInt(events[0].substring(5, 7)) - 1;
			curDate = Integer.parseInt(events[0].substring(8, 10));
			tvDate.setText(curDate + " " + mon[curMonth]);
			tvYear.setText(String.valueOf(curYear));
		} else {
			Toast.makeText(this, this.getString(R.string.noticeNoDayEvents), Toast.LENGTH_SHORT).show();
			if(extra == 1){
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
			}
			else{
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
		Log.v("curDate",""+curDate);
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
		//Log.v("curDate", ""+curDate);
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
		String result = "";
		InputStream is = null;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					this.getString(R.string.httpRequestUrl));

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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		result = result.replace("[", "");
		result = result.replace("]", "");
		result = result.replace("\"", "");
		Log.v("r", "r: " + result);
		String[] tmp = result.split(",");
		return tmp;
	}
}
