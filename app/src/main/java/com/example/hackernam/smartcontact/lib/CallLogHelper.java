package com.example.hackernam.smartcontact.lib;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;

import java.util.Calendar;

/**
 * Created by HackerNam on 6/16/2016.
 */
public class CallLogHelper {
    public static Cursor getCallLogByDateCursor(ContentResolver calllogHelper,
                                                Integer Thang, Integer Nam) {
        Uri callUri = Uri.parse("content://call_log/calls");
        String[] projection = {CallLog.Calls._ID,CallLog.Calls.CACHED_NAME,CallLog.Calls.NUMBER,CallLog.Calls.TYPE ,CallLog.Calls.DATE,CallLog.Calls.DURATION};
        String mSelectionClause = CallLog.Calls.DATE + " <= ?";
        String[] mSelectionArgs = {createDate(Nam, Thang, 1).toString()};

        Cursor cur = null;

        try {
            cur = calllogHelper.query(callUri, projection, mSelectionClause, mSelectionArgs, null);
            cur.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cur;
    }

    public static Long createDate(int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);

        return calendar.getTimeInMillis();

    }
}
