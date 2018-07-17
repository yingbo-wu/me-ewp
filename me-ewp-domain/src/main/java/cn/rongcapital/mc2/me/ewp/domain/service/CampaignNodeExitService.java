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
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNode;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignNodeRepository;

@Service
public class CampaignNodeExitService {

	@Autowired
	private CampaignNodeRepository campaignNodeRepository;

	/**
	 * 使流程节点退出运行
	 * @param flowId
	 */
	public void exit(String flowId) {
		List<CampaignNode> nodes = campaignNodeRepository.findByFlowId(flowId);
		CampaignStatApi campaignStatApi = IgniteServiceFactory.newService(CampaignStatApi.class);
		String taskId = "CAMPAIGN_NODE_EXIT_" + flowId;
		DjobTask task = new DjobTask(taskId, () -> {
			CampaignStatFinaledIn in = new CampaignStatFinaledIn(flowId);
			ApiResult<Void> apiResult = campaignStatApi.isFinaled(in);
			if (apiResult.isOk()) {
				// 释放节点
				nodes.stream().forEach(node -> {
					node.closeStream();
				});
				DjobScheduledExecutor.cancel(taskId);
				campaignNodeRepository.save(nodes);
			}
		});
		DjobScheduledExecutor.schedule(task, "0 */10 * * * ?");
	}

}
