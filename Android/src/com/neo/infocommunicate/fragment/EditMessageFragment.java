package com.neo.infocommunicate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neo.infocommunicate.R;

public class EditMessageFragment extends BaseFragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, null);
		initView(view);
		return view;
	}

	private void initView(View view){
		
	}
}
