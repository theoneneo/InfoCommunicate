package com.neo.infocommunicate.data;

import java.util.ArrayList;

/**
 * @author LiuBing
 * @version 2014-3-6 下午5:01:41
 */
public class ChatRoomInfo {
	public ChatRoomInfo(String id, MessageInfo info){
		sender_id = id;
		msg_infos = new ArrayList<MessageInfo>();
		if(info != null)
			msg_infos.add(info);
	}
	
    public String sender_id;
    public ArrayList<MessageInfo> msg_infos;
}
