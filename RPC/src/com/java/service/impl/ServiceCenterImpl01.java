package com.java.service.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.java.service.ServiceCenter01;

public class ServiceCenterImpl01 implements ServiceCenter01{
	private static Map<String,Class> hashMap=new HashMap<>();
	private static int port;
	public static void setPort(int port) {
		ServiceCenterImpl01.port = port;
	}

	//���ӳأ����ӳ��д��ڶ�����Ӷ���ÿ�����Ӷ��󶼿��Դ���һ���ͻ�����
	private static ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	//��־�����Ƿ���
	private static boolean isRunning=false;
	@Override
	public void start() {
		System.out.println("��������");
		isRunning=true;
		try {
			// TODO Auto-generated method stub
			//��ͻ��˽�������
			ServerSocket serverSocket = new ServerSocket();
			//�󶨶˿ں�
			serverSocket.bind(new InetSocketAddress(port));
			//��ʼ���տͻ���
			Socket socket=serverSocket.accept();
			
			executor.execute(new earnRunnable(socket));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	private static class  earnRunnable implements Runnable{
		Socket socket=null;
		public earnRunnable(Socket socket) {
			this.socket=socket;
		}
		@Override
		public void run() {
			ObjectInputStream input=null;
			ObjectOutputStream output=null;
			// TODO Auto-generated method stub
			try {
				input=new ObjectInputStream(socket.getInputStream());
				//�������Ľ��ձ����ϸ��������ķ���˳��
				//�ӿ�����
				String serviseInterfaceName=input.readUTF();
				//��������
				String methodName=input.readUTF();
				//������������
				Class[] methodParameteType=(Class[])input.readObject();
				//���������б�
				Object[]  methodArgs= (Object[])input.readObject();
				
				Class serviceImpl01 = hashMap.get(serviseInterfaceName);
				Method method=serviceImpl01.getMethod(methodName, methodParameteType);
				Object result=method.invoke(serviceImpl01.newInstance(), methodArgs);
				
				output=new ObjectOutputStream(socket.getOutputStream());
				
				output.writeObject(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(output!=null) {
					try {
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(input!=null) {
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

//	@SuppressWarnings()
	@Override
	public void register(Class server,Class serverImpl) {
		// TODO Auto-generated method stub
		//ע��ĳһ������
		hashMap.put(server.getName(), serverImpl);
	
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		executor.shutdown();	
		isRunning=false;
		
	}

}
