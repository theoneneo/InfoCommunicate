package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.data.NoticeInfo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class UserListFragment extends BaseListFragment {
	private UserAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void initUI() {
		adapter = new UserAdapter(getActivity());
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				createContexMenu(PersonManager.getInstance().getReceiverList().get(arg2).user_id);
			}
		});
	}
	
	protected void createContexMenu(String id) {
		ContextMenuFragment menu = new ContextMenuFragment();
		menu.setId(id);
		menu.show(getFragmentManager(), null);
	}


	public void updateAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();

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
			holder.row_switch.setVisibility(View.GONE);
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
