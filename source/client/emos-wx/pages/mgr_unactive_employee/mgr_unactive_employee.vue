<template>
	<view> 
		<button @click="insertEmployee()" v-if="false" type="default">新建员工</button>
		<uni-list>
			<uni-list-item v-for="one in employees" :title="one.name" :showArrow='true' clickable @click="onClick(one)"
			 @longpress="deleteByCode(one.code)">
			</uni-list-item>
		</uni-list>
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

				employees: []
			}
		},
		onLoad: function() {

		},
		onShow: function() {
			this.getUnActivieEmployee()
		},
		methods: {
			deleteByCode: function(code) {

				let that = this
				uni.showModal({
					title: '提示',
					content: "确定要删除此员工信息",
					success: function(resp) {
						if (resp.confirm) {
							that.ajax(that.url.deleteEmployee, 'POST', {
								code: code
							}, function(resp) {
							
								uni.showToast({
									title: '删除成功'
								})
							})
							that.getUnActivieEmployee()
						}
					}
				});

				
			},
			insertEmployee: function() {

				uni.navigateTo({
					url: '../mgr_unactive_employee_detail/mgr_unactive_employee_detail?action=insert'
				})
			},
			getUnActivieEmployee: function() {
				let that = this
				that.ajax(that.url.searchUnActiveEmployees, 'POST', {

				}, function(resp) {
					that.employees = resp.data.result
				})
			},

			onClick: function(employee) {
				uni.navigateTo({
					url: '../mgr_unactive_employee_detail/mgr_unactive_employee_detail?action=edit&code=' + employee.code
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('mgr_unactive_employee.less');
</style>
