<template>
	<view>
<!-- 		<picker :value="selectedMachineIndex" @change="onMachineChange" :range="MachineArray">
			<view>选择机台：{{ MachineArray[selectedMachineIndex] }}</view>
		</picker>

		<picker v-model="selectedLineIndex" @change="onLineChange" :range="LineArray">
			<view>选择线别：{{ LineArray[selectedLineIndex] }}</view>
		</picker> -->

		<button @click="scanPurchase">采购单入库</button>
		<button @click="purchaseOrders">采购单明细</button>
		<button @click="submitMporintps">入库单明细</button>
		<!-- <button @click="selectMachineAndLine">加工入库扫码</button> -->
		<!-- <button @click="selectMachineAndLine">成品入库扫码</button> -->
		<button @click="reLogin">重新登录</button>
		<button @click="reConnect">更改连接配置</button>
		<button @click="setCharset">修改编码集</button>
	</view>
</template>

<script>
	export default {
		onShow: function() {
			this.testConnect();
			this.testLogin();
		},
		data() {
			return {
				selectedMachineIndex: '',
				selectedLineIndex: '',
				MachineArray: ['4H', '机台2'],
				LineArray: ['A', '线别2'],
				user: {
					id: 1
				}
			};
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
			onMachineChange(event) {
				this.selectedMachineIndex = event.detail.value;
			},
			onLineChange(event) {
				this.selectedLineIndex = event.detail.value;
			},
			reConnect() {
				uni.navigateTo({
					url: '/pages/index/connect'
				});
			},
			reLogin() {
				uni.navigateTo({
					url: '/pages/index/login'
				});
			},
			scanPurchase() {
				uni.navigateTo({
					url: '/pages/index/scanOrderResult'
				});
			},
			purchaseOrders() {
				uni.navigateTo({
					url: '/pages/index/PurchaseOrdersInfo'
				});
			},
			submitMporintps() {
				uni.navigateTo({
					url: '/pages/index/submitMporintps'
				});
			},
			setCharset() {
				uni.navigateTo({
					url: '/pages/index/setCharset'
				});
			},
			selectMachineAndLine() {
				// console.log(this.selectedMachineIndex);
				// console.log(this.selectedLineIndex);
				// if (this.selectedMachineIndex !== '' && this.selectedLineIndex !== '') {
				// 	// 如果机台和线别都已选择，则执行扫描操作
				// 	this.scanQRCode();
				// } else {
				// 	// 提示用户选择机台和线别
				// 	uni.showToast({
				// 		title: '请先选择机台和线别',
				// 		icon: 'none'
				// 	});
				// }
				this.scanQRCode();
			},
			scanQRCode() {
				//#ifdef APP-PLUS
				// 在uni-app中使用条件编译判断是否在安卓平台
				// 调用uni.scanCode实现扫码功能
				uni.scanCode({
					success: (res) => {
						console.log('扫码结果：' + res.result);
						uni.navigateTo({
						    url: '/pages/index/scanResult?result=' + res.result
						});
					},
					fail: (err) => {
						console.error('扫码失败：' + err);
					}
				});
				return;
				//#endif
				//是pc端
				
			},
			getUser(id) {
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: 'http://127.0.0.1:8099/common/user/getUserById?id=' + id,
					method: 'GET',
					header: {},
					success: (res) => {
						console.log(res.data);
						if (res.data.code == 1) {
							let result = res.data.data;
							console.log(result);
						} else {
							if(res.data.msg === 'sql执行出现异常'){
								//重启当前端口服务
								this.restart();
								//切换备用端口服务
								this.changePort();
								this.getUser(id);
								return;
							}
							console.log(res.data.msg);
						}
					}
				})
			},
			restart(){
				var ip = uni.getStorageSync('ip');
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: ip + '/restart2?port=' + ip.split(":")[2],
					method: 'POST',
					success: (res) => {
						console.log(res.data);
						if (res.data.code == 1) {
						} else {
							
						}
					}
				})
			},
			changePort(){
				var ip = uni.getStorageSync('ip');
				var ip2 = uni.getStorageSync('ip2');
				uni.setStorageSync('ip',ip2);
				uni.setStorageSync('ip2',ip);
			}
		}
	}
</script>