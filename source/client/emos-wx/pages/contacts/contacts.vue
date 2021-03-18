<template>
	<view class="page">
		<view class="head">
			<view class="search"@click="search"> 
				<image class="icon" src="../../static/search.png" mode="widthFix"></image>
				<view class="search-word">搜索</view>
			</view>
		</view>
	<!-- 	<view class="title">部门列表:</view> -->
		<uni-list>
			<uni-list-item v-for="one in departList" :title="one.dept_name" :showArrow='true' :note="`人数:${one.employees.length}`" clickable  @click="onClick(one)" >
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
				departList:[]
			}
		},
		methods: {
			search:function(){
				uni.navigateTo({
					url:"../search-contact/search-contact"
				})
			},
				onClick:function(employees){
	
					console.log(JSON.stringify(employees))
					
					uni.setStorage({
					                key:'employees',
					                data:JSON.stringify(employees),
					                success:()=>{
					                    uni.navigateTo({
											title:employees.dept_name,
					                    	url:"../employeelist/employeelist"
					                    })
					                }
					            })

					uni.navigateTo({
						
					})
				}
		},
		onShow: function() {
			let that = this
			that.ajax(that.url.emplyeeList, 'GET', null,function(resp) {
				let list = resp.data.list
				if(list!= null){		
					that.departList = list
				}
				
			})
		}
	}
</script>

<style lang="less">
	@import url('contacts.less');

</style>
