package com.abner.c1n.beans.vo.url;

/**
 * 对应时间的访问量
 * @author LW
 * @time 2019年6月8日 下午3:02:47
 */
public class PvByDate extends Pv{
	
	private String logDate;

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public PvByDate(String logDate) {
		super();
		this.logDate = logDate;
	}

	public PvByDate() {
		super();
	}
	
	


}
