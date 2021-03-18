<template>
	<view class="page">

		<view class="detail">

			<view>
				<view class="">
					<view class="name">{{detail.name}}</view>
				</view>
				<text class="head">基本信息</text>
				<view class="item" @click="call(detail.tel)">
					<view class="left">
						<view class="title">{{detail.tel}}</view>
						<view class="des">手机</view>
					</view>
					<image class="icon" src="../../static/call.png" mode="widthFix"></image>
				</view>
				<view class="item">
					<view class="left">
						<view class="title">{{departName}}</view>
						<view class="des">部门</view>
					</view>
					<image class="icon" src="" mode="widthFix"></image>
				</view>
				<view class="item">
					<view class="left">
						<view class="title">{{detail.email}}</view>
						<view class="des">邮箱</view>
					</view>
					<image class="icon" src="." mode="widthFix"></image>
				</view>
			</view>

		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				detail: null,
				departName: ''
			}
		},
		methods: {

			call: function(tel) {
				let that = this
				uni.showModal({
					title: "提示",
					content: `是否拨打${tel}?`,
					success: function(resp) {
						if (resp.confirm) {

							uni.makePhoneCall({
							    phoneNumber: that.detail.tel
							});

						}
					}
				})
			}
		},
		onLoad: function(option) {
			this.detail = JSON.parse(option.detail)
			this.departName = option.dept_name
		}
	}
</script>

<style lang="less">
	@import url('employee-detail.less');


</style>
