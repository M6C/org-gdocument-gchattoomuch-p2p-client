package org.gdocument.gchattoomuch.p2p.client.db.datasource;

import java.util.HashMap;
import java.util.Map;

import org.gdocument.gchattoomuch.p2p.client.business.MessageTypeBusiness.CONTENT_PROVIDER;
import org.gdocument.gchattoomuch.p2p.client.db.helper.DBContentProviderHelper;
import org.gdocument.gchattoomuch.p2p.client.model.ContentProviderData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cameleon.common.android.db.sqlite.datasource.GenericDBDataSource;
import com.cameleon.common.android.inotifier.INotifierMessage;
import com.cameleon.common.tool.DbTool;

public class DBContentProviderDataSource extends GenericDBDataSource<ContentProviderData> {

	private static final String TAG = DBContentProviderDataSource.class.getCanonicalName();

	private String[] columnList;

	public DBContentProviderDataSource(Context context, INotifierMessage notificationMessage, CONTENT_PROVIDER contentProvider) {
		super(new DBContentProviderHelper(context, notificationMessage, contentProvider), notificationMessage);
		columnList = ((DBContentProviderHelper)dbHelper).getColumnList().toArray(new String[0]);
	}

	@Override
	protected String[] getAllColumns() {
		return columnList;
	}

	@Override
	protected void putContentValue(ContentValues values, ContentProviderData content) {
		Map<String, String> data = content.getData();
		for(String name : columnList) {
			String d = data.get(name);
			if (d != null) {
				values.put(name, d);
			}
		}
	}

	@Override
	protected ContentProviderData cursorToPojo(Cursor cursor) {
		ContentProviderData ret = new ContentProviderData();
		HashMap<String, String> data = new HashMap<String, String>();
		for(int i=0 ; i < columnList.length ; i++) {
			String d = DbTool.getInstance().toString(cursor, i);
			if (d != null) {
				data.put(columnList[i], d);
			}
		}
		ret.setData(data);
		return ret;
	}

	@Override
	protected String getTag() {
		return TAG;
	}

	public String[] getColumnList() {
		return columnList;
	}
}