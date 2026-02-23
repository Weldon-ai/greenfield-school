package com.greenfield.sms.repository;

import com.greenfield.sms.model.Message;
import com.greenfield.sms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Inbox
    List<Message> findByReceiverOrderBySentAtDesc(User receiver);

    // Sent messages
    List<Message> findBySenderOrderBySentAtDesc(User sender);

    // Unread messages
    List<Message> findByReceiverAndReadStatusFalseOrderBySentAtDesc(User receiver);

    // Messages between sender and receiver only
    List<Message> findBySenderAndReceiverOrderBySentAtDesc(User sender, User receiver);

    // Conversation: both directions
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.sentAt DESC")
    List<Message> findConversation(User user1, User user2);
}
