package cn.rongcapital.mc2.me.ewp.domain.model;

import java.util.Date;
import java.util.List;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.commons.communication.event.CampaignFlowStartedEvent;
import cn.rongcapital.mc2.me.commons.communication.event.CampaignFlowStopedEvent;
import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteEntity;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonEventPublisher;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;

@Document(collection = "campaign_flow")
public class CampaignFlow extends IgniteEntity {

	@Expose
	@QuerySqlField(index = true)
	@Field(FieldName.FIELD_TENANT_ID)
	private int tenantId;

	@Expose
	@QuerySqlField(index = true)
	@Field(FieldName.FIELD_CAMPAIGN_ID)
	private String campaignId;

	@Expose
	@Field(FieldName.FIELD_DIAGRAM)
	private CampaignFlowDiagram diagram;

	@Expose
	@Field(FieldName.FIELD_STARTUP_POLICY)
	private CampaignFlowStartupPolicy startupPolicy;

	@Expose
	@Field(FieldName.FIELD_SHUTDOWN_POLICY)
	private CampaignFlowShutdownPolicy shutdownPolicy;

	public CampaignFlow() {
		this.status = CampaignFlowStatus.INITIALIZED;
	}

	/**
	 * 提取节点信息
	 * @return
	 */
	public List<CampaignNode> parseNodes() {
		return this.diagram.parseNodes(this.tenantId, this.id);
	}

	/**
	 * 注册调度
	 */
	public void schedule() {
		this.startupPolicy.schedule(this.id);
		this.shutdownPolicy.schedule(this.id);
	}

	/**
	 * 启动流程
	 */
	public void startup() {
		if (CampaignFlowStatus.STARTED != this.status) {
			CampaignFlowStartedEvent event = new CampaignFlowStartedEvent(this.id);
			RedissonEventPublisher.publish(CampaignFlowStartedEvent.EVENT_NAME, event);
			this.status = CampaignFlowStatus.STARTED;
			this.updateAt = new Date();
		}
	}

	/**
	 * 终止流程
	 */
	public void shutdown() {
		CampaignFlowStopedEvent event = new CampaignFlowStopedEvent(this.id);
		RedissonEventPublisher.publish(CampaignFlowStopedEvent.EVENT_NAME, event);
		this.status = CampaignFlowStatus.STOPED;
		this.updateAt = new Date();
	}

	/**
	 * 手动终止
	 * @param shutdownOption
	 */
	public void manualShutdown(int shutdownOption) {
		this.shutdownPolicy.manual(this.id, shutdownOption);
	}

}
