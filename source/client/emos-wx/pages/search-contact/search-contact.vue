<template>
	<view>
		<view class="head">
			<view class="search">
				<view style="display: inline; width: 90%;">
					<input class="search-word" placeholder="搜索" @confirm='confirm' @input='input' v-model="key"></view>

				<view class="option" @click="cancle">{{optitle}}</view>
			</view>
		</view>
		<uni-list>
			<uni-list-item v-for="one in result" :title="one.name" :showArrow='true' clickable @click="onClick(one)">
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
				optitle: '取消',
				key: '',
				result: []
			}
		},
		methods: {

			confirm: function(resp) {
				console.log(this.key)
				this.search(this.key)
			},
			input: function(resp) {
				console.log(resp)
			},
			cancle: function() {
				uni.navigateBack()
			},
			onClick: function(detail) {

				let that = this
				uni.navigateTo({
					url: "../employee-detail/employee-detail?detail=" + JSON.stringify(detail) + "&dept_name=" + detail.deptName
				})
			},

			search: function(name) {
				let that = this

				that.ajax(that.url.employeeSearch, 'POST', {
					'name': name
				}, function(resp) {
					if (resp.data.list != null) {
						that.result = resp.data.list
					} else {
						uni.showToast({
							icon: 'none',
							title: '没有查询到相关记录'
						})
					}
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('../../style.less');

	.head {
		padding: 30rpx 30rpx;
		background-color: #F6F6F6;
	}

	.search {

		background-image: url(../../static/search.png) no-repeat 0 center;
		height: 60rpx;
		border: 1px solid #EEEEEE;
		color: #BDBDBD;
		background-color: #FFFFFF;
		width: 90%;
		border-radius: 8px;
	}

	.search-word {
		display: inline;
		float: left;
		height: 60rpx;

		margin-left: 60rpx;
	}

	.icon {
		margin-left: 10rpx;
		margin-right: 20rpx;
		width: 40rpx;
		display: inline;
	}

	.option {
		display: inline;
		position: absolute;
		right: 10rpx;
		height: 60rpx;
		color: @background-color;
		vertical-align: middle;
	}
</style>
