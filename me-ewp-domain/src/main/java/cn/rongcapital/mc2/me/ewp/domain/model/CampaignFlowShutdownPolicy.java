package cn.rongcapital.mc2.me.ewp.domain.model;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignFlowWatchCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobScheduledExecutor;
import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobTask;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignFlowShutdownPolicy {

	@Expose
	@Field(FieldName.FIELD_MODE)
	private String mode;

	@Expose
	@Field(FieldName.FIELD_EXPRESSION)
	private String expression;

	@Expose
	@Field(FieldName.FIELD_ADVANCED)
	private CampaignFlowShutdownAdvanced advanced;

	/**
	 * 执行调度安排
	 * @param flowId 流程ID
	 */
	public void schedule(String flowId) {
		CampaignFlowShutdownMode modeEnum = CampaignFlowShutdownMode.valueOf(this.mode);
		// 终止流程
		// 判断终止模式：运行一次即终止 or 定时终止
		if (CampaignFlowShutdownMode.AUTO.equals(modeEnum)) {
			// 判断是否所有流程节点停留数都为0
			CampaignFlowWatchCommand command = new CampaignFlowWatchCommand(flowId);
			RedissonCommandInvoker.action(CampaignFlowWatchCommand.COMMAND_NAME, command);
		} else if (CampaignFlowShutdownMode.TIMING.equals(modeEnum)) {
			// 终止任务
			DjobTask task = advanced.buildShutdownTask(flowId);
			DjobScheduledExecutor.schedule(task, this.expression);
		}
	}

	/**
	 * 手动执行
	 * @param flowId
	 * @param shutdownOption
	 */
	public void manual(String flowId, int shutdownOption) {
		CampaignFlowShutdownAdvanced advanced = new CampaignFlowShutdownAdvanced(shutdownOption);
		DjobTask task = advanced.buildShutdownTask(flowId);
		CompletableFuture.runAsync(task);
	}

}
