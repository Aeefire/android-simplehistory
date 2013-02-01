package at.sapps.simplehistorylib;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.util.Log;

public class Entry {

	private static final String TAG = "simplehistorylib.Entry";

	private int id;
	private String filename;
	private String filepath;
	private String date;

	public Entry(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;

		this.date = getCurrentDate(System.currentTimeMillis());
	}

	private String getCurrentDate(long current) {
		Calendar cal = new GregorianCalendar(Locale.getDefault());
		cal.setTimeInMillis(current);

		String date = cal.get(Calendar.DATE) + "-" + cal.get(Calendar.MONTH)
				+ "-" + cal.get(Calendar.YEAR);

		Log.d(TAG, "date generated: " + date);
		return date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getDate() {
		return date;
	}

	public void setDate(long millis) {
		this.date = getCurrentDate(millis);
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Entry [id=" + id + ", filename=" + filename + ", filepath="
				+ filepath + ", date=" + date + "]";
	}
}
