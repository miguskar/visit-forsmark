package se.forsmark.visit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ContactForsmarkActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contactforsmarkview);

		Initialize(savedInstanceState);
	}

	private void Initialize(Bundle savedInstanceState) {
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.contactForsmark));
		
		TextView telTitleOpenTv = (TextView) findViewById(R.id.telOpenTitleTextView);
		TextView telTitleTv = (TextView) findViewById(R.id.telTitleTextView);
		TextView telOpenTv = (TextView) findViewById(R.id.telOpenTextView);
		TextView telTv = (TextView) findViewById(R.id.telTextView);
		TextView mailTv = (TextView) findViewById(R.id.mailTextView);
		TextView webTv = (TextView) findViewById(R.id.webPageTextView);
		
		
		telTitleOpenTv.setVisibility(View.VISIBLE);
		telTitleTv.setVisibility(View.VISIBLE);
		telOpenTv.setVisibility(View.VISIBLE);
		
		telOpenTv.setText(getString(R.string.forsmarkTelOpen));
		telTv.setText(getString(R.string.forsmarkTel));
		mailTv.setText(getString(R.string.forsmarkMail));
		webTv.setText(getString(R.string.forsmarkVisitWebPage));
		
	}

	public void bottomBackClick(View v) {
		this.finish();
	}
}
