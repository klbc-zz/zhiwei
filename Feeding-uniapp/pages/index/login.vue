<template>
	<view  class="container">
<!-- 		<input v-model="loginUser.peson" placeholder="请输入用户代号">
		</input>
		
		<input v-model="loginUser.password1" placeholder="请输入密码">
		</input> -->
		
		<label>请输入用户代号</label>
		<input v-model="loginUser.peson"></input>
		<label>请输入密码</label>
		<input v-model="loginUser.password1"></input></br>
		
		<button @click="login()">登录</button>
		<!-- <button @click="cancel()">取消</button> -->
	</view>
</template>

<script>
	export default {
		onShow: function() {
			this.testConnect();
		},
		data() {
			return {
				loginUser : {
					peson : '',
					cname : '',
					password1 : '',
					dbfPath : ''
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
			login(){
				if(this.loginUser.peson === ''){
					uni.showToast({
						title: '请输入用户代号和密码',
						duration: 2000,
						mask: true //是否有透明蒙层，默认为false
					})
					return
				}
				var ip = uni.getStorageSync('ip');
				this.loginUser.dbfPath = uni.getStorageSync('dbfPath');
				console.log(this.loginUser);
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: ip + '/common/user/login',
					method: 'POST',
					data: this.loginUser,
					success: (res) => {
						if (res.data.code == 1) {
							let result = res.data.data;
							console.log(result);
							uni.setStorageSync('loginUser', result);
							//跳转到首页
							uni.showToast({
								title: '登录成功，用户名: ' + res.data.data.cname,
								duration: 2000,
								mask: true //是否有透明蒙层，默认为false
							})
							uni.reLaunch({
								url: '/pages/index/index'
							});
						} else {
							if(res.data.msg === 'sql执行出现异常'){
								//重启当前端口服务
								this.restart();
								//切换备用端口服务
								this.changePort();
								this.login();
								return;
							}
							uni.showModal({
								title: '登录失败，请检查输入',
								content: res.data.msg,
								success: function (res) {
									if (res.confirm) {
										// console.log('用户点击确定');
									} else if (res.cancel) {
										// console.log('用户点击取消');
									}
								}
							});
							console.log(res.data.msg);
						}
					}
				})
			},
			cancel(){
				uni.reLaunch({
					url: '/pages/index/index'
				});
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

<style>
	input{
		border: 1px solid #FFC0CB;
		border-radius: 4px; /* 可以根据喜好调整圆角大小 */
		border-width: 2px; /* 可以根据需要调整边框宽度 */
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 调整阴影参数以获得理想效果 */
		padding: 6px 12px; /* 可根据需要调整内边距大小 */
		background-color: #f8f9fa; /* 示例：浅灰色背景 */
		width: 80%;
		word-wrap: break-word;
		overflow-wrap: break-word;
	}
	.container {
		padding-top: 150px;
	}
</style>
