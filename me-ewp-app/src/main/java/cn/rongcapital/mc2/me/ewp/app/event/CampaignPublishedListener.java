package cn.rongcapital.mc2.me.ewp.app.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.event.CampaignPublishedEvent;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonEventListener;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowPublishService;

@Component
public class CampaignPublishedListener extends RedissonEventListener<CampaignPublishedEvent> {

	@Autowired
	private CampaignFlowPublishService campaignFlowPublishService;

	@Override
	public void listen(CampaignPublishedEvent event) {
		String diagramJson = (String) event.getSource();
		campaignFlowPublishService.publish(diagramJson);
	}

	@Override
	public String getEventName() {
		return CampaignPublishedEvent.EVENT_NAME;
	}

}
