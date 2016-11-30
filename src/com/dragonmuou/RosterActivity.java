package com.dragonmuou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data.Data_check;
import com.data.Data_roster;
import com.reciever.ChildService;
import com.sqlit_db.Avoid_roster_db;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RosterActivity extends Activity {

	List<String> groups;
	List<Map<String, String>> children;
	ExpandableListView elv;
	ExpandableAdapter viewAdapter;

	@Override
	protected void onResume() {

		super.onResume();
		super.onPause();
		Data_roster data_roster = this.get_data();
		groups.clear();
		children.clear();
		groups.addAll(data_roster.number);
		children.addAll(data_roster.num_type);
		viewAdapter = new ExpandableAdapter(this, groups, children);
		elv.setAdapter(viewAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.roster);
		groups = new ArrayList<String>();
		children = new ArrayList<Map<String, String>>();
		Data_roster data_roster = this.get_data();
		groups.addAll(data_roster.number);
		children.addAll(data_roster.num_type);
		elv = (ExpandableListView) findViewById(R.id.roster_Expandable);
		viewAdapter = new ExpandableAdapter(this, groups, children);
		elv.setAdapter(viewAdapter);
	}

	public Data_roster get_data() {
		Data_roster data_roster = new Data_roster();
		Map<String, String> map = new HashMap<String, String>();
		Avoid_roster_db roster_db = new Avoid_roster_db(RosterActivity.this, "avoid_roster_db");
		SQLiteDatabase db = roster_db.getReadableDatabase();
		Cursor cursor = db.query("in_message", new String[] { "number", "type" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			String num = cursor.getString(cursor.getColumnIndex("number"));
			data_roster.number.add(num);
			map.put(num, cursor.getString(cursor.getColumnIndex("type")));
			data_roster.num_type.add(map);
		}
		cursor.close();
		db.close();
		return data_roster;
	}
	
	

	/**
	 *class ExpandableAdapter 
	 * @author XiaoJuan
	 *
	 */
	class ExpandableAdapter extends BaseExpandableListAdapter {
		final static int len = 1;
		List<String> groups;
		Context context;
		List<Map<String, String>> childs;

		public ExpandableAdapter(Context context, List<String> groups, List<Map<String, String>> childs) {
			this.context = context;
			this.groups = groups;
			this.childs = childs;
		}

		@Override
		public Object getChild(int arg0, int arg1) {
			return childs.get(arg0).get(arg1);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 该方法决定每个子选项的外观
		@Override
		public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
				View convertView, ViewGroup parent) {

			final String text = groups.get(groupPosition);
			final Data_check check = new Data_check();

			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout childLinelayout = (RelativeLayout) layoutInflater.inflate(R.layout.children, null);
			CheckBox msn_check = (CheckBox) childLinelayout.findViewById(R.id.child_checkmsn);
			CheckBox phone_check = (CheckBox) childLinelayout.findViewById(R.id.child_numCheck);
			Button okButton = (Button) childLinelayout.findViewById(R.id.changEnd_button);
			Button deleteButton = (Button) childLinelayout.findViewById(R.id.delete_button);

			Map<String, String> map = childs.get(groupPosition);
			String type = map.get(groups.get(groupPosition));
			if (type.equals("1")) {
				msn_check.setChecked(true);
				phone_check.setChecked(false);
				check.checkmsn = 1;
			}
			if (type.equals("2")) {
				msn_check.setChecked(false);
				phone_check.setChecked(true);
				check.checkphone = 2;
			}
			if (type.equals("3")) {
				msn_check.setChecked(true);
				phone_check.setChecked(true);
				check.checkmsn = 1;
				check.checkphone = 2;
			}

			// 监听check状态

			msn_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if (isChecked) {
						check.checkmsn = 1;
					} else {
						check.checkmsn = 0;
					}

				}
			});

			// 监听check状态
			phone_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if (isChecked) {
						check.checkphone = 2;
					} else {
						check.checkphone = 0;
					}
				}
			});

			// 传输修改数据
			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String check_ = String.valueOf(check.checkmsn + check.checkphone);
					System.out.println(check_);
					Intent intent = new Intent(context, ChildService.class);
					intent.setAction("com.reciever.ChildService");
					intent.putExtra("check", check_);
					intent.putExtra("num", text);
					context.startService(intent);

				}
			});

			// 移除黑名单
			deleteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AlertDialog.Builder dialog = new AlertDialog.Builder(context);
					dialog.setMessage("确定移除该号码？");
					dialog.setCancelable(false);
					dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 获取组数据
							String deleteNumber = groups.get(groupPosition);

							Avoid_roster_db roster_db = new Avoid_roster_db(context, "avoid_roster_db");
							SQLiteDatabase db = roster_db.getWritableDatabase();
							// 通过获取的组数据中的Number删除数据库中对应的数据
							db.delete("in_message", "number =?", new String[] { deleteNumber });
							db.close();
							Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
							groups.remove(groupPosition);
							childs.remove(childPosition);
							if (viewAdapter == null) {
								viewAdapter = new ExpandableAdapter(context, groups, childs);
								elv.setAdapter(viewAdapter);
							} else {
								viewAdapter.notifyDataSetChanged();
								elv.setAdapter(viewAdapter);
							}

						}
					});
					dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();

				}
			});

			// System.out.println(map.get(groups.get(groupPosition)));
			return childLinelayout;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return len;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groups.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

			String text = groups.get(groupPosition);
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout groupLinelayout = (RelativeLayout) layoutInflater.inflate(R.layout.groups, null);
			TextView textview = (TextView) groupLinelayout.findViewById(R.id.group);
			textview.setText(text);

			// 获取一级列表布局文件,设置相应元素属性
			return groupLinelayout;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
