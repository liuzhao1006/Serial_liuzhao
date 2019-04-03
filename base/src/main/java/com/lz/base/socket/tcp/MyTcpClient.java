/*
 *Copyright 2014 DDPush
 *Author: AndyKwok(in English) GuoZhengzhu(in Chinese)
 *Email: ddpush@126.com
 *

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/
package com.lz.base.socket.tcp;

import com.lz.base.socket.Message;
import com.lz.base.socket.util.StringUtil;



public class MyTcpClient extends TCPClientBase {

	public MyTcpClient(String serverAddr,
			int serverPort, int connectTimeout) throws Exception {
		super(serverAddr, serverPort, connectTimeout);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hasNetworkConnection() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onPushMessage(Message msg) {
		if(msg == null){
			System.out.println("msg is null");
		}
		if(msg.getData() == null || msg.getData().length == 0){
			System.out.println("msg has no data");
		}
		System.out.println("---"+StringUtil.convert(msg.getData()));
	}

	@Override
	public void trySystemSleep() {
		//System.out.println("try sleep");

	}
	
	public static void main(String[] args){
		try{
			MyTcpClient myTcpClient = new MyTcpClient( "192.168.2.111", 9966, 5);
			myTcpClient.setHeartbeatInterval(50);
			myTcpClient.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
