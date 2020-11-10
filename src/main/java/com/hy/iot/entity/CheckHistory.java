package com.hy.iot.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "check_histories")
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class CheckHistory implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NonNull
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "created_by", referencedColumnName="id")
    private User createdBy;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
}
