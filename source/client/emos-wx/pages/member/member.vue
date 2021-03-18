<template>
	<view class="page">
		<checkbox-group @change="selected">
			<block v-for="dept in list" :key="dept.id">
				<view class="list-title">{{ dept.deptName }}（{{ dept.count }}人）</view>
				<view class="item" v-for="member in dept.members" :key="member.userId">
					<view class="key">{{ member.name }}</view>
					<checkbox class="value" :value="member.userId" :checked="member.checked"></checkbox>
				</view>
			</block>
		</checkbox-group>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [],
				members:null
			}
		},
		onShow: function() {
			this.loadData(this)
		},
		onLoad:function(options){
			console.log(JSON.stringify(options))
			let that = this
			if(options.hasOwnProperty('members')){
				let members = options.members
				console.log(options.members)
				that.members = members.split(',')
			}
		},
		methods: {
			loadData: function(ref) {
				ref.ajax(ref.url.searchUserGroupByDept, 'POST', {
					keyword: ref.keyword
				}, function(resp) {

					let result = resp.data.result
					ref.list = result
					for (let dept in ref.list) {
						for (let member in dept.members) {

							if (ref.memebers.indexOf(member.userId + '') != -1) {
								member.checked = true
							} else {
								member.checked = false
							}
						}
					}

				})
			},
			selected: function(e) {
				console.log(JSON.stringify(e))
				let that = this
				that.members = e.detail.value
				let pages = getCurrentPages()
				let prevPage = pages[pages.length - 2]
				prevPage.members = that.members
				prevPage.finishMembers = true

			}
		}
	}
</script>

<style lang="less">
	@import url('member.less');
</style>
