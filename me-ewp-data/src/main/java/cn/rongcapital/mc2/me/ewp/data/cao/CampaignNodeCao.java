package cn.rongcapital.mc2.me.ewp.data.cao;

import org.apache.ignite.springdata.repository.config.RepositoryConfig;

import cn.rongcapital.mc2.me.ewp.data.CacheName;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignNodeRepository;

@RepositoryConfig(cacheName = CacheName.CAMPAIGN_NODE_CACHE_NAME)
public interface CampaignNodeCao extends CampaignNodeRepository {

}
