package com.reciever;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sqlit_db.Avoid_list_db;
import com.sqlit_db.Avoid_roster_db;

import android.content.ContentValues;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsMessage;

public class MsnReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String time = null, num = null, msn = null;
		System.out.println(intent.getAction());
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		SmsMessage[] message = new SmsMessage[pdus.length];
		for (int i = 0; i < pdus.length; i++) {
			message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			num = message[i].getDisplayOriginatingAddress();
			msn = message[i].getDisplayMessageBody();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = format.format((new Date()));
		Avoid_roster_db roster_db = new Avoid_roster_db(context, "avoid_roster_db");
		SQLiteDatabase db = roster_db.getReadableDatabase();
		Cursor cursor = db.query("in_message", new String[] { "number", "type" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			String s_num = cursor.getString(cursor.getColumnIndex("number"));
			String s_type = cursor.getString(cursor.getColumnIndex("type"));
			if ((num.equals(s_num) && s_type.equals("1")) || (num.equals(s_num) && s_type.equals("3"))) {
				Avoid_list_db list_db = new Avoid_list_db(context, "avoid_list_db");
				SQLiteDatabase l_db = list_db.getReadableDatabase();
				ContentValues values = new ContentValues();
				values.put("number", num);
				values.put("time", time);
				values.put("content", "¶ÌÐÅ:\n" + msn);
				l_db.insert("in_content", null, values);
				System.out.println("lanjieduixiang");
				abortBroadcast();

				l_db.close();
				break;
			}

		}
		cursor.close();
		db.close();

	}

}
