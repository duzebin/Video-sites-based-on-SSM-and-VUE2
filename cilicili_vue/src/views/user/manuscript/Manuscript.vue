<template>
    <div>
        <div class="head">
            <my-header></my-header>
        </div>
        <div class="body" >
            <div class="aside">
                <div style="text-align:center;margin:15px 0px;">
                    <el-button type="primary" style="font-size:18px" @click="toSubmission">
                        <i class="iconfont icon-shangchuan_huaban " style="margin-right:10px"></i>投稿
                    </el-button>
                </div>
                <el-menu default-active="2" router class="el-menu-vertical-demo" >
                    <el-menu-item index="/manuscript">
                            <i class="el-icon-menu"></i>
                            <span slot="title">首页</span>
                        </el-menu-item>
                    <el-submenu index="video">
                        <template slot="title">
                            <i class="iconfont icon-shipin icon"></i>
                            <span>内容管理</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="/manuscript/videoManage">稿件管理</el-menu-item>
                            <el-menu-item index="/manuscript/videoComplaint">申诉中心</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="comments">
                        <template slot="title">
                            <i class="iconfont icon-pinglun icon"></i>
                            <span>互动管理</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="/manuscript/commentsList">评论管理</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                </el-menu>
            </div>
            <div class="main">
                <router-view style="width: 100%;height: 100%;overflow-y: auto;background: #EEE;padding: 10px;border-radius: 10px;"></router-view>
            </div>
        </div>
    </div>
</template>

<script>
import Cookies from 'js-cookie';
import judge from '@/utils/judge';
import myHeader from '@/components/my-header.vue';

export default {
    components: { myHeader },
    name: "Manuscript",
    data() {
        return {
            
        }
    },
    created() {
       
    },
    methods: {
        toSubmission(){
            this.$router.push({name:'Submission'}).catch();
        },

    },
}
</script>

<style scoped>
.icon{
    margin-left: 2px;
    margin-right: 8px;
    font-size: 18px;
    font-weight: bold;
}
.head {
    height: 60px;
    background: white;
    display: flex;
}

.title {
    font-size: 30px;
    line-height: 60px;
    color: deepskyblue;
    text-shadow: 1px 1px 5px;
}

.name {
    font-size: 20px;
    line-height: 60px;
    margin-left: auto;
}

.body {
    margin-top: 8px;
    height: calc(100vh - 70px);
    width: 100%;
    display: flex;

}

.aside {
    width: 200px;
    background-color: white;
    box-shadow: 0px 0px 3px;
    height: 100%;
    flex-shrink: 0;
    border-radius: 5px;
    padding: 5px 0px;
}

.main {
    margin-left: 5px;
    width: calc(100% - 205px);
    height: 100%;
    background-color: white;
    box-shadow: 0px 0px 3px;
    flex-shrink: 1;
    border-radius: 5px;
    padding: 5px;
}

.el-dropdown-link {
    font-size: 20px;
    cursor: pointer;
    color: #409EFF;
}

.el-icon-arrow-down {
    font-size: 12px;
}
</style>