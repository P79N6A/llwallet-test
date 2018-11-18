package com.llwallet.interfaces.bean.enterprise;

import com.llwallet.interfaces.bean.BaseBean;

public class UserPayment extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String offset;
	private String maxrecordes;
	private String dt_start;
	private String dt_end;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getMaxrecordes() {
		return maxrecordes;
	}
	public void setMaxrecordes(String maxrecordes) {
		this.maxrecordes = maxrecordes;
	}
	public String getDt_start() {
		return dt_start;
	}
	public void setDt_start(String dt_start) {
		this.dt_start = dt_start;
	}
	public String getDt_end() {
		return dt_end;
	}
	public void setDt_end(String dt_end) {
		this.dt_end = dt_end;
	}
}
