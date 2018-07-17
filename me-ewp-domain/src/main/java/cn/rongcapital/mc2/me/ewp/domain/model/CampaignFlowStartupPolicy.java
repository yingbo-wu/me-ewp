package cn.rongcapital.mc2.me.ewp.domain.model;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowStartupCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobScheduledExecutor;
import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobTask;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignFlowStartupPolicy {

	@Expose
	@Field(FieldName.FIELD_MODE)
	private String mode;

	@Expose
	@Field(FieldName.FIELD_EXPRESSION)
	private String expression;

	/**
	 * 执行调度安排
	 * @param flowId 流程ID
	 */
	public void schedule(String flowId) {
		// 启动流程
		String taskId = "CAMPAIGN_FLOW_STARTUP_" + flowId;
		DjobTask task = new DjobTask(taskId, () -> {
			CampaignFlowStartupCommand command = new CampaignFlowStartupCommand(flowId);
			RedissonCommandInvoker.action(CampaignFlowStartupCommand.COMMAND_NAME, command);
		});
		CampaignFlowStartupMode modeEnum = CampaignFlowStartupMode.valueOf(this.mode);
		// 判断启动模式：立即启动 or 定时启动
		if (CampaignFlowStartupMode.IMMEDIATELY.equals(modeEnum)) {
			// 直接启动
			CompletableFuture.runAsync(task);
		} else if (CampaignFlowStartupMode.TIMING.equals(modeEnum)) {
			DjobScheduledExecutor.schedule(task, this.expression);
		}
	}

}
