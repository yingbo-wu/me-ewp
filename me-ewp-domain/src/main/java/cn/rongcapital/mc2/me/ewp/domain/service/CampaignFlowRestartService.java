package cn.rongcapital.mc2.me.ewp.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignFlowRepository;

@Service
public class CampaignFlowRestartService {

	@Autowired
	private CampaignFlowRepository campaignFlowRepository;

	/**
	 * 重启流程
	 * @param flowId
	 */
	public void restart(String flowId) {
		CampaignFlow flow = campaignFlowRepository.findOne(flowId);
		flow.startup();
	}

}
