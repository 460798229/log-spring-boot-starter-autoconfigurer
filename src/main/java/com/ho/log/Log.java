package com.ho.log;

import java.util.Date;

public class Log {
	private long id;
	private long action;
	private String logBody;
	private long userId;
	private Date createTime;
	private long applicationId;
	private String ip;
	private String host;
	private int logLevel;
	private long executeTime; //执行时间 毫秒
	private String sessionId;
	private String mac;
	private String remark;
	private long threadId;

	/*public Log(String logBody) {
		this.logBody = logBody;
		id = ID.get18ID();
		this.createTime = new Date();
		IUserInfo userInfo = Context.getUserInfo();
		if(userInfo != null) {
			this.userId = userInfo.getId();
			this.ip = userInfo.getIp();
			this.host = userInfo.getHost();
			this.mac = userInfo.getMac();
			this.sessionId = userInfo.getToken();
		}
		this.remark = "";
		this.threadId = Thread.currentThread().getId();
	}*/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public long getAction() {
		return action;
	}

	public void setAction(long action) {
		this.action = action;
	}

	public String getLogBody() {
		return logBody;
	}

	public void setLogBody(String logBody) {
		this.logBody = logBody;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
