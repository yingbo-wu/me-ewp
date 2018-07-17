package cn.rongcapital.mc2.me.ewp.data.store;

import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import org.apache.ignite.cache.store.CacheStoreAdapter;

import cn.rongcapital.mc2.me.commons.infrastructure.spring.BeanContext;
import cn.rongcapital.mc2.me.ewp.data.dao.CampaignNodeDao;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNode;

public class CampaignNodeStore extends CacheStoreAdapter<Object, CampaignNode> {

	@Override
	public CampaignNode load(Object key) throws CacheLoaderException {
		CampaignNodeDao dao = BeanContext.build().getBean(CampaignNodeDao.class);
		return dao.findOne((String) key);
	}

	@Override
	public void write(Entry<? extends Object, ? extends CampaignNode> entry) throws CacheWriterException {
		CampaignNodeDao dao = BeanContext.build().getBean(CampaignNodeDao.class);
		CampaignNode entity = entry.getValue();
		entity.setId((String) entry.getKey());
		dao.save(entity);
	}

	@Override
	public void delete(Object key) throws CacheWriterException {
		CampaignNodeDao dao = BeanContext.build().getBean(CampaignNodeDao.class);
		dao.delete(load(key));
	}

}