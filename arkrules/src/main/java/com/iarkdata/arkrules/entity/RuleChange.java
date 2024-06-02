package com.iarkdata.arkrules.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * CREATE TABLE tb_rule_changes (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * rule_id INT,
 * name VARCHAR(255) NOT NULL,
 * description TEXT,
 * condition JSON NOT NULL,
 * registered TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 * modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 * FOREIGN KEY (rule_id) REFERENCES rules(id) ON DELETE CASCADE )
 */
@Entity
@Getter
@Setter
@Table(name = "tb_rule_changes")
public class RuleChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, name = "rule_id")
    private Integer ruleId;
    private String name;
    private String description;
    @Column(columnDefinition = "json")
    private String conditions;
    private LocalDateTime registered;
    private LocalDateTime modified;
}
