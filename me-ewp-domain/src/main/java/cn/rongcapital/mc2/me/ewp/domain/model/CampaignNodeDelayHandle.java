package cn.rongcapital.mc2.me.ewp.domain.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignNodeDelayHandle {

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_MODE)
	private int mode;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_INTERVAL)
	private long interval;

	/**
	 * 计算延迟到期时间戳
	 * @return
	 */
	public long calculateExpire() {
		if (CampaignNodeDelayMode.RELATIVE == this.mode) {
			return System.currentTimeMillis() + this.interval;
		} else if (CampaignNodeDelayMode.ABSOLUTE == this.mode) {
			return this.interval;
		}
		return 0;
	}

}
