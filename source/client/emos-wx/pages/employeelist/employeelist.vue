<template>
	<view>
		<view class="head">
			<view class="depart-name">{{depart.dept_name}}</view>
			<view class="count">({{depart.employees.length}})</view>
		</view>
		<uni-list>
			<uni-list-item v-for="one in depart.employees" :title="one.name" :showArrow='true'  clickable  @click="onClick(one)" >
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
				depart:null
			}
		},
		methods: {
			onClick:function(detail){
				let that = this
				uni.navigateTo({
					url:"../employee-detail/employee-detail?detail="+JSON.stringify(detail)+"&dept_name="+that.depart.dept_name
				})
			}
		},
		onShow:function(){
			let that = this
			uni.getStorage({
			                key:'employees',
			                success:(res)=>{	
								console.log(res)
								that.depart = JSON.parse(res.data)
			                }
			            })
		}
	}
</script>
<style lang="less">
	@import url('employeelist.less');
	
	.head{
		padding: 30rpx;
	}
	.depart-name{
		display: inline;
		color: @background-color;
		font-size: 30rpx;
	}
	.count{
		margin-left: 5rpx;
		display: inline;
		color: @background-color;
		font-size: 30rpx;
	}
	
	uni-list-item{
		
		color: @font-color; 
		font-size: 36rpx;
	}
</style>
