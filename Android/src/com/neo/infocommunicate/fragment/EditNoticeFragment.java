package com.neo.infocommunicate.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.tools.RingTong;

import de.greenrobot.event.EventBus;

public class EditNoticeFragment extends BaseFragment {
	private Button btn_id, btn_send, btn_date, btn_time;
	private EditText edit_title, edit_msg, edit_place;
	private int mYear = -1, mMonth = -1, mDay = -1, mHour = -1, mMinute = -1;

	private static String json;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, ServiceEvent.class);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this, ServiceEvent.class);
		PersonManager.getInstance().clearReceiverSelect();
		super.onDestroy();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notice_edit, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		btn_id = (Button) view.findViewById(R.id.btn_id);
		btn_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadUser();
			}
		});

		btn_send = (Button) view.findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage();
			}
		});

		btn_date = (Button) view.findViewById(R.id.btn_date);
		btn_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDate();
			}
		});

		btn_time = (Button) view.findViewById(R.id.btn_time);
		btn_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTime();
			}
		});

		edit_title = (EditText) view.findViewById(R.id.edit_title);
		edit_msg = (EditText) view.findViewById(R.id.edit_message);
		edit_place = (EditText) view.findViewById(R.id.edit_place);

		TextView text_id = (TextView) view.findViewById(R.id.text_id);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < PersonManager.getInstance().getReceiverList()
				.size(); i++) {
			if (PersonManager.getInstance().getReceiverList().get(i).isSelect) {
				buf.append(PersonManager.getInstance().getReceiverList().get(i).nick_name);
				buf.append(",");
			}
		}

		text_id.setText(buf.toString());
	}

	public void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_SEND_PUSH_NOTICE_EVENT:
			destroyProgressBar();
			if ((String) event.getObject() == null) {
				Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT)
						.show();
			} else if ("fail".equals((String) event.getObject())) {
				Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT)
						.show();
				MessageManager.getInstance().addSendNoticeInfo(json,
						(String) event.getObject());
				MyFragmentManager.getInstance().backFragment();
			}
			break;
		default:
			break;
		}
	}

	private void loadUser() {
		MyFragmentManager.getInstance().replaceFragment(R.id.content_frame,
				new SelectFriendFragment(), MyFragmentManager.PROCESS_MAIN,
				MyFragmentManager.FRAGMENT_SELECT_FRIEND);
	}

	private void sendMessage() {
		if (mYear == -1 || mMonth == -1 || mDay == -1 || mHour == -1
				|| mMinute == -1) {
			Toast.makeText(getActivity(), "请设置时间", Toast.LENGTH_SHORT).show();
			return;
		}

		boolean isSelect = false;
		for (int i = 0; i < PersonManager.getInstance().getReceiverList()
				.size(); i++) {
			if (PersonManager.getInstance().getReceiverList().get(i).isSelect) {
				isSelect = true;
			}
			if (!isSelect) {
				Toast.makeText(getActivity(), "请添加发送用户", Toast.LENGTH_SHORT)
						.show();
				return;
			}
		}

		if (edit_title.getText().toString().length() == 0) {
			Toast.makeText(getActivity(), "请设置会议名称", Toast.LENGTH_SHORT).show();
			return;
		}

		if (edit_msg.getText().toString().length() == 0) {
			Toast.makeText(getActivity(), "请设置会议简介", Toast.LENGTH_SHORT).show();
			return;
		}

		if (edit_place.getText().toString().length() == 0) {
			Toast.makeText(getActivity(), "请设置会议地点", Toast.LENGTH_SHORT).show();
			return;
		}

		createProgressBar("发送中...");

		Calendar c = Calendar.getInstance();
		c.set(mYear, mMonth, mDay, mHour, mMinute, 0);
		String time = String.valueOf(c.getTimeInMillis());
		ArrayList<String> ids = new ArrayList<String>();
		for (int i = 0; i < PersonManager.getInstance().getReceiverList()
				.size(); i++) {
			if (PersonManager.getInstance().getReceiverList().get(i).isSelect) {
				ids.add(PersonManager.getInstance().getReceiverList().get(i).user_id);
			}	
		}
		json = ServiceManager.getInstance().sendPushNotice(ids,
				edit_title.getText().toString(), edit_msg.getText().toString(),
				edit_place.getText().toString(), "", time);

	}

	private void setDate() {
		Calendar c = Calendar.getInstance();
		new DatePickerDialog(getActivity(),
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						setDateBtn();
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void setTime() {
		Calendar c = Calendar.getInstance();
		new TimePickerDialog(getActivity(),
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						mHour = hourOfDay;
						mMinute = minute;
						setTimeBtn();
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
				.show();
	}

	private void setDateBtn() {
		btn_date.setText(String.valueOf(mYear) + "-" + String.valueOf(mMonth+1)
				+ "-" + String.valueOf(mDay));
	}

	private void setTimeBtn() {
		btn_time.setText(String.valueOf(mHour) + ":" + String.valueOf(mMinute));
	}
}
