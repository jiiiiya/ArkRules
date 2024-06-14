package com.iarkdata.arkrules.controller;

import com.iarkdata.arkrules.entity.Rule;
import com.iarkdata.arkrules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @GetMapping
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/{id}")
    public Rule getRuleById(@PathVariable int id) {
        return ruleService.getRuleById(id);
    }

    @PostMapping
    public Rule createRule(@RequestBody Rule rule) throws Exception {
        return ruleService.createRule(rule);
    }

    @PutMapping("/{id}")
    public Rule updateRule(@PathVariable int id, @RequestBody Rule ruleDetails) throws Exception {
        return ruleService.updateRule(id, ruleDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable int id) {
        ruleService.deleteRule(id);
    }

    @PostMapping("/test")
    public String simulateRule(@RequestBody String testData) throws Exception {
        return ruleService.simulateRule(testData);
    }

    @PostMapping("/test/{id}")
    public String simulateRule(@PathVariable int id, @RequestBody String testData) throws Exception {
        return ruleService.simulateRuleById(id, testData);
    }
}
