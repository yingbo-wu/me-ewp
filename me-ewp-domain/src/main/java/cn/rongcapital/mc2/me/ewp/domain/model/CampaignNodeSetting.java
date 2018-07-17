package cn.rongcapital.mc2.me.ewp.domain.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignNodeSetting {

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_ERROR_HANDLE)
	private int errorHandle;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_DELAY_HANDLE)
	private CampaignNodeDelayHandle delayHandle;

	public boolean isIgnoreError() {
		return this.errorHandle == CampaignNodeErrorHandle.IGNORE;
	}

	public long lookupDelayExpire() {
		return this.delayHandle.calculateExpire();
	}

}
