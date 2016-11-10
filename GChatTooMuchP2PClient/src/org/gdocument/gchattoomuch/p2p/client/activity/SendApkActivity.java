package org.gdocument.gchattoomuch.p2p.client.activity;

import java.io.File;
import java.io.IOException;

import org.gdocument.gchattoomuch.lib.log.Logger;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cameleon.common.tool.ApkTool;

public class SendApkActivity extends Activity {

	private final static String TAG = SendApkActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finish();

		try {
			String sourceDir = ApkTool.getInstance(this).querySourceDir(this.getPackageName());
			if (sourceDir != null) {
				Intent intentSendApk = new Intent();
				intentSendApk.setAction(Intent.ACTION_SEND);
				intentSendApk.setType("application/octet-stream");
//				intentSendApk.setPackage("com.android.bluetooth");
				intentSendApk.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(sourceDir)));

//				PackageManager packageManager = getPackageManager();
//				List<ResolveInfo> list = packageManager.queryIntentActivities(intentSendApk, PackageManager.MATCH_DEFAULT_ONLY);
//				logMe("queryIntentActivities list.size:" + (list == null ? 0 : list.size()));
//				for(ResolveInfo resolver : list) {
//					try {
//						// com.android.bluetooth
//						logMe("queryIntentActivities resolver.activityInfo.packageName:" + resolver.activityInfo.packageName);
//						packageManager.clearPackagePreferredActivities(resolver.activityInfo.packageName);
//					} catch (RuntimeException ex) {
//						logMe(ex.getMessage());
//					}
//				}

				this.startActivity(intentSendApk);
				
			}
		} catch (IOException e) {
    		logMe(e);
    	}
	}

	@SuppressWarnings("unused")
	private void logMe(String msg) {
		Logger.logMe(TAG, msg);
	}

	private static void logMe(Exception ex) {
		Logger.logMe(TAG, ex);
    }
}
