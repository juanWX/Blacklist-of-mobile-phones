package com.dragonmuou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sqlit_db.Avoid_list_db;
import com.sqlit_db.no_avoid_list_db;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class List_Activity extends Activity {

	ListView listview;
	SimpleAdapter simpleadapter;
	List<Map<String, String>> list_map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		listview = (ListView) findViewById(R.id.listView1);
		list_map = new ArrayList<Map<String, String>>();
		list_map.clear();
		Avoid_list_db list_db = new Avoid_list_db(List_Activity.this, "avoid_list_db");
		SQLiteDatabase db = list_db.getReadableDatabase();
		Cursor cursor = db.query("in_content", new String[] { "number", "time" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			String s_num = cursor.getString(cursor.getColumnIndex("number"));
			String s_time = cursor.getString(cursor.getColumnIndex("time"));
			map.put("time", s_time);
			map.put("number", s_num);
			list_map.add(map);
		}
		cursor.close();
		db.close();
		simpleadapter = new SimpleAdapter(this, list_map, R.layout.list_child, new String[] { "time", "number" },
				new int[] { R.id.time_textView, R.id.num_textView });
		listview.setAdapter(simpleadapter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub\
		super.onResume();
		list_map.clear();
		Avoid_list_db list_db = new Avoid_list_db(List_Activity.this, "avoid_list_db");
		SQLiteDatabase db = list_db.getReadableDatabase();
		Cursor cursor = db.query("in_content", new String[] { "number", "time" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			String s_num = cursor.getString(cursor.getColumnIndex("number"));
			String s_time = cursor.getString(cursor.getColumnIndex("time"));
			map.put("time", s_time);
			map.put("number", s_num);
			list_map.add(map);
		}
		cursor.close();
		db.close();
		simpleadapter = new SimpleAdapter(this, list_map, R.layout.list_child, new String[] { "time", "number" },
				new int[] { R.id.time_textView, R.id.num_textView });
		listview.setAdapter(simpleadapter);

		listview.setOnItemClickListener(new List_click());
	}

	/**
	 * item点击事件
	 * @author XiaoJuan
	 *
	 */
	
	class List_click implements OnItemClickListener {
	
		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			
			
			ListView listView = (ListView) parent;
		
			HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
			final String num = map.get("number");
			
			Avoid_list_db list_db = new Avoid_list_db(List_Activity.this, "avoid_list_db");
			SQLiteDatabase db = list_db.getReadableDatabase();
			Cursor cursor = db.query("in_content", new String[] { "number", "time", "content" }, null, null, null, null,
					null);
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(List_Activity.this);
			final AlertDialog dialog = builder.create();
			// 加载自定义对话框的布局文件
			View dialog_view = getLayoutInflater().inflate(R.layout.dialog_choose, null);

			// 设置对话框标题
			dialog.setTitle(num);
			// 加载对话框布局
			dialog.setView(dialog_view);
			dialog.show();

			//初始化自定义对话框的三个按钮
			Button mDeleteFromContent = (Button)dialog_view.findViewById(R.id.delete_from_context);
			Button mRemoveFromMessage = (Button)dialog_view.findViewById(R.id.remove_num);
			Button mCall =(Button)dialog_view.findViewById(R.id.call);
			
			//按钮点击事件 
			/**
			 * 删除拦截记录
			 */
			mDeleteFromContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Avoid_list_db list_db = new Avoid_list_db(List_Activity.this, "avoid_list_db");
					SQLiteDatabase db = list_db.getReadableDatabase();

					db.delete("in_content", "number=?", new String[] { num });
					// 对话框消失
					dialog.dismiss();
					// 刷新页面
					list_map.remove(position);
					simpleadapter.notifyDataSetChanged();
					listview.setAdapter(simpleadapter);
					Toast.makeText(List_Activity.this, "成功删除该记录！", Toast.LENGTH_SHORT).show();
				}
			});

			/**
			 * 移除黑名单，设置该号码不被拦截
			 */
			mRemoveFromMessage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 存入数据库
					// 1.得到SqlitDatabase
					no_avoid_list_db no_avoid_db = new no_avoid_list_db(List_Activity.this, "no_avoid_db");
					SQLiteDatabase db = no_avoid_db.getWritableDatabase();
					
					//2.插入数据
					
					ContentValues contentValues = new ContentValues();
					contentValues.put("callnumber", num);
					
					db.insert("call_number", null, contentValues);
					
				
					// 对话框消失
					dialog.dismiss();
					Toast.makeText(List_Activity.this, "该号码成功移出黑名单！不会被拦截", Toast.LENGTH_SHORT).show();
				}
			});

			/**
			 * 呼叫该号码
			 */
			mCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 根据号码拨打电话
					// 创建一个意图
					Intent intent = new Intent();
					// 指定其动作为拨打电话
					intent.setAction(Intent.ACTION_CALL);
					// 指定将要拨出的号码
					intent.setData(Uri.parse("tel:" + num));
					// 执行这个动作
					startActivity(intent);
					// 设置对话框消失
					dialog.dismiss();

				}
			});
			
			cursor.close();
			db.close();
		}

		
	}
	
}
