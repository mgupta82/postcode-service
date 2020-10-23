package com.auspost.postcode.repository.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "postcode")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Postcode {
    @Id
    private Integer code;
    private String suburb;
    private String state;
}
