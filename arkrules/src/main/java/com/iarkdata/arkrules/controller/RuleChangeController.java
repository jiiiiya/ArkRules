package com.iarkdata.arkrules.controller;

import com.iarkdata.arkrules.entity.RuleChange;
import com.iarkdata.arkrules.service.RuleChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/changes")
public class RuleChangeController {

    @Autowired
    private RuleChangeService ruleChangeService;

    @GetMapping("/{ruleId}")
    public List<RuleChange> getRuleChangesByRuleId(@PathVariable int ruleId) {
        return ruleChangeService.getRuleChangesByRuleId(ruleId);
    }
}
