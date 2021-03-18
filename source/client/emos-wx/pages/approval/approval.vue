<template>
	 <!-- v-if="checkPermission(['WORKFLOW:APPROVAL'])" -->
	<view class="page">
		<view class="meeting" v-if="processType == 'meeting'">
			<view class="header">{{title}}</view>
			<view class="attr">
				<view class="list">
					<view class="item">
						<view class="key">日期</view>
						<text class="value">{{ date }}</text>
					</view>
					<view class="item">
						<view class="key">开始时间</view>
						<text class="value">{{ start }}</text>
					</view>
					<view class="item">
						<view class="key">结束时间</view>
						<text class="value">{{ end }}</text>
					</view>
					<view class="item">
						<view class="key">会议类型</view>
						<text class="value">{{typeArray[typeIndex]}}</text>
					</view>
					<view class="item" v-if="typeArray[typeIndex] == '线下会议'">
						<view class="key">地点</view>
						<view class="value">{{ place }}</view>
					</view>
				</view>
				<view>
					<text class="desc">{{ desc }}</text>
				</view>
			</view>
			<view class="members">
				<view class="number">参会者（{{ members.length }}人）</view>
				<view class="member">
					<view class="user" v-for="one in members" :key="one.id">
						<image :src="one.photo" mode="widthFix" class="photo"></image>
						<text class="name">{{ one.name }}</text>
					</view>
				</view>
			</view>
			<block v-if="uuid!=null">
				<button class="btn" @tap="approvalMeeting('同意')" type="primary">同意</button>
				<button class="btn" @tap="approvalMeeting('不同意')" type="warn">不同意</button>
			</block>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				processType: null,
				taskId: null,
				id: null,
				uuid: null,
				title: '',
				date: '',
				start: '',
				end: '',
				typeArray: ['在线会议', '线下会议'],
				typeIndex: 0,
				place: '',
				desc: '',
				members: []
			}
		},
		methods: {
			approvalMeeting: function(approval) {
				let that = this;
				let data = {
					state: approval =='同意'?1:0,
					uuid: that.uuid
				}
				uni.showModal({
					title: '提示信息',
					content: '你' + approval + '这条申请？',
					success: function(resp) {
						if (resp.confirm) {
							that.ajax(
								that.url.approvalMeeting, 'POST', data,
								function(resp) {
									uni.showToast({
										title: '处理完成',
										icon: 'success',
										complete: function() {
											setTimeout(function() {
												uni.navigateBack({});
											}, 2000);
										}
									});
								}
							);
						}
					}
				});
			}
		},
		onLoad: function(options) {
			this.processType = options.processType;
			this.id = options.id;
			this.uuid = options.uuid;
			if (options.hasOwnProperty('taskId')) {
				this.taskId = options.taskId;
			}
		},
		onShow: function() {
			let that = this;
			if (that.processType == 'meeting') {
				that.ajax(
					that.url.searchMeetingById, 'POST', {
						id: that.id
					},
					function(resp) {
						let result = resp.data.result;
						that.uuid = result.uuid;
						that.title = result.title;
						that.date = result.date;
						that.start = result.start;
						that.end = result.end;
						that.typeIndex = result.type - 1;
						that.place = result.place;
						let desc = result.desc;
						if (desc != null && desc != '') {
							that.desc = desc;
						}
						that.members = result.members;
					}
				);
			}
		}
	}
</script>

<style lang="less">
	@import url('approval.less');
</style>
