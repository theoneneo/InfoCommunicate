package com.neo.tools;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;

public class GetPhotoFromAlbum {

	private static File mCurrentPhotoFile;

	/**
	 * 通过何种方式获取图片 PHOTO_PICKED_WITH_DATA 相册 CAMERA_WITH_DATA 相机
	 * 
	 * @param way
	 */
	public static void ChooseWay(Context context, int way) {
		if (CameraUtil.PHOTO_PICKED_WITH_DATA == way) {
			try {
				Intent intent = CameraUtil.getPhotoPickIntent();
				((Activity) context).startActivityForResult(intent,
						CameraUtil.PHOTO_PICKED_WITH_DATA);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (CameraUtil.CAMERA_WITH_DATA == way) {
			try {
				if (!CameraUtil.PHOTO_DIR.exists()) {
					CameraUtil.PHOTO_DIR.mkdirs();
				}
				mCurrentPhotoFile = new File(CameraUtil.PHOTO_DIR,
						CameraUtil.getPhotoFileName());
				Intent intent = CameraUtil.getTakePickIntent(mCurrentPhotoFile);
				((Activity) context).startActivityForResult(intent,
						CameraUtil.CAMERA_WITH_DATA);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param requestCode
	 * @param albumdata
	 *            如果是相册调用 cameraFile传null即可
	 * @param cut
	 *            是否调用系统剪辑
	 */
	@SuppressLint("NewApi")
	public static void GetPhoto(Activity context, int requestCode, Intent data,
			boolean cut, PhotoCallBack callBack) {
		if (requestCode == CameraUtil.PHOTO_PICKED_WITH_DATA) {
			try {
				String imagePath = "";
				Uri uri = data.getData();
				System.out.println("uri----" + uri);
				if (uri != null) {
					if (Integer.parseInt(Build.VERSION.SDK) > 18) {

						if (DocumentsContract.isDocumentUri(
								context.getApplicationContext(), uri)) {
							String wholeID = DocumentsContract
									.getDocumentId(uri);
							String id = wholeID.split(":")[1];
							String[] column = { MediaStore.Images.Media.DATA };
							String sel = MediaStore.Images.Media._ID + "=?";
							Cursor cursor = context
									.getApplicationContext()
									.getContentResolver()
									.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											column, sel, new String[] { id },
											null);
							int columnIndex = cursor.getColumnIndex(column[0]);
							if (cursor.moveToFirst()) {
								imagePath = cursor.getString(columnIndex);
							}
							if (cut) {
								doCropPhoto(context, new File(imagePath));
								if (callBack != null) {
									callBack.Success(imagePath);
								}
							} else {
								callBack.Success(imagePath);
							}
							cursor.close();

						}
					} else {
						String[] projection = new String[] { Media.DATA };
						Cursor managedCursor = context.managedQuery(uri,
								projection, null, null,
								Media.DEFAULT_SORT_ORDER);
						managedCursor.moveToFirst();
						imagePath = managedCursor.getString(0);
						if (cut) {
							doCropPhoto(context, new File(imagePath));
							if (callBack != null) {
								callBack.Success(imagePath);
							}
						} else {
							callBack.Success(imagePath);
						}
						if (Integer.parseInt(Build.VERSION.SDK) < 14) {
							managedCursor.close();
						}
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null) {
						// 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
						Bitmap image = extras.getParcelable("data");
						if (image != null) {
							imagePath = CameraUtil.saveMyBitmap(image);
							if (cut) {
								doCropPhoto(context, new File(imagePath));
								if (callBack != null) {
									callBack.Success(imagePath);
								}
							} else {
								callBack.Success(imagePath);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// photoCallBack.Failed(context.getResources().getString(R.string.gallay_error));
			}
		}

		if (requestCode == CameraUtil.CAMERA_WITH_DATA) {// 照相
			if (cut) {
				doCropPhoto(context, mCurrentPhotoFile);
				if (callBack != null) {
					callBack.Success(mCurrentPhotoFile.getAbsolutePath());
				}
			} else {
				callBack.Success(mCurrentPhotoFile.getAbsolutePath());
			}
		}

		if (requestCode == CameraUtil.CAMERA_COMPLETE) {// 裁剪
			Bitmap image = null;
			if (CameraUtil.isMotorlaMobile()) {
				image = data.getParcelableExtra("data");
			} else {
				image = CameraUtil.decodeUriAsBitmap(CameraUtil.OUT_FILE);
			}
			callBack.Crop(image);
		}
	}

	public static void doCropPhoto(Context context, File filePath) {
		try {
			final Intent intent = CameraUtil.getCropImageIntent(
					Uri.fromFile(filePath), 1, 1, CameraUtil.ICON_SIZE,
					CameraUtil.ICON_SIZE);
			((Activity) context).startActivityForResult(intent,
					CameraUtil.CAMERA_COMPLETE);
		} catch (Exception e) {
			Toast.makeText(context, "截取失败", Toast.LENGTH_LONG).show();
		}
	}
}