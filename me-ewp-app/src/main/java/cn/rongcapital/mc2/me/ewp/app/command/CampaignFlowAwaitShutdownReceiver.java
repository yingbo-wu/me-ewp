package cn.rongcapital.mc2.me.ewp.app.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowAwaitShutdownCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandReceiver;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowShutdownService;

@Component
public class CampaignFlowAwaitShutdownReceiver extends RedissonCommandReceiver<CampaignFlowAwaitShutdownCommand> {

	@Autowired
	private CampaignFlowShutdownService campaignFlowShutdownService;

	@Override
	public void listen(CampaignFlowAwaitShutdownCommand command) {
		String flowId = (String) command.getSource();
		campaignFlowShutdownService.shutdownForAwait(flowId);
	}

	@Override
	public String getCommandName() {
		return CampaignFlowAwaitShutdownCommand.COMMAND_NAME;
	}

}
