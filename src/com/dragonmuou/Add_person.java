package com.dragonmuou;

import com.dragonmuou.R;
import com.sqlit_db.Avoid_roster_db;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Add_person extends Activity {
	Button ok;
	Button link;
	EditText edite_num;
	CheckBox check_msn;
	CheckBox check_phone;
	int s_check_msn;
	int s_check_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_person);
		
		initView();
		
		//�õ��洢���ļ��е���ϵ�˺��룬�������ı�����
		SharedPreferences pref = getSharedPreferences("ContactsList",
				MODE_PRIVATE);
		String c_number = pref.getString("Number", "");
		System.out.println("number"+c_number);
		edite_num.setText(c_number);
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		s_check_msn=0;
		s_check_phone=0;
		ok = (Button)findViewById(R.id.button1);
		link = (Button)findViewById(R.id.link);
		edite_num = (EditText)findViewById(R.id.phone);
		check_msn = (CheckBox)findViewById(R.id.checkBox_msn);
		check_phone = (CheckBox)findViewById(R.id.checkBox_phone);
		ok.setOnClickListener(new Ok_click());
		link.setOnClickListener(new Link_click());
		
		check_msn.setOnCheckedChangeListener(new Msn_check());
		check_msn.setChecked(false);
		check_phone.setOnCheckedChangeListener(new Phone_check());
		check_phone.setChecked(false);
		
	}

	//�����Ӱ�ť����������
		class Ok_click implements OnClickListener {

			@Override
			public void onClick(View v) {
				
				
				String s_number = edite_num.getText().toString();
				int type = s_check_msn + s_check_phone;
				
				Boolean exit = false;
				Avoid_roster_db roster_db;
				SQLiteDatabase db;
				// �������ĺ��벻Ϊ�գ����ȶ����ݿ���в�ѯ���ж��Ƿ��ظ�����Ϊ������
				if (!s_number.equals("") && type != 0) {
					roster_db = new Avoid_roster_db(Add_person.this, "avoid_roster_db");
					db = roster_db.getReadableDatabase();
					// ��ѯavoid_roster_db���ݿ�
					Cursor cursor = db.query("in_message", new String[] { "number" }, null, null, null, null, null);
					while (cursor.moveToNext()) {
						// ��ȡ����number�ֶ�
						String number = cursor.getString(cursor.getColumnIndex("number"));
						// ������ĺ��������ݿ��е�Number�ֶ����Ƚ�
						if (number.equals(s_number)) {
							// �����ȣ���֤���ú����Ѽ��������
							exit = true;
							break;
						}
					}
					// �����Ѵ��ڣ���ֱ����ʾ�Ѵ���
					if (exit) {
						Toast.makeText(Add_person.this, "�ú����Ѿ�����¼���뵽�������������޸ģ�����", Toast.LENGTH_SHORT).show();
					} else {
						// ���벻���ڣ��򽫸ú���������
						ContentValues values = new ContentValues();
						values.put("number", s_number);
						values.put("type", String.valueOf(type));
						db.insert("in_message", null, values);
						Toast.makeText(Add_person.this, "��ӳɹ�", Toast.LENGTH_SHORT).show();
					}
					cursor.close();
					db.close();
				} else {
					Toast.makeText(Add_person.this, "��ʽ����ȷ��δ��������ѡ�����ط�ʽ������", Toast.LENGTH_SHORT).show();
				}

			}
		}

			//�����Ƭ�а�ť����������
			class Link_click implements OnClickListener {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Add_person.this, Contacts_list.class);
					startActivity(intent);
				}
				
				
			}
			
			
		class Msn_check implements OnCheckedChangeListener {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					s_check_msn = 1;
				} else {
					s_check_msn = 0;
				}
			}

		}

		class Phone_check implements OnCheckedChangeListener {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					s_check_phone = 2;
				} else {
					s_check_phone = 0;
				}
			}

		}


}
