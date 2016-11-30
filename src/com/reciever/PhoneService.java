package com.reciever;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.internal.telephony.ITelephony;
import com.sqlit_db.Avoid_list_db;
import com.sqlit_db.Avoid_roster_db;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 注册一个监听器对电话状态进行监听
		telManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		String phoneNumber;

		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
				phoneNumber = incomingNumber;
				if (isend(phoneNumber)) {
					Method method = null;
					try {
						method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// 获取远程TELEPHONY_SERVICE的IBinder对象的代理
					IBinder binder = null;
					try {
						binder = (IBinder) method.invoke(null, new Object[] { TELEPHONY_SERVICE });
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 将IBinder对象的代理转换为ITelephony对象
					ITelephony telephony = ITelephony.Stub.asInterface(binder);
					// 挂断电话
					try {
						telephony.endCall();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(phoneNumber);
				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

		public Boolean isend(String phoneNumber) {
			Avoid_roster_db roster_db = new Avoid_roster_db(PhoneService.this, "avoid_roster_db");
			SQLiteDatabase db = roster_db.getReadableDatabase();
			Cursor cursor = db.query("in_message", new String[] { "number", "type" }, null, null, null, null, null);
			while (cursor.moveToNext()) {
				String s_num = cursor.getString(cursor.getColumnIndex("number"));
				String s_type = cursor.getString(cursor.getColumnIndex("type"));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format((new Date()));
				if ((phoneNumber.contains(s_num) && s_type.equals("2"))
						|| (phoneNumber.contains(s_num) && s_type.equals("3"))) {
					Avoid_list_db list_db = new Avoid_list_db(PhoneService.this, "avoid_list_db");
					SQLiteDatabase l_db = list_db.getReadableDatabase();
					ContentValues values = new ContentValues();
					values.put("number", phoneNumber);
					values.put("time", time);
					values.put("content", "来电");
					l_db.insert("in_content", null, values);
					l_db.close();
					cursor.close();
					db.close();
					return true;
				}

			}
			cursor.close();
			db.close();

			return false;
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

}
