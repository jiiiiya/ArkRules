package com.iarkdata.arkrules.repository;

import com.iarkdata.arkrules.entity.RuleChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleChangeRepository extends JpaRepository<RuleChange, Integer> {
    List<RuleChange> findByRuleId(int rule_id);
}
