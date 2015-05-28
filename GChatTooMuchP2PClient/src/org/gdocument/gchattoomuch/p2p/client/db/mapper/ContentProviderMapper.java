package org.gdocument.gchattoomuch.p2p.client.db.mapper;

import java.util.HashMap;

import org.gdocument.gchattoomuch.p2p.client.model.ContentProviderData;

import android.database.Cursor;

import com.cameleon.common.android.mapper.GenericMapper;
import com.cameleon.common.tool.DbTool;


public class ContentProviderMapper extends GenericMapper<ContentProviderData> {
	
	private static final String TAG = ContentProviderMapper.class.getName();
	private String[] columnList;

	public ContentProviderMapper(String[] columnList) {
		this.columnList = columnList;
	}

	@Override
	protected ContentProviderData cursorToPojo(Cursor cursor) {
		ContentProviderData pojo = new ContentProviderData();
		HashMap<String, String> data = new HashMap<String, String>();
		for(int i=0 ; i < columnList.length ; i++) {
			if (Cursor.FIELD_TYPE_BLOB != cursor.getType(i)) {
				String d = DbTool.getInstance().toString(cursor, i);
				if (d != null) {
					data.put(columnList[i], d);
				}
			}
		}
		pojo.setData(data);
		return pojo;
	}

	@Override
	public String[] getListColumn() {
		return columnList;
	}

	@Override
	protected String tagLogTrace() {
		return TAG;
	}
}