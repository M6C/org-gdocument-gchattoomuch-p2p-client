package org.gdocument.gchattoomuch.p2p.client.business;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.gdocument.gchattoomuch.lib.log.Logger;
import org.gdocument.gchattoomuch.p2p.client.business.MessageTypeBusiness.CONTENT_PROVIDER;
import org.gdocument.gchattoomuch.p2p.client.db.service.DbContentProviderService;
import org.gdocument.gchattoomuch.p2p.client.manager.ContentProviderManager;
import org.gdocument.gchattoomuch.p2p.client.model.ContentProviderData;

import com.cameleon.common.android.inotifier.INotifierMessage;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Handler;

public class ContentProviderExportBusiness {

	private static final String TAG = ContentProviderExportBusiness.class.getName();
	private INotifierMessage notifier;

	public ContentProviderExportBusiness() {
	}

	public ContentProviderExportBusiness(INotifierMessage notifier) {
		this.notifier = notifier;
	}

	public void export(final Handler handler, final Context context, final CONTENT_PROVIDER contentProvider, final CountDownLatch latch) {
		new AsyncTask<Object, Void, Void>() {
			private DbContentProviderService service;
			private ContentProviderManager manager;
			private CountDownLatch latchData = new CountDownLatch(1);

			@Override
			protected void onPreExecute() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						service = new DbContentProviderService(context, null, contentProvider);
						manager = new ContentProviderManager(context, contentProvider);
						latchData.countDown();
					}
				});
			};

			@Override
			protected Void doInBackground(Object... params) {
				try {
					latchData.await();
					exportSmsInDb();
	
					if (latch != null) {
						latch.countDown();
					}
				} catch (InterruptedException e) {
					logMe(e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
			};

			private void exportSmsInDb() {
				logMe("exportSmsInDb contentProvider:" + contentProvider);
				List<ContentProviderData> data = manager.getList();

				try {
					service.deleteAll();
				} catch (SQLiteException e) {
					logMe(e);
				}
				for (ContentProviderData d : data) {
					service.createOrUpdate(d);
				}
			}
		}.execute();
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