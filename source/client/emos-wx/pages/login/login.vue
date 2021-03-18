<template>
	<view>
		<image class="logo" src="../../static/logo-1.png" mode="widthFix"></image>
		<view class="logo-title">EMOS在线办公系统</view>
		<view class="logo-subtitle">v1.0.0</view>
		<button class="login-btn" open-type="getUserInfo" @tap="login()">登录系统</button>
		<view class="register-container">
			没有账号？
			<text class="register" @tap="toRegister()">立即注册</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		onLoad:function(){
		
			let code =	uni.getStorageSync('code')
			if(code != null && code.length > 0){
				this.login()
			}
		},
		methods: { 
			login: function() {
				let that = this
				
				uni.showLoading({
					title:"登录中"
				})
				uni.login({
					provider: 'weixin',
					success: function(resp) {

						let code = resp.code;

						that.ajax(that.url.login, "POST", {
							"code": code
						}, function(resp) {
							uni.hideLoading()
							console.log('登录成功')
							let permisson = resp.data.permission
							uni.setStorageSync('code',code)
							uni.setStorageSync('permission', permisson)
							uni.showToast({
								icon: 'none',
								title: '登录成功'
							})
							
							uni.switchTab({
								url:"../index/index"
							})
						})
					},
					fail: function(e) {
						console.log(e)
						uni.setStorageSync('code','')
						uni.showToast({
							icon: 'none',
							title: '登录异常'
						})
					}
				})
			},
			toRegister: function() {
				uni.navigateTo({
					url: '../register/register'
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('login.less');
</style>
