package cn.rongcapital.mc2.me.ewp.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNode;
import cn.rongcapital.mc2.me.ewp.domain.model.CampaignNodeStatus;
import cn.rongcapital.mc2.me.ewp.domain.repository.CampaignNodeRepository;

@Service
public class CampaignNodeRunService {

	@Autowired
	private CampaignNodeRepository campaignNodeRepository;

	/**
	 * 运行全部已启动流程的节点
	 */
	public void run() {
		List<CampaignNode> nodes = campaignNodeRepository.findByStatus(CampaignNodeStatus.RUNNING);
		nodes.stream().forEach(node -> {
			node.openStream();
		});
		campaignNodeRepository.save(nodes);
	}

	/**
	 * 运行流程的节点
	 * @param flowId
	 */
	public void run(String flowId) {
		List<CampaignNode> nodes = campaignNodeRepository.findByFlowId(flowId);
		nodes.stream().forEach(node -> {
			node.createStat();
			node.openStream();
		});
		campaignNodeRepository.save(nodes);
	}

}
