package cn.rongcapital.mc2.me.ewp.domain.model;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeStayCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.kafka.KafkaStreamProcessor;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;

public class CampaignNodeStreamEnd extends CampaignNodeStream {

	@Override
	public KafkaStreamProcessor processor(CampaignNode node) {
		String nodeId = node.getId();
		return new KafkaStreamProcessor(nodeId) {
			@Override
			public void process(String key, String value) {
				CampaignNodeStayCommand command = new CampaignNodeStayCommand(value);
				RedissonCommandInvoker.action(CampaignNodeStayCommand.COMMAND_NAME, command);
				this.context.forward(key, value);
				this.context.commit();
			}
		};
	}

}
