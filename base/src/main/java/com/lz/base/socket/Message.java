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

import com.lz.base.util.ConvertUtil;

import java.net.SocketAddress;
import java.nio.ByteBuffer;


public final class Message {

	protected SocketAddress address;
	protected byte[] data;
	
	public Message(SocketAddress address, byte[] data){
		this.address = address;
		this.data = data;
	}



	public int getCmd(){
		byte b = data[2];
		return b & 0xff;
	}


	@Override
	public String toString() {
		return "" +ConvertUtil.bytesToHexString(data);
	}

	public void setData(byte[] data){
		this.data = data;
	}
	
	public byte[] getData(){
		return this.data;
	}
	
	public void setSocketAddress(SocketAddress address){
		this.address = address;
	}
	
	public SocketAddress getSocketAddress(){
		return this.address;
	}





}
