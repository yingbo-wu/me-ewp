package cn.rongcapital.mc2.me.ewp.data.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNode;

@Repository
public interface CampaignNodeDao extends MongoRepository<CampaignNode, String> {

}
