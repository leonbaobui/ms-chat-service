package com.twitter.chat.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "chats_participants",
        indexes = {
            @Index(name = "chats_participants_user_id_idx", columnList = "user_id"),
            @Index(name = "chats_participants_chat_id_idx", columnList = "chat_id")
        }
)
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_participants_seq")
    @SequenceGenerator(name = "chats_participants_seq", sequenceName = "chats_participants_seq", initialValue = 10, allocationSize = 1)
    private Long id;

    @Column(name = "left_chat")
    private boolean leftChat = false;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // This is by default every *ToOne type is always EAGER,
    // we may not want this always EAGER cause sometime will slow down our app (we dont need full response)
    // and may be the cause of N + 1 problem.

    // TODO: Also because of the design, currently this in the ManyToOne relationship
    // means that one chat will have many participants,
    // and for every new chat created with the same userId, it will re-create new chat participant here
    // with another id (primary key) but have the same userId
    // TODO: consider to refactor to ManyToMany relationship if need (for 1NF normalization).
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
}
