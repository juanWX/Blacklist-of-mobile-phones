package com.dragonmuou;

import java.util.ArrayList;
import java.util.List;

import com.data.ContactBean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Contacts_list extends Activity {

	private ListView mContactsView;

	// ��List���ϵĶ��ֵ��װ�ɶ���б���
	ArrayAdapter<ContactBean> adapter;

	// �����ϵ����Ϣ
	List<ContactBean> contactsList = new ArrayList<ContactBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.link_list);

		initView();

		// �洢��ϵ����Ϣ�ļ���Ϊ�б����ṩ����
		adapter = new ArrayAdapter<ContactBean>(this, android.R.layout.simple_list_item_1, contactsList);
		mContactsView.setAdapter(adapter);
		// ��ȡ��ϵ��
		readContacts();

		mContactsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String number = contactsList.get(position).getNumber();

				// (1)�õ�sharedPreferences(Context�ķ���,����ָ���ļ���)
				SharedPreferences sp = getSharedPreferences("ContactsList", MODE_PRIVATE);
				// (2)�õ�Editor
				SharedPreferences.Editor editor = sp.edit();
				// (3)��ֵ
				editor.putString("Number", number);
				// (4)�ύ
				editor.commit();

				Intent intent = new Intent(Contacts_list.this, Main_face.class);
				startActivity(intent);
				//intent.putExtra("C_Number", number);
				finish();
				Toast.makeText(Contacts_list.this, number, 0).show();

			}
		});

	}

	/**
	 * ��ȡ��ϵ��
	 */
	private void readContacts() {

		Cursor cursor = null;

		try {
			// ��ѯ��ϵ������
			cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
					null);
			while (cursor.moveToNext()) {
				// ��ȡ��ϵ������
				String displayName = cursor
						.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				// ��ȡ��ϵ���ֻ���
				String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

				ContactBean contactBean = new ContactBean(displayName, number);
				contactsList.add(contactBean);
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private void initView() {
		mContactsView = (ListView) findViewById(R.id.contacts_view);
	}
}
