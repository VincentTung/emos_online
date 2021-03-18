<template>
	<view class="contianer">
		<view class="page">
			<text class="tip">当前所在地区风险等级</text>
			<image :src="img" class="icon" mode="widthFix"></image>
			<text class="risk green" v-if="state ==1">{{risk}}</text>
			<text class="risk yellow" v-if="state ==2">{{risk}}</text>
			<text class="risk red" v-if="state ==3">{{risk}}</text>
			
			
			<view class="count-text">
				
				<text class="count">累计签到统计</text>
				
				<text class="count">高风险地区：{{highRiskCheckinCount}}次</text>
				
				<text class="count">中风险地区：{{middleRiskCheckinCount}}次</text>
				
				<text class="count">低风险地区：{{lowRiskCheckinCount}}次</text>
				
				
			</view>
		</view>
	</view>
</template>

<script>
	var QQMapWX = require("../../lib/qqmap-wx-jssdk.min.js")
	var qqMapSdk
	export default {
		data() {
			return {
				risk: '',
				state: 1,
				img: '',
				highRiskCheckinCount: 0,
				middleRiskCheckinCount: 0,
				lowRiskCheckinCount: 0,
			}
		},
		onLoad() {
			let that = this
			qqMapSdk = new QQMapWX({
				key: that.config.qqMapSdkKey
			})
			that.getRiskAtLocation()
			that.getRiskCheckCount(1)
			that.getRiskCheckCount(2)
			that.getRiskCheckCount(3)

		},
		methods: {
			getRiskCheckCount: function(risk) {

				let that = this
				that.ajax(that.url.getRiskCheckCount, 'POST', {
					risk: risk
				}, function(resp) {

					let count = resp.data.result
					if (risk == 1) {
						that.lowRiskCheckinCount = count
					} else if (risk == 2) {
						that.middleRiskCheckinCount = count
					} else if (risk == 3) {
						that.highRiskCheckinCount = count
					}
				})
			},
			getRiskAtLocation: function() {
				uni.showLoading({
					title: '查询中...'
				})
				let that = this
				uni.getLocation({
					type: "wgs84",
					success: function(resp) {
						let latitude = resp.latitude
						let longitude = resp.longitude

						console.log(`---${latitude}`)
						console.log(`---${longitude}`)
						qqMapSdk.reverseGeocoder({
							location: {
								latitude: latitude,
								longitude: longitude
							},
							success: function(resp) {
								console.log(resp.result)
								let address = resp.result.address
								let addressComponent = resp.result.address_component
								let nation = addressComponent.nation
								let province = addressComponent.province
								let city = addressComponent.city
								let district = addressComponent.district


								that.ajax(that.url.riskAtLocation, 'POST', {
									address: address,
									country: nation,
									province: province,
									city: city,
									district: district
								}, function(resp) {

									that.state = resp.data.result
									if (that.state == 1) {
										that.risk = '低风险'
										that.img = '../../static/low-risk.png'
									} else if (that.state == 2) {
										that.risk = '中风险'
										that.img = '../../static/middle-risk.png'
									} else if (that.state == 3) {
										that.risk = '高风险'
										that.img = '../../static/high-risk.png'
									} else {
										that.risk = '未知风险'
										that.img = '../../static/low-risk.png'
									}

									uni.hideLoading()

								})
							}
						})
					},
					fail: function(resp) {
						console.log('location failed:' + JSON.stringify(resp))
						uni.hideLoading()
					}

				})

			}
		}
	}
</script>

<style lang="less">
	@import url('healthy.less');
</style>
