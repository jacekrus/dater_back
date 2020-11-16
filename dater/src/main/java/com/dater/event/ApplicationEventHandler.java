package com.dater.event;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dater.event.AbstractApplicationEvent.ApplicationEventVisitor;
import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;

@Component
public class ApplicationEventHandler {

	private static final String USER_NOTIFICATIONS_QUEUE = "/queue/notifications";
	
	private final SimpMessagingTemplate simpTemplate;
	

	@Autowired
	public ApplicationEventHandler(SimpMessagingTemplate simpTemplate) {
		this.simpTemplate = simpTemplate;
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onApplicationEvent(AbstractApplicationEvent event) {
		event.apply(new ApplicationEventVisitor<Void>() {

			@Override
			public Void visit(MessageSendEvent e) {
				ConversationMessageEntity message = e.getMessage();
				ConversationEntity conversation = message.getConversation();
				Set<UserEntity> users = new HashSet<>(conversation.getUsers());
				conversation.setCreateTime(null);
				conversation.setName(null);
				conversation.setUsers(null);
				conversation.setLatestMessageTime(null);
				for(UserEntity user : users) {
					if(!user.getUsername().equals(message.getSender().getUsername())) {
						simpTemplate.convertAndSendToUser(user.getUsername(), USER_NOTIFICATIONS_QUEUE, e);
					}
				}
				return null;
			}

			@Override
			public Void visit(DateCreatedEvent e) {
				simpTemplate.convertAndSendToUser(e.getUser().getUsername(), USER_NOTIFICATIONS_QUEUE, e);
				simpTemplate.convertAndSendToUser(e.getDate().getUsername(), USER_NOTIFICATIONS_QUEUE, e);
				return null;
			}

			@Override
			public Void visit(FavoriteAddedEvent e) {
				simpTemplate.convertAndSendToUser(e.getUser().getUsername(), USER_NOTIFICATIONS_QUEUE, e);
				return null;
			}

			@Override
			public Void visit(UserLikedEvent e) {
				simpTemplate.convertAndSendToUser(e.getUser().getUsername(), USER_NOTIFICATIONS_QUEUE, e);
				return null;
			}
		});
	}
	
}
