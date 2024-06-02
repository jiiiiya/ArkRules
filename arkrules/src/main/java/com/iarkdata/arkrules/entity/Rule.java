package com.iarkdata.arkrules.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * CREATE TABLE tb_rules (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * name VARCHAR(255) NOT NULL,
 * description TEXT,
 * condition JSON NOT NULL,
 * registered TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 * modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP )
 */
@Entity
@Getter
@Setter
@Table(name = "tb_rules")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @Column(columnDefinition = "json")
    private String conditions;
    private LocalDateTime registered;
    private LocalDateTime modified;
}
