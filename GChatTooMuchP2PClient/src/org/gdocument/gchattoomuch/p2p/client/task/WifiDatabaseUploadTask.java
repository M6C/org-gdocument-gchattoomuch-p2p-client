package org.gdocument.gchattoomuch.p2p.client.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.gdocument.gchattoomuch.lib.log.Logger;
import org.gdocument.gchattoomuch.p2p.common.P2PConstant;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.cameleon.common.android.inotifier.INotifierMessage;
import com.cameleon.common.tool.FileTool;
import com.cameleon.common.tool.NetworkUtil;

public class WifiDatabaseUploadTask extends AsyncTask<Void, Void, Void> {

	private String TAG = WifiDatabaseUploadTask.class.getName();
	private List<String> databasePath;
	private INotifierMessage notifier;

	public WifiDatabaseUploadTask(Context context, List<String> databasePath) {
		this(context, null, databasePath);
	}

	public WifiDatabaseUploadTask(Context context, INotifierMessage notifier, List<String> databasePath) {
		this.notifier = notifier;
		this.databasePath = databasePath;
	}

	@Override
	protected Void doInBackground(Void... params) {
		logMe("doInBackground");
		String filename = Environment.getExternalStorageDirectory() + "/db" + System.currentTimeMillis() + ".piz";
		logMe("zip filename:" + filename);
		FileTool.createZip(databasePath, filename);
		uploadFile(filename);
		deleteFile(filename);
		return null;
	}

	/** * Start activity that can handle the JPEG image */
	@Override
	protected void onPostExecute(Void result) {
	}

	private void uploadFile(String filename) {
		logMe("uploadFile");
		String host = P2PConstant.getHost();
		int port  = P2PConstant.getPort();
		int timeout = P2PConstant.P2P_UPLOAD_TIMEOUT;
		File file = new File(filename);

		try {
			NetworkUtil.uploadFile(host, port, timeout, file, notifier);
		} catch (FileNotFoundException e) {
			notify(e);
		} catch (IOException e) {
			notify(e);
		}
	}

	private void deleteFile(String filename) {
		logMe("deleteFile");
		try {
			new File(filename).delete();
	    } catch (RuntimeException e) {
	    	logMe(e);
	    }
	}

	private void notify(String message) {
		if (notifier != null) {
			notifier.notifyMessage(message);
		}
	}

	private void notify(Exception ex) {
		if (notifier != null) {
			notifier.notifyError(ex);
		}
	}

	private void logMe(String message) {
		Logger.logMe(TAG, message);
		notify(message);
	}

	private void logMe(Exception ex) {
		Logger.logMe(TAG, ex);
		notify(ex);
    }
}