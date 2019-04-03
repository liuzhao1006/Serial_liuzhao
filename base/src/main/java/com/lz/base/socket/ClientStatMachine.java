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
package com.lz.base.socket;

import com.lz.base.socket.tcp.MessengerTask;
import com.lz.base.socket.util.PropertyUtil;

import java.lang.ref.WeakReference;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/*
 * uuid终端状态机
 */
public class ClientStatMachine {

	public static final int CMD_0x00 = 0;//心跳包
	
	public static int PUSH_IM_AFTER_ACTIVE_SECOND = 60;
	
	private static final int POLICY_NEVER = 0;
	private static final int POLICY_SA_CHANGED = 1;
	private static final int POLICY_ALWAYS = 2;
	private static int ackHeartbeatPolicy;
	
	private static boolean createByClient = "YES".equalsIgnoreCase(PropertyUtil.getProperty("CREATE_MACHINE_BY_CLIENT"))?true:false;
	
	
	private long lastTick = -1;//最后心跳时间
	private SocketAddress lastAddr = null;//最后心跳等的网络地址
	
	private boolean hasMessage0x10 = false;//是否有通用信息未接收
	private long last0x10Time = -1;//最后通用信息时间
	
	private long message0x11 = 0;//最新分类信息通知
	private long last0x11Time = -1;//最新分类信息通知时间
	
	private int message0x20Len = 0;//是否有自定义信息未接收
	private long last0x20Time = -1;//最新自定义信息时间
	private byte[] message0x20 = null;
	
	//private MessengerTask messengerTask = null;
	private WeakReference<MessengerTask> messengerTaskRef = null;
	
	static{
		String strPolicy = PropertyUtil.getProperty("ACK_HEARTBEAT_POLICY");
		if("always".equalsIgnoreCase(strPolicy)){
			ackHeartbeatPolicy = POLICY_ALWAYS;
		}else if("sa_changed".equalsIgnoreCase(strPolicy)){
			ackHeartbeatPolicy = POLICY_SA_CHANGED;
		}else{
			ackHeartbeatPolicy = POLICY_NEVER;
		}
	}
	
	private ClientStatMachine(){
		
	}
	
	public void setMessengerTask(MessengerTask messengerTask){
		this.messengerTaskRef = new WeakReference<>(messengerTask);
	}
	
	public MessengerTask getMessengerTask(){
		if(this.messengerTaskRef == null)return null;
		return this.messengerTaskRef.get();
	}
	
	
	public long getLastTick(){
		return lastTick;
	}
	
	public SocketAddress getLastAddr(){
		return lastAddr;
	}
	
	public long getLast0x10Time(){
		return last0x10Time;
	}
	
	public long getLast0x11Time(){
		return last0x11Time;
	}
	
	public long getLast0x20Time(){
		return last0x20Time;
	}
	
	public int getMessage0x20Len(){
		return this.message0x20Len;
	}
	
	public byte[] getMessage0x20(){
		return this.message0x20;
	}
	
	public static ClientStatMachine newByClientTick(ClientMessage m) throws NullPointerException{
		
		if(m == null){
			return null;
		}
		if(createByClient == false){
			return null;
		}
//		if(m.getSocketAddress() == null){
//			return null;
//		}
		if(m.getCmd() != ClientStatMachine.CMD_0x00){
			return null;//非心跳包不产生新的状态机，以后可能登录包也会产生新状态机
		}
		ClientStatMachine csm = new ClientStatMachine();
		//csm.lastAddr = address;不能设置该值，否则创建状态机后第一次onClientMessage不回发心跳了
		csm.lastTick = System.currentTimeMillis();
		return csm;
	}
	

	public static ClientStatMachine newFromFile(long lastTick, boolean hasMessage0x10, long last0x10Time, long message0x11, long last0x11Time, int message0x20Len, long last0x20Time, byte[] message0x20){
		ClientStatMachine csm = new ClientStatMachine();
		csm.lastTick = lastTick;
		csm.hasMessage0x10 = hasMessage0x10;
		csm.last0x10Time = last0x10Time;
		csm.message0x11 = message0x11;
		csm.last0x11Time = last0x11Time;
		csm.message0x20Len = message0x20Len;
		csm.last0x20Time = last0x20Time;
		csm.message0x20 = message0x20;
		return csm;
	}
	
	public boolean has0x10Message(){
		return hasMessage0x10;
	}

	public boolean has0x20Message(){
		if(this.message0x20 == null || this.message0x20Len <= 0){
			return false;
		}
		return true;
	}
	
	public long get0x11Message(){
		return this.message0x11;
	}
	


}
