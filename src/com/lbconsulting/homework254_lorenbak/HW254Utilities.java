package com.lbconsulting.homework254_lorenbak;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Spinner;

public class HW254Utilities {

	public static final String TAG = "AList";

	public static int boolToInt(boolean b) {
		return b ? 1 : 0;
	}

	public static boolean intToBoolean(int value) {
		if (value == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static long boolToLong(boolean b) {
		return b ? 1 : 0;
	}

	public static boolean longToBoolean(long value) {
		if (value == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Execute all of the SQL statements in the ArrayList<String>.
	 * 
	 * @param db
	 *            The database on which to execute the statements.
	 * @param sqlStatements
	 *            An array of SQL statements to execute.
	 */
	public static void execMultipleSQL(SQLiteDatabase db,
			ArrayList<String> sqlStatements) {
		for (String statement : sqlStatements) {
			if (statement.trim().length() > 0) {
				db.execSQL(statement);
			}
		}
	}

	public static int GetColorInt(String ColorString) {
		int colorInt = 0;
		try {
			colorInt = Color.parseColor(ColorString);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in GetColorInt. ", e);
		}
		return colorInt;
	}

	public static String GetColorString(int ColorInt) {
		String colorString = null;
		try {
			colorString = String.format("#%06X", 0xFFFFFF & ColorInt);

		} catch (Exception e) {
			Log.e(TAG, "An Exception error occurred in GetColorString. ", e);
		}
		return colorString;
	}

	@SuppressWarnings("resource")
	public static int getIndex(Spinner spinner, long itemID) {
		Cursor spinnerItem = null;
		long spinnerItemID = -1;
		int spinnerIndex = -1;

		for (int i = 0; i < spinner.getCount(); i++) {
			spinnerItem = (Cursor) spinner.getItemAtPosition(i);
			spinnerItemID = spinnerItem.getLong(spinnerItem.getColumnIndexOrThrow("_id"));
			if (spinnerItemID == itemID) {
				spinnerIndex = i;
				break;
			}
		}

		// Cannot close the spinnerItem
		// It is used in the spinnter
		/*if (spinnerItem != null) {
			spinnerItem.close();
		}*/
		return spinnerIndex;
	}

	public static int getPositionById(Cursor cursor, long theTargetId) {

		int theWantedPosition = -1;
		if (cursor != null && theTargetId > 0) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					if (cursor.getLong(0) == theTargetId) {
						theWantedPosition = cursor.getPosition();
						break;
					}
					cursor.moveToNext();
				}
			}
		}
		return theWantedPosition;
	}

	public static String formatInt(int number) {
		return NumberFormat.getNumberInstance(Locale.US).format(number);
	}

	public static String formatDateTime(Context context, String timeToFormat) {

		String finalDateTime = "";

		SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date date = null;
		if (timeToFormat != null) {
			try {
				date = iso8601Format.parse(timeToFormat);
			} catch (Exception e) {
				date = null;
			}

			if (date != null) {
				long when = date.getTime();
				int flags = 0;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
				flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
				flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

				finalDateTime = android.text.format.DateUtils.formatDateTime(context,
						when + TimeZone.getDefault().getOffset(when), flags);
			}
		}
		return finalDateTime;
	}

	public static String formatDateTime(long timeToFormatInMilliseconds) {

		/*SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = iso8601Format.parse(timeToFormatInMilliseconds);*/

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

		formatter.setTimeZone(TimeZone.getDefault());
		String currentDate = formatter.format(timeToFormatInMilliseconds);
		return currentDate;

	}

	/*	public static int getListsCursorPositon(Cursor listsCursor, long listID) {
			int listPosition = 0;
			if (listsCursor != null && listID > 1) {
				listsCursor.moveToPosition(-1);
				int position = 0;
				long id = 0;
				while (listsCursor.moveToNext()) {
					id = listsCursor.getLong(listsCursor.getColumnIndexOrThrow(ListsTable.COL_LIST_ID));
					if (id == listID) {
						listPosition = position;
						break;
					} else {
						position++;
					}
				}
			}
			return listPosition;
		}*/

	public static long getIdByPosition(Cursor cursor, int position) {
		long id = -1;
		if (cursor.getCount() >= position + 1) {
			cursor.moveToPosition(position);
			id = cursor.getLong(cursor.getColumnIndex("_id"));
		}

		return id;
	}

	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static int pxToDp(Context context, int px) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}
}
