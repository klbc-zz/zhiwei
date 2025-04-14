<template>
	<view>
		<view>
			<label>采购单号</label>
			<view class="inline-input-buttons">
				<input style="width: 50%;" type="number" maxlength="8" v-model="order.ordeno" @input="checkOrderNoLength"></input>
				<button style="width: 20%; font-size: 10px;" @click="scanQRCode()">扫码</button>
				<button style="width: 30%; font-size: 10px;" @click="getOrder()">查询</button>
			</view>
			</br>
			<label>送货单号</label>
			<input v-model="order.mkoutno"></input></br>
			<label>物料编号</label>
			<input readonly v-model="order.mpdno" disabled="true"></input></br>
			<view class="item-container">
			  <view class="item">
			    <label>物料名称</label>
			    <textarea readonly disabled="true" class="material-input pink-border" style="height: 80px;width: 90%;" v-model="order.mpdne"></textarea></br>
			  </view>
			
			  <view class="quantity-row">
			    <view class="quantity-item">
			      <label>采购数量</label>
			      <input readonly v-model="order.vol" disabled="true"></input>
			    </view>
			    <view class="quantity-item">
			      <label>未交数量</label>
			      <input readonly v-model="order.nopy" disabled="true"></input>
			    </view>
			  </view>
			</view>
			</br>
			<label>本次交货</label>
			<input type="number" v-model="order.outvol"></input></br>
			<label>备品数量</label>
			<input type="number" v-model="order.flowvol"></input></br>
			
			<button @click="saveOrder()">保存</button>
			<button @click="cancelOrder()">取消</button>
		</view>
	</view>
	
</template>

