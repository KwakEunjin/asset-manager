#!/bin/bash

# EC2 인스턴스 초기 설정 스크립트

echo "=== EC2 인스턴스 설정 시작 ==="

# 시스템 업데이트
sudo yum update -y

# Java 17 설치
sudo yum install -y java-17-amazon-corretto-devel

# Node.js 18 설치
curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo yum install -y nodejs

# Git 설치
sudo yum install -y git

# 방화벽 설정 (포트 8080, 3000 열기)
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --permanent --add-port=3000/tcp
sudo firewall-cmd --reload

# 프로젝트 디렉토리 생성
mkdir -p /home/ec2-user/apps
cd /home/ec2-user/apps

echo "=== 기본 설정 완료 ==="
echo "다음 단계: git clone으로 프로젝트 다운로드"