package cn.rongcapital.mc2.me.ewp.app.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowWatchCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandReceiver;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowShutdownService;

@Component
public class CampaignFlowWatchReceiver extends RedissonCommandReceiver<CampaignFlowWatchCommand> {

	@Autowired
	private CampaignFlowShutdownService campaignFlowShutdownService;

	@Override
	public void listen(CampaignFlowWatchCommand event) {
		String flowId = (String) event.getSource();
		campaignFlowShutdownService.shutdownForWatch(flowId);
	}

	@Override
	public String getCommandName() {
		return CampaignFlowWatchCommand.COMMAND_NAME;
	}

}
