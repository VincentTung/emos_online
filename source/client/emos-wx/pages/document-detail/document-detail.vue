<template>
	<view>
		 <web-view  :src="link"></web-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				baseUrl:'',
				link:''
			}
		},
		methods: {
			
		},
		onLoad: function(option) {
			this.link=this.baseUrl+option.key
		}
	}
</script>

<style>

</style>