<script>
	export default {
		onLoad(options) {
		},
		data() {
			return {
				orderBK : {
					dono : '',
					xh : ''	,
					type : '1',
					dbfPath : '',
					pdusername : '',
					okvol : '',
					isLoading: false,
					isSaveLoading: false
				},
				order : {
					ordeno : '',//采购单号
					mkoutno : '',//送货单号
					mpdno : '',//物料编号
					mpdne : '',//物料名称
					mpdne1 : '',//附加说明
					vol : '',//采购数量
					nopy : '',//未交数量
					outvol : '',//本次交货
					dbfPath : '',//dbf
					jmkcuna : '',//用户名
					mkcuno : '',//厂商代号
					flowvol : '',//备品数量
					isRepeated : 0,//备品数量
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
			scanQRCode() {
				//#ifdef APP-PLUS
				// 在uni-app中使用条件编译判断是否在安卓平台
				// 调用uni.scanCode实现扫码功能
				uni.scanCode({
					success: (res) => {
						console.log('扫码结果：' + res.result);
						this.order.ordeno = res.result;
						this.getOrder();
					},
					fail: (err) => {
						console.error('扫码失败：' + err);
					}
				});
				return;
				//#endif
				//是pc端
				
			},
			// 当输入内容变化时触发此方法
			checkOrderNoLength() {
				if (this.order.ordeno.length === 8) {
					this.getOrder();
				}
			},
			getOrder() {
				if(!this.order.ordeno){
					uni.showToast({
						title: '请先填写采购单号或扫码填充',
						duration: 2000,
						mask: true //是否有透明蒙层，默认为false
					})
					return;
				}
				if(this.isLoading){
					uni.showModal({
						title: '请勿重复点击',
						content: '请勿重复点击',
						success: function (res) {
							if (res.confirm) {
								// console.log('用户点击确定');
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					});
					return;
				}
				this.isLoading = true;
				var ip = uni.getStorageSync('ip');
				var dbfPath = uni.getStorageSync('dbfPath');
				var loginUser = uni.getStorageSync('loginUser');
				this.order.dbfPath = dbfPath;
				this.order.jmkcuna = loginUser.cname;
				this.order.mkcuno = loginUser.peson;
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: ip + '/common/scan/scanPurchase',
					method: 'POST',
					data: this.order,
					success: (res) => {
						console.log(res.data);
						if (res.data.code == 1) {
							let result = res.data.data;
							this.order = result;
							this.isLoading = false;
							console.log(this.order);
							// uni.showToast({
							// 	title: '查询成功',
							// 	duration: 2000,
							// 	mask: true //是否有透明蒙层，默认为false
							// })
						} else {
							console.log(res.data.msg);
							this.testConnect();
							this.testLogin();
							this.isLoading = false;
							if(res.data.msg === 'sql执行出现异常'){
								let that = this;
								//重启当前端口服务
								that.restart();
								//切换备用端口服务
								that.changePort();
								that.getOrder();
								// uni.showModal({
								// 	title: '网络异常,是否切换备用路线',
								// 	content: res.data.msg,
								// 	success: function (res) {
								// 		if (res.confirm) {
								// 			that.restart();
								// 			//切换备用端口服务
								// 			that.changePort();
								// 			that.getOrder();
								// 			// console.log('用户点击确定');
								// 		} else if (res.cancel) {
								// 			// console.log('用户点击取消');
								// 		}
								// 	}
								// });
								return;
							}
							uni.showModal({
								title: '查询失败',
								content: res.data.msg,
								success: function (res) {
									if (res.confirm) {
										// console.log('用户点击确定');
									} else if (res.cancel) {
										// console.log('用户点击取消');
									}
								}
							});
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
			},
			saveOrder(){
				var ip = uni.getStorageSync('ip');
				//采购入库
				if(!this.order.ordeno){
					uni.showToast({
						title: '请先填写采购单号或扫码填充',
						duration: 2000,
						mask: true //是否有透明蒙层，默认为false
					})
					return;
				}
				if(this.isSaveLoading){
					uni.showModal({
						title: '请勿重复点击',
						content: '请勿重复点击',
						success: function (res) {
							if (res.confirm) {
								// console.log('用户点击确定');
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					});
					return;
				}
				if(!this.order.mpdne){
					uni.showModal({
						title: '物料名称不能为空',
						content: '物料名称不能为空',
						success: function (res) {
							if (res.confirm) {
								// console.log('用户点击确定');
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					});
					return;
				}
				if(this.order.outvol == '0'){
					uni.showModal({
						title: '交货数量不能为0',
						content: '交货数量不能为0',
						success: function (res) {
							if (res.confirm) {
								// console.log('用户点击确定');
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					});
					return;
				}
				this.isSaveLoading = true;
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: ip + '/common/scan/savePurchase',
					method: 'POST',
					data: this.order,
					success: (res) => {
						console.log(res.data);
						if (res.data.code == 1) {
							this.isSaveLoading = false;
							uni.showModal({
								title: '入库成功',
								content: res.data.msg + '点击确定继续录入',
								success: function (res) {
									if (res.confirm) {
									} else if (res.cancel) {
										uni.reLaunch({
											url: '/pages/index/index'
										});
									}
								},
							});
							//清除数据
							let order = {
								ordeno : '',//采购单号
								mkoutno : '',//送货单号
								mpdno : '',//物料编号
								mpdne : '',//物料名称
								mpdne1 : '',//附加说明
								vol : '',//采购数量
								nopy : '',//未交数量
								outvol : '',//本次交货
								dbfPath : '',//dbf
								jmkcuna : '',//用户名
								mkcuno : '',//厂商代号
								flowvol : '',//备品数量
							};
							this.order = order;
						} else {
							console.log(res.data.msg);
							this.isSaveLoading = false;
							let that = this;
							if(res.data.msg === 'sql执行出现异常'){
								that.restart();
								//切换备用端口服务
								that.changePort();
								that.saveOrder();
								//重启当前端口服务
								// uni.showModal({
								// 	title: '网络异常,是否切换备用路线',
								// 	content: res.data.msg,
								// 	success: function (res) {
								// 		if (res.confirm) {
								// 			that.restart();
								// 			//切换备用端口服务
								// 			that.changePort();
								// 			that.saveOrder();
								// 			// console.log('用户点击确定');
								// 		} else if (res.cancel) {
								// 			// console.log('用户点击取消');
								// 		}
								// 	}
								// });
								return;
							}
							uni.showModal({
								title: '入库失败',
								content: res.data.msg,
								success: function (res1) {
									if (res1.confirm) {
										if(res.data.code == 3){
											that.order.isRepeated = 1;
											that.saveOrder();
										}
										// console.log('用户点击确定');
									} else if (res1.cancel) {
										// console.log('用户点击取消');
									}
								}
							});
							this.testConnect();
							this.testLogin();
						}
					}
				})
			},
			cancelOrder(){
				//取消保存
				uni.reLaunch({
					url: '/pages/index/index'
				});
			}
		}
	}
</script>

<style>
	.pink-border {
	  border: 2px solid #FFC0CB; /* 粉色边框 */
	}
	.loading-mask {
	  position: fixed;
	  top: 0;
	  left: 0;
	  width: 100%;
	  height: 100%;
	  background-color: rgba(0, 0, 0, 0.5);
	  z-index: 1000;
	  display: flex;
	  justify-content: center;
	  align-items: center;
	}
	.loading-content {
	  padding: 20px;
	  background-color: #fff;
	  border-radius: 5px;
	}
	.inline-input-buttons {
	  display: flex;
	  justify-content: space-between; /* 使按钮靠右对齐并保持间距 */
	}

	.inline-input-buttons button:last-child {
	  margin-left: auto; /* 使第二个按钮靠右顶格 */
	}
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
	.item-container {
	  display: flex;
	  flex-direction: column;
	}
	
	.material-input {
	  /* 保证物料名称输入框内的文字自动换行 */
	  word-wrap: break-word;
	  overflow-wrap: break-word;
	}
	
	.quantity-row {
	  display: flex;
	  justify-content: space-between;
	}
	
	.quantity-item {
	  width: 50%; /* 每个数量输入框占据父容器50%的宽度 */
	}
</style>
