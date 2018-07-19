package cn.rongcapital.mc2.me.ewp.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignFlowRepository;

@Service
public class CampaignFlowFindService {

	@Autowired
	private CampaignFlowRepository campaignFlowRepository;

	public String flowId(String campaignId) {
		CampaignFlow flow = campaignFlowRepository.findOneByCampaignId(campaignId);
		return flow.getId();
	}

}
