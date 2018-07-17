package cn.rongcapital.mc2.me.ewp.domain.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignNodeLine {

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_FROM_ID)
	protected String fromId;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_TO_ID)
	protected String toId;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_TRANSIATION)
	protected CampaignNodeTransition transiation;

	@Override
	public int hashCode() {
		if (null == this.fromId && null == this.toId) {
			throw new RuntimeException("fromId、toId都不能为空");
		}
		return (this.fromId + this.toId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (null == this.fromId && null == this.toId) {
			throw new RuntimeException("fromId、toId都不能为空");
		}
		if (obj instanceof CampaignNodeLine) {
			CampaignNodeLine line = (CampaignNodeLine) obj;
			return this.fromId.equals(line.fromId) && this.toId.equals(line.toId);
		} else {
			return false;
		}
	}

}
