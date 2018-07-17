package cn.rongcapital.mc2.me.ewp.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import cn.rongcapital.mc2.me.commons.infrastructure.kafka.KafkaStreamFilter;
import cn.rongcapital.mc2.me.commons.infrastructure.kafka.KafkaStreamProcessor;
import cn.rongcapital.mc2.me.commons.infrastructure.kafka.KafkaStreamStoreBuilderFactory;
import cn.rongcapital.mc2.me.commons.infrastructure.spring.PropertyContext;
import cn.rongcapital.mc2.me.commons.model.EwpContext;
import cn.rongcapital.mc2.me.commons.model.EwpResult;
import cn.rongcapital.mc2.me.commons.util.GsonUtils;

public abstract class CampaignNodeStream {

	protected StreamsBuilder streamsBuilder;

	protected KafkaStreams kafkaStreams;

	protected Topology topology;

	protected KStream<String, String> kStream;

	/**
	 * 打开节点流
	 * @param node
	 */
	public void open(CampaignNode node) {
		String nodeId = node.getId();
		List<CampaignNodeLineOutcoming> outcomings = node.lookupOutcomings();
		KafkaStreamStoreBuilderFactory storeBuilderFactory = KafkaStreamStoreBuilderFactory.build(nodeId);
		this.streamsBuilder = new StreamsBuilder();
		this.streamsBuilder.addStateStore(storeBuilderFactory.getStoreBuilder());
		this.topology = this.streamsBuilder.build();
		this.kStream = this.streamsBuilder.stream(node.obtainTopic());
		this.kStream.process(processor(node));
		// 配置转移目标
		outcomings.stream().filter(outcoming -> outcoming.canTransiation()).forEach(outcoming -> {
			this.kStream.filter(new KafkaStreamFilter() {
				@Override
				public boolean test(String key, String value) {
					EwpContext context = GsonUtils.create().fromJson(value, EwpContext.class);
					EwpResult result = context.lookupResult(nodeId);
					if (null != result) {
						Object transition = result.getTransition();
						return outcoming.isTransiation(transition);
					}
					return false;
				}
			}).to(outcoming.obtainTopic());
		});
		// 设置kafka streams启动参数
		String env = PropertyContext.build().getProperty("spring.profiles.active");
		String servers = PropertyContext.build().getProperty(String.format("me.ewp.kstream.servers.%s", env));
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ME-EWP-STREAMS-" + nodeId);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, Runtime.getRuntime().availableProcessors());
		StreamsConfig streamsConfig = new StreamsConfig(props);
		this.kafkaStreams = new KafkaStreams(this.topology, streamsConfig);
		this.kafkaStreams.start();
	}

	/**
	 * 关闭节点流
	 */
	public void close() {
		kafkaStreams.close();
		kafkaStreams.cleanUp();
		kafkaStreams = null;
		topology = null;
		streamsBuilder = null;
	}

	/**
	 * 创建流处理器
	 * @param node
	 * @return
	 */
	public abstract KafkaStreamProcessor processor(CampaignNode node);

}
