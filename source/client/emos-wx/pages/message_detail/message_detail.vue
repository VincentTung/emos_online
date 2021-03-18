<template>
	<view class="page">

		<view class="message">
			<view class="header">
				<view class="desc">{{sendTime}}</view>
				<view class="opt" @tap='deleteMsg'>删除</view>
			</view>
			<view class="content">{{msg}}</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				id:'',
				readFlag:false,
				refId :'',
				sendTime: '2021-01-25 12:00',
				msg: ""
			}
		},
		onShow:function(){
			let that = this
			uni.setNavigationBarTitle({
				title:'系统通知'
			})
			that.ajax(that.url.searchMessageById,'POST',{'id':that.id},function(resp){
				let result = resp.data.result
				that.sendTime = result.sendTime
				that.msg = result.msg
			})
		},
		onLoad:function(options){
			let that = this
			that.id = options.id
			that.readFlag = options.readFlag == 'true'?true:false
			that.refId = options.refId
			if(!that.readFlag){
				
				that.ajax(that.url.updateUnreadMessage,'POST',{'id':that.refId},function(resp){
					console.log('消息更新成已读状态')
				})
			}
		},
		methods: {
			deleteMsg:function(){
				let that = this
				uni.showModal({
					title:'提示消息',
					content:'是否要删除这条消息？',
					success:function(resp){
						if(resp.confirm){
							
							that.ajax(that.url.deleteMessageRefById,'POST',{'id':that.refId},function(resp){
								console.log('消息已经删除')
								uni.showToast({
									icon:'success',
									title:'删除成功',
									complete:function(){
										setTimeout(function(){
											uni.navigateBack({
												delta:1
											})
										})
									,1000}
								})
							})
						}
					}
				})
			}
		}
	}
</script>

<style lang="less">
	@import url('message_detail.less');
</style>
