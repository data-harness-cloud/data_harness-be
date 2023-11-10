@echo off
echo 欢迎使用一键部署脚本！！！
setlocal enabledelayedexpansion
set "package_path="
:input
set /p "package_path=请输入编译好的包，(相对路径)(如果不存在则会一直提示): "
if not exist "!package_path!" (
    echo 文件不存在，请重新输入。
    goto input
)
echo 当前包的路径为：%package_path%
ssh root@162.14.122.254 "cd /opt/applications/SDS/sds-test && make stop-be"
scp -r "%package_path%" root@162.14.122.254:/opt/applications/SDS/sds-test/backend/
ssh root@162.14.122.254 "cd /opt/applications/SDS/sds-test && make start-be-ps-logs"
echo 部署完毕