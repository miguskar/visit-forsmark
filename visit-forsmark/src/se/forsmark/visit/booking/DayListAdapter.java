package se.forsmark.visit.booking;

import java.util.ArrayList;

import se.forsmark.visit.R;
import se.forsmark.visit.R.drawable;
import se.forsmark.visit.R.id;
import se.forsmark.visit.R.layout;
import se.forsmark.visit.R.string;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DayListAdapter extends ArrayAdapter<DayListItem> {
	private ArrayList<DayListItem> entries;
	private Activity activity;;

	public DayListAdapter(Activity a, int textViewResourceId,
			ArrayList<DayListItem> entries) {
		super(a, textViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		DayListItem day = entries.get(position);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.daylistitem, null);
			holder = new ViewHolder();
			holder.item1 = (TextView) v.findViewById(R.id.itemStart);
			holder.item2 = (TextView) v.findViewById(R.id.itemSeats);
			holder.item3 = (TextView) v.findViewById(R.id.itemEnd);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (day != null) {
			if(day.getSeats().equals(activity.getString(R.string.noSeats))){
				v.setBackgroundResource(R.drawable.daylistitem_full);
			}else{
				v.setBackgroundResource(R.drawable.daylistitem_normal);
			}
			holder.item1.setText(day.getStart());
			holder.item2.setText(day.getSeats());
			holder.item3.setText(day.getEnd());
		}
		return v;
	}

	public static class ViewHolder {
		public TextView item1;
		public TextView item2;
		public TextView item3;
	}
}