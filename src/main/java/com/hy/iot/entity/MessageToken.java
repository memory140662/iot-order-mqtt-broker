package com.hy.iot.entity;

import com.hy.iot.enums.MessageTokenType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "message_tokes")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MessageToken {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column
    private String token;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageTokenType type;
}
