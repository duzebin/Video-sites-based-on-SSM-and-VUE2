<template>
    <div style="min-width: 1500px;">
        <div class="head">
            <div class="title">
                <span>
                    CiliCili后台管理系统
                </span>
            </div>
            <div class="name">
                <span>
                    欢迎您：
                </span>
                <el-dropdown trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{ admin.name }}
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown" style="margin-top: 5px;">
                        <el-dropdown-item command="out">退出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>

        <div class="body">
            <div class="aside">
                <el-menu default-active="2" router class="el-menu-vertical-demo">
                    <el-menu-item index="/admin">
                        <i class="el-icon-menu"></i>
                        <span slot="title">首页</span>
                    </el-menu-item>
                    <el-submenu index="video" :disabled="videoDisabled">
                        <template slot="title">
                            <i class="iconfont icon-shipin icon"></i>
                            <span>视频管理</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="/admin/videoAudit">视频审核</el-menu-item>
                            <el-menu-item index="/admin/videoList">视频列表</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="comments" :disabled="commentsDisabled">
                        <template slot="title">
                            <i class="iconfont icon-pinglun icon"></i>
                            <span>评论管理</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="/admin/commentsAudit">评论审核</el-menu-item>
                            <el-menu-item index="/admin/commentsList">评论列表</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="user" :disabled="userDisabled">
                        <template slot="title">
                            <i class="iconfont icon-yonghu icon"></i>
                            <span>用户管理</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="/admin/userList">用户列表</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-submenu index="admin" :disabled="adminDisabled">
                        <template slot="title">
                            <i class="iconfont icon-guanliyuan icon"></i>
                            <span>管理员管理</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="/admin/addAdmin">管理员添加</el-menu-item>
                            <el-menu-item index="/admin/adminOperate">管理员列表</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                </el-menu>
            </div>
            <div class="main" style="min-width: 1200px;">
                <router-view style="width: 100%;height: 100%;overflow-y: auto;padding: 10px;"></router-view>
            </div>
        </div>
    </div>
</template>

<script>
import Cookies from 'js-cookie';
import judge from '@/utils/judge';

export default {
    name: "Main",
    data() {
        return {
            admin: '',
            videoDisabled: true,
            commentsDisabled: true,
            userDisabled: true,
            adminDisabled: true,
        }
    },
    created() {
        this.load();
    },
    methods: {
        load() {
            this.getAdmin();
        },
        getAdmin() {
            const adminJSON = Cookies.get('admin');
            if (!judge.judgeNull(adminJSON) && !judge.judgeUndefined(adminJSON)) {
                this.admin = JSON.parse(adminJSON);
                if(this.admin.privileges==1){
                    this.videoDisabled=false;
                    this.commentsDisabled=false;
                    this.userDisabled=false;
                    this.adminDisabled=false;
                }
                if(this.admin.privileges==2){
                    this.videoDisabled=false;
                }
                if(this.admin.privileges==3){
                    
                    this.commentsDisabled=false;
                }
                if(this.admin.privileges==4){
                    this.userDisabled=false;
                }
            } else {
                this.$notify.error("用户信息错误，请重新登录");
            }
        },
        handleCommand(command) {
            if (command === 'out') {
                Cookies.remove('admin');
                this.$router.push('/admin/login')
            }
        },

    },
}
</script>

<style scoped>
.icon {
    margin-left: 2px;
    margin-right: 8px;
    font-size: 18px;
    font-weight: bold;
}

.head {
    height: 60px;
    background: white;
    border-radius: 5px;
    box-shadow: 0 0 3px;
    display: flex;
    padding: 0px 20px;
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
    margin-top: 5px;
    height: calc(100vh - 65px);
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