package com.java.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
//b'b'b
public class Client01 {
	
	public static <T>T getREmoteProxyObj(Class serviceInterface,InetSocketAddress addressPort){
		
		
		//�ڶ�������:��Ҫ����Ķ��󣬾߱���Щ����  --�ӿ�  (��������)
		return (T)Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface},new InvocationHandler() {
			/**
			 * proxy ����Ķ���  method�����Ǹ�����   args �����Ĳ����б�
			 */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				// TODO Auto-generated method stub
				Socket socket=new Socket();
				//��������
				socket.connect(addressPort);
				//�������������в���
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				//�ӿ�����
				//��������
				output.writeUTF(serviceInterface.getName());
				output.writeUTF(method.getName());
				//������������������
				output.writeObject(method.getParameterTypes());
				//���������б�
				output.writeObject(args);
				
				ObjectInputStream input=new ObjectInputStream(socket.getInputStream());
				Object readObject = input.readObject();
				return readObject;
			}
		});
	}

}
