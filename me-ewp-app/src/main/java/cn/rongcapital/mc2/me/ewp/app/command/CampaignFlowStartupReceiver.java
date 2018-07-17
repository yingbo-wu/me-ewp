package cn.rongcapital.mc2.me.ewp.app.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowStartupCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandReceiver;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowStartupService;

@Component
public class CampaignFlowStartupReceiver extends RedissonCommandReceiver<CampaignFlowStartupCommand> {

	@Autowired
	private CampaignFlowStartupService campaignFlowStartupService;

	@Override
	public void listen(CampaignFlowStartupCommand command) {
		String flowId = (String) command.getSource();
		campaignFlowStartupService.startup(flowId);
	}

	@Override
	public String getCommandName() {
		return CampaignFlowStartupCommand.COMMAND_NAME;
	}

}
