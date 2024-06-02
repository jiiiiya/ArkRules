package com.iarkdata.arkrules.repository;

import com.iarkdata.arkrules.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Integer> {
}
