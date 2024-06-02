package com.iarkdata.arkrules.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iarkdata.arkrules.entity.Rule;
import com.iarkdata.arkrules.entity.RuleChange;
import com.iarkdata.arkrules.exception.InvalidJsonException;
import com.iarkdata.arkrules.service.RuleChangeService;
import com.iarkdata.arkrules.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private RuleChangeService ruleChangeService;

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public Rule getRuleById(int id) {
        return ruleRepository.findById(id).orElse(null);
    }

    public Rule createRule(Rule rule) throws Exception {
        validateJson(rule.getConditions());
        return ruleRepository.save(rule);
    }

    public Rule updateRule(int id, Rule ruleDetails) throws Exception {
        validateJson(ruleDetails.getConditions());
        return ruleRepository.findById(id)
                .map(rule -> {

                    // 수정 전 -> RuleChange 저장
                    RuleChange ruleChange = new RuleChange();
                    ruleChange.setRuleId(id);
                    ruleChange.setName(rule.getName());
                    ruleChange.setDescription(rule.getDescription());
                    ruleChange.setConditions(rule.getConditions());
                    ruleChangeService.saveRuleChange(ruleChange);

                    rule.setName(ruleDetails.getName());
                    rule.setDescription(ruleDetails.getDescription());
                    rule.setConditions(ruleDetails.getConditions());
                    return ruleRepository.save(rule);
                })
                .orElseThrow(() -> new RuntimeException("Rule not found"));
    }

    public void deleteRule(int id) {
        ruleRepository.findById(id).ifPresent(rule -> {

            // 삭제 전 -> RuleChange 저장
            RuleChange ruleChange = new RuleChange();
            ruleChange.setRuleId(id);
            ruleChange.setName(rule.getName());
            ruleChange.setDescription(rule.getDescription());
            ruleChange.setConditions(rule.getConditions());
            ruleChangeService.saveRuleChange(ruleChange);

            // 삭제
            ruleRepository.deleteById(id);
        });
    }

    private void validateJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readTree(json);
        } catch (Exception e) {
            throw new InvalidJsonException("Invalid JSON format: " + json);
        }
    }
}

