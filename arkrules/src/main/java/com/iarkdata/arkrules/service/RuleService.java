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
        return ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rule not found."));
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
                .orElseThrow(() -> new RuntimeException("Rule not found."));
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

        // input data parsing
        RuleTest input = parseTest(testData);
        // classify rule 등록
        Map<Integer, String> classifyRules = new HashMap<>();
        List<Rule> rulesC = getRuleByType("C");
        List<Rule> rulesV = getRuleByType("V");
        List<Rule> rulesT = getRuleByType("T");
        // target 지정
        if(!input.getCode().isEmpty()) {
            rulesC = rulesC.stream().filter(rule -> rule.getRuleCode().equals(input.getCode())).toList();
        }
        
        rulesC.forEach(rule -> classifyRules.put(rule.getId(), rule.getRuleConditions()));
        ClassifyResult cr = classify(classifyRules, input.getInput());
        if (!cr.getIds().isEmpty()) {
            for(Integer key : cr.getIds()) {
                try {
                    // 찾은 classify code 와 일치하는 code를 가진 validate 룰 찾기
                    String matchRule = getRuleById(key).getRuleCode();
                    Rule matchValidate = rulesV.stream().filter(rule -> rule.getRuleCode().equals(matchRule)).findAny().orElseThrow(() -> new RuntimeException("Validate-Rule not found."));
                    ValidateResult vr = validate(matchValidate, input.getInput());
                    if (vr.isSuccess()) {
                        // 찾은 validate code 와 일치하는 code를 가진 convert 룰 찾기
                        Rule matchConvert = rulesT.stream().filter(rule -> rule.getRuleCode().equals(matchValidate.getRuleCode())).findAny().orElseThrow(() -> new RuntimeException("Convert-Rule not found."));
                        ConvertResult cvr = convert(matchConvert, input.getInput());
                        if (cvr.isSuccess()) {
                            result = cvr.getJson();
                            break;
                        } else {
                            throw new Exception("Invalid convert.\n" + cvr.getFailReason());
                        }
                    } else {
                        throw new Exception("Invalid convert.\n" + vr.getFailReason());
                    }
                } catch(Exception ex) {
                    result = ex.getMessage();
                }
            }
        } else {
            throw new Exception("No matching classification.\n" + cr.getFailReason());
        }

        return result;
    }

    public String simulateRuleById(int id, String testData) throws Exception {
        String result;

        // input data parsing
        RuleTest input = parseTest(testData);

        // rule check
        Rule rule = ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rule not found"));
        Map<Integer, String> classifyRules = new HashMap<>();
        classifyRules.put(rule.getId(), rule.getRuleConditions());

        if(rule.getRuleType().equals("C")) {
            ClassifyResult cr = classify(classifyRules, input.getInput());
            if(!cr.getIds().isEmpty()) {
                result = cr.show();
            } else {
                throw new Exception("No matching classification.\n" + cr.show());
            }
        } else if(rule.getRuleType().equals("V")) {
            ValidateResult vr = validate(rule, input.getInput());
            if(vr.isSuccess()) {
                result = vr.getJson();
            } else  {
                throw new Exception("Invalid validate.\n" + vr.getFailReason());
            }
        } else {
            ConvertResult cr = convert(rule, input.getInput());
            if(cr.isSuccess()) {
                result = cr.getJson();
            } else {
                throw new Exception("Invalid convert.\n" + cr.getFailReason());
            }
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
            throw new Exception("Invalid JSON format: " + json);
        }
    }

    private RuleTest parseTest(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, RuleTest.class);
        } catch (Exception e) {
            throw new Exception("Invalid JSON format: " + json);
        }
    }

    private ClassifyResult classify(Map<Integer, String> classifyRules, String inputData) throws Exception {
        ClassifyResult result;
        JsonClassifier jc = JsonClassifier.apply(classifyRules, true);
        if (jc.isSuccess()) {
            result = jc.search(inputData);
            if(result.isSuccess()) {
                return result;
            } else {
                throw new Exception("No matching classification.\n" + result.getFailReason());
            }
        } else {
            throw new Exception("Invalid Classification.\n" + jc.getFailReason());
        }
    }

    private ValidateResult validate(Rule rule, String inputData) throws Exception {
        ValidateResult result;
        JsonValidator jv = JsonValidator.apply(rule.getRuleConditions());
        if (jv.isSuccess()) {
            result = jv.validate(inputData);
            return result;
        } else {
            throw new Exception("Invalid validation.\n" + jv.getFailReason());
        }
    }

    private ConvertResult convert(Rule rule, String inputData) throws Exception {
        ConvertResult result;
        JsonConverter jc = JsonConverter.apply(rule.getRuleConditions());
        if (jc.isSuccess()) {
            result = jc.convert(inputData);
            return result;
        } else {
            throw new Exception("Invalid convert.\n" + jc.getFailReason());
        }
    }

    private List<Rule> getRuleByType(String type) {
        return ruleRepository.findByRuleType(type);
    }
}
