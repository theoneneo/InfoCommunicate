package com.neo.infocommunicate.task;

import org.json.JSONException;

import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.protocol.ProtocolDataInput;
import com.neo.tools.DHttpClient;

import android.os.AsyncTask;

/**
 * @author LiuBing
 * @version 2014-4-2 下午2:27:16
 */
public class GetReceiverListTask extends AsyncTask<String, Integer, Void> {
	// onPreExecute方法用于在执行后台任务前做一些UI操作
	@Override
	protected void onPreExecute() {
	}

	// doInBackground方法内部执行后台任务,不可在此方法内修改UI
	@Override
	protected Void doInBackground(String... params) {
		DHttpClient client = new DHttpClient();
		String msg = client.post(params[0], params[1]);
		try {
			ProtocolDataInput.parseReceiverListFromJSON(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// onProgressUpdate方法用于更新进度信息
	@Override
	protected void onProgressUpdate(Integer... progresses) {
	}

	// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	@Override
	protected void onPostExecute(Void result) {
		ServiceManager.getInstance().getServiceListenerAbility()
				.notifyGetReceiverListListener();
	}

	// onCancelled方法用于在取消执行中的任务时更改UI
	@Override
	protected void onCancelled() {
	}
}
