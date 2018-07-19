package cn.rongcapital.mc2.me.ewp.domain.model;

import static cn.rongcapital.mc2.me.commons.infrastructure.reactor.ReactorNettyClient.REACTOR_NETTY_CLIENT;

import java.util.Map;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.commons.model.EwpContext;
import cn.rongcapital.mc2.me.commons.model.EwpResult;
import cn.rongcapital.mc2.me.commons.util.GsonUtils;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class CampaignNodeComponent {

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_DATA)
	private Map<String, Object> data;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_PRINCIPAL)
	private CampaignNodePrincipal principal;

	public int extractCount() {
		Integer total = (Integer) data.get("total");
		return total;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> extractSubscription() {
		return (Map<String, Object>) data.get("subscription");
	}

	public int extractRefreshMode() {
		Integer refreshMode = (Integer) data.get("refresh_mode");
		return refreshMode;
	}

	public boolean isRefreshable() {
		Integer refreshMode = (Integer) data.get("refresh_mode");
		return CrowdRefreshMode.UN_REFRESH != refreshMode;
	}

	/**
	 * 创建上下文对象
	 * 
	 * @param mid
	 * @param campaignId
	 * @param flowId
	 * @param nodeId
	 * @param nodeType
	 * @return
	 */
	public EwpContext newContext(int mid, String campaignId, String flowId, String nodeId, String nodeType) {
		String ndataJson = GsonUtils.create().toJson(this.data);
		return new EwpContext(mid, campaignId, flowId, nodeId, nodeType, ndataJson);
	}

	/**
	 * 运行组件
	 * @param tenantId
	 * @param context
	 * @return
	 */
	public Mono<EwpResult> execute(int tenantId, EwpContext context) {
		Mono<EwpResult> mono = Mono.create(callback -> {
			try {
				REACTOR_NETTY_CLIENT.post(this.principal.buildAccessUrl(tenantId), (request) -> {
					request.headers(this.principal.buildHeaders(tenantId));
					return request.sendObject(context);
				}).doOnError(error -> {
					callback.success(EwpResult.error(error));
				}).publishOn(Schedulers.parallel()).subscribe(response -> {
					response.receiveObject().ofType(EwpResult.class).doOnError(error -> {
						callback.success(EwpResult.error(error));
					}).subscribe(result -> {
						try {
							callback.success(result);
						} catch (Exception e) {
							callback.success(EwpResult.error(e));
						}
					});
				});
			} catch (Exception e) {
				callback.success(EwpResult.error(e));
			}
		});
		return mono;
	}

}
