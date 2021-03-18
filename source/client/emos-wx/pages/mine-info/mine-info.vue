<!-- 个人信息界面 -->
<template>
	<view class="page">

		<uni-list>
			<!-- <uni-list-item title="头像" ></uni-list-item> -->
			<uni-list-item title="姓名" @click="editName" link :rightText="user.name"></uni-list-item>
			<uni-list-item title="部门" :rightText="user.deptName"></uni-list-item>
			<uni-list-item title="手机" link @click="editTel" :rightText="user.tel"></uni-list-item>
			<uni-list-item title="邮箱" :rightText="user.email"></uni-list-item>
		</uni-list>

		<uni-popup ref="popupName" type="dialog">
			<uni-popup-dialog mode="input" title="编辑名字" :value="user.name" placeholder="输入姓名" @confirm="finishName"></uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="popupTel" type="dialog">
			<uni-popup-dialog mode="input" title="编辑手机号" :value="user.tel" placeholder="输入手机号" @confirm="finishTel"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	import uniList from '../../components/uni-list/uni-list.vue';
	import uniListItem from '../../components/uni-list-item/uni-list-item.vue';
	import uniPopup from '@/components/uni-popup/uni-popup.vue';
	import uniPopupMessage from '@/components/uni-popup/uni-popup-message.vue';
	import uniPopupDialog from '@/components/uni-popup/uni-popup-dialog.vue';
	export default {
		components: {
			uniPopup,
			uniPopupMessage,
			uniPopupDialog,
			uniList,
			uniListItem
		},
		data() {
			return {
				user: {
					name: '',
					deptName: '研发部',
					tel: '18888888888',
					email: '',
					photo: ''

				}
			}
		},
		onShow: function() {
			this.getUserInfo()
		},
		methods: {

			getUserInfo: function() {
				let that = this
				that.ajax(that.url.searchUserSummary, 'GET', null, function(resp) {

					that.user = resp.data.result
					that.name = result.name
					that.deptName = result.deptName
					that.photo = result.photo

					console.log(that.photo)

				})
			},
			editTel: function() {
				this.$refs.popupTel.open()
			},
			editName: function() {
				this.$refs.popupName.open()
			},
			finishName: function(done, value) {

				let that = this
				if (value != null && value != '') {
					that.user.name = value
					done()
					that.updateUserInfo()
				} else {
					uni.showToast({
						icon: 'none',
						title: '姓名不能为空'
					})
				}

			},
			finishTel: function(done, value) {

				let that = this
				if (value != null && value != '') {
					this.user.tel = value
					done()
					that.updateUserInfo()


				} else {
					uni.showToast({
						icon: 'none',
						title: '手机号不能为空'
					})
				}

			},
			updateUserInfo: function() {
				uni.showLoading({
					title: '请稍后...'
				})
				let that = this
				that.ajax(that.url.updateUserInfo, 'POST', {
					'name': that.user.name,
					'tel': that.user.tel,
					'email': that.user.email
				}, function(resp) {
					
					uni.showToast({
						icon: 'none',
						title: '操作成功'
					})
					uni.hideLoading()
					that.getUserInfo()
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('mine-info.less');
</style>
