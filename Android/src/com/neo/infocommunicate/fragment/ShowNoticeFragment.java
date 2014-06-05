package com.neo.infocommunicate.fragment;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.data.ChatRoomInfo;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.db.DBTools;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.event.ServiceEvent;

import de.greenrobot.event.EventBus;

public class ShowNoticeFragment extends BaseFragment {
	private String key = null;
	private NoticeInfo info = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			key = args.getString("key");
			if (key == null)
				return;
			for (int i = 0; i < MessageManager.getInstance().getNoticeInfos()
					.size(); i++) {
				if (key.equals(MessageManager.getInstance().getNoticeInfos()
						.get(i).key)) {
					info = MessageManager.getInstance().getNoticeInfos().get(i);
				}
			}
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notice_show, null);
		initView(view);
		return view;
	}

	private void initView(View v) {
		TextView text_name = (TextView) v.findViewById(R.id.text_name);
		TextView text_content = (TextView) v.findViewById(R.id.text_content);
		TextView text_add = (TextView) v.findViewById(R.id.text_add);
		TextView text_time = (TextView) v.findViewById(R.id.text_time);

		CheckBox prompt = (CheckBox) v.findViewById(R.id.check_prompt);

		if (info == null)
			return;
		text_name.setText(info.title);
		text_content.setText(info.message);
		text_add.setText(info.place);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(info.time);
		StringBuffer buf = new StringBuffer();
		buf.append(c.get(Calendar.YEAR));
		buf.append("-");
		buf.append(c.get(Calendar.MONTH));
		buf.append("-");
		buf.append(c.get(Calendar.DAY_OF_MONTH));
		buf.append(" ");
		buf.append(c.get(Calendar.HOUR_OF_DAY));
		buf.append(":");
		buf.append(c.get(Calendar.MINUTE));
		text_time.setText(buf.toString());

		if (info.prompt == 0)
			prompt.setChecked(false);
		else
			prompt.setChecked(true);

		prompt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					info.prompt = 1;
				} else {
					info.prompt = 0;
				}
				DBTools.instance(mContext).changeNoticePrompt(info.key,
						info.prompt);
				EventBus.getDefault().post(
						new BroadCastEvent(BroadCastEvent.CHANGE_PROMPT_EVENT,
								null));
			}
		});
	}

}
