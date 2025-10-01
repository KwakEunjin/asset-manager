#!/bin/bash

echo "=== DynamoDB 테이블 생성 시작 ==="

# AssetManager 테이블 생성
aws dynamodb create-table \
    --table-name AssetManager \
    --attribute-definitions \
        AttributeName=pk,AttributeType=S \
        AttributeName=sk,AttributeType=S \
        AttributeName=yearMonth,AttributeType=S \
        AttributeName=type,AttributeType=S \
    --key-schema \
        AttributeName=pk,KeyType=HASH \
        AttributeName=sk,KeyType=RANGE \
    --global-secondary-indexes \
        IndexName=yearMonth-type-index,KeySchema=[{AttributeName=yearMonth,KeyType=HASH},{AttributeName=type,KeyType=RANGE}],Projection={ProjectionType=ALL},BillingMode=PAY_PER_REQUEST \
    --billing-mode PAY_PER_REQUEST \
    --region ap-northeast-3

echo "테이블 생성 중... 잠시 기다려주세요."

# 테이블 생성 완료 대기
aws dynamodb wait table-exists --table-name AssetManager --region ap-northeast-3

echo "=== DynamoDB 테이블 생성 완료 ==="