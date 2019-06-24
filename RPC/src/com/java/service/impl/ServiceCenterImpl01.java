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

	//连接池：连接池中存在多个连接对象，每个连接对象都可以处理一个客户请求
	private static ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	//标志服务是否开启
	private static boolean isRunning=false;
	@Override
	public void start() {
		System.out.println("开启服务！");
		isRunning=true;
		try {
			// TODO Auto-generated method stub
			//与客户端建立连接
			ServerSocket serverSocket = new ServerSocket();
			//绑定端口号
			serverSocket.bind(new InetSocketAddress(port));
			//开始接收客户端
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
				//对象流的接收必须严格遵守它的发送顺序
				//接口名称
				String serviseInterfaceName=input.readUTF();
				//方法名称
				String methodName=input.readUTF();
				//方法参数类型
				Class[] methodParameteType=(Class[])input.readObject();
				//方法参数列表
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
		//注册某一个服务
		hashMap.put(server.getName(), serverImpl);
	
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		executor.shutdown();	
		isRunning=false;
		
	}

}
