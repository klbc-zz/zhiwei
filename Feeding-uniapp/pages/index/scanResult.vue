<template>
	<view>
		<view>
			<label>制造序号</label>
			<input v-model="order.dono"></input></br>
			<label>客户代号</label>
			<input v-model="order.cuno"></input></br>
			<label>客户品号</label>
			<input v-model="order.cupdno"></input></br>
			<label>订单数量</label>
			<input v-model="order.vol"></input></br>
			<label>未交数量</label>
			<input v-model="order.nopy"></input></br>
			<label>尾箱每箱数量</label>
			<input v-model="order.okvol"></input></br>
		</view>
	</view>
</template>

<script>
	export default {
		  onLoad(options) {
			  this.testConnect();
			  this.testLogin();
			console.log('扫码结果:', options.result);
			var parts = options.result.split(" "); // 使用空格作为分隔符分割字符串
			var partBeforeSpace = parts[0]; // 获取空格前的部分
			var partAfterSpace = parts[1]; // 获取空格后的一部分
			var partAfterSpace2 = parts[2]; // 获取空格后的一部分
			this.orderBK.dono = partBeforeSpace;
			this.orderBK.xh = partAfterSpace;
			this.orderBK.okvol = partAfterSpace2;
			this.getOrder();
		  },
		data() {
			return {
				orderBK : {
					dono : '',
					xh : ''	,
					type : '1',
					dbfPath : '',
					pdusername : '',
					okvol : ''
				},
				order : {
					dono : '',
					cuno : '',
					cupdno : '',
					vol : '',
					nopy : '',
					okvol : ''
				}
			}
		},
		methods: {
			testConnect(){
				var ip = uni.getStorageSync('ip');
				var dbfPath = uni.getStorageSync('dbfPath');
				console.log(ip + dbfPath);
				if(!ip || !dbfPath){
					uni.reLaunch({
						url: '/pages/index/connect'
					});
				}
				
			},
			testLogin(){
				var loginUser = uni.getStorageSync('loginUser');
				console.log(loginUser);
				if(!loginUser){
					uni.reLaunch({
						url: '/pages/index/login'
					});
				}
				
			},
			getOrder() {
				var ip = uni.getStorageSync('ip');
				var dbfPath = uni.getStorageSync('dbfPath');
				var loginUser = uni.getStorageSync('loginUser');
				this.orderBK.dbfPath = dbfPath;
				this.orderBK.pdusername = loginUser.cname;
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: ip + '/common/scan/scanOrder',
					method: 'POST',
					data: this.orderBK,
					success: (res) => {
						console.log(res.data);
						if (res.data.code == 1) {
							let result = res.data.data;
							this.order = result;
							console.log(this.order);
							uni.showToast({
								title: '扫码成功',
								duration: 2000,
								mask: true //是否有透明蒙层，默认为false
							})
						} else {
							console.log(res.data.msg);
							this.testConnect();
							this.testLogin();
							uni.showToast({
								title: '扫码失败,' + res.data.msg,
								duration: 2000,
								mask: true //是否有透明蒙层，默认为false
							})
						}
					}
				})
			}
		}
	}
</script>

<style>

</style>
