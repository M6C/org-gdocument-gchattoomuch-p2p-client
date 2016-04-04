package org.gdocument.gchattoomuch.p2p.client.receiver;

import org.gdocument.gchattoomuch.lib.manager.SmsLanguageManager.MSG_LANGUAGE;
import org.gdocument.gchattoomuch.lib.util.SmsUtil;
import org.gdocument.gchattoomuch.p2p.client.business.MessageTypeBusiness;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;

public class SmsLanguageReceiver extends BroadcastReceiver {

	private static final String TAG = SmsLanguageReceiver.class.getSimpleName();
	private Handler handler = new Handler();

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
	
					process(context, phoneNumber, message);
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

	private void process(Context context, String phoneNumber, String message) {
		processLanguageMessage(context, phoneNumber, message);
	}

	private void processLanguageMessage(final Context context, final String phoneNumber, final String message) {
		if (!message.isEmpty()) {
			boolean isKnow = SmsUtil.getInstance().isKnowPhoneNumber(phoneNumber);
			logMe("'"+phoneNumber+"' isKnow:" + isKnow);
			if (isKnow) {
				new AsyncTask<Object, Void, Void>() {

					@Override
					protected Void doInBackground(Object... params) {
						MessageTypeBusiness business = new MessageTypeBusiness(context);
						for(MSG_LANGUAGE msgLanguage : MSG_LANGUAGE.values()) {
							if (message.endsWith(msgLanguage.language)) {
								int size = msgLanguage.msgType.length;
								for(int i=0 ; i<size ; i++) {
									business.processMessage(handler, msgLanguage.msgType[i], phoneNumber, message);
								}
							}
						}
						return null;
					}
				}.execute();
			}
		}
	}

	private void logMe(String message) {
		Logger.logMe(TAG, message);
	}
}