package cn.rongcapital.mc2.me.ewp.domain.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.commons.api.ApiResult;
import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeFailCommand;
import cn.rongcapital.mc2.me.commons.feign.gen.GenResult;
import cn.rongcapital.mc2.me.commons.feign.gen.api.SubscribeApi;
import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteEntity;
import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteServiceFactory;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;
import cn.rongcapital.mc2.me.commons.infrastructure.spring.PropertyContext;
import cn.rongcapital.mc2.me.commons.model.EwpContext;
import cn.rongcapital.mc2.me.commons.model.EwpResult;
import cn.rongcapital.mc2.me.commons.util.FeignUtils;
import cn.rongcapital.mc2.me.commons.util.GsonUtils;
import cn.rongcapital.mc2.me.ewa.api.CampaignStatApi;
import cn.rongcapital.mc2.me.ewa.api.dto.CampaignStatCreateIn;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;
import cn.rongcapital.mc2.me.ewq.api.CampaignQueueApi;
import cn.rongcapital.mc2.me.ewq.api.dto.CampaignQueuePullIn;
import cn.rongcapital.mc2.me.ewq.api.dto.CampaignQueuePushIn;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SuppressWarnings("serial")
@Document(collection = "campaign_node")
public class CampaignNode extends IgniteEntity {

	@Expose
	@QuerySqlField(index = true)
	@Field(FieldName.FIELD_TENANT_ID)
	private int tenantId;

	@Expose
	@QuerySqlField(index = true)
	@Field(FieldName.FIELD_CAMPAIGN_ID)
	private String campaignId;

	@Expose
	@QuerySqlField(index = true)
	@Field(FieldName.FIELD_FLOW_ID)
	private String flowId;

