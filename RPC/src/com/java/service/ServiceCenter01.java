package com.java.service;

public interface ServiceCenter01 {

	//��������
	public void start();
	
	//����ע��
	public void register(Class server,Class serverImpl);
	
	
	//����ر�
	public void stop();
	
	
}
