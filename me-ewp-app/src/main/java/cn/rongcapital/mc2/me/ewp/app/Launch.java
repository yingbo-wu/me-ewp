package cn.rongcapital.mc2.me.ewp.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteServiceDeployment;
import cn.rongcapital.mc2.me.ewp.api.CampaignFlowApi;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowStartupService;

@Component
public class Launch implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private CampaignFlowStartupService campaignFlowStartupService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		IgniteServiceDeployment.deploy(CampaignFlowApi.class);
		campaignFlowStartupService.startup();
	}

}

