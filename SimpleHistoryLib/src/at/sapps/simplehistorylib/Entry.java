package at.sapps.simplehistorylib;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.util.Log;

public class Entry {

	private static final String TAG = "simplehistorylib.Entry";

	/* database fields */
	private int id;
	private String filename;
	private String filepath;
	private String date;
	private DateFormat formatter = DateFormat.getDateTimeInstance();

	/* constants */
	public static final String DATE_DELIMITER = "-";

	public Entry(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;

		this.date = getCurrentDate(System.currentTimeMillis());
	}

	private String getCurrentDate(long current) {
		Calendar cal = new GregorianCalendar(Locale.getDefault());
		cal.setTimeInMillis(current);

		StringBuilder builder = new StringBuilder();
		builder.append(cal.get(Calendar.HOUR_OF_DAY)).append(DATE_DELIMITER);
		builder.append(cal.get(Calendar.MINUTE)).append(DATE_DELIMITER);
		builder.append(cal.get(Calendar.DATE)).append(DATE_DELIMITER);
		builder.append(cal.get(Calendar.MONTH)).append(DATE_DELIMITER);
		builder.append(cal.get(Calendar.YEAR));

		Log.d(TAG, "date generated: " + builder.toString());
		return builder.toString();
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

	public String getBeautifulDate() {
		String[] dateParts = date.split(DATE_DELIMITER);
		Date date = new Date(Integer.valueOf(dateParts[4])-1900, Integer.valueOf(dateParts[3]),
				Integer.valueOf(dateParts[2]), Integer.valueOf(dateParts[0]),
				Integer.valueOf(dateParts[1]));

		return formatter.format(date);

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
