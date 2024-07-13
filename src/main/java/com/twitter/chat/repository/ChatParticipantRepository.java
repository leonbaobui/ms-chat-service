package com.twitter.chat.repository;

import com.twitter.chat.model.ChatParticipant;
import com.twitter.chat.repository.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    @Query("SELECT chatParticipant.userId FROM ChatParticipant chatParticipant " +
            "WHERE chatParticipant.chat.id = :chatId AND chatParticipant.id = :participantId")
    Optional<Long> getParticipantUserId(@Param("participantId") Long participantId, @Param("chatId") Long chatId);

    @Query("SELECT chatParticipant.userId FROM ChatParticipant chatParticipant WHERE chatParticipant.chat.id = :chatId")
    List<Long> getChatParticipantIds(@Param("chatId") Long chatId);

    // TODO: Currently this still not support for group chat. Need enhance in future plan
    @Query("SELECT chatParticipant.userId FROM ChatParticipant chatParticipant " +
            "WHERE chatParticipant.chat.id = :chatId " +
            "GROUP BY chatParticipant.userId " +
            "HAVING chatParticipant.userId != :authUserId ")
    Long getChatParticipantId(@Param("authUserId") Long authUserId, @Param("chatId") Long chatId);
}
