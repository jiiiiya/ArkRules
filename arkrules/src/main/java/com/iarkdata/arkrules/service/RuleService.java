package com.iarkdata.arkrules.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iarkdata.arkrules.dto.RuleTest;
import com.iarkdata.arkrules.entity.Rule;
import com.iarkdata.arkrules.entity.RuleChange;
import com.iarkdata.arkrules.exception.InvalidJsonException;
import com.iarkdata.arkrules.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transform.java.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rule not found"));
    }

    public Rule createRule(Rule rule) throws Exception {
        if(!rule.getRuleType().equals("C")) {
            validateJson(rule.getRuleConditions());
        }
        return ruleRepository.save(rule);
    }

    public Rule updateRule(int id, Rule ruleDetails) throws Exception {
        if(!ruleDetails.getRuleType().equals("C")){
            validateJson(ruleDetails.getRuleConditions());
        }
        return ruleRepository.findById(id)
                .map(rule -> {
                    // 수정 전 -> RuleChange 저장
                    changeRules(id, rule);

                    rule.setRuleType(ruleDetails.getRuleType());
                    rule.setRuleCode(ruleDetails.getRuleCode());
                    rule.setRuleConditions(ruleDetails.getRuleConditions());
                    return ruleRepository.save(rule);
                })
                .orElseThrow(() -> new RuntimeException("Rule not found"));
    }

    public void deleteRule(int id) {
        ruleRepository.findById(id).ifPresent(rule -> {
            // 삭제 전 -> RuleChange 저장
            changeRules(id, rule);

            // 삭제
            ruleRepository.deleteById(id);
        });
    }

    public String simulateRule(String testData) throws Exception {
        String result = "";

        // test 로직 구현
        // input data parsing
        String input = parseTest(testData);

        // classify rule 등록
        Map<Integer, String> classifyRules = new HashMap<>();
        List<Rule> rulesC = ruleRepository.findByRuleType("C");
        List<Rule> rulesV = ruleRepository.findByRuleType("V");
        List<Rule> rulesT = ruleRepository.findByRuleType("T");

        rulesC.forEach(rule -> classifyRules.put(rule.getId(), rule.getRuleConditions()));

        JsonClassifier jc = JsonClassifier.apply(classifyRules, false);
        if (jc.isSuccess()) {
            ClassifyResult cr = jc.search(input);
            if(cr.isSuccess()) {
                // classify 일치 룰 찾기
                Rule matchRule = ruleRepository.findById(cr.getIds().get(0)).orElseThrow(() -> new RuntimeException("Rule not found"));
                Rule matchValidate = rulesV.stream().filter(rule -> rule.getRuleCode().equals(matchRule.getRuleCode())).findAny().orElseThrow(() -> new RuntimeException("Rule not found"));
                JsonValidator jv = JsonValidator.apply(matchValidate.getRuleConditions());
                if(jv.isSuccess()) {
                    ValidateResult vr = jv.validate(input);
                    if(vr.isSuccess()) {
                        Rule matchConvert = rulesT.stream().filter(rule -> rule.getRuleCode().equals(matchValidate.getRuleCode())).findAny().orElseThrow(() -> new RuntimeException("Rule not found"));
                        JsonConverter jcv = JsonConverter.apply(matchConvert.getRuleConditions());
                        if(jcv.isSuccess()) {
                            ConvertResult cvr = jcv.convert(input);
                            if(cvr.isSuccess()) {
                                result = cvr.getJson();
                            } else {
                                throw new Exception("Invalid convert.\n" + cvr.getFailReason());
                            }
                        } else {
                            throw new Exception("Invalid convert.\n" + jcv.getFailReason());
                        }
                    } else {
                        throw new Exception("Invalid validation.\n" + vr.getFailReason());
                    }
                } else {
                    throw new Exception("Invalid validation.\n" + jv.getFailReason());
                }
            } else {
                throw new Exception("No matching classification.\n" + cr.getFailReason());
            }
        } else {
            throw new Exception("Invalid Classification.\n" + jc.getFailReason());
        }

        return result;
    }

    private void changeRules(int id, Rule rule) {
        RuleChange ruleChange = new RuleChange();
        ruleChange.setRuleId(id);
        ruleChange.setRuleType(rule.getRuleType());
        ruleChange.setRuleCode(rule.getRuleCode());
        ruleChange.setRuleConditions(rule.getRuleConditions());
        ruleChangeService.saveRuleChange(ruleChange);
    }

    private void validateJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            if (!jsonNode.isObject() && !jsonNode.isArray()) {
                throw new InvalidJsonException("Invalid JSON format: " + json);
            }
        } catch (Exception e) {
            throw new InvalidJsonException("Invalid JSON format: " + json);
        }
    }

    private String parseTest(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RuleTest parseString = objectMapper.readValue(json, RuleTest.class);
            return parseString.getInput();
        } catch (Exception e) {
            throw new InvalidJsonException("Invalid JSON format: " + json);
        }
    }
}
