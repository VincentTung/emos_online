<template>
	<view class="page">
		<uni-popup ref="popupName" type="top">
			<uni-popup-message type="success" :message="'接收到' + lastRows + '条消息'" :duration="2000" />
		</uni-popup>

		<swiper class="swiper" circular="true" interval="8000" duration="1000">
			<swiper-item>
				<image mode="widthFix" src="https://xxx/swiper-1.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://xxx/swiper-2.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://xxx/swiper-3.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://xxx/swiper-4.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image mode="widthFix" src="https://xxx/swiper-5.jpg"></image>
			</swiper-item>
		</swiper>
		<view v-show="unreadRows > 0" class="notify-container" @tap="toPage('消息提醒', '/pages/message_list/message_list')">
			<view class="notify-title">
				<image class="notify-icon" src="../../static/icon-1.png" mode="widthFix"></image>
				消息提醒
			</view>
			<view class="notify-content">
				你有{{unreadRows}}条未读消息
			</view>
			<image class="more-icon" src="../../static/icon-2.png" mode="widthFix"></image>
		</view>
		<view class="nav-container">
			<view class="nav-row">

				<view class="nav">
					<image class="icon" src="../../static/nav-1.png" mode="widthFix" @tap="toPage('在线签到','../checkin/checkin')"></image>
					<text class="name">在线签到</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-2.png" mode="widthFix"  @tap="toPage('员工健康','../healthy/healthy')"></image>
					<text class="name">员工健康</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-3.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">在线请假</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-4.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">公务出差</text>
				</view>
			</view>

			<view class="nav-row">
				<view class="nav">
					<image class="icon" src="../../static/nav-5.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">员工日报</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-6.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">我的加班</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-7.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">付款申请</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-8.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">费用报销</text>
				</view>
			</view>


			<view class="nav-row">

				<view class="nav">
					<image class="icon" src="../../static/nav-9.png" mode="widthFix" @tap="toPage('公告通知', '/pages/message_list/message_list')"></image>
					<text class="name">公告通知</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-10.png" mode="widthFix" @tap="toPage('在线审批','../approval_list/approval_list')"></image>
					<text class="name">在线审批</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-11.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">物品领用</text>
				</view>

				<view class="nav">
					<image class="icon" src="../../static/nav-12.png" mode="widthFix" @tap="notOpen()"></image>
					<text class="name">采购审批</text>
				</view>
			</view>

		</view>

		<view class="calendar-container">
			<uni-calendar :insert="true" :lunar="true" :selected="calendar" @monthSwitch="changeMonth" />
		</view>
		<view class="meeting-container" v-for="one in meetingList" :key="one.id">
			<view class="meeting">
				<view class="row">
					<text class="title">{{ one.title }}</text>
					<text class="hours">时长：{{ one.hour == 0 ? 1 : one.hour }}小时</text>
				</view>
				<view class="row">
					<image src="../../static/icon-3.png" mode="widthFix" class="icon"></image>
					<text class="desc" space="emsp">日期：{{ one.date }} {{ one.start }}</text>
				</view>
				<view class="row">
					<image src="../../static/icon-4.png" mode="widthFix" class="icon"></image>
					<text class="desc">地点：{{ one.type == '线上会议' ? one.type : one.place }}</text>
				</view>
				<image :src="one.photo" class="photo"></image>
			</view>
		</view>
	</view>

</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue'
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue'
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue'
	import uniCalendar from '@/components/uni-calendar/uni-calendar.vue'
	export default {
		components: {
			uniPopup,
			uniPopupMessage,
			uniPopupDialog,
			uniCalendar
		},
		data() {
			return {
				title: '',
				unreadRows: 0,
				lastRows: 0,
				timer: null,
				calendar: [],
				meetingPage: 1,
				meetingLength: 20,
				meetingList: [],
				isMeetingLastPage: false

			}
		},
		onLoad: function() {
			let that = this;
			uni.$on('showMessage', function() {
				that.$refs.popupName.open()
			});

		},
		onUnload: function() {
			uni.$off('showMessage')
		},
		onShow: function() {
			let that = this
			// that.timer = setInterval(function() {
			// 	that.ajax(that.url.refreshMessage, 'GET', null, function(resp) {
			// 		that.unreadRows = resp.data.unreadRows
			// 		that.lastRows = resp.data.lastRows
			// 		if (that.lastRows > 0) {
			// 			uni.$emit('showMessage')
			// 		}
			// 	})
			// }, 5 * 1000)

			that.meetingPage = 1
			that.isMeetingLastPage = false
			that.meetingList = []
			//加载分页会议列表
			that.loadMeetingList(that);
			//加载本月会议日期
			let date = new Date();
			that.loadMeetingInMonth(that, date.getFullYear(), date.getMonth() + 1);
		},
		onHide: function() {
			clearInterval(this.timer)
		},
		methods: {
			
			notOpen:function(){
				uni.showToast({
					title:'暂未开放',
					icon:'none'
				})
			},
			onReachBottom: function() {
				if (this.isMeetingLastPage) {
					return
				}
				this.meetingPage = this.meetingPage + 1;
				this.loadMeetingList(this)
			},
			changeMonth: function(e) {
				let that = this
				let year = e.year
				let month = e.month
				that.loadMeetingInMonth(that, year, month)
			},
			toPage: function(name, url) {
				uni.navigateTo({
					url: url
				})
			},
			loadMeetingList: function(ref) {
				let data = {
					page: ref.meetingPage,
					length: ref.meetingLength
				};
				ref.ajax(ref.url.searchMyMeetingListByPage, 'POST', data, function(resp) {
					let result = resp.data.result
					if (result == null || result.length == 0) {
						ref.isMeetingLastPage = true
						ref.meetingPage = ref.meetingPage - 1
						if (ref.meetingPage > 1) {
							uni.showToast({
								icon: 'none',
								title: '已经到底了'
							})
						}
					} else {
						let list = []
						for (let one of result) {
							for (let meeting of one.list) {
								if (meeting.type == 1) {
									meeting.type = '线上会议'
								} else if (meeting.type == 2) {
									meeting.type = '线下会议'
								}
								if (meeting.status == 3) {
									meeting.status = '未开始'
								} else if (meeting.status == 4) {
									meeting.status = '进行中'
								}
								list.push(meeting)
							}
						}
						ref.meetingList = list
					}
				});
			},
			loadMeetingInMonth: function(ref, year, month) {
				let data = {
					year: year,
					month: month
				}
				ref.ajax(ref.url.searchUserMeetingInMonth, 'POST', data, function(resp) {
					ref.calendar = []
					for (let one of resp.data.result) {
						ref.calendar.push({
							date: one,
							info: '会议'
						})
					}
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('index.less');
</style>
