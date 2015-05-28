package org.gdocument.gchattoomuch.p2p.client.db.datasource;

import org.gdocument.gchattoomuch.p2p.client.db.helper.DBSmsCacheHelper;
import org.gdocument.gchattoomuch.p2p.client.model.Sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cameleon.common.android.db.sqlite.datasource.GenericDBDataSource;
import com.cameleon.common.android.inotifier.INotifierMessage;
import com.cameleon.common.tool.DbTool;

public class DBSmsCacheDataSource extends GenericDBDataSource<Sms> {

	private static final String TAG = DBSmsCacheDataSource.class.getCanonicalName();

	// Database fields
	private String[] allColumns = {
		DBSmsCacheHelper.COLUMN_ID,
		DBSmsCacheHelper.COLUMN_ADDRESS,
		DBSmsCacheHelper.COLUMN_BODY,
		DBSmsCacheHelper.COLUMN_READ,
		DBSmsCacheHelper.COLUMN_DATE,
		DBSmsCacheHelper.COLUMN_TYPE
	};

	public DBSmsCacheDataSource(Context context, INotifierMessage notificationMessage) {
		super(new DBSmsCacheHelper(context, notificationMessage), notificationMessage);
	}

	@Override
	protected String[] getAllColumns() {
		return allColumns;
	}

	@Override
	protected void putContentValue(ContentValues values, Sms sms) {
		values.put(DBSmsCacheHelper.COLUMN_ID, sms.getId());
		values.put(DBSmsCacheHelper.COLUMN_ADDRESS, sms.getAddress());
		values.put(DBSmsCacheHelper.COLUMN_BODY, sms.getBody());
		values.put(DBSmsCacheHelper.COLUMN_READ, sms.getRead());
		values.put(DBSmsCacheHelper.COLUMN_DATE, sms.getDate());
		values.put(DBSmsCacheHelper.COLUMN_TYPE, sms.getType());
	}

	@Override
	protected Sms cursorToPojo(Cursor cursor) {
		int col = 0;
		Sms sms = new Sms(DbTool.getInstance().toLong(cursor, col++));
		sms.setAddress(DbTool.getInstance().toString(cursor, col++));
		sms.setBody(DbTool.getInstance().toString(cursor, col++));
		sms.setRead(DbTool.getInstance().toString(cursor, col++));
		sms.setDate(DbTool.getInstance().toString(cursor, col++));
		sms.setType(DbTool.getInstance().toString(cursor, col++));
		return sms;
	}

	@Override
	protected String getTag() {
		return TAG;
	}
}