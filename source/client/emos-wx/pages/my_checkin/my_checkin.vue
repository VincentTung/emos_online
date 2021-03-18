<template>
	<view class="page">
		<view class="statistics">
			<image src="../../static/big-icon-1.png" mode="widthFix" class="big-icon"></image>
			<view class="report-title">
				<text class="days">{{sum_1}}</text>
				<text class="unit">天</text>
			</view>
			<view class="sub-title">
				<text>本月签到</text>
			</view>
			<view class="report">
				<view class="column green">
					<text class="column-title">正常签到</text>
					<text class="number">{{sum_1}}</text>
				</view>
				<view class="column orange">
					<text class="column-title">迟到签到</text>
					<text class="number">{{sum_2}}</text>
				</view>
				<view class="column red">
					<text class="column-title">缺勤签到</text>
					<text class="number">{{sum_3}}</text>
				</view>
			</view>
		</view>
		<view class="calendar-container">
			<uni-calendar :insert="true" :lunar="false" :selected="list" @monthSwitch="changeMonth" @confirm="changeMonth" />
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [],
				sum_1: 19,
				sum_2: 0,
				sum_3: 0
			}
		},
		onShow: function() {
			let that = this
			let now = new Date()
			let year = now.getFullYear()
			let month = now.getMonth()+1
			that.searchMonthCheckin(that,year, month)
		},
		methods: {
			searchMonthCheckin: function(ref, year, month) {

				let that = ref
				// that.sum_1 = 0
				// that.sum_2 = 0
				// that.sum_3 = 0
				that.list.length = 0
				that.ajax(that.url.searchMonthCheckin, 'POST', {
					year: year,
					month: month
				}, function(resp) {

					for (let one of resp.data.list) {
						if (one.status != null && one.status != '') {
							let color
							if (one.status === '正常') {
								color = 'green'
							} else if (one.status === '迟到') {
								color = 'orange'
							} else if (one.status === '缺勤') {
								color = 'red'
							}
							that.list.push({
								date: one.date,
								info: one.status,
								color: color
							})
						}
					}

					that.sum_1 = resp.sum_1
					that.sum_2 = resp.sum_2
					that.sum_3 = resp.sum_3
				})


			},
			changeMonth: function(e) {

				let that = this
				let year = e.year
				let month = e.month
				that.searchMonthCheckin(that,year, month)
			},

		}
	}
</script>

<style lang="less">
	@import url('my_checkin.less');
</style>
