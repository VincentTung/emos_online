<template>
	<view class="page" v-if="checkPermission(['ROOT', 'ROLE:SELECT'])">
		<view class="list">
			<view class="item" v-for="one in list" :key="one.id" @longpress="deleteRole(one.id, one.roleName)" @tap="toRolePage(one.id,one.roleName)">
				<text class="key">{{ one.roleName }}</text>
				<image src="../../static/icon-2.png" mode="widthFix" class="icon"></image>
			</view>
		</view>
		<button class="btn" @tap="insertRole" v-if="checkPermission(['ROOT', 'ROLE:INSERT'])">添加</button>
		<uni-popup ref="popupRole" type="dialog">
			<uni-popup-dialog mode="input" title="编辑角色名称" :value="roleName" placeholder="输入角色名称" @confirm="finishRole"></uni-popup-dialog>
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
				roleName: ''
			};
		},
		onShow() {
			this.getAllRoles();
		},
		methods: {

			getAllRoles: function() {

				let that = this
				that.ajax(that.url.searAllRoles, 'GET', {}, function(resp) {
					that.list = resp.data.result
				})
			},
			toRolePage: function(roleId, roleName) {
				uni.navigateTo({
					url: '../role/role?opt=edit&id=' + roleId + '&roleName=' + roleName
				})
			},
			insertRole: function() {
				this.$refs.popupRole.open()

			},
			deleteRole: function(id, name) {

				let that = this
				uni.showModal({
					title: '提示',
					content: '确定要删除角色：' + name,
					success: function(res) {
						if (res.confirm) {

							uni.showLoading({
								title: '请稍后..'
							})
							that.ajax(that.url.deleteRole, 'POST', {
								'id': id
							}, function(resp) {
								uni.hideLoading()
								uni.showToast({
									title: '删除成功',
									icon: 'none'
								})
								that.getAllRoles()
							})
						}

					}
				})



			},
			finishRole: function(done, value) {

				let that = this
				if (value != null && value != '') {
					this.roleName = value
					done()
					uni.navigateTo({
						url: '../role/role?opt=insert&roleName=' + this.roleName,

					})
					this.roleName = ''
				} else {

					uni.showToast({
						icon: 'none',
						title: '角色名称不能为空'
					})
				}

			}
		}
	}
</script>

<style lang="less">
	@import url("role_list.less");
</style>
