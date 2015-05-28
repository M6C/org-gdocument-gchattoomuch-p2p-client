package org.gdocument.gchattoomuch.p2p.client.db.service;

import org.gdocument.gchattoomuch.p2p.client.business.MessageTypeBusiness.CONTENT_PROVIDER;
import org.gdocument.gchattoomuch.p2p.client.db.datasource.DBContentProviderDataSource;
import org.gdocument.gchattoomuch.p2p.client.model.ContentProviderData;

import android.content.Context;

import com.cameleon.common.android.db.sqlite.service.GenericService;
import com.cameleon.common.android.inotifier.INotifierMessage;

public class DbContentProviderService extends GenericService<ContentProviderData> {

	public DbContentProviderService(Context context, INotifierMessage notificationMessage, CONTENT_PROVIDER contentProvider) {
		super(context, new DBContentProviderDataSource(context, notificationMessage, contentProvider), notificationMessage);
	}

	public String[] getColumnList() {
		return ((DBContentProviderDataSource)dbDataSource).getColumnList();
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