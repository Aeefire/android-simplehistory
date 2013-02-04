package at.sapps.simplehistorylib;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.simplehistorylib.R;

public class HistoryAdapter extends ArrayAdapter<Entry> {
	private final Activity ctx;
	private List<Entry> entries;
	private final int rowlayout;

	public HistoryAdapter(Activity ctx, int rowlayout, List<Entry> entries) {
		super(ctx, rowlayout, entries);
		this.ctx = ctx;
		this.entries = entries;
		this.rowlayout = rowlayout;
	}

	static class ViewHolder {
		public TextView tvFilename;
		public TextView tvFilepath;
		public TextView tvDate;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = ctx.getLayoutInflater();
			rowView = inflater.inflate(rowlayout, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tvFilename = (TextView) rowView
					.findViewById(R.id.tvFilename);
			viewHolder.tvFilepath = (TextView) rowView
					.findViewById(R.id.tvFilepath);
			viewHolder.tvDate = (TextView) rowView.findViewById(R.id.tvDate);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		Entry temp = entries.get(position);
		holder.tvFilename.setText(temp.getFilename());
		holder.tvFilepath.setText(temp.getFilepath());
		holder.tvDate.setText(temp.getBeautifulDate());

		return rowView;
	}

	public void sort() {
		Collections.sort(entries);
	}

	public void addAll(List<Entry> collection) {

		for (Entry temp : collection) {
			super.add(temp);
		}
		this.entries = collection;
		super.notifyDataSetChanged();
	}

/*	@Override
	public void clear() {
		synchronized (ctx) {
			if (entries != null) {
				entries.clear();
			}
		}
	} */

}
