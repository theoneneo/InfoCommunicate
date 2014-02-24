package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MessageManager.MessageInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MessageListFragment extends ListFragment {
	private SiteAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void initUI() {
		adapter = new SiteAdapter(getActivity());
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			}
		});
		adapter.notifyDataSetChanged();
	}

	public void updateAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	public class SiteAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public SiteAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			SiteViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_message,
						parent, false);
				holder = new SiteViewHolder();
				holder.row_name = (TextView) convertView
						.findViewById(R.id.row_name);
				holder.row_time = (TextView) convertView
						.findViewById(R.id.row_time);
				holder.row_place = (TextView) convertView
						.findViewById(R.id.row_place);
				holder.row_switch = (CheckBox) convertView
						.findViewById(R.id.row_switch);
				convertView.setTag(holder);
			} else {
				holder = (SiteViewHolder) convertView.getTag();
			}

			MessageInfo messageInfo = MessageManager.getInstance().getMessageInfos().get(position);
			holder.row_name.setText(messageInfo.name);
			holder.row_time.setText(messageInfo.show_time);
			holder.row_place.setText(messageInfo.place);
			holder.row_switch.setOnCheckedChangeListener(null);
			if (MessageManager.getInstance().getMessageInfos().get(position).is_remind == 1)
				holder.row_switch.setChecked(true);
			else
				holder.row_switch.setChecked(false);
			holder.row_switch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
				}		
			});

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MessageManager.getInstance().getMessageInfos().size();
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

	static class SiteViewHolder {
		TextView row_name;
		TextView row_time;
		TextView row_place;
		CheckBox row_switch;
	}
}
