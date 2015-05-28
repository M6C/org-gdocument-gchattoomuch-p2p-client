package org.gdocument.gchattoomuch.p2p.client.manager;

import java.util.ArrayList;
import java.util.List;

import org.gdocument.gchattoomuch.p2p.client.business.MessageTypeBusiness.CONTENT_PROVIDER;
import org.gdocument.gchattoomuch.p2p.client.db.mapper.ContentProviderMapper;
import org.gdocument.gchattoomuch.p2p.client.model.ContentProviderData;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import com.cameleon.common.android.manager.GenericCursorManager;

public class ContentProviderManager extends GenericCursorManager<ContentProviderData, ContentProviderMapper> {

	private List<String> columnList = null;

	public ContentProviderManager(Context context, CONTENT_PROVIDER provider) {
		super(context);

		getCursorLoader().setUri(Uri.parse(provider.url));
		initializeColumnList(context);
	}

	public List<String> getColumnList() {
		return columnList;
	}

	@Override
	protected CursorLoader buildCursorLoader(Context context) {
		return new CursorLoader(context);
	}

	@Override
	protected ContentProviderMapper getMapper() {
		return new ContentProviderMapper(columnList.toArray(new String[0]));
	}

	@Override
	protected boolean showLogCursor() {
		return false;
	}

	private void initializeColumnList(Context context) {
		if (columnList == null) {
			columnList = new ArrayList<String>();
			Cursor cursor = null;
			try {
				cursor = getCursorLoader().loadInBackground();
				if (cursor!=null) {
					int columnsQty = cursor.getColumnCount();
					for(int i=0 ; i<columnsQty ; i++) {
						columnList.add(cursor.getColumnName(i));
					}
				}
			} finally {
				if (cursor!=null) {
					cursor.close();
				}
			}
		}
	}
}