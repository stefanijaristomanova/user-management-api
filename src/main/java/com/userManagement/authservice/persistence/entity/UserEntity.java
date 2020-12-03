package com.userManagement.authservice.persistence.entity;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USERS")
@ApiModel(description = "All details about the User")
public class UserEntity implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private long id;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @Column(name = "DATE")
    private String date;

    @NotNull
    @Column(name = "EMAIL",nullable = false)
    private String email;

    @NotNull
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "DELETED")
    private Integer deleted;

    @Column(name = "DATE_ENTERED")
    private LocalDateTime dateCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

}
