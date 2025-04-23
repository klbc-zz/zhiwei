#!/bin/bash
# 获取当前应用的进程ID
PID=$(ps -ef | grep "zhiwei-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')

# 2. 杀死进程（先优雅终止，再强制杀死）
if [ -n "$PID" ]; then
  echo "正在停止进程 $PID..."
  kill $PID      # 发送SIGTERM信号（优雅停机）
  sleep 10       # 等待10秒
  if ps -p $PID > /dev/null; then
    kill -9 $PID # 强制杀死（如果未正常退出）
    echo "进程已强制终止"
  fi
fi

# 等待进程终止
sleep 2
# 3. 重新启动应用
echo "正在重启应用..."
# 重新启动应用（需替换为实际启动命令）
nohup java -jar zhiwei-0.0.1-SNAPSHOT.jar > consoleMsg.log 2>&1 &
echo "Application restarted."