package cn.rongcapital.mc2.me.ewp.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignFlowRepository;

@Service
public class CampaignFlowCancelService {

	@Autowired
	private CampaignFlowRepository campaignFlowRepository;

	/**
	 * 终止发布流程
	 * @param campaignId
	 * @param shutdownOption
	 */
	public void cancel(String campaignId, int shutdownOption) {
		CampaignFlow flow = campaignFlowRepository.findOneByCampaignId(campaignId);
		flow.manualShutdown(shutdownOption);
	}

}
