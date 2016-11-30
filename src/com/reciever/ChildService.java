package com.reciever;

import com.sqlit_db.Avoid_roster_db;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

public class ChildService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String number = null;
		String type = null;
		Intent typeInten = intent;
		number = typeInten.getStringExtra("num");
		type = typeInten.getStringExtra("check");
		Avoid_roster_db roster_db = new Avoid_roster_db(ChildService.this, "avoid_roster_db");
		SQLiteDatabase db = roster_db.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", type);
		db.update("in_message", values, "number=?", new String[] { number });
		db.close();
		return super.onStartCommand(intent, flags, startId);
	}

}
