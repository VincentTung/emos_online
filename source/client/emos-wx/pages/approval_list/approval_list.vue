<template>
	<view class="page">
		<uni-segmented-control :current="current" :values="items" @clickItem="onClickItem" style-type="button" active-color="#3474FF" />
		<!--第一个选项卡-->
		<view v-if="current == 0" class="list">
			<view class="item" v-for="one in list" :key="one.id">
				<view class="left">
					<image :src="one.photo" mode="widthFix" class="photo"></image>
					<view class="name">{{ one.name }}</view>
					<view class="desc">（发起）</view>
				</view> 
				<view class="center">
					<view class="title">{{ one.sameDept ? '本部门' : '跨部门' }}{{ one.type }}申请</view>
					<view class="attr">日期：{{ one.date }}</view>
					<view class="attr">时长：{{ one.hours >= 1 ? one.hours : '小于1' }}小时</view>
					<view class="status">状态：待审批</view>
				</view>
		 		<view class="right"><button class="btn" @tap="toPage('meeting',one.id,one.uuid,one.taskId)">审批</button></view>
			</view>
		</view>
		<view v-if="current == 1" class="list">
			<view class="item" v-for="one in list" :key="one.id" @tap="toPage('other',one.id, one.uuid, null)">
				<view class="left">
					<image :src="one.photo" mode="widthFix" class="photo"></image>
					<view class="name">{{ one.name }}</view>
					<view class="desc">（发起）</view>
				</view>
				<view class="center">
					<view class="title">{{ one.sameDept ? '本部门' : '跨部门' }}{{ one.type }}申请</view>
					<view class="attr">日期：{{ one.date }}</view>
					<view class="attr">时长：{{ one.hours >= 1 ? one.hours : '小于1' }}小时</view>
					<view class="attr">
						本人审批：
						<text :class="{ green: one.result_1 == '同意', red: one.result_1 == '不同意' }">{{ one.result_1 }}</text>
					</view>
					<view class="attr">
						最终结果：
						<text class="result" v-if="one.result_2 == null">等待中</text>
						<text v-if="one.result_2 != null" :class="{ green: one.result_2 == '同意', red: one.result_2 == '不同意' }">{{ one.result_2 }}</text>
					</view>
				</view>
				<view class="right">
					<block v-if="one.processStatus == '已结束'">
						<image :src="one.lastUserPhoto" mode="widthFix" class="icon"></image>
						<view class="name">{{ one.lastUserName }}</view>
						<view class="desc">（终审）</view>
					</block>
					<block v-if="one.processStatus == '未结束'">
						<image src="../../static/icon-20.png" mode="widthFix" class="icon"></image>
						<view class="desc">审批进行中</view>
					</block>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import uniSegmentedControl from "@/components/uni-segmented-control/uni-segmented-control.vue"
	export default {
		components: {
			uniSegmentedControl
		},
		data() {
			return {
				page: 1,
				length: 20,
				list: [],
				isLastPage: false,
				items: ['待审批', '已审批'],
				current: 0
			}
		},

		onLoad() {
			
		},
		
		onShow:function(){
			this.fetchApprovalList()
		},
		methods: {
			onClickItem: function(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
					this.page = 1
					this.list = []
					this.isLastPage = false
					this.loadData(this)
				}
			},

			fetchApprovalList: function() {

				let that = this
				//待审批
				if (that.current === 0) {

					that.ajax(that.url.searchMeetingNeedApproval, 'POST', {}, function(resp) {

						that.list = resp.data.result
					})
				} else {
					that.ajax(that.url.searchMeetingAlreadyApproval, 'POST', {}, function(resp) {

						that.list = resp.data.result
					})
				}

			},
			loadData: function(ref) {
				this.fetchApprovalList()
				// if (ref.current == 1 && list.length > 0) {
				// 	ref.ajax(ref.url.selectUserPhotoAndName, 'POST', {
				// 		ids: JSON.stringify(list)
				// 	}, function(resp) {
				// 		let nameAndPhoto = resp.data.result;
				// 		for (let one of result) {
				// 			if (one.hasOwnProperty("lastUser")) {
				// 				let temp = nameAndPhoto[0]
				// 				one.lastUserName = temp.name
				// 				one.lastUserPhoto = temp.photo
				// 			}
				// 		}
				// 		ref.list = result;
				// 	});
				// }
			},
			toPage: function(processType,id, uuid, taskId) {
				let url = '../approval/approval?processType=' + processType + '&uuid=' + uuid+'&id='+id;
				if (taskId != null) {
					url += '&taskId=' + taskId;
				}
				uni.navigateTo({
					url: url
				});
			}
		}
	}
</script>

<style lang="less">
	@import url('approval_list.less');
</style>
