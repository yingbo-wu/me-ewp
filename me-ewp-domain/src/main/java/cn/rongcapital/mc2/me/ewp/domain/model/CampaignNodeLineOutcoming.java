package cn.rongcapital.mc2.me.ewp.domain.model;

public class CampaignNodeLineOutcoming extends CampaignNodeLine {

	/**
	 * 是否满足转移
	 * @param transiation
	 * @return
	 */
	public boolean isTransiation(Object transiation) {
		if (null == transiation) {
			return this.transiation.isDefault();
		} else {
			return this.transiation.equals(transiation);
		}
	}

	/**
	 * 是否可以转移
	 * @return
	 */
	public boolean canTransiation() {
		return null != this.toId;
	}

	/**
	 * 获取话题
	 * @return
	 */
	public String obtainTopic() {
		return String.format("ME-EWP-%s", this.toId);
	}

}
