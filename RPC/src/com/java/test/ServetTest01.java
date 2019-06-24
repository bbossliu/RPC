package com.java.test;

import com.java.server.Server01;
import com.java.server.impl.ServerImpl01;
import com.java.service.ServiceCenter01;
import com.java.service.impl.ServiceCenterImpl01;

public class ServetTest01 {
	public static void main(String[] args) {
		ServiceCenterImpl01 serviceCenter01=new ServiceCenterImpl01();
		serviceCenter01.setPort(9999);
		serviceCenter01.register(Server01.class, ServerImpl01.class);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				
				serviceCenter01.start();
			}
			
		}).start();
				
				
	
		
	}

}
