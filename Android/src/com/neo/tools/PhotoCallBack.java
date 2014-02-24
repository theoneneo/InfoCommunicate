package com.neo.tools;

import android.graphics.Bitmap;

public interface PhotoCallBack {

	public void Success(String path);
	public void Failed(String errormsg);
	public void Crop(Bitmap bitmap);
}
