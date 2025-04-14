<template>
	<view>
		<label>请输入要修改的编码集 例: GBK  UTF-8</label>
		<input v-model="connectInfo.charset"></input>
		<button @click="setCharset()">修改</button>
	</view>
</template>

<script>
	export default {
		onShow: function() {
			this.connectInfo.charset = uni.getStorageSync('charset');
		},
		data() {
			return {
				connectInfo : {
					ip : '',
					dbfPath : '',
					charset : ''
				}
			}
		},
		methods: {
			testLogin(){
				var loginUser = uni.getStorageSync('loginUser');
				console.log(loginUser);
				if(!loginUser){
					uni.reLaunch({
						url: '/pages/index/login'
					});
				}
				
			},
			connect() {
				console.log(this.connectInfo);
				if(this.connectInfo.ip === '' || this.connectInfo.dbfPath === ''){
					uni.showToast({
						title: '请输入ip地址和数据表地址',
						duration: 2000,
						mask: true //是否有透明蒙层，默认为false
					})
					return
				}
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: 'http://' +  this.connectInfo.ip + '/common/user/testConnect',
					method: 'POST',
					data: this.connectInfo,
					success: (res) => {
						if (res.data.code == 1) {
							let result = res.data.data;
							console.log(result);
							uni.setStorageSync('ip','http://' + this.connectInfo.ip);
							uni.setStorageSync('dbfPath', this.connectInfo.dbfPath);
							//跳转到user页
							uni.showToast({
								title: '连接成功',
								duration: 2000,
								mask: true //是否有透明蒙层，默认为false
							})
							this.testLogin();
							uni.reLaunch({
								url: '/pages/index/index'
							});
						} else {
							if(res.data.msg === 'sql执行出现异常'){
								//重启当前端口服务
								this.restart();
								//切换备用端口服务
								this.changePort();
								this.connect();
								return;
							}
							uni.showModal({
								title: '连接失败',
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
			setCharset(){
				uni.setStorageSync('charset',this.connectInfo.charset);
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
</style>
