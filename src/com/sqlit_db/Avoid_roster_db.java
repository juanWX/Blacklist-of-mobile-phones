package com.sqlit_db;

import android.content.Context; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Avoid_roster_db extends SQLiteOpenHelper{
	private static final int VERSION=1;
	public Avoid_roster_db(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public Avoid_roster_db(Context context, String name) {
		this(context, name,  null, VERSION);
		// TODO Auto-generated constructor stub
	}

	public Avoid_roster_db(Context context, String name,int version) {
		this(context, name,  null, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table in_message(number varchar(13),type varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
