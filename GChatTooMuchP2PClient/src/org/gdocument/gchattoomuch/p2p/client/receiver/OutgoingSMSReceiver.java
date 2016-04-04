package org.gdocument.gchattoomuch.p2p.client.receiver;

import org.gdocument.gchattoomuch.p2p.client.service.OutgoingSMSService;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OutgoingSMSReceiver extends BroadcastReceiver {

	private static final String TAG = OutgoingSMSReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
        logMe("Caller History: onReceive. OutgoingSMSReceiver");
		context.startService(new Intent(context.getApplicationContext(), OutgoingSMSService.class));
	}

	private void logMe(String message) {
		Logger.logMe(TAG, message);
	}
}
