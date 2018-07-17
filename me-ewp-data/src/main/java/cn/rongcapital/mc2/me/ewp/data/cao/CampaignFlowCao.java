package cn.rongcapital.mc2.me.ewp.data.cao;

import org.apache.ignite.springdata.repository.config.RepositoryConfig;

import cn.rongcapital.mc2.me.ewp.data.Consts;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignFlowRepository;

@RepositoryConfig(cacheName = Consts.CAMPAIGN_FLOW_CACHE_NAME)
public interface CampaignFlowCao extends CampaignFlowRepository {

}
