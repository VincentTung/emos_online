<template>
	<view>
		<image class="logo" src="../../static/logo-2.png" mode="widthFix"></image>
		<view class="register-container">
			<input class="register-code" type="text" maxlength="4" v-model="registerCode" placeholder="请输入您的邀请码" />
			<view class="register-desc">管理员创建员工账号后，请向管理员获取注册邀请码</view>
			<button class="register-btn" open-type="getUserInfo" @tap="register()">注册账号</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				registerCode: ''
			}
		},
		methods: {
			register: function() {
				let that = this
				if (that.registerCode == null || that.registerCode.length == 0) {
					uni.showToast({
						icon: 'none',
						title: '邀请码不能为空'
					})
					return
				} else if (/^[0-9]{4}$/.test(that.registerCode) == false) {
					uni.showToast({
						icon: 'none',
						title: '邀请码必须是4位数字'
					})
					return
				}

				uni.showLoading({
					title: "请稍后"
				})


				that.ajax(that.url.checkRegisterCode, 'POST', {
					'registerCode': that.registerCode
				}, function(resp) {


					let msssage = resp.data.mssage
					console.log(resp)
					uni.hideLoading()
					if (resp.data.name != null) {
						uni.showModal({
							title: '提示消息',
							content: '请确认名字为' + resp.data.name,
							success: function(resp) {
								if (resp.confirm) {

									uni.login({
										provider: 'weixin',
										success: function(resp) {

											let code = resp.code
											uni.showLoading({
												title: '加载中...'
											})
											uni.getUserInfo({
												provider: 'weixin',
												success: function(resp) {
													let nickName = resp
														.userInfo.nickName;
													let avatarUrl = resp
														.userInfo
														.avatarUrl;
													console.log(
														`wexin_userinfo:${resp.userInfo.nickName}__${resp.userInfo.avatarUrl}`
													)
													let data = {
														code: code,
														nickName: nickName,
														photo: avatarUrl,
														registerCode: that
															.registerCode
													}

													uni.showLoading({
														title: '注册中...'
													})
													that.ajax(that.url
														.reigster,
														'POST', data,
														function(
															resp) {
															console
																.log(
																	'register done'
																)
															uni.setStorageSync(
																'code',
																code
															)
															let permission =
																resp
																.data
																.permission
															uni.setStorageSync(
																'permission',
																permission
															)
															console
																.log(
																	permission
																)

															uni.navigateTo({
																url: '../login/login'
															})
															uni.hideLoading()

														})

													uni.hideLoading()
												},
												fail: function(e) {

													console.log(e)
													uni.hideLoading()
												}
											})
										},
										fail: function(e) {
											console.log(e)
											uni.hideLoading()

										}
									})

								}
							}
						})

					} else {
						uni.showToast({
							icon: 'none',
							title: message
						})
					}
				})


			}
		}
	}
</script>

<style lang="less">
	@import url('register.less');
</style>
