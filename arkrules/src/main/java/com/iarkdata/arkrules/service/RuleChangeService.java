package com.iarkdata.arkrules.service;

import com.iarkdata.arkrules.entity.RuleChange;
import com.iarkdata.arkrules.repository.RuleChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleChangeService {
    @Autowired
    private RuleChangeRepository ruleChangeRepository;

    public List<RuleChange> getRuleChangesByRuleId(int ruleId) {
        return ruleChangeRepository.findByRuleId(ruleId);
    }

    public RuleChange saveRuleChange(RuleChange ruleChange) {
        return ruleChangeRepository.save(ruleChange);
    }
}
