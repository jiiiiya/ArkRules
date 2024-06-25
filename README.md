# ArkRule Management

### How-to
#### 1. create table

```
CREATE TABLE tb_rules (
  id INT AUTO_INCREMENT PRIMARY KEY,
  rule_type varchar(10) NOT NULL,
  rule_code TEXT NOT NULL,
  rule_conditions TEXT NOT NULL,
  register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modify_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP );


CREATE TABLE tb_rule_changes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  rule_id INT NOT NULL,
  rule_type varchar(10) NOT NULL,
  rule_code TEXT NOT NULL,
  rule_conditions TEXT NOT NULL,
  register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modify_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (rule_id) REFERENCES tb_rules(id) ON DELETE CASCADE );
```

-----

#### 2. sample data
* classify
```
'prescription' & 'drugs'
```
* validate
```json
[
  {
    "prescription": {
      "id": "_length(36) && _isString",
      "insureKindCd": "_any",
      "insureKindNm": "_any",
      "hospitalCd": "_digit && _length(8)",
      "prescriptionDt": "_digit && _length(8)",
      "prescriptionNo": "_any",
      "patientNm": "_any",
      "identificationNo": "_any",
      "hospitalNm": "_any",
      "hospitalTel": "_digitOr(-)",
      "hospitalFax": "_digitOr(-)",
      "doctorNm": "_any",
      "doctorLicenseClsNm": "_any",
      "doctorLicenseNo": "_any",
      "primaryDiagnosisCd": "_any",
      "secondaryDiagnosisCd": "_any"
    },
    "drugs": [
      {
        "id": "_length(36) && _isString",
        "seq": "gt(0)",
        "drugNm": "_any",
        "kdCode": "_any",
        "doseQtyPer1Tim": "_any",
        "doseQtyPer1Day": "gt(0)",
        "doseDay": "gt(0)",
        "$$$$": "${doseQtyPer1Day} <= ${doseDay}"
      }
    ]
  }
]
```
* convert
```json
{
  "[$$]": "$[?(.prescription && .drugs)]",
  "[$>]": {
    "patient": {
      "{::}": "$.prescription",
      "{=>}": {
        "id": "@.id",
        "name": "@.patientNm"
      }
    },
    "prescription": {
      "{::}": "$.prescription",
      "{=>}": {
        "id": "@.id",
        "insureKindCd": "@.insureKindCd",
        "insureKindNm": "@.insureKindNm",
        "hospitalCd": "@.hospitalCd",
        "prescriptionDt": "@.prescriptionDt",
        "prescriptionNo": "@.prescriptionNo",
        "patientNm": "@.patientNm",
        "identificationNo": "@.identificationNo",
        "hospitalNm": "@.hospitalNm",
        "hospitalTel": "@.hospitalTel",
        "hospitalFax": "@.hospitalFax",
        "doctorNm": "@.doctorNm",
        "doctorLicenseClsNm": "@.doctorLicenseClsNm",
        "doctorLicenseNo": "@.doctorLicenseNo",
        "primaryDiagnosisCd": "@.primaryDiagnosisCd",
        "secondaryDiagnosisCd": "@.secondaryDiagnosisCd"
      }
    },
    "drugs": {
      "[::]": "$.drugs[0:]",
      "[=>]": {
        "id": "@.id",
        "seq": "@.seq",
        "drugNm": "@.drugNm",
        "kdCode": "@.kdCode",
        "doseQtyPer1Tim": "@.doseQtyPer1Tim",
        "doseQtyPer1Day": "@.doseQtyPer1Day",
        "doseDay": "@.doseDay"
      }
    }
  }
}
```
