package se.forsmark.visit;

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
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
		setContentView(R.layout.dayview); // CHANGE
		Bundle extras = getIntent().getExtras();

		Initialize(extras);
	}

	public void Initialize(Bundle extras) {
		curDate = extras.getInt("DATE");
		curMonth = extras.getInt("MONTH");
		curYear = extras.getInt("YEAR");

		String[] mon = this.getResources().getStringArray(
				R.array.calMonthStringsSwe);

		tvDate = (TextView) findViewById(R.id.border_title);
		tvYear = (TextView) findViewById(R.id.border_small);
		tvDate.setText(curDate + " " + mon[curMonth]);
		tvYear.setVisibility(View.VISIBLE);
		tvYear.setText(String.valueOf(curYear));

		Button bb = (Button) findViewById(R.id.button_back_top);
		Button bn = (Button) findViewById(R.id.button_next_top);
		bn.setVisibility(View.VISIBLE);
		bb.setVisibility(View.VISIBLE);

		array = new ArrayList<DayListItem>();

		adapter = new DayListAdapter(this, R.id.dayItemList, array);

		ListView list = (ListView) findViewById(R.id.dayItemList);

		list.setAdapter(adapter);

		for (int i = 0; i < 10; i++) {
			array.add( new DayListItem("Bengt " +i, "Har", "FLESK"));
		}
		

		adapter.notifyDataSetChanged();
	}

	public void setDay() {

	}

	//
	//
	public void topBackButton(View v) {

	}

	public void topNextButton(View v) {

	}

	public String[] getDateInfo() {
		String result = "";
		InputStream is = null;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					this.getString(R.string.httpRequestUrl));

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("case", "getDayInfo"));
			pairs.add(new BasicNameValuePair("date", "" + (curDate + 1)));
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
		Log.v("r", "r: " + result);
		result = result.substring(1, result.length() - 2);
		result = result.replace("\"", "");

		String[] tmp = result.split(",");

		return tmp;
	}
}
