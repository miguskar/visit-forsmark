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

import se.forsmark.visit.database.DatabaseSQLite;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class TermsActivity extends Activity{
	
	private static int BOOKING = 10;
	private String bookingId;
	private int seats;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.termsview);
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.termsTitle));
		Bundle extras = getIntent().getExtras();
		bookingId =  extras.getString("bookingId");
		seats = extras.getInt("seats");
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == BOOKING) {
            if (resultCode == RESULT_CANCELED) {
            	deleteBooking();
            	setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
	
	public void bottomNextClick(View v){
		Intent terms = new Intent(getApplicationContext(), ContactActivity.class);
		terms.putExtra("bookingId", bookingId);
		terms.putExtra("seats", seats);
		startActivityForResult(terms, BOOKING);
	}
	
	public void bottomCancelClick(View v){
		deleteBooking();
		finish();
	}
	
	private void deleteBooking() {
		String result = "";
		InputStream is = null;
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					this.getString(R.string.httpRequestUrl));

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("case", "delete"));
			pairs.add(new BasicNameValuePair("id", "" + bookingId));
			
			httppost.setEntity(new UrlEncodedFormEntity(pairs));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
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
		Log.e("result", result);
		
		
		DatabaseSQLite db = new DatabaseSQLite(getApplicationContext());
		db.open();
		int cId = db.getLatestContactId();
		db.deleteBooking(bookingId, cId);
		db.close();
	}
	
}
