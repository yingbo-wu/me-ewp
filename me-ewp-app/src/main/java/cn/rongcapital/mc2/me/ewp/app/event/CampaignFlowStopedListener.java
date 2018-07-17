package cn.rongcapital.mc2.me.ewp.app.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.event.CampaignFlowStopedEvent;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonEventListener;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignNodeExitService;

@Component
public class CampaignFlowStopedListener extends RedissonEventListener<CampaignFlowStopedEvent> {

	@Autowired
	private CampaignNodeExitService campaignNodeExitService;

	@Override
	public void listen(CampaignFlowStopedEvent event) {
		String flowId = (String) event.getSource();
		campaignNodeExitService.exit(flowId);
	}

	@Override
	public String getEventName() {
		return CampaignFlowStopedEvent.EVENT_NAME;
	}

}
