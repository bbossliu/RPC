package com.java.service;

public interface ServiceCenter01 {

	//开启服务
	public void start();
	
	//服务注册
	public void register(Class server,Class serverImpl);
	
	
	//服务关闭
	public void stop();
	
	
}
