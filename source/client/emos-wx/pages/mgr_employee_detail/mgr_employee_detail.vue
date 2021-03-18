<template>
	<view class="page">
		<view class="summary">
			<view>
				<text class="title">姓名</text>
				<input class="value" type="text" :value="name" placeholder="请输入姓名" @input="inputNameChange" />
			</view>

			<view>
				<text class="title">部门</text>
				<picker mode="selector" :value="dept" range-key="deptName" @change="endDept" :range="deptRange">
					<view class="uni-input">{{ deptDisplay }}</view>
				</picker>
			</view>

			<view>
				<text class="title">性别</text>
				<picker mode="selector" :value="sex" @change="endSex" :range="sexRange">
					<view class="uni-input">{{ sexDisplay }}</view>
				</picker>
			</view>

			<view>
				<text class="title">电话</text>
				<input class="value" type="text" :value="tel" placeholder="请输入手机号" @input="inputTelChange" />
			</view>
			
			<view>
				<text class="title">Email</text>
				<input class="value" type="text" :value="email" placeholder="请输入邮箱地址" @input="inputEmailChange" />
			</view>

			<view>
				<text class="title">入职日期</text>
				<picker mode="date" :value="hiredate" @change="endHireDate">
					<view class="uni-input">{{ hiredate }}</view>
				</picker>
			</view>
			<view>
				<text class="title">角色</text>
				<picker mode="selector" :value="role" @change="endRole" range-key="roleName" :range="roleRange">
					<view class="uni-input">{{ roleDisplay }}</view>
				</picker>
			</view>
		</view>
		<button type="default" class="save btn" @click="save()">保存</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isEdit: false,
				id: 0,
				name: '',
				tel: '',
				email: '',
				hiredate: '2021-02-12',
				sex: 0,
				sexRange: ['男', '女'],
				sexDisplay: '男',
				dept: 0,
				deptRange: [],
				deptDisplay: '请选择',


				role: 0,
				roleRange: [],
				roleDisplay: '请选择',
				employee: null,
				status: 0

			};
		},
		onLoad: function(options) {

			if (options.action === 'edit') {
				this.isEdit = true
				this.id = options.id
				this.getEmployeeDetail()
			} else {
				this.isEdit = false
				this.getDeptList()
				this.getRoleList()
			}

		},
		methods: {

			inputNameChange: function(e) {
				this.name = e.detail.value
			},
			inputTelChange: function(e) {
				this.tel = e.detail.value
			},
			inputEmailChange: function(e) {
				this.email = e.detail.value
			},
			endHireDate: function(e) {
				this.hiredate = e.detail.value
			},
			endSex: function(e) {
				this.sex = e.detail.value
				this.sexDisplay = this.sexRange[this.sex]
			},
			endDept: function(e) {
				this.dept = e.detail.value
				this.deptDisplay = this.deptRange[this.dept].deptName
			},
			endRole: function(e) {
				this.role = e.detail.value
				this.roleDisplay = this.roleRange[this.role].roleName
			},

			getDeptList: function() {
				let that = this
				that.ajax(that.url.searchAllDepts, 'GET', {}, function(resp) {
					that.deptRange = resp.data.result
					if (that.isEdit) {
						let deptId = that.employee.deptId
						for (var i = 0; i < that.deptRange.length; i++) {

							let item = that.deptRange[i]
							console.log(`${item.id}---${deptId}`)
							if (item.id === deptId) {
								that.dept = i
								that.deptDisplay = item.deptName
								break;
							}
						};
					}

				})

			},
			getRoleList: function() {

				let that = this
				that.ajax(that.url.searAllRoles, 'GET', {}, function(resp) {
					that.roleRange = resp.data.result
					if (that.isEdit) {
						let role = that.employee.role
						for (var i = 0; i < that.roleRange.length; i++) {

							let item = that.roleRange[i]
							if (role.indexOf(item.id) > 0) {
								that.role = i
								that.roleDisplay = item.roleName
								break;
							}
						};
					}

				})
			},
			getEmployeeDetail: function() {

				let that = this
				that.ajax(that.url.searchUserById, 'POST', {
					id: that.id
				}, function(resp) {
					that.employee = resp.data.result
					that.name = that.employee.name
					that.tel = that.employee.tel
					that.email = that.employee.email
					that.sexDisplay = that.employee.sex
					that.hiredate = that.employee.hiredate.substring(0, 10)
					that.status = that.employee.status
					that.getDeptList()
					that.getRoleList()

				})
			},
			save: function() {

				var that = this
				console.log(that.name)
				console.log(that.deptRange[that.dept].deptId)
				if (that.name.length == 0) {

					uni.showToast({
						title: '名字不能为空'
					})
					return
				}
				if (that.email.length == 0) {

					uni.showToast({
						title: '邮箱不能为空'
					})
					return
				}
				if (that.tel.length == 0) {

					uni.showToast({
						title: '手机号不能为空'
					})
					return
				}

				if (that.deptDisplay === '请选择') {

					uni.showToast({
						title: '请选择部门'
					})
					return
				}


				if (that.roleDisplay === '请选择') {
					uni.showToast({
						title: '请选择角色'
					})
					return
				}

				let role = '[' + that.roleRange[that.role].id + ']'

				uni.showLoading({
					title: '请稍后...'
				})


				that.ajax(that.url.updateUserInfo, 'POST', {
					dept_id: that.deptRange[that.dept].id,
					email: that.email,
					hiredate: that.hiredate,
					name: that.name,
					role: role,
					tel: that.tel,
					code: that.code,
					status: that.status,
					id: that.id
				}, function(resp) {

					uni.hideLoading()
					uni.showModal({
						title: '保存成功',
						showCancel: false,
						success: function(resp) {
							if (resp.confirm) {
								uni.navigateBack({});
							}
						}
					});


				})
			}
		}
	}
</script>

<style lang="less">
	@import url('mgr_employee_detail.less');
</style>
