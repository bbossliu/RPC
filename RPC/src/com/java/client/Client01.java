package com.java.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client01 {
	
	public static <T>T getREmoteProxyObj(Class serviceInterface,InetSocketAddress addressPort){
		
		
		//第二个参数:需要代理的对象，具备哪些方法  --接口  (数组类型)
		return (T)Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface},new InvocationHandler() {
			/**
			 * proxy 代理的对象  method代理那个方法   args 方法的参数列表
			 */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				// TODO Auto-generated method stub
				Socket socket=new Socket();
				//建立连接
				socket.connect(addressPort);
				//构建对象流进行操作
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				//接口名称
				//方法名称
				output.writeUTF(serviceInterface.getName());
				output.writeUTF(method.getName());
				//方法参数类型是数组
				output.writeObject(method.getParameterTypes());
				//方法参数列表
				output.writeObject(args);
				
				ObjectInputStream input=new ObjectInputStream(socket.getInputStream());
				Object readObject = input.readObject();
				return readObject;
			}
		});
	}

}
