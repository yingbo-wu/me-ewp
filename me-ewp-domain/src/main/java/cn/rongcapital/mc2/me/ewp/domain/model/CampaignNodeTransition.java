package cn.rongcapital.mc2.me.ewp.domain.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignNodeTransition {

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_IS_DEFAULT)
	private boolean isDefault;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_THRESHOLD)
	private Object threshold;

	public boolean isDefault() {
		return this.isDefault;
	}

	@Override
	public int hashCode() {
		if (null != this.threshold) {
			return this.threshold.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return null != this.threshold && this.threshold.equals(obj);
	}

}
