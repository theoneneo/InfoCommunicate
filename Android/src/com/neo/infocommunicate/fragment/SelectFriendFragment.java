package com.neo.infocommunicate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.MainActivity;
import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.data.ChatRoomInfo;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.fragment.UserListFragment.UserViewHolder;

import de.greenrobot.event.EventBus;

public class SelectFriendFragment extends BaseFragment {
	private ListView list;
	private UserAdapter adapter;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_select_friend, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		TextView title = (TextView) v.findViewById(R.id.title).findViewById(
				R.id.title_text);
		title.setText("请选择要发送的好友");
		ImageButton btn_select = (ImageButton)v.findViewById(R.id.btn_right);
		btn_select.setVisibility(View.VISIBLE); 
		list = (ListView) v.findViewById(R.id.list);
		adapter = new UserAdapter(getActivity());
		list.setAdapter(adapter);
	}


	public class UserAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public UserAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			UserViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_user,
						parent, false);
				holder = new UserViewHolder();
				holder.row_name = (TextView) convertView
						.findViewById(R.id.row_name);
				holder.row_switch = (CheckBox) convertView
						.findViewById(R.id.row_switch);
				convertView.setTag(holder);
			} else {
				holder = (UserViewHolder) convertView.getTag();
			}

			String nick_name = PersonManager.getInstance().getReceiverList()
					.get(position).nick_name;
			holder.row_name.setText(nick_name);
			holder.row_switch.setOnCheckedChangeListener(null);
			holder.row_switch
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0,
						boolean arg1) {
					// TODO Auto-generated method stub
					if (arg1) {
						PersonManager
								.getInstance()
								.getReceiverList().get(position).isSelect = true;
		
					} else {
						PersonManager
						.getInstance()
						.getReceiverList().get(position).isSelect = false;
					}
				}
			});

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PersonManager.getInstance().getReceiverList().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static class UserViewHolder {
		TextView row_name;
		CheckBox row_switch;
	}
}
