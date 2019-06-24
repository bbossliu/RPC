package com.java.test;

import java.net.InetSocketAddress;

import com.java.client.Client01;
import com.java.server.Server01;

public class ClientTest01 {
	public static void main(String[] args) {
		try {
			Server01 server01=new Client01().getREmoteProxyObj(Class.forName("com.java.server.Server01"), new InetSocketAddress("127.0.0.1",9999));
			String helloName = server01.helloName("yyy");
			System.out.println(helloName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
