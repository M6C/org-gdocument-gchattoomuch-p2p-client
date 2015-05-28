package org.gdocument.gchattoomuch.p2p.client.db.service;

import org.gdocument.gchattoomuch.p2p.client.db.datasource.DBSmsCacheDataSource;
import org.gdocument.gchattoomuch.p2p.client.model.Sms;

import android.content.Context;

import com.cameleon.common.android.db.sqlite.service.GenericService;
import com.cameleon.common.android.inotifier.INotifierMessage;

public class DbSmsCacheService extends GenericService<Sms> {

	public DbSmsCacheService(Context context, INotifierMessage notificationMessage) {
		super(context, new DBSmsCacheDataSource(context, notificationMessage), notificationMessage);
	}

	public void create(Sms sms) {
    	try {
    		dbDataSource.open();
    		// Save in database
			dbDataSource.create(sms);
    	}
    	finally {
    		dbDataSource.close();
    	}
	}

	public void deleteAll() {
    	try {
    		dbDataSource.open();
    		// Delete in database
			dbDataSource.delete((String)null);
    	}
    	finally {
    		dbDataSource.close();
    	}
	}
}