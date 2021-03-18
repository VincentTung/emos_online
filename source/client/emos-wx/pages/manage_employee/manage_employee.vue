<template>
	<view class="page">
		<button class="btn" @click="insertEmployee()" type="default">新建员工</button>
		<button class="btn" @click="unactiveEmployee()" type="default">未激活员列表</button>

		<view v-for="dept in list" :key="dept.id">
			<view class="list-title">{{ dept.deptName }}（{{ dept.count }}人）</view>
			<uni-list>
				<uni-list-item v-for="member in dept.members" :title="member.name" :showArrow='true' clickable @click="onClick(member)"
				 @longpress="deleteById(member.userId)">
				</uni-list-item>
			</uni-list>
		</view>

	</view>
</template>

<script>
	import uniList from '@/components/uni-list/uni-list.vue';
	import uniListItem from '@/components/uni-list-item/uni-list-item.vue';
	export default {
		components: {
			uniList,
			uniListItem
		},
		data() {
			return {

				employees: [],
				list: []
			}
		},
		onLoad: function() {

		},
		onShow: function() {
			this.loadData(this)
		},
		methods: {
			deleteById:function(id){
				let that  = this
				uni.showModal({
					title: '提示',
					content: "确定设置为离职状态?",
					success: function(resp) {
						if (resp.confirm) {
							that.ajax(that.url.dimissionEmployee, 'POST', {
								id: id
							}, function(resp) {
							
								uni.showToast({
									title: '操作成功'
								})
								that.loadData(that)
							})
						
						}
					}
				});
			},
			loadData: function(ref) {
				ref.ajax(ref.url.searchUserGroupByDept, 'POST', {
					keyword: ref.keyword
				}, function(resp) {

					let result = resp.data.result
					ref.list = result


				})
			},

			insertEmployee: function() {

				uni.navigateTo({
					url:'../mgr_unactive_employee_detail/mgr_unactive_employee_detail?action=insert'
				})
			},
			onClick: function(employee) {
				uni.navigateTo({
					url: '../mgr_employee_detail/mgr_employee_detail?action=edit&id=' + employee.userId
				})
			},
			unactiveEmployee: function() {

				uni.navigateTo({
					url: '../mgr_unactive_employee/mgr_unactive_employee'
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('manage_employee.less');
</style>
