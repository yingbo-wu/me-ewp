package cn.rongcapital.mc2.me.ewp.app.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.event.CampaignFlowStartedEvent;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonEventListener;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignNodeRunService;

@Component
public class CampaignFlowStartedListener extends RedissonEventListener<CampaignFlowStartedEvent> {

	@Autowired
	private CampaignNodeRunService campaignNodeRunService;

	@Override
	public void listen(CampaignFlowStartedEvent event) {
		String flowId = (String) event.getSource();
		campaignNodeRunService.run(flowId);
	}

	@Override
	public String getEventName() {
		return CampaignFlowStartedEvent.EVENT_NAME;
	}

}
