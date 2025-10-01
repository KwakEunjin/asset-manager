#!/bin/bash

# 간단한 시작 스크립트

echo "=== Asset Manager 시작 ==="

# 프로젝트 디렉토리로 이동
cd /home/ec2-user/apps/asset-manager

# 로그 및 PID 디렉토리 생성
mkdir -p logs pids

# 백엔드 시작
echo "백엔드 시작 중..."
cd backend
nohup ./gradlew bootRun > ../logs/backend.log 2>&1 &
echo $! > ../pids/backend.pid
echo "백엔드 PID: $(cat ../pids/backend.pid)"

# 백엔드 시작 대기
sleep 15

# 프론트엔드 시작
echo "프론트엔드 시작 중..."
cd ../frontend
nohup npx serve -s dist -l 3000 > ../logs/frontend.log 2>&1 &
echo $! > ../pids/frontend.pid
echo "프론트엔드 PID: $(cat ../pids/frontend.pid)"

echo "=== 시작 완료 ==="
echo "접속 URL: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):3000"