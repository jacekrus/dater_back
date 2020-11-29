package com.dater.event;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dater.event.AbstractApplicationEvent.ApplicationEventVisitor;
import com.dater.model.ConversationEntity;
import com.dater.model.ConversationMessageEntity;
import com.dater.model.UserEntity;
import com.dater.service.PasswordResetService;

@Component
public class ApplicationEventHandler {

	private static final String USER_NOTIFICATIONS_QUEUE = "/queue/notifications";
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEventHandler.class);
	
	private final SimpMessagingTemplate simpTemplate;
	private final JavaMailSender mailSender;
	private final PasswordResetService passResetSvc;

	@Autowired
	public ApplicationEventHandler(SimpMessagingTemplate simpTemplate, JavaMailSender mailSender, PasswordResetService passResetSvc) {
		this.simpTemplate = simpTemplate;
		this.mailSender = mailSender;
		this.passResetSvc = passResetSvc;
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onApplicationEvent(AbstractApplicationEvent event) {
		event.apply(new ApplicationEventVisitor<Void>() {

			@Override
			public Void visit(MessageSendEvent e) {
				ConversationMessageEntity message = e.getMessage();
				ConversationEntity conversation = message.getConversation();
				Set<UserEntity> users = new HashSet<>(conversation.getUsers());
				conversation.setCreateTime(null);
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
				simpTemplate.convertAndSendToUser(e.getUsernameOrEmail(), USER_NOTIFICATIONS_QUEUE, e);
				simpTemplate.convertAndSendToUser(e.getDateUsername(), USER_NOTIFICATIONS_QUEUE, e);
				return null;
			}

			@Override
			public Void visit(FavoriteAddedEvent e) {
				simpTemplate.convertAndSendToUser(e.getUsernameOrEmail(), USER_NOTIFICATIONS_QUEUE, e);
				return null;
			}

			@Override
			public Void visit(UserLikedEvent e) {
				simpTemplate.convertAndSendToUser(e.getUsernameOrEmail(), USER_NOTIFICATIONS_QUEUE, e);
				return null;
			}

			@Override
			public Void visit(PasswordResetRequestedEvent e) {
				SimpleMailMessage email = createEmailMessage(e.getUsernameOrEmail(), e.getPassReset().getId());
				try {
					mailSender.send(email);
					passResetSvc.setEmailSuccessful(e.getPassReset().getId());
				}
				catch(Exception ex) {
					LOGGER.error(String.format("Exception occured while sending email to user: %s", e.getUsernameOrEmail()), ex);
				}
				return null;
			}
		});
	}
	
	private SimpleMailMessage createEmailMessage(String email, String requestId) {
		SimpleMailMessage emailMessage = new SimpleMailMessage();
		emailMessage.setTo(email);
		emailMessage.setFrom("dater.noreply@gmail.com");
		emailMessage.setSubject("Dater password reset");
		emailMessage.setText("Hi, you have requested a password reset on Dater. Click on the link below to set up a new password.\n\n"
					  	+ "http://localhost:3000/restore/" + requestId + "\n\n"
						+ "This links is valid only for about 30 minutes. If you have not requested password reset please ignore this message.\n\n"
						+ "Happy dates,\nDater team");
		return emailMessage;
	}
	
}
