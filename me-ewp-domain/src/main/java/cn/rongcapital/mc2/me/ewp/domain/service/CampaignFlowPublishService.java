package cn.rongcapital.mc2.me.ewp.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.commons.util.GsonUtils;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignFlowRepository;

@Service
public class CampaignFlowPublishService {

	@Autowired
	private CampaignFlowRepository campaignFlowRepository;

	/**
	 * 发布流程
	 * @param diagramJson
	 */
	public void publish(String diagramJson) {
		CampaignFlow flow = GsonUtils.createExposeLowerCaseWithUnderscores().fromJson(diagramJson, CampaignFlow.class);
		// 持久化流程定义
		//String id = UuidUtils.base58Uuid();
		campaignFlowRepository.save(flow);
		// 注册调度
		flow.schedule();
	}

}
