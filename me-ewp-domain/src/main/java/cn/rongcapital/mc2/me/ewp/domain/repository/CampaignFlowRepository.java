package cn.rongcapital.mc2.me.ewp.domain.repository;

import java.util.List;

import org.apache.ignite.springdata.repository.IgniteRepository;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignFlow;

public interface CampaignFlowRepository extends IgniteRepository<CampaignFlow, String> {

	List<CampaignFlow> findByStatus(Integer status);

	CampaignFlow findOneByCampaignId(String campaignId);

}
