<template>
	<view>
		<view class="flex-view">
			<text>验收单号</text>
			<input type="number" style="border: 2px solid #FFC0CB;width: 80px;" v-model="mpordeVO.recno"/>
			<button @click="getTableInfo">查询</button>
			<button @click="selectMore">高级查询</button>
		</view>
		
		<view>
			<!-- 输入框示例 -->
			<uni-popup ref="inputDialog" type="dialog" style="height: 300px;">
				<view style="background-color: white;height: 300px;width: 300px; padding: 5px;">
					<view style="text-align: center;"><text >高级查询</text></view></br>
					<text>验收单号</text><input v-model="mpordeVO.recno" type="number" style="border: 2px solid #FFC0CB;width: 150px;"/>
					<text>采购单号</text><input v-model="mpordeVO.ordeno" type="number" style="border: 2px solid #FFC0CB;width: 150px;"/>
					<text>物料编号</text><input v-model="mpordeVO.mpdno" style="border: 2px solid #FFC0CB;width: 150px;"/>
					<text>交货日期</text><input v-model="mpordeVO.inday" style="border: 2px solid #FFC0CB;width: 150px;"/>
					<button @click="getTableInfo">查询</button>
				</view>
<!-- 				<uni-popup-dialog  ref="inputClose"  mode="input" title="输入内容" value="对话框预置提示内容!"
					placeholder="请输入内容" @confirm="dialogInputConfirm"></uni-popup-dialog> -->
			</uni-popup>
		</view>
		
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="popup" background-color="#fff" @change="change">
				<view class="popup-content" :class="{ 'popup-height': type === 'left' || type === 'right' }"><text
						class="text">popup 内容</text></view>
			</uni-popup>
		</view>

		<table class="order-table">
			<thead>
				<tr>
					<th>验收单号</th>
					<th>采购单号</th>
					<th>物料编号</th>
					<th>物料名称</th>
					<th>交货数量</th>
					<th>交货日期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(p, index) in purchaseOrders" :key="index">
					<td>{{p.RECNO}}</td>
					<td>{{p.ORDENO}}</td>
					<td>{{p.MPDNO}}</td>
					<td>{{p.MPDNE}}</td>
					<td>{{p.OUTVOL}}</td>
					<td>{{p.INDAY}}</td>
					<td @click="deleteMp(p)">删除</td>
				</tr>
			</tbody>
		</table>
		<uni-section title="默认样式" type="line" padding >
			<uni-pagination :current="mpordeVO.pageInfo.current" :total="mpordeVO.pageInfo.total" :pageSize="mpordeVO.pageInfo.size" title="标题文字" @change="change" />
		</uni-section>
		<uni-section title="修改数据长度" type="line" padding>
			<view class="btn-view">
				<view>
					<view class="btn-flex">
						<text class="example-info">当前页：{{ mpordeVO.pageInfo.current }}，数据总量：{{ mpordeVO.pageInfo.total }}条，</text>
						<text>每页条数</text>
						<picker style="border: 2px solid #FFC0CB;width: 30px;" @change="pickerChange" :value="sizeIndex" :range="pageSizes">
							<view class="picker-input" @tap="showPicker">{{ mpordeVO.pageInfo.size || '请选择' }}</view>
						</picker>
						<!-- <input v-model="mpordeVO.pageInfo.size" style="border: 2px solid #FFC0CB;width: 20px;" type="number" min="1"/> -->
					</view>
				</view>
			</view>
		</uni-section>
	</view>
</template>

