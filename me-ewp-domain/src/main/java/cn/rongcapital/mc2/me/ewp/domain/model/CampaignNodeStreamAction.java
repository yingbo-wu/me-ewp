package cn.rongcapital.mc2.me.ewp.domain.model;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;

import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeMoveCommand;
import cn.rongcapital.mc2.me.commons.communication.command.CampaignNodeStayCommand;
import cn.rongcapital.mc2.me.commons.infrastructure.kafka.KafkaStreamProcessor;
import cn.rongcapital.mc2.me.commons.infrastructure.redisson.RedissonCommandInvoker;

public class CampaignNodeStreamAction extends CampaignNodeStream {

	@Override
	public KafkaStreamProcessor processor(CampaignNode node) {
		String nodeId = node.getId();
		return new KafkaStreamProcessor(nodeId) {
			@Override
			public void process(String key, String value) {
				CampaignNodeStayCommand command = new CampaignNodeStayCommand(value);
				RedissonCommandInvoker.action(CampaignNodeStayCommand.COMMAND_NAME, command);
				node.execute(value, (context) -> {
					this.kvStore.put(key, context);
				});
			}

			@Override
			public void punctuate(long timestamp) {
				KeyValueIterator<String, String> iterator = this.kvStore.all();
				boolean commitFlag = false;
				while (iterator.hasNext()) {
					KeyValue<String, String> entry = iterator.next();
					CampaignNodeMoveCommand command = new CampaignNodeMoveCommand(entry.value);
					RedissonCommandInvoker.action(CampaignNodeMoveCommand.COMMAND_NAME, command);
					this.context.forward(entry.key, entry.value);
					this.kvStore.delete(entry.key);
					commitFlag = true;
				}
				if (commitFlag) {
					this.context.commit();
				}
			}
		};
	}

}
