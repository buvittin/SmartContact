package com.example.hackernam.smartcontact.lib;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;

public class ContactHelper {

	public static Cursor getContactCursor(ContentResolver contactHelper,
			String startsWith,int SapXep) {
		String ChuoiSapXep = "";
		if (SapXep == 0)
		{
			ChuoiSapXep = " ASC";
		}
		if (SapXep == 1)
		{
			ChuoiSapXep = " DESC";
		}
		String[] projection = { Phone._ID,
				Phone.DISPLAY_NAME, Phone.PHOTO_THUMBNAIL_URI,Phone.NUMBER, Phone.TYPE,Phone.LOOKUP_KEY};
		Cursor cur = null;
		try {
			if (startsWith != null && !startsWith.equals("")) {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection,
						Phone.DISPLAY_NAME
								+ " like \"" + startsWith + "%\"", null,
						Phone.DISPLAY_NAME
								+ ChuoiSapXep);
			} else {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection, null, null,
						Phone.DISPLAY_NAME
								+ ChuoiSapXep);
			}
			cur.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cur;
	}

	public static Cursor getContactByNumberCursor(ContentResolver contactHelper,
										  String number) {

		String[] projection = { Phone._ID,
				Phone.DISPLAY_NAME, Phone.PHOTO_THUMBNAIL_URI,Phone.NUMBER, Phone.TYPE,Phone.LOOKUP_KEY};
		String[] mSelectionArgs = {number};
		Cursor cur = null;
		try {
			if (number != null && !number.equals("")) {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection,
						Phone.NUMBER + " = ?", mSelectionArgs,
						Phone.DISPLAY_NAME
								+ " ASC");
			} else {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection, null, null,
						Phone.DISPLAY_NAME
								+ " ASC");
			}
			cur.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cur;
	}

	public static Cursor getContactFavouritesCursor(ContentResolver contactHelper,
													String startsWith) {

		String[] projection = { Phone._ID,
				Phone.DISPLAY_NAME, Phone.PHOTO_THUMBNAIL_URI,Phone.NUMBER, Phone.TYPE,Phone.LOOKUP_KEY};
		Cursor cur = null;

		String selection = ContactsContract.Contacts.STARRED + "='1'";

		try {
			if (startsWith != null && !startsWith.equals("")) {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection,
						selection, null,
						Phone.DISPLAY_NAME
								+ " ASC");
			} else {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection, selection, null,
						Phone.DISPLAY_NAME
								+ " ASC");
			}
			cur.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cur;
	}

	public static Cursor getContactByDateCursor(ContentResolver contactHelper,
												Integer Thang, Integer Nam) {

		String[] projection = { Phone._ID,
				Phone.DISPLAY_NAME, Phone.PHOTO_THUMBNAIL_URI,Phone.NUMBER, Phone.TYPE,Phone.LOOKUP_KEY};
		Cursor cur = null;

		try {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection, null, null,
						Phone.DISPLAY_NAME
								+ " ASC");
			cur.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cur;
	}

	public static Cursor getContactToIDCursor(ContentResolver contactHelper,
										  String PhoneID) {

		String[] projection = { Phone._ID,
				Phone.DISPLAY_NAME, Phone.PHOTO_URI,Phone.NUMBER, Phone.TYPE,Phone.LOOKUP_KEY};
		Cursor cur = null;

		try {
				cur = contactHelper.query(
						Phone.CONTENT_URI,
						projection,
						Phone._ID + " like \"" + PhoneID + "%\"", null,
						null);
			cur.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cur;
	}

	public static long getContactID(ContentResolver contactHelper,
			String number) {
		Uri contactUri = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI,
				Uri.encode(number));

		String[] projection = { Phone._ID};
		Cursor cursor = null;

		try {
			cursor = contactHelper.query(contactUri, projection, null, null,
					null);

			if (cursor.moveToFirst()) {
				int personID = cursor.getColumnIndex(Phone._ID);
				return cursor.getLong(personID);
			}

			return -1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}

		return -1;
	}

	public static boolean insertContact(ContentResolver contactAdder,
			String firstName, String mobileNumber) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation
				.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null)
				.build());
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
						firstName).build());
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.NUMBER,
						mobileNumber)
				.withValue(Phone.TYPE,
						Phone.TYPE_MOBILE).build());



		try {
			contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static void deleteContact(ContentResolver contactHelper,
			String number) {

		ContentResolver cr = contactHelper;
		Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				Phone._ID + " = ?" , new String[] { number }, null);

		if (cur.getCount() > 0) {

			while (cur.moveToNext()) {

				try {
					String lookupKey = cur .getString(cur .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
					Uri uri = Uri.withAppendedPath( ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
					cr.delete(uri, null, null);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		}
	}

}