<script>
	export default {
		onShow: function() {
			this.testConnect();
			this.testLogin();
			
		},
		onLoad(options) {
			this.getUnPayPurchaseOrders();
		},
		data() {
			return {
				mpordeVO : {
					recno : '',
					ordeno : '',
					mpdno : '',
					inday : '',
					dbfPath : '',//dbf
					mkcuno : '',//厂商代号
					charset : '',
					pageInfo : {
						current : 1,
						size : 8,
						total : 0
					}
				},
				chooseMpordeVO : {
					recno : 0,
					dbfPath : '',//dbf
					mkcuno : '',//厂商代号
					charset : '',
					pageInfo : {
						current : 1,
						size : 8,
						total : 0
					}
				},
				sizeIndex: 0,// 与pageSizes数组对应的索引，默认选中第一个
				pageSizes: [5, 8 , 10, 15, 20,30,40,50,100,1000], // 下拉选项
				purchaseOrders : []
			}
		},
		watch: {
			'mpordeVO.pageInfo.size': {
				handler(newValue) {
					if (newValue < 1) {
						this.mpordeVO.pageInfo.size = 1;
					}
				},
				immediate: true // 立即触发一次，确保初始值也符合规则
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
			changePort(){
				var ip = uni.getStorageSync('ip');
				var ip2 = uni.getStorageSync('ip2');
				uni.setStorageSync('ip',ip2);
				uni.setStorageSync('ip2',ip);
			},
			getTableInfo(){
				this.getUnPayPurchaseOrders();
				this.$refs.inputDialog.close()
			},
			selectMore(){
				this.$refs.inputDialog.open()
			},
			deleteMp(p){
				console.log(p.RECNO);
				this.chooseMpordeVO.recno = p.RECNO;
				let that = this;
				uni.showModal({
					title: '是否删除?',
					content: '是否删除?',
					success: function (res) {
						if (res.confirm) {
							//查询未交采购单
							var ip = uni.getStorageSync('ip');
							var dbfPath = uni.getStorageSync('dbfPath');
							var loginUser = uni.getStorageSync('loginUser');
							that.chooseMpordeVO.dbfPath = dbfPath;
							that.chooseMpordeVO.mkcuno = loginUser.peson;
							that.chooseMpordeVO.charset = uni.getStorageSync('charset');
							uni.request({
								// url: 'http://127.0.0.1:8091/mhs/article/list',
								url: ip + '/common/mporde/deleteMporintp',
								method: 'POST',
								data: that.chooseMpordeVO,
								success: (res) => {
									console.log(res.data);
									if (res.data.code == 1) {
										uni.showToast({
											title: '删除成功',
											duration: 1000,
											mask: true //是否有透明蒙层，默认为false
										})
										that.getTableInfo();
									} else {
										that.testConnect();
										that.testLogin();
										console.log(res.data.msg)
										if(res.data.msg === 'sql执行出现异常'){
											//重启当前端口服务
											that.restart();
											//切换备用端口服务
											that.changePort();
											that.deleteMp(p);
											// uni.showModal({
											// 	title: '网络异常,是否切换备用路线',
											// 	content: res.data.msg,
											// 	success: function (res) {
											// 		if (res.confirm) {
											// 			that.restart();
											// 			//切换备用端口服务
											// 			that.changePort();
											// 			that.deleteMp(p);
											// 			// console.log('用户点击确定');
											// 		} else if (res.cancel) {
											// 			// console.log('用户点击取消');
											// 		}
											// 	}
											// });
							
											return;
										}
										uni.showModal({
											title: '删除失败',
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
							// console.log('用户点击确定');
						} else if (res.cancel) {
							// console.log('用户点击取消');
						}
					}
				});
			},
			getUnPayPurchaseOrders(){
				//查询未交采购单
				var ip = uni.getStorageSync('ip');
				var dbfPath = uni.getStorageSync('dbfPath');
				var loginUser = uni.getStorageSync('loginUser');
				this.mpordeVO.dbfPath = dbfPath;
				this.mpordeVO.mkcuno = loginUser.peson;
				this.mpordeVO.charset = uni.getStorageSync('charset');
				uni.request({
					// url: 'http://127.0.0.1:8091/mhs/article/list',
					url: ip + '/common/mporde/getSubmitMporintps',
					method: 'POST',
					data: this.mpordeVO,
					success: (res) => {
						console.log(res.data);
						if (res.data.code == 1) {
							this.purchaseOrders = res.data.data.list;
							this.mpordeVO.pageInfo.total = res.data.data.total;
						} else {
							this.testConnect();
							this.testLogin();
							console.log(res.data.msg)
							if(res.data.msg === 'sql执行出现异常'){
								let that = this;
								//重启当前端口服务
								that.restart();
								//切换备用端口服务
								that.changePort();
								that.getUnPayPurchaseOrders();
								// uni.showModal({
								// 	title: '网络异常,是否切换备用路线',
								// 	content: res.data.msg,
								// 	success: function (res) {
								// 		if (res.confirm) {
								// 			that.restart();
								// 			//切换备用端口服务
								// 			that.changePort();
								// 			that.getUnPayPurchaseOrders();
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
			add() {
				this.mpordeVO.pageInfo.total += 10
			},
			reset() {
				this.mpordeVO.pageInfo.total = 0
				this.mpordeVO.pageInfo.current = 1
			},
			change(e) {
				this.mpordeVO.pageInfo.current = e.current;
				this.getTableInfo();
			},
			pickerChange(e) {
				const selectedIndex = e.detail.value;
				this.sizeIndex = selectedIndex;
				this.mpordeVO.pageInfo.size = this.pageSizes[selectedIndex];
				// 可以在此处添加额外的处理逻辑，如更新页面数据等
				this.getTableInfo();
			},
			showPicker() {
				// 如果需要，这里可以手动控制picker的显示，但通常picker会在点击时自动显示
			}
		}
	}
</script>

<style lang="scss">
	.example-body {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
	}

	.btn-view {
		/* #ifndef APP-NVUE */
		display: flex;
		flex-direction: column;
		/* #endif */
		padding: 15px;
		text-align: center;
		background-color: #fff;
		justify-content: center;
		align-items: center;
	}
	
	.flex-view{
		/* #ifndef APP-NVUE */
		display: flex;
		align-items: center;
		/* #endif */
	}

	.btn-flex {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
	}

	.button {
		margin: 20px;
		width: 150px;
		font-size: 14px;
		color: #333;
	}
	.order-table {
		border-collapse: collapse;
		font-size: 8px;
	}

	.order-table th,
	.order-table td {
		border: 1px solid #FFB6C1;
		padding: 4px;
		text-align: center;
		word-wrap: break-word;
	}

	/* 调整'采购单号'和'物料编号'这两列的宽度并强制文本换行 */
	// .order-table th:first-child, /* 第一列：采购单号 */
	// .order-table td:first-child,
	// .order-table th:nth-child(2), /* 第二列：物料编号 */
	// .order-table td:nth-child(2),
	.order-table th:nth-child(3), /* 第二列：物料编号 */
	.order-table td:nth-child(3) {
		/* 请根据实际情况调整列宽 */
		width: 40px;
		white-space: normal;
		/* 允许连续数字换行 */
		word-break : break-all;
		 /* 适用于较旧的浏览器 */
		/* word-wrap: break-word; */
		 /* 当单词太长时强制换行 */
		/* overflow-wrap: break-word; */
	}

	/* 其他列的样式保持不变 */

	.order-table th {
		font-weight: bold;
		border-bottom-width: 2px;
	}

	/* 容器滚动条设置保持 */
	.table-container {
		overflow-x: auto;
		display: block;
		width: 100%;
		box-sizing: border-box;
	}
</style>
