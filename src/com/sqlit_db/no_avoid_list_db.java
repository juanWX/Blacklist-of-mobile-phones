package com.sqlit_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class no_avoid_list_db  extends SQLiteOpenHelper{

	private static final int VERSION=1;
	public no_avoid_list_db(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	
	public no_avoid_list_db(Context context, String name) {
		this(context, name,  null, VERSION);
		// TODO Auto-generated constructor stub
	}

	public no_avoid_list_db(Context context, String name,int version) {
		this(context, name,  null, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table call_number(callnumber varchar(15))");
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
