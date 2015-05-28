package org.gdocument.gchattoomuch.p2p.client.model;

import java.util.List;
import java.util.Map;

import com.cameleon.common.android.model.GenericDBPojo;

public class ContentProviderData extends GenericDBPojo<Long> {

	private static final long serialVersionUID = 1L;

	private List<String> columnList;
	private Map<String, String> data = null;

	public ContentProviderData() {
		super();
	}

	public ContentProviderData(Long id) {
		super(id);
	}

	public ContentProviderData(Map<String, String> data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
}