pdmkcu.dbf
mkcuno  用户代号
password 用户密码
登录时用，输入mkcuno后自动显示pdmkcu.jmkcuna,密码正确可进入

mporde.dbf
mkcuno:厂商代号
ordeno:采购单号
mpdno:物料编号
vol:采购数量
nopy:未交数量
dollno:币别
price:单价
unit:单位
其它栏位可忽略

mater.dbf
mpdno:物料编号
mpdne:物料名称
mpdne1:附加说明
其它栏位可忽略

mporinhi
recno:验收单号
其它栏位可忽略

mporintp.dbf  入库档，主要要写入的资料
recno:验收单号,比如24年4月，如果二个档中没有2404的单号，则recno='24040001',如果有，取mporintp.recno与mporinhi.recno中以2404开头的单号，再用最大值+1
ordeno:采购单号(可录入，可扫描),录入后，select mkcuno,mpdno,mater.mpdne,mater.mpdne1,mater.partno,pdmkcu.jmkcuna,mporde.dollno,mporde.price from mporde  left join pdmkcu on mporde.mkcuno=pdmkcu.mkcuno left join mater on mporde.mpdno=mater.mpdno where ordeno=?mordeno
自动核对mporde.mkcuno=用户代号，相同方可继续
mporde.mpdno:物料编号自动显示
mater.mpdne：物料名称自动显示
mater.mpdne1:附加说明自动显示
mporde.vol:采购数量自动显示
mporde.nopy:未交数量自动显示
mporintp.outvol:本次交货（自动显示为未交数量，可人工录入修改)
mporintp.flowvol:备品数量（默认为0，可人工录入)
mporintp.mkoutno:送货单号(人工录入)
保存后
insert into mporintp (recno,ordeno,dollno,price,mkcuno,outvol,inday,mpdno,partno,flowvol,qctype) values (验收单号，采购单号，币别，单价，用户代号，本次交货，备品数量，当天日期，物料编号，仓库代号，备品数量,'Y')

本机通过测试后，再发远程控制服务器部署的方法给你