<template>
	<view class="page">

		<view class="user-info">

			<view class="border-outer">
				<view class="border-inner">
					<image class="photo" :src="photo" mode="widthFix"></image>
				</view>
			</view>
			<view class="summary">
				<view>
					<text class="title">姓名</text>
					<text class="value">{{name}}</text>
				</view>
				<view>
					<text class="title">部门</text>
					<text class="value">{{deptName}}</text>
				</view>
				<view>
					<text class="title">状态</text>
					<text class="value">在职</text>
				</view>
			</view>
		</view>


		<view class="list-title">用户信息栏目</view>

		<view class="uni-list">


		</view>
		<uni-list>
			<uni-list-item title="个人资料" link to="/pages/mine-info/mine-info" link></uni-list-item>
			<uni-list-item title="我的考勤" link to="/pages/my_checkin/my_checkin"></uni-list-item>
			<uni-list-item title="罚款记录" @click="fineRecord" link></uni-list-item>
		</uni-list>
		<view class="list-title" v-show="checkPermission(['ROOT','EMPLOYEE:SELECT','DEPT:SELECT','ROLE:SELECT'])">系统管理栏目</view>

		<uni-list>
			<uni-list-item title="员工管理" link to="/pages/manage_employee/manage_employee" v-show="checkPermission(['ROOT','EMPLOYEE:SELECT'])"></uni-list-item>
			<uni-list-item title="部门管理" link to="/pages/dept_list/dept_list" v-show="checkPermission(['ROOT','DEPT:SELECT'])"></uni-list-item>
			<uni-list-item title="权限管理" link to="/pages/role_list/role_list" v-show="checkPermission(['ROOT','ROLE:SELECT'])"></uni-list-item>
		</uni-list>
	</view>
</template>
<script>
	import uniList from '../../components/uni-list/uni-list.vue';
	import uniListItem from '../../components/uni-list-item/uni-list-item.vue';
	export default {
		components: {
			uniList,
			uniListItem
		},
		data() {
			return {

				name: '',
				deptName: '',
				photo: ''
			}
		},
		onShow: function() {
			let that = this
			that.ajax(that.url.searchUserSummary, 'GET', null, function(resp) {

				let result = resp.data.result
				that.name = result.name
				that.deptName = result.deptName
				that.photo = result.photo

				console.log(that.photo)

			})
		},
		methods: {

			//罚款记录
			fineRecord: function() {
				uni.showToast({
					title: '暂无记录',
					icon: 'none'
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('mine.less');
</style>
