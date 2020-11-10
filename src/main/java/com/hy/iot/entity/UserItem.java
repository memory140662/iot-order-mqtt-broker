package com.hy.iot.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.Expose;
import com.hy.iot.enums.UserItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "user_items")
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@AllArgsConstructor
@NoArgsConstructor
public class UserItem {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Expose
    private String id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @JoinColumn(name = "item_id")
    @ManyToOne
    @Expose
    private Item item;

    @Column
    @Expose
    private Integer amount;

    @Column
    @Enumerated(EnumType.STRING)
    @Expose
    private UserItemStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    @Expose
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Expose
    private Date updatedAt;

}
