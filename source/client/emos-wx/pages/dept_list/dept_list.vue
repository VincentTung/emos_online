<template>
	<view class="page" v-if="checkPermission(['ROOT', 'DEPT:SELECT'])">
		<view class="list">
			<view class="item" v-for="one in list" :key="one.id" @longpress="deleteDept(one.id, one.deptName)" @tap="editDept(one.id,one.deptName)">
				<text class="key">{{ one.deptName }}</text>
				<image src="../../static/icon-2.png" mode="widthFix" class="icon"></image>
			</view>
		</view>
		<button class="btn" @tap="insertDept" v-if="checkPermission(['ROOT', 'DEPT:INSERT'])">添加</button>
		<uni-popup ref="popupDept" type="dialog">
			<uni-popup-dialog mode="input" title="编辑部门名称" :value="deptName" placeholder="输入部门名称" @confirm="finishDept"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue';
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue';
	export default {
		components: {
			uniPopup,
			uniPopupMessage,
			uniPopupDialog
		}, 
		data() {
			return {
				list: [], 
				id: 0,
				deptName: ''
			};
		},
		methods: {
			editDept: function(id, deptName) {
				this.id = id;
				this.deptName = deptName
				this.$refs.popupDept.open()
			},
			insertDept: function() {
				this.$refs.popupDept.open()

			},
			finishDept: function(done, value) {

				let that = this
				if (value != null && value != '') {
					that.deptName = value
					uni.showLoading({
						title: '请稍后...'
					})

					if (that.id != 0) {
						that.ajax(that.url.updateDept, 'POST', {
							'id': this.id,
							'deptName': value
						}, function(resp) {
							uni.hideLoading()
							done()
							uni.showToast({
								title: '操作成功',
								icon: 'none'
							})
							that.id = 0
							that.deptName = ''
							that.getAllDepts()
						})
					} else {
						that.ajax(that.url.insertDept, 'POST', {
							'deptName': value
						}, function(resp) {
							uni.hideLoading()
							done()
							uni.showToast({
								title: '操作成功',
								icon: 'none'
							})
							that.getAllDepts()
						})
					}
				} else {

					uni.showToast({
						icon: 'none',
						title: '角色名称不能为空'
					})
				}

			},
			getAllDepts: function() {
				let that = this
				that.ajax(that.url.searchAllDepts, 'GET', {}, function(resp) {
					that.list = resp.data.result
				})
			},
			deleteDept: function(id, name) {

				let that = this
				uni.showModal({
					title: '提示',
					content: '确定要删除角色：' + name,
					success: function(res) {
						if (res.confirm) {

							uni.showLoading({
								title: '请稍后..'
							})
							that.ajax(that.url.deleteDept, 'POST', {
								'id': id
							}, function(resp) {
								uni.hideLoading()


								uni.showToast({
									title: '删除成功',
									icon: 'none'
								})
								that.getAllDepts()
							})
						}

					}
				})



			},

		},
		onLoad: function(opt) {

		},
		onShow: function() {

			this.getAllDepts()
		}
	}
</script>

<style lang="less">
	@import url("dept_list.less");
</style>
