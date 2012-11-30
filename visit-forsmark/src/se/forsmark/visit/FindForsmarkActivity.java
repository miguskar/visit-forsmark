package se.forsmark.visit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class FindForsmarkActivity extends Activity{// implements OnClickListener {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.findforsmarkview);

		Initialize(savedInstanceState);
	}
//
	private void Initialize(Bundle savedInstanceState) {
		TextView title = (TextView) findViewById(R.id.border_title);
		title.setText(getString(R.string.findForsmark));
		//set map image
		ImageView im= (ImageView) findViewById(R.id.imageViewMap);
	
		
	}	
//		Button map = (Button)findViewById(R.id.mapButton);  
//		map.setOnClickListener(new OnClickListener() {  
//		    @Override  
//		    public void onClick(View v) {  
//		        // TODO: start map Activity here  
//		    }  
//		});
//	}
//
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		
//	}
	
	public void bottomBackClick(View v) {
		this.finish();
	}

}
