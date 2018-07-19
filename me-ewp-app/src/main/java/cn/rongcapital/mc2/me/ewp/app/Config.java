package cn.rongcapital.mc2.me.ewp.app;

import java.util.Collections;

import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.rongcapital.mc2.me.commons.infrastructure.djob.DjobTaskExecutor;
import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteNodeType;

@Configuration
public class Config {

	@Value("${me.ewp.service.ignite.name}")
	private String name;

	@Value("${me.ewp.service.ignite.addresses}")
	private String[] addresses;

	@Bean
	public DjobTaskExecutor djobTaskExecutor() {
		return new DjobTaskExecutor();
	}

	@Bean
	public IgniteSpringBean igniteSpringBean() {
		IgniteConfiguration configuration = new IgniteConfiguration();
		configuration.setIgniteInstanceName(name);
		configuration.setPeerClassLoadingEnabled(true);
		configuration.setUserAttributes(Collections.singletonMap(IgniteNodeType.SERVICE_NODE.name(), true));
		IgniteSpringBean bean = new IgniteSpringBean();
		bean.setConfiguration(configuration);
		return bean;
	}

}
