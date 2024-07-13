package com.twitter.chat.repository;

import com.twitter.chat.model.ChatMessage;
import com.twitter.chat.repository.projection.ChatMessageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT chatMessage FROM ChatMessage chatMessage " +
            "WHERE chatMessage.chat.id = :chatId")
    List<ChatMessageProjection> findChatMessagesByChatId(@Param("chatId") Long chatId);

    // Update if the other chat participants have read the msg
    // except the owner of that msg itself
    @Modifying
    @Query( "UPDATE ChatMessage chatMessage " +
            "SET chatMessage.unread = false " +
            "WHERE chatMessage.chat.id = :chatId " +
            "AND chatMessage.authorId != :userId")
    void readChatMessages(@Param("chatId") Long chatId, @Param("userId") Long userId);

    // Get the amount of unread messages from all the chat box
    // that the auth user still joined.
    @Query("SELECT COUNT(chatMessage) FROM ChatMessage chatMessage " +
            "WHERE chatMessage.chat.id IN ( " +
            "   SELECT chat.id FROM Chat chat " +
            "   LEFT JOIN chat.participants chatParticipant " +
            "   WHERE chatParticipant.userId = :userId " +
            "   AND chatParticipant.leftChat = false " +
            ") " +
            "AND chatMessage.unread = true " +
            "AND chatMessage.authorId != :userId")
    Long getUnreadMessagesCount(@Param("userId") Long userId);

    @Query("SELECT chatMessage FROM ChatMessage chatMessage WHERE chatMessage.id = :messageId")
    Optional<ChatMessageProjection> getChatMessageById(@Param("messageId") Long messageId);
}
