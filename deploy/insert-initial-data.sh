#!/bin/bash

echo "=== 초기 데이터 삽입 시작 ==="

# NAME 코드 삽입
aws dynamodb put-item \
    --table-name AssetManager \
    --item '{
        "pk": {"S": "CODE#NAME"},
        "sk": {"S": "METADATA"},
        "codeType": {"S": "NAME"},
        "codeValue": {"S": "배우자1,배우자2"}
    }' \
    --region ap-northeast-3

echo "NAME 코드 삽입 완료"

# INCOME_TYPE 코드 삽입
aws dynamodb put-item \
    --table-name AssetManager \
    --item '{
        "pk": {"S": "CODE#INCOME_TYPE"},
        "sk": {"S": "METADATA"},
        "codeType": {"S": "INCOME_TYPE"},
        "codeValue": {"S": "월급,상여,기타수입"}
    }' \
    --region ap-northeast-3

echo "INCOME_TYPE 코드 삽입 완료"

# FIXED_EXPENSE_TYPE 코드 삽입
aws dynamodb put-item \
    --table-name AssetManager \
    --item '{
        "pk": {"S": "CODE#FIXED_EXPENSE_TYPE"},
        "sk": {"S": "METADATA"},
        "codeType": {"S": "FIXED_EXPENSE_TYPE"},
        "codeValue": {"S": "대출,주거,통신,곗돈,구독"}
    }' \
    --region ap-northeast-3

echo "FIXED_EXPENSE_TYPE 코드 삽입 완료"

# LIVING_EXPENSE_CATEGORY 코드 삽입
aws dynamodb put-item \
    --table-name AssetManager \
    --item '{
        "pk": {"S": "CODE#LIVING_EXPENSE_CATEGORY"},
        "sk": {"S": "METADATA"},
        "codeType": {"S": "LIVING_EXPENSE_CATEGORY"},
        "codeValue": {"S": "식비,교통비,의료비,문화생활,기타"}
    }' \
    --region ap-northeast-3

echo "LIVING_EXPENSE_CATEGORY 코드 삽입 완료"

# ACCOUNT_TYPE 코드 삽입
aws dynamodb put-item \
    --table-name AssetManager \
    --item '{
        "pk": {"S": "CODE#ACCOUNT_TYPE"},
        "sk": {"S": "METADATA"},
        "codeType": {"S": "ACCOUNT_TYPE"},
        "codeValue": {"S": "적금1,적금2,적금3,청약,연금저축,주식,코인"}
    }' \
    --region ap-northeast-3

echo "ACCOUNT_TYPE 코드 삽입 완료"

# SHORT_SAVINGS_TYPE 코드 삽입
aws dynamodb put-item \
    --table-name AssetManager \
    --item '{
        "pk": {"S": "CODE#SHORT_SAVINGS_TYPE"},
        "sk": {"S": "METADATA"},
        "codeType": {"S": "SHORT_SAVINGS_TYPE"},
        "codeValue": {"S": "운동,여행,경조비"}
    }' \
    --region ap-northeast-3

echo "SHORT_SAVINGS_TYPE 코드 삽입 완료"

echo "=== 초기 데이터 삽입 완료 ===