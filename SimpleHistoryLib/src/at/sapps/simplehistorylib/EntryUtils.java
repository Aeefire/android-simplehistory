package at.sapps.simplehistorylib;

import java.io.File;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class EntryUtils {
	/**
	 * @param entry needs to contain the WHOLE filepath (including filename)
	 * @return Intent which can be used with startActivity(intent) to open the file
	 */
	public static Intent getLaunchIntent(Entry entry){
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(entry.getFilepath());

		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName()
				.substring(file.getName().indexOf(".") + 1).toLowerCase(Locale.US);
		String type = mime.getMimeTypeFromExtension(ext);
		intent.setDataAndType(Uri.fromFile(file), type);
		return intent;
	}

}
