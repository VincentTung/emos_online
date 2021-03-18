<template>
	<view>
		<uni-list>
			<uni-list-item thumb="../../static/document.png" v-for="one in fileList" :note="one.fileSize" :title="one.key"
			 :showArrow='true' clickable @click="onClick(one)">
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

				fileList: []
			}
		},
		onLoad: function() {


		},
		onShow: function() {
			let that = this
			that.ajax(that.url.documentList, 'GET', null, function(resp) {

				if (resp.data.list != null) {
					that.fileList = resp.data.list
				} 

			})
		},
		methods: {
			onClick: function(document) {
				let that = this
				// uni.navigateTo({
				// 	url: "../document-detail/document-detail?key=" + document.key
				// })
				
				
				let url = that.url.documentBaseUrl+document.key
				uni.showLoading({
					title:'加载中...'
				})
				uni.downloadFile({
									url: url,
									success: (res) => {
										
										if (res.statusCode === 200) {
											uni.hideLoading()
											uni.openDocument({
												filePath: res.tempFilePath, 
				                                // 如果文件名包含中文，建议使用escape(res.tempFilePath)转码，防止ios和安卓客户端导致的差异
												success: function(res) {
													console.log('打开文档成功');
												}
											});
										}else{
											uni.hideLoading()
										}
									},
									fail:(res)=>{
										uni.hideLoading()
									}
								});
			}
		}
	}
</script>

<style lang="less">
	@import url('document.less');
</style>
