package cn.rongcapital.mc2.me.ewp.domain.model;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeMoveCommand;
import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeStayCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.kafka.KafkaStreamProcessor;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;

public class CampaignNodeStreamStart extends CampaignNodeStream {

	@Override
	public KafkaStreamProcessor processor(CampaignNode node) {
		String nodeId = node.getId();
		return new KafkaStreamProcessor(nodeId) {
			@Override
			public void process(String key, String value) {
				String contextJson = node.newContextJson(Integer.parseInt(value));
				if (node.isRefreshable()) {
					CampaignNodeStayCommand command = new CampaignNodeStayCommand(contextJson);
					RedissonCommandInvoker.action(CampaignNodeStayCommand.COMMAND_NAME, command);
				}
				CampaignNodeMoveCommand command = new CampaignNodeMoveCommand(contextJson);
				RedissonCommandInvoker.action(CampaignNodeMoveCommand.COMMAND_NAME, command);
				this.context.forward(key, contextJson);
				this.context.commit();
			}
		};
	}

}
