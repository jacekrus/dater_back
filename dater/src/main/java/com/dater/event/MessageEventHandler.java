package com.dater.event;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;

@Component
public class MessageEventHandler {

	
	private final SimpMessagingTemplate simpTemplate;

	@Autowired
	public MessageEventHandler(SimpMessagingTemplate simpTemplate) {
		this.simpTemplate = simpTemplate;
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onMessageEvent(MessageSendEvent event) {
		ConversationMessageEntity message = event.getMessage();
		ConversationEntity conversation = message.getConversation();
		Set<UserEntity> users = new HashSet<>(conversation.getUsers());
		conversation.setCreateTime(null);
		conversation.setName(null);
		conversation.setUsers(null);
		conversation.setLatestMessageTime(null);
		for(UserEntity user : users) {
			if(!user.getUsername().equals(message.getSender().getUsername())) {
				simpTemplate.convertAndSendToUser(user.getUsername(), "/queue/messages", message);
			}
		}
	}
	
}
