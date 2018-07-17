package cn.rongcapital.mc2.me.ewp.domain.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.commons.infrastructure.spring.PropertyContext;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;

public class CampaignNodePrincipal {

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_NAME)
	private String name;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_VERSION)
	private String version;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_TOKEN)
	private String token;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_UID)
	private String uid;

	/**
	 * 创建身份headers
	 * @return
	 */
	public HttpHeaders buildHeaders(int tenantId) {
		HttpHeaders headers = new DefaultHttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("X-ME-AUTHED", "true");
		headers.add("Access-Token", token);
		headers.add("User-Id", uid);
		headers.add("Tenant-Id", String.valueOf(tenantId));
		return headers;
	}

	/**
	 * 构建组件访问地址
	 * @param tenantId
	 * @return
	 */
	public String buildAccessUrl(int tenantId) {
		String baseUrl = PropertyContext.build().getProperty("faas.gateway.base.url", String.class);
		String uri = PropertyContext.build().getProperty("faas.gateway.uri", String.class);
		return baseUrl + String.format(uri, tenantId, this.name, this.version);
	}

}
