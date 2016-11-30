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
	 * item����¼�
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
			// �����Զ���Ի���Ĳ����ļ�
			View dialog_view = getLayoutInflater().inflate(R.layout.dialog_choose, null);

			// ���öԻ������
			dialog.setTitle(num);
			// ���ضԻ��򲼾�
			dialog.setView(dialog_view);
			dialog.show();

			//��ʼ���Զ���Ի����������ť
			Button mDeleteFromContent = (Button)dialog_view.findViewById(R.id.delete_from_context);
			Button mRemoveFromMessage = (Button)dialog_view.findViewById(R.id.remove_num);
			Button mCall =(Button)dialog_view.findViewById(R.id.call);
			
			//��ť����¼� 
			/**
			 * ɾ�����ؼ�¼
			 */
			mDeleteFromContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Avoid_list_db list_db = new Avoid_list_db(List_Activity.this, "avoid_list_db");
					SQLiteDatabase db = list_db.getReadableDatabase();

					db.delete("in_content", "number=?", new String[] { num });
					// �Ի�����ʧ
					dialog.dismiss();
					// ˢ��ҳ��
					list_map.remove(position);
					simpleadapter.notifyDataSetChanged();
					listview.setAdapter(simpleadapter);
					Toast.makeText(List_Activity.this, "�ɹ�ɾ���ü�¼��", Toast.LENGTH_SHORT).show();
				}
			});

			/**
			 * �Ƴ������������øú��벻������
			 */
			mRemoveFromMessage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// �������ݿ�
					// 1.�õ�SqlitDatabase
					no_avoid_list_db no_avoid_db = new no_avoid_list_db(List_Activity.this, "no_avoid_db");
					SQLiteDatabase db = no_avoid_db.getWritableDatabase();
					
					//2.��������
					
					ContentValues contentValues = new ContentValues();
					contentValues.put("callnumber", num);
					
					db.insert("call_number", null, contentValues);
					
				
					// �Ի�����ʧ
					dialog.dismiss();
					Toast.makeText(List_Activity.this, "�ú���ɹ��Ƴ������������ᱻ����", Toast.LENGTH_SHORT).show();
				}
			});

			/**
			 * ���иú���
			 */
			mCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// ���ݺ��벦��绰
					// ����һ����ͼ
					Intent intent = new Intent();
					// ָ���䶯��Ϊ����绰
					intent.setAction(Intent.ACTION_CALL);
					// ָ����Ҫ�����ĺ���
					intent.setData(Uri.parse("tel:" + num));
					// ִ���������
					startActivity(intent);
					// ���öԻ�����ʧ
					dialog.dismiss();

				}
			});
			
			cursor.close();
			db.close();
		}

		
	}
	
}
