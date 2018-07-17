package cn.rongcapital.mc2.me.ewp.domain.model;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.processor.PunctuationType;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeMoveCommand;
import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeStayCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.kafka.KafkaStreamProcessor;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;

public class CampaignNodeStreamDelay extends CampaignNodeStream {

	@Override
	public KafkaStreamProcessor processor(CampaignNode node) {
		String nodeId = node.getId();
		return new KafkaStreamProcessor(nodeId, TimeUnit.MINUTES.toMillis(10), PunctuationType.WALL_CLOCK_TIME) {
			@Override
			public void process(String key, String value) {
				CampaignNodeStayCommand command = new CampaignNodeStayCommand(value);
				RedissonCommandInvoker.action(CampaignNodeStayCommand.COMMAND_NAME, command);
				node.pushContextDelay(value);
			}

			@Override
			public void punctuate(long timestamp) {
				List<String> contexts = node.pullContextExpired();
				contexts.stream().forEach(value -> {
					CampaignNodeMoveCommand command = new CampaignNodeMoveCommand(value);
					RedissonCommandInvoker.action(CampaignNodeMoveCommand.COMMAND_NAME, command);
					String key = UUID.randomUUID().toString();
					this.context.forward(key, value);
					this.context.commit();
				});
			}
		};
	}

}
