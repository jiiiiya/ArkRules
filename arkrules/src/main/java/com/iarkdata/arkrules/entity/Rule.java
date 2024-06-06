package com.iarkdata.arkrules.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * CREATE TABLE tb_rules (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * rule_type varchar(10) NOT NULL,
 * rule_code TEXT NOT NULL,
 * rule_conditions TEXT NOT NULL,
 * register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 * modify_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP );
 */
@Entity
@Getter
@Setter
@Table(name = "tb_rules")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "rule_type")
    private String ruleType;
    @Column(name = "rule_code")
    private String ruleCode;
    @Column(name = "rule_conditions")
    private String ruleConditions;
    @Column(name = "register_date")
    private LocalDateTime registerDate;
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}
