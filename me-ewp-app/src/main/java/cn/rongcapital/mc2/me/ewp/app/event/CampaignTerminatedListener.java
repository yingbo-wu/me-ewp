package cn.rongcapital.mc2.me.ewp.app.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.event.CampaignTerminatedEvent;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonEventListener;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowCancelService;

@Component
public class CampaignTerminatedListener extends RedissonEventListener<CampaignTerminatedEvent> {

	@Autowired
	private CampaignFlowCancelService campaignFlowCancelService;

	@Override
	public void listen(CampaignTerminatedEvent event) {
		String campaignId = (String) event.get("campaignId");
		Integer shutdownOption = (Integer) event.get("campaignId");
		campaignFlowCancelService.cancel(campaignId, shutdownOption);
	}

	@Override
	public String getEventName() {
		return CampaignTerminatedEvent.EVENT_NAME;
	}

}
