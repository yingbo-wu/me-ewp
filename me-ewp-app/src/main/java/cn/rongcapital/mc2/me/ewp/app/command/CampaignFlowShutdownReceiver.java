package cn.rongcapital.mc2.me.ewp.app.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowShutdownCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandReceiver;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowShutdownService;

@Component
public class CampaignFlowShutdownReceiver extends RedissonCommandReceiver<CampaignFlowShutdownCommand> {

	@Autowired
	private CampaignFlowShutdownService campaignFlowShutdownService;

	@Override
	public void listen(CampaignFlowShutdownCommand command) {
		String flowId = (String) command.getSource();
		campaignFlowShutdownService.shutdown(flowId);
	}

	@Override
	public String getCommandName() {
		return CampaignFlowShutdownCommand.COMMAND_NAME;
	}

}
