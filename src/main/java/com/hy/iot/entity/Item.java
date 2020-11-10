package com.hy.iot.entity;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "items")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@NoArgsConstructor
@RequiredArgsConstructor
public class Item implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Expose
    private String id;

    @Column
    @NonNull
    @Expose
    private String name;

    @Column
    @NonNull
    @Expose
    private String description;

    @Column
    @NonNull
    @Expose
    private Integer amount;

    @Column
    @Expose
    private String type;

    @Column
    @Expose
    private Double price;

}
