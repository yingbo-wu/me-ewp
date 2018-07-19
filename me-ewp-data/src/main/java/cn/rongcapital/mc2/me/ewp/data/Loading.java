package cn.rongcapital.mc2.me.ewp.data;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteCacheLoader;

@Component
public class Loading implements ApplicationListener<ApplicationReadyEvent> {

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		IgniteCacheLoader.load(CacheName.CAMPAIGN_FLOW_CACHE_NAME);
		IgniteCacheLoader.load(CacheName.CAMPAIGN_NODE_CACHE_NAME);
	}

}
