package at.sapps.simplehistorylib;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryHelper extends SQLiteOpenHelper {

	private HistoryLoadedListener listener;
	private CursorFactory cf;

	/* database stuff */
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "history.db";

	/* table definitions */
	private static final String TABLE_DOWNLOADS = "downloads";
	private static final String KEY_ID = "id";
	private static final String KEY_FILENAME = "filename";
	private static final String KEY_FILEPATH = "filepath"; // can be null
	private static final String KEY_DATE = "date";

	public HistoryHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public HistoryHelper(Context ctx, CursorFactory cf) {
		super(ctx, DATABASE_NAME, cf, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DOWNLOADS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_FILENAME + " TEXT NOT NULL, " + KEY_FILEPATH + " TEXT, "
				+ KEY_DATE + " CHAR(10) NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// upgrade of version will destroy old table, TODO: do it better?
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADS);
		onCreate(db);
	}

	public void setHistoryLoadedListener(HistoryLoadedListener l) {
		this.listener = l;
	}

	public HistoryLoadedListener getHistoryLoadedListener() {
		return listener;
	}

	private ContentValues getValuesFromEntry(Entry entry) {
		ContentValues values = new ContentValues();
		values.put(KEY_FILENAME, entry.getFilename());
		values.put(KEY_FILEPATH, entry.getFilepath());
		values.put(KEY_DATE, entry.getDate());
		return values;
	}

	private Entry getEntryFromCursor(Cursor resultCursor)
			throws NullPointerException {
		Entry temp = new Entry(resultCursor.getString(resultCursor
				.getColumnIndex(KEY_FILENAME)),
				resultCursor.getString(resultCursor
						.getColumnIndex(KEY_FILEPATH)));
		temp.setId(resultCursor.getInt(resultCursor.getColumnIndex(KEY_ID)));
		temp.setDate(resultCursor.getString(resultCursor
				.getColumnIndex(KEY_DATE)));
		return temp;
	}

	/**
	 * @param entry
	 *            specifies Entry to add
	 * @return SQLException if one occured, null if everything worked.
	 */

	public SQLException insertEntry(Entry entry) {

		ContentValues values = getValuesFromEntry(entry);

		try {
			getWritableDatabase().insertOrThrow(TABLE_DOWNLOADS, null, values);

		} catch (SQLException e) {

			e.printStackTrace();
			return e;
		}
		return null;
	}

	/**
	 * updates existing entry object (use getEntry.. to get the Object and then
	 * use the update method with it)
	 * 
	 * @param entry
	 * @return number of rows affected (0 if none)
	 */
	public int updateEntry(Entry entry) {
		int result = 0;
		ContentValues values = getValuesFromEntry(entry);
		result = getWritableDatabase().update(TABLE_DOWNLOADS, values,
				KEY_ID + "=" + entry.getId(), null);
		return result;
	}

	/**
	 * @return all entries
	 */
	public List<Entry> getAllEntries() {
		List<Entry> result = new ArrayList<Entry>();
		Cursor resultCursor;
		String statement = "SELECT * FROM " + TABLE_DOWNLOADS + " ORDER BY "
				+ KEY_ID + " DESC;";
		try {
			if (cf != null)
				resultCursor = getWritableDatabase().rawQueryWithFactory(cf,
						statement, null, TABLE_DOWNLOADS);
			else
				resultCursor = getWritableDatabase().rawQuery(statement, null);

			if (resultCursor == null)
				return null;

			resultCursor.moveToFirst();

			while (!resultCursor.isAfterLast()) {
				result.add(getEntryFromCursor(resultCursor));
				resultCursor.moveToNext();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			result = null;
		}
		if (listener != null)
			listener.onHistoryLoaded(result);
		return result;
	}

	/**
	 * @param id
	 * @return Entry-object with given id, null if SQLException occurred / no
	 *         result was retrievable
	 */
	public Entry getEntry(int id) {
		Entry result = null;

		try {
			Cursor resultCursor = getWritableDatabase().rawQuery(
					"SELECT * FROM " + TABLE_DOWNLOADS + " WHERE " + KEY_ID
							+ "=" + id + ";", null);
			if (resultCursor != null) {
				resultCursor.moveToFirst();
				result = getEntryFromCursor(resultCursor);
			} // else result-entry stays null
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			result = null;
		}
		return result;
	}

	/**
	 * @param attribute
	 *            from HistoryHelper.KEY_
	 * @param value
	 *            value of the specific attribute to look for
	 * @return list with all entries, which fit to the search construct
	 */
	public List<Entry> getEntriesByAttribute(String attribute, String value) {
		List<Entry> result = new ArrayList<Entry>();
		Cursor resultCursor = null;
		String statement = "SELECT DISTINCT * FROM " + TABLE_DOWNLOADS
				+ " WHERE " + attribute + "=" + value + " ORDER BY " + KEY_ID
				+ " DESC;";
		try {
			if (cf != null)
				resultCursor = getWritableDatabase().rawQueryWithFactory(cf,
						statement, null, TABLE_DOWNLOADS);
			else
				resultCursor = getWritableDatabase().rawQuery(statement, null);

			if (resultCursor == null)
				return null;

			resultCursor.moveToFirst();

			while (!resultCursor.isAfterLast()) {
				result.add(getEntryFromCursor(resultCursor));
				resultCursor.moveToNext();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (listener != null)
			listener.onHistoryLoaded(result);
		return result;
	}

	/**
	 * @param id
	 *            identifies entry to delete
	 * @return number of deleted entries
	 */
	public int deleteEntry(int id) {
		int result = 0;

		try {
			result = getWritableDatabase().delete(TABLE_DOWNLOADS,
					KEY_ID + "=" + id, null);

		} catch (SQLException e) {
			e.printStackTrace();
			result = 0;
		} catch (NullPointerException e) {
			result = 0;
		}
		return result;
	}

	/**
	 * DROPS THE WHOLE TABLE AND RECREATES IT!
	 */
	public void deleteAllEntries() {
		this.onUpgrade(getWritableDatabase(), DATABASE_VERSION,
				DATABASE_VERSION);
	}
}
