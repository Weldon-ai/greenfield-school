
package com.greenfield.sms.service;

import com.greenfield.sms.model.Message;
import com.greenfield.sms.model.User;
import com.greenfield.sms.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // ===== Inbox: All messages received by the user =====
    public List<Message> getInbox(User user) {
        return messageRepository.findByReceiverOrderBySentAtDesc(user);
    }

    /**
     * ADDED: Alias for getInbox to fix TeacherController compilation error
     */
    public List<Message> getMessagesForUser(User user) {
        return getInbox(user);
    }

    // ===== Sent messages: All messages sent by the user =====
    public List<Message> getSent(User user) {
        return messageRepository.findBySenderOrderBySentAtDesc(user);
    }

    /**
     * ADDED: Alias for getSent to match naming conventions in newer controllers
     */
    public List<Message> getSentMessages(User user) {
        return getSent(user);
    }

    // ===== Unread messages =====
    public List<Message> getUnreadMessages(User user) {
        return messageRepository.findByReceiverAndReadStatusFalseOrderBySentAtDesc(user);
    }

    // ===== Send a new message =====
    public Message sendMessage(String subject, String body, User sender, User receiver) {
        Message message = new Message(subject, body, sender, receiver);
        return messageRepository.save(message);
    }

    // ===== Mark a single message as read =====
    public void markAsRead(Message message) {
        if (!message.isReadStatus()) {
            message.setReadStatus(true);
            messageRepository.save(message);
        }
    }

    // ===== Mark multiple messages as read =====
    public void markMessagesAsRead(List<Message> messages) {
        for (Message message : messages) {
            if (!message.isReadStatus()) {
                message.setReadStatus(true);
            }
        }
        messageRepository.saveAll(messages);
    }

    // ===== Delete message by ID =====
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    // ===== Get message by ID =====
    public Message getById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    // ===== Get conversation between two users (both directions) =====
    public List<Message> getConversation(User user1, User user2) {
        return messageRepository.findConversation(user1, user2);
    }

    // ===== Get messages sent from one user to another only =====
    public List<Message> getMessagesBetween(User sender, User receiver) {
        return messageRepository.findBySenderAndReceiverOrderBySentAtDesc(sender, receiver);
    }
}