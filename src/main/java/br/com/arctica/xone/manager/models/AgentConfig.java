package br.com.arctica.xone.manager.models;

import java.util.Objects;

public class AgentConfig {

	private String login;
	private Boolean send;
	private Boolean update;
	private Boolean collect;
	private String version;

	public AgentConfig() {
	}

	public AgentConfig(String login, Boolean send, Boolean update, Boolean collect, String version) {
		this.login = login;
		this.send = send;
		this.update = update;
		this.collect = collect;
		this.version = version;
	}

	public AgentConfig(String login, String send, String update, String collect, String version) {
		this.login = login;
		this.send = false;
		this.update = false;
		this.collect = false;
	
		if(send.equals("1"))
			this.send = true;
	
		if(update.equals("1"))
			this.update = true;
		
		if(collect.equals("1"))
			this.collect = true;  	

		this.version = version;
	}


	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean isSend() {
		return this.send;
	}

	public Boolean getSend() {
		return this.send;
	}

	public void setSend(Boolean send) {
		this.send = send;
	}

	public Boolean isUpdate() {
		return this.update;
	}

	public Boolean getUpdate() {
		return this.update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public Boolean isCollect() {
		return this.collect;
	}

	public Boolean getCollect() {
		return this.collect;
	}

	public void setCollect(Boolean collect) {
		this.collect = collect;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public AgentConfig login(String login) {
		this.login = login;
		return this;
	}

	public AgentConfig send(Boolean send) {
		this.send = send;
		return this;
	}

	public AgentConfig update(Boolean update) {
		this.update = update;
		return this;
	}

	public AgentConfig collect(Boolean collect) {
		this.collect = collect;
		return this;
	}

	public AgentConfig version(String version) {
		this.version = version;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof AgentConfig)) {
			return false;
		}
		AgentConfig agentConfig = (AgentConfig) o;
		return Objects.equals(login, agentConfig.login) && Objects.equals(send, agentConfig.send) && Objects.equals(update, agentConfig.update) && Objects.equals(collect, agentConfig.collect) && Objects.equals(version, agentConfig.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(login, send, update, collect, version);
	}

	@Override
	public String toString() {
		return "{" +
			" login='" + getLogin() + "'" +
			", send='" + isSend() + "'" +
			", update='" + isUpdate() + "'" +
			", collect='" + isCollect() + "'" +
			", version='" + getVersion() + "'" +
			"}";
	}

}