package cn.rongcapital.mc2.me.ewp.data.store;

import java.util.List;

import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;

import cn.rongcapital.mc2.me.commons.infrastructure.spring.BeanContext;
import cn.rongcapital.mc2.me.ewp.data.dao.CampaignFlowDao;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;

public class CampaignFlowStore extends CacheStoreAdapter<Object, CampaignFlow> {

	@Override
	public void loadCache(IgniteBiInClosure<Object, CampaignFlow> clo, Object... args) {
		CampaignFlowDao dao = BeanContext.build().getBean(CampaignFlowDao.class);
		List<CampaignFlow> entities = dao.findAll();
		entities.parallelStream().forEach(entity -> {
			clo.apply(entity.getId(), entity);
		});
	}

	@Override
	public CampaignFlow load(Object key) throws CacheLoaderException {
		CampaignFlowDao dao = BeanContext.build().getBean(CampaignFlowDao.class);
		return dao.findOne((String) key);
	}

	@Override
	public void write(Entry<? extends Object, ? extends CampaignFlow> entry) throws CacheWriterException {
		CampaignFlowDao dao = BeanContext.build().getBean(CampaignFlowDao.class);
		CampaignFlow entity = entry.getValue();
		entity.setId((String) entry.getKey());
		dao.save(entity);
	}

	@Override
	public void delete(Object key) throws CacheWriterException {
		CampaignFlowDao dao = BeanContext.build().getBean(CampaignFlowDao.class);
		dao.delete(load(key));
	}

}