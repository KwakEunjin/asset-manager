#!/bin/bash

# 애플리케이션 배포 스크립트

echo "=== 애플리케이션 배포 시작 ==="

# 프로젝트 디렉토리로 이동
cd /home/ec2-user/apps/asset-manager

# 최신 코드 가져오기
git pull origin main

# 기존 프로세스 종료
echo "기존 프로세스 종료 중..."
pkill -f "gradle.*bootRun" || true
pkill -f "node.*serve" || true

# 백엔드 빌드 및 실행
echo "백엔드 빌드 및 실행 중..."
cd backend
chmod +x gradlew
nohup ./gradlew bootRun > ../logs/backend.log 2>&1 &
BACKEND_PID=$!
echo "백엔드 PID: $BACKEND_PID"

# 잠시 대기 (백엔드 시작 시간)
sleep 10

# 프론트엔드 빌드 및 실행
echo "프론트엔드 빌드 및 실행 중..."
cd ../frontend
npm install
npm run build
nohup npx serve -s dist -l 3000 > ../logs/frontend.log 2>&1 &
FRONTEND_PID=$!
echo "프론트엔드 PID: $FRONTEND_PID"

# PID 저장
echo $BACKEND_PID > ../pids/backend.pid
echo $FRONTEND_PID > ../pids/frontend.pid

echo "=== 배포 완료 ==="
echo "백엔드: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
echo "프론트엔드: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):3000"