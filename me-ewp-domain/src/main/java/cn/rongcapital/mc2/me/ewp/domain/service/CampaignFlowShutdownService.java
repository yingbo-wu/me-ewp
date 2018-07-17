package cn.rongcapital.mc2.me.ewp.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.commons.api.ApiResult;
import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobScheduledExecutor;
import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobTask;
import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteServiceFactory;
import cn.rongcapital.mc2.me.ewa.api.CampaignStatApi;
import cn.rongcapital.mc2.me.ewa.api.dto.CampaignStatFinaledIn;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNode;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignFlowRepository;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignNodeRepository;

@Service
public class CampaignFlowShutdownService {

	@Autowired
	private CampaignFlowRepository campaignFlowRepository;

	@Autowired
	private CampaignNodeRepository campaignNodeRepository;

	/**
	 * 终止流程同时释放节点; 适用于用户选择立即终止场景
	 * @param flowId
	 */
	public void shutdown(String flowId) {
		CampaignFlow flow = campaignFlowRepository.findOne(flowId);
		List<CampaignNode> nodes = campaignNodeRepository.findByFlowId(flowId);
		// 终止流程
		flow.shutdown();
		// 释放节点
		nodes.stream().forEach(node -> {
			node.closeStream();
		});
		campaignFlowRepository.save(flow);
		campaignNodeRepository.save(nodes);
	}

	/**
	 * 等待流程实例结束后, 终止流程同时释放节点(最终一致); 适用于活动运行一次后自动终止场景
	 * @param flowId
	 */
	public void shutdownForWatch(String flowId) {
		CampaignStatApi campaignStatApi = IgniteServiceFactory.newService(CampaignStatApi.class);
		String taskId = "CAMPAIGN_FLOW_FINAL_SHUTDOWN_" + flowId;
		DjobTask task = new DjobTask(flowId, () -> {
			CampaignStatFinaledIn in = new CampaignStatFinaledIn(flowId);
			ApiResult<Void> apiResult = campaignStatApi.isFinaled(in);
			if (apiResult.isOk()) {
				shutdown(flowId);
				DjobScheduledExecutor.cancel(taskId);
			}
		});
		DjobScheduledExecutor.schedule(task, "0 */10 * * * ?");
	}

	/**
	 * 终止流程但不释放节点, 等待流程实例运行完毕后再释放节点; 适用于用户选择等待活动中所有人完成活动后终止场景
	 * @param flowId
	 */
	public void shutdownForAwait(String flowId) {
		CampaignFlow flow = campaignFlowRepository.findOne(flowId);
		// 终止流程
		flow.shutdown();
		campaignFlowRepository.save(flow);
	}

}
