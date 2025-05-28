package com.epoint.lsyc.comprehensivewindow.meta.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

@Entity(table = "metasharerelation", id = {"rowguid"})
public class MetaShareRelation extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public String getRowguid() {
		return (String) super.get("rowguid");
	}

	public void setRowguid(String rowguid) {
		super.set("rowguid", rowguid);
	}

	public String getShareguid() {
		return (String) super.get("shareguid");
	}

	public void setShareguid(String shareguid) {
		super.set("shareguid", shareguid);
	}
	
	public String getMetaguid() {
		return (String) super.get("metaguid");
	}

	public void setMetaguid(String metaguid) {
		super.set("metaguid", metaguid);
	}
	
	public String getMetaspguid() {
		return (String) super.get("metaspguid");
	}

	public void setMetaspguid(String metaspguid) {
		super.set("metaspguid", metaspguid);
	}
}
