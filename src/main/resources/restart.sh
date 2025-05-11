#!/bin/bash

# 定义日志文件路径
LOG_FILE="restart.log"

# 记录带时间戳的日志函数
log() {
  echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" >> "$LOG_FILE"
}

# 创建或清空日志文件（若需追加可改为 >>）
: > "$LOG_FILE"

# 1. 查找进程ID（优化命令，避免误杀）
PID=$(pgrep -f "zhiwei-0.0.1-SNAPSHOT.jar")

# 2. 终止进程流程
if [ -n "$PID" ]; then
  log "正在停止进程 $PID..."
  kill "$PID"

  # 等待优雅退出（最多15秒）
  TIMEOUT=15
  while [ $TIMEOUT -gt 0 ]; do
    if ! ps -p "$PID" > /dev/null; then
      log "进程 $PID 已正常退出"
      break
    fi
    sleep 1
    ((TIMEOUT--))
  done

  # 强制终止检测
  if ps -p "$PID" > /dev/null; then
    log "进程 $PID 未正常退出，强制终止..."
    kill -9 "$PID"
    sleep 2
  fi
fi

# 3. 重启应用（保留原有日志分离）
log "正在启动应用..."
nohup java -jar zhiwei-0.0.1-SNAPSHOT.jar > consoleMsg.log 2>&1 &

# 验证启动是否成功
sleep 3
NEW_PID=$(pgrep -f "zhiwei-0.0.1-SNAPSHOT.jar")
if [ -n "$NEW_PID" ]; then
  log "应用启动成功，新进程ID: $NEW_PID"
else
  log "应用启动失败！"
  exit 1
fi