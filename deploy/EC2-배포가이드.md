# EC2 배포 가이드

## 1. EC2 인스턴스 생성

### AWS 콘솔에서 설정
1. **EC2 대시보드** → **인스턴스 시작**
2. **AMI 선택**: Amazon Linux 2023 (프리티어)
3. **인스턴스 유형**: t2.micro (프리티어)
4. **키 페어**: 새로 생성 또는 기존 사용
5. **보안 그룹 설정**:
   ```
   SSH (22) - 내 IP
   HTTP (80) - 0.0.0.0/0
   사용자 지정 TCP (8080) - 0.0.0.0/0
   사용자 지정 TCP (3000) - 0.0.0.0/0
   ```
6. **스토리지**: 8GB gp3 (프리티어)

## 2. 인스턴스 접속

```bash
# SSH 접속
ssh -i "your-key.pem" ec2-user@<EC2-PUBLIC-IP>
```

## 3. 초기 설정

```bash
# 설정 스크립트 다운로드 및 실행
curl -O https://raw.githubusercontent.com/your-repo/asset-manager/main/deploy/ec2-setup.sh
chmod +x ec2-setup.sh
./ec2-setup.sh
```

## 4. 프로젝트 배포

```bash
# 프로젝트 클론
cd /home/ec2-user/apps
git clone https://github.com/your-repo/asset-manager.git
cd asset-manager

# 로그 및 PID 디렉토리 생성
mkdir -p logs pids

# 배포 스크립트 실행
chmod +x deploy/deploy.sh
./deploy/deploy.sh
```

## 5. 접속 확인

### 백엔드 API 테스트
```bash
curl http://<EC2-PUBLIC-IP>:8080/api/codes/NAME
```

### 프론트엔드 접속
```
브라우저에서: http://<EC2-PUBLIC-IP>:3000
```

## 6. 서비스 관리

### 로그 확인
```bash
# 백엔드 로그
tail -f logs/backend.log

# 프론트엔드 로그  
tail -f logs/frontend.log
```

### 프로세스 상태 확인
```bash
# 실행 중인 프로세스 확인
ps aux | grep -E "(gradle|serve)"

# PID 파일 확인
cat pids/backend.pid
cat pids/frontend.pid
```

### 서비스 재시작
```bash
# 배포 스크립트 재실행
./deploy/deploy.sh
```

### 수동 종료
```bash
# PID로 종료
kill $(cat pids/backend.pid)
kill $(cat pids/frontend.pid)

# 또는 프로세스명으로 종료
pkill -f "gradle.*bootRun"
pkill -f "node.*serve"
```

## 7. 업데이트 배포

```bash
# 코드 업데이트 후 재배포
cd /home/ec2-user/apps/asset-manager
./deploy/deploy.sh
```

## 8. 모니터링

### 시스템 리소스
```bash
# CPU, 메모리 사용량
top
htop

# 디스크 사용량
df -h

# 네트워크 연결
netstat -tlnp
```

### 애플리케이션 상태
```bash
# 백엔드 헬스체크
curl http://localhost:8080/api/codes/NAME

# 프론트엔드 상태
curl http://localhost:3000
```

## 9. 문제 해결

### 포트 충돌
```bash
# 포트 사용 중인 프로세스 확인
sudo lsof -i :8080
sudo lsof -i :3000

# 프로세스 강제 종료
sudo kill -9 <PID>
```

### 권한 문제
```bash
# 실행 권한 부여
chmod +x deploy/deploy.sh
chmod +x backend/gradlew
```

### 메모리 부족
```bash
# 스왑 파일 생성 (t2.micro용)
sudo dd if=/dev/zero of=/swapfile bs=1M count=1024
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
```

## 10. 보안 설정

### 방화벽 설정
```bash
# 필요한 포트만 열기
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --permanent --add-port=3000/tcp
sudo firewall-cmd --reload
```

### SSL 인증서 (선택사항)
```bash
# Let's Encrypt 설치
sudo yum install -y certbot

# 인증서 발급 (도메인 필요)
sudo certbot certonly --standalone -d your-domain.com
```

## 11. 비용 최적화

### 인스턴스 스케줄링
```bash
# 야간 자동 종료 (crontab)
crontab -e

# 매일 밤 11시 종료, 아침 8시 시작
0 23 * * * sudo shutdown -h now
0 8 * * * # AWS CLI로 인스턴스 시작
```

### 모니터링 알람
- CloudWatch에서 CPU 사용률 모니터링
- 비용 알람 설정 ($1 초과 시 알림)

## 12. 백업

### 데이터 백업
```bash
# 데이터 파일 백업
cp backend/src/main/resources/data/asset-data.json ~/backup/

# S3에 백업 (선택사항)
aws s3 cp ~/backup/asset-data.json s3://your-bucket/backup/
```

### 스냅샷 생성
- EC2 콘솔에서 EBS 스냅샷 생성
- 주기적 백업 스케줄 설정