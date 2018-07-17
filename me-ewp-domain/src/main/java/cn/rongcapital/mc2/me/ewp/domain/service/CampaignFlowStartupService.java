package cn.rongcapital.mc2.me.ewp.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlowStatus;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNode;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignFlowRepository;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignNodeRepository;

@Service
public class CampaignFlowStartupService {

	@Autowired
	private CampaignFlowRepository campaignFlowRepository;

	@Autowired
	private CampaignNodeRepository campaignNodeRepository;

	/**
	 * 加载启动状态流程
	 */
	public void startup() {
		List<CampaignFlow> flows = campaignFlowRepository.findByStatus(CampaignFlowStatus.STARTED);
		flows.stream().forEach(flow -> {
			flow.startup();
		});
	}

	/**
	 * 启动单个流程
	 * @param flowId
	 */
	public void startup(String flowId) {
		CampaignFlow flow = campaignFlowRepository.findOne(flowId);
		List<CampaignNode> nodes = flow.parseNodes();
		campaignNodeRepository.save(nodes);
		flow.startup();
		campaignFlowRepository.save(flow);
	}

}
