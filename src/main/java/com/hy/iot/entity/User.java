package com.hy.iot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hy.iot.enums.UserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Table(name = "users")
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Expose
    private String id;

    @Column
    @NonNull
    @ApiModelProperty(value = "使用者編碼")
    @Expose
    private String code;

    @Column
    @NonNull
    @ApiModelProperty(value = "使用者名稱")
    @Expose
    private String name;

    @Column
    @NonNull
    @ApiModelProperty(value = "使用者類型(STUDENT: 學生; CLIENT: 購物顧客)")
    @Enumerated(EnumType.STRING)
    @Expose
    private UserType type;

    @Column(name = "is_checked")
    @NonNull
    @ApiModelProperty(value = "使用者是否點名(學生用)")
    @SerializedName("is_checked")
    @Expose
    private Boolean isChecked;

    @Column
    @ApiModelProperty(value = "使用者年齡")
    @Expose
    private Integer age;

    @Column
    @ApiModelProperty(value = "使用者班級(學生用)")
    @SerializedName("class")
    @JsonProperty("class")
    @Expose
    private String clx;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Expose
    private Collection<UserItem> orders;


}
