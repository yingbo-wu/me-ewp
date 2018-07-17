package cn.rongcapital.mc2.me.ewp.domain.repository;

import java.util.List;

import org.apache.ignite.springdata.repository.IgniteRepository;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNode;

public interface CampaignNodeRepository extends IgniteRepository<CampaignNode, String> {

	List<CampaignNode> findByFlowId(String flowId);

	List<CampaignNode> findByStatus(Integer status);

}
