#!/bin/bash

echo "=== DynamoDB 전체 설정 시작 ==="

# 1. 테이블 생성
echo "1. 테이블 생성 중..."
chmod +x create-dynamodb-table.sh
./create-dynamodb-table.sh

# 2. 초기 데이터 삽입
echo "2. 초기 데이터 삽입 중..."
chmod +x insert-initial-data.sh
./insert-initial-data.sh
ㅋ
echo "=== DynamoDB 설정 완료 ==="
echo "테이블명: AssetManager"
echo "리전: ap-northeast-2"
echo ""
echo "확인 명령어:"
echo "aws dynamodb list-tables --region ap-northeast-3"
echo "aws dynamodb scan --table-name AssetManager --region ap-northeast-3"