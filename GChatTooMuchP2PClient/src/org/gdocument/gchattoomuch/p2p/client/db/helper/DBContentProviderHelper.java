package org.gdocument.gchattoomuch.p2p.client.db.helper;

import java.util.ArrayList;
import java.util.List;

import org.gdocument.gchattoomuch.p2p.client.business.MessageTypeBusiness.CONTENT_PROVIDER;
import org.gdocument.gchattoomuch.p2p.client.manager.ContentProviderManager;

import android.content.Context;

import com.cameleon.common.android.db.sqlite.helper.GenericDBHelper;
import com.cameleon.common.android.inotifier.INotifierMessage;

public class DBContentProviderHelper extends GenericDBHelper {

	private static final String TAG = DBContentProviderHelper.class.getCanonicalName();

	public static final int DATABASE_VERSION = 1;

	private List<String> columnList = new ArrayList<String>();
	private String databaseCreate = null;

	private CONTENT_PROVIDER provider;

	public DBContentProviderHelper(Context context, INotifierMessage notificationMessage, CONTENT_PROVIDER provider) {
		super(context, notificationMessage, provider.databaseName, DATABASE_VERSION);
		this.provider = provider;
		this.columnList.addAll(new ContentProviderManager(context, provider).getColumnList());
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public String getTableName() {
		return provider.tableName;
	}

	@Override
	public String getDatabaseCreate() {
		if (databaseCreate == null) {
			StringBuilder sb = new StringBuilder("CREATE TABLE " + provider.tableName + "(");
			for(int i = 0 ; i < getColumnList().size() ; i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(getColumnList().get(i)).append(" TEXT NULL");
			}
			sb.append(");");
			databaseCreate = sb.toString();
		}
		return databaseCreate;
	}

	public List<String> getColumnList() {
		return columnList;
	}
}