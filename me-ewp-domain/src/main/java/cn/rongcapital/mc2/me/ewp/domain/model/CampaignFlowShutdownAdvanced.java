package cn.rongcapital.mc2.me.ewp.domain.model;

import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowAwaitShutdownCommand;
import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowShutdownCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobTask;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignFlowShutdownAdvanced {

	@Expose
	@Field(FieldName.FIELD_OPTION)
	private int option;

	public CampaignFlowShutdownAdvanced(int option) {
		this.option = option;
	}

	public DjobTask buildShutdownTask(String flowId) {
		if (CampaignFlowShutdownOption.IMMEDIATELY == option) {
			String taskId = "CAMPAIGN_FLOW_SHUTDOWN_" + flowId;
			return new DjobTask(taskId, () -> {
				CampaignFlowShutdownCommand command = new CampaignFlowShutdownCommand(flowId);
				RedissonCommandInvoker.action(CampaignFlowShutdownCommand.COMMAND_NAME, command);
			});
		} else {
			String taskId = "CAMPAIGN_FLOW_AWAIT_SHUTDOWN_" + flowId;
			return new DjobTask(taskId, () -> {
				CampaignFlowAwaitShutdownCommand command = new CampaignFlowAwaitShutdownCommand(flowId);
				RedissonCommandInvoker.action(CampaignFlowAwaitShutdownCommand.COMMAND_NAME, command);
			});
		}
	}

}
