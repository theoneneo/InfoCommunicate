package com.neo.tools;

/**
 * 这个工具类是关于通过相机或者是媒体库取图片的函数，但是有时候startactivityforResult()没有返回值。
 * 所以我把这些函数又放回到各个类里面了，这个类我也不删了。
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class CameraUtil {

	public static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	public static final File CROP_FILE_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/CROP");
	public static final int ICON_SIZE = 96;
	public static final int ICON_WIDTH = 400;
	public static final int ICON_HEIGHT = 320;
	public File mCurrentPhotoFile;// 照相机拍照得到的图片

	/* 处理图片功能 */
	public static final int CAMERA_WITH_DATA = 0x3023;
	public static final int CAMERA_COMPLETE = 0x3022;
	public static final int PHOTO_PICKED_WITH_DATA = 0x3021;

	Activity mActivity;
	private static CameraUtil instance = null;

	private CameraUtil(Activity activity) {
		mActivity = activity;
	}

	public static CameraUtil getInstance(Activity activity) {
		if (instance == null)
			instance = new CameraUtil(activity);
		return instance;
	}

	// 拍照
	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;
	}

	// 获得一个文件名，文件名为当前的时间
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// 图片浏览器
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		return intent;
	}

	/***
	 * 获得压缩后的图片，大分辨率图片的话，这个方法比较耗时
	 * 
	 * @param picPath
	 *            源图片路径
	 * @param toWidth
	 *            期望图片的宽
	 * @param toHeight
	 *            期望图片的高
	 * @return
	 */
	public static Bitmap getCompressBitmap(String picPath, int toWidth,
			int toHeight) {
		if ("".equals(picPath))
			return null;
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap comBitmap = BitmapFactory.decodeFile(picPath, op);
		int srcWidth = op.outWidth;
		int srcHeight = op.outHeight;
		int wRadio = 1, hRadio = 1;

		if (srcWidth < srcHeight) {
			if (srcWidth > toWidth || srcHeight > toHeight) {
				wRadio = (int) Math.ceil(op.outWidth / toWidth);
				hRadio = (int) Math.ceil(op.outHeight / toHeight);
			}

		} else {
			if (srcWidth > toWidth || srcHeight > toHeight) {
				wRadio = (int) Math.ceil(op.outWidth / toHeight);
				hRadio = (int) Math.ceil(op.outHeight / toWidth);
			}

		}

		if (wRadio >= 1 && hRadio >= 1) {
			if (wRadio > hRadio) {
				op.inSampleSize = wRadio;
			} else {
				op.inSampleSize = hRadio;
			}
		}
		op.inJustDecodeBounds = false;
		comBitmap = BitmapFactory.decodeFile(picPath, op);
		return comBitmap;
	}

	/***
	 * 保存一个图片到CameraUtil.CROP_FILE_DIR目录，并返回得到的路径
	 * 
	 * @param mBitmap
	 * @return
	 */
	public static String saveMyBitmap(Bitmap mBitmap) {
		File f;
		FileOutputStream fOut = null;
		String imagePath = "";
		try {
			if (!CameraUtil.CROP_FILE_DIR.exists()) {
				CameraUtil.CROP_FILE_DIR.mkdirs();
			}
			f = new File(CameraUtil.CROP_FILE_DIR,
					CameraUtil.getPhotoFileName());
			f.createNewFile();
			imagePath = f.getAbsolutePath();
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			return imagePath;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imagePath;
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static final String OUT_FILE = Environment
			.getExternalStorageDirectory() + "/out.jpg";

	public static Intent getCropImageIntent(Uri photoUri, int aspectX,
			int aspectY, int outputX, int outputY) {
		Intent intent = null;
		if (Integer.parseInt(Build.VERSION.SDK) > 18) {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		} else {
			intent = new Intent("com.android.camera.action.CROP");
		}
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		if (isMotorlaMobile()) {
			intent.putExtra("return-data", true);// 若为false则表示不返回数据
			// } else if (Integer.parseInt(Build.VERSION.SDK) > 18) {
			// intent.putExtra("return-data", true);
		} else {
			File outFile = new File(CameraUtil.OUT_FILE);
			try {
				if (outFile.exists()) {
					outFile.delete();
				}
				outFile.getParentFile().mkdirs();
				outFile.createNewFile();
			} catch (IOException ex) {
				Log.e("io", ex.getMessage());
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra("return-data", false);// 若为false则表示不返回数据
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", false);
		}

		return intent;
	}

	public final static String Model = android.os.Build.MODEL;
	public final static String Device = android.os.Build.DEVICE;
	public final static String Manufacture = android.os.Build.MANUFACTURER;
	public static final String MOTOROLA_MODEL = "MB860";
	public static final String MOTOROLA_DEVICE = "olympus";
	public static final String MOTOROLA_MANUFACTURE = "motorola";

	public static boolean isMotorlaMobile() {
		if (MOTOROLA_MODEL.equals(Model) && MOTOROLA_DEVICE.equals(Device)
				&& MOTOROLA_MANUFACTURE.equals(Manufacture)) {
			return true;
		}
		return false;
	}

	public static Bitmap decodeUriAsBitmap(String outPut) {
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeFile(outPut);
		return bitmap;
	}
}