	@Expose
	@QuerySqlField(index = true)
	@Field(FieldName.FIELD_NODE_ID)
	private String nodeId;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_TYPE)
	private String type;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_SETTING)
	private CampaignNodeSetting setting;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_COMPONENT)
	private CampaignNodeComponent component;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_INCOMINGS)
	private List<CampaignNodeLineIncoming> incomings;

	@Expose
	@QuerySqlField
	@Field(FieldName.FIELD_OUTCOMINGS)
	private List<CampaignNodeLineOutcoming> outcomings;

	@Transient
	private CampaignNodeStream stream;

	public CampaignNode() {
		this.status = CampaignNodeStatus.INITIALIZED;
	}

	/**
	 * 判断start节点是否是可刷新的
	 * @return
	 */
	public boolean isRefreshable() {
		CampaignNodeType typeEnum = CampaignNodeType.valueOf(type);
		if (CampaignNodeType.START.equals(typeEnum)) {
			return component.isRefreshable();
		} else {
			throw new RuntimeException("当前节点不是START类型, 不能调用此方法");
		}
	}

	/**
	 * 追加租户ID
	 * @param tenantId
	 * @return
	 */
	public CampaignNode appendTenandId(int tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * 追加租户ID
	 * @param campaignId
	 * @return
	 */
	public CampaignNode appendCampaignId(String campaignId) {
		this.campaignId = campaignId;
		return this;
	}

	/**
	 * 追加流程ID
	 * @param flowId
	 * @return
	 */
	public CampaignNode appendFlowId(String flowId) {
		this.flowId = flowId;
		return this;
	}

	/**
	 * 查找输出线
	 * @return
	 */
	public List<CampaignNodeLineOutcoming> lookupOutcomings() {
		return this.outcomings;
	}

	/**
	 * 查找输出连线
	 * @param threshold 阀值
	 * @return
	 */
	public CampaignNodeLineOutcoming lookupOutcoming(Object threshold) {
		Optional<CampaignNodeLineOutcoming> optional = this.outcomings.stream().filter(line -> line.isTransiation(threshold)).findFirst();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	/**
	 * 判断是不是start节点
	 * @return
	 */
	public boolean isStartNode() {
		CampaignNodeType typeEnum = CampaignNodeType.valueOf(type);
		return CampaignNodeType.START.equals(typeEnum);
	}

	/**
	 * 创建统计信息
	 * @return
	 */
	public void createStat() {
		CampaignStatApi campaignStatApi = IgniteServiceFactory.newService(CampaignStatApi.class);
		CampaignNodeType typeEnum = CampaignNodeType.valueOf(this.type);
		if (CampaignNodeType.START.equals(typeEnum)) {
			int stayCount = this.component.extractCount();
			CampaignStatCreateIn in = new CampaignStatCreateIn(this.campaignId, this.flowId, this.nodeId, this.type, stayCount);
			campaignStatApi.create(in);
		} else {
			CampaignStatCreateIn in = new CampaignStatCreateIn(this.campaignId, this.flowId, this.nodeId, this.type, 0);
			campaignStatApi.create(in);
		}
	}

	/**
	 * 生成环境上下文
	 * @param mid
	 * @return
	 */
	public String newContextJson(int mid) {
		EwpContext context = this.component.newContext(mid, this.campaignId, this.flowId, this.nodeId, this.type);
		return GsonUtils.create().toJson(context);
	}

	/**
	 * 延迟存储上下文
	 * @param context
	 */
	public void pushContextDelay(String context) {
		long expire = this.setting.lookupDelayExpire();
		CampaignQueueApi campaignQueueApi = IgniteServiceFactory.newService(CampaignQueueApi.class);
		CampaignQueuePushIn in = new CampaignQueuePushIn(this.flowId, this.nodeId, expire, context);
		campaignQueueApi.push(in);
	}

	/**
	 * 拉取过期的上下文
	 * @return
	 */
	public List<String> pullContextExpired() {
		CampaignQueueApi campaignQueueApi = IgniteServiceFactory.newService(CampaignQueueApi.class);
		CampaignQueuePullIn in = new CampaignQueuePullIn(this.flowId, this.nodeId);
		ApiResult<List<String>> apiResult = campaignQueueApi.pull(in);
		if (apiResult.isOk()) {
			return apiResult.getData();
		}
		return null;
	}

	/**
	 * 运行组件
	 * @param value
	 * @param consumer
	 * @return
	 */
	public void execute(String value, Consumer<String> consumer) {
		EwpContext context = GsonUtils.create().fromJson(value, EwpContext.class);
		Mono<EwpResult> mono = this.component.execute(this.tenantId, context);
		mono.publishOn(Schedulers.parallel()).subscribe(result -> {
			context.appendResult(this.nodeId, result);
			String contextJson = GsonUtils.create().toJson(context);
			if (result.isOk()) {
				consumer.accept(contextJson);
			} else {
				if (this.setting.isIgnoreError()) {
					consumer.accept(contextJson);
				}
				CampaignNodeFailCommand command = new CampaignNodeFailCommand(contextJson);
				RedissonCommandInvoker.action(CampaignNodeFailCommand.COMMAND_NAME, command);
			}
		});
	}

	/**
	 * 获取话题
	 * @return
	 */
	public String obtainTopic() {
		return String.format("ME-EWP-%s", nodeId);
	}

	/**
	 * 打开流
	 */
	public void openStream() {
		CampaignNodeType nodeType = CampaignNodeType.valueOf(this.type);
		if (CampaignNodeType.START.equals(nodeType)) {
			String url = PropertyContext.build().getProperty("generator.proxy.base.url");
			SubscribeApi api = FeignUtils.proxyRestful(SubscribeApi.class, url);
			GenResult<Object> result = api.subscribe(this.component.extractSubscription());
			if (!result.isOk()) {
				throw new RuntimeException("订阅人群数据失败");
			}
			this.stream = new CampaignNodeStreamStart();
		} else if (CampaignNodeType.END.equals(nodeType)) {
			this.stream = new CampaignNodeStreamEnd();
		} else if (CampaignNodeType.ACTION.equals(nodeType)) {
			this.stream = new CampaignNodeStreamAction();
		} else if (CampaignNodeType.DECISION.equals(nodeType)) {
			this.stream = new CampaignNodeStreamDecision();
		} else if (CampaignNodeType.DELAY.equals(nodeType)) {
			this.stream = new CampaignNodeStreamDelay();
		}
		this.stream.open(this);
		this.status = CampaignNodeStatus.RUNNING;
		this.updateAt = new Date();
	}

	/**
	 * 关闭流
	 */
	public void closeStream() {
		CampaignNodeType nodeType = CampaignNodeType.valueOf(this.type);
		if (CampaignNodeType.START.equals(nodeType)) {
			String url = PropertyContext.build().getProperty("generator.proxy.base.url");
			SubscribeApi api = FeignUtils.proxyRestful(SubscribeApi.class, url);
			GenResult<Object> result = api.unsubscribe(this.id);
			if (!result.isOk()) {
				throw new RuntimeException("取消订阅人群数据失败");
			}
		}
		this.stream.close();
		this.status = CampaignNodeStatus.RUN_EXIT;
		this.updateAt = new Date();
	}

}
