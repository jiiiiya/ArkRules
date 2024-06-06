package com.iarkdata.arkrules.repository;

import com.iarkdata.arkrules.entity.Rule;
import com.iarkdata.arkrules.entity.RuleChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Integer> {
    List<Rule> findByRuleType(String type);
    List<Rule> findByRuleCode(String code);
}
