<template>
    <div>
        <h1 style="text-align: center;">评论审核</h1>
        <div class="sear">
            <div>
                <el-input placeholder="请输入vid" class="input-with-select" v-model="page.vid" clearable>
                </el-input>
            </div>
            <div>
                <el-input placeholder="请输入uid" class="input-with-select" v-model="page.uid" clearable>
                </el-input>
            </div>
            <div>
                <el-input placeholder="请输入内容" class="input-with-select" v-model="page.text" clearable>
                </el-input>
            </div>
            <el-button slot="append" icon="el-icon-search" @click="search()" style="margin-left: 20px;"></el-button>
        </div>
        <div class="main">
            <div class="table">
                <el-table :data="comments.commentsList" :header-cell-style="{ textAlign: 'center' }"
                    :cell-style="{ textAlign: 'center', }">
                    <el-table-column prop="vid" label="视频id" width="100">
                    </el-table-column>
                    <el-table-column prop="uid" label="发布人id" width="100">
                        <template slot-scope="scope">
                            <div @click="handleSeleUId(scope.row.uid)" style="color:blue;">{{ scope.row.uid }}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="text" label="发布内容" />
                    <el-table-column prop="adhere" label="是否主评论" width="100">
                        <template slot-scope="scope">
                            <div>{{ scope.row.adhere == 0 ? '是' : '否' }}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="uptime" label="发布时间" width="200" sortable />
                    <el-table-column prop="stateName" label="当前状态" width="100" />
                    <el-table-column fixed="right" label="操作" width="200">
                        <template slot-scope="scope">
                            <el-button type="primary" @click="pass(scope.row.id)">通过</el-button>
                            <el-button type="primary" @click="notPass(scope.row.id)">未通过</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <br /><br /><br />
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
                    :current-page="page.PageNum" :page-sizes="[5, 10, 20, 50, 100]" :page-size="page.PageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="comments.commentsCount" class="page">
                </el-pagination>

            </div>
            <el-drawer title="用户详情" :visible.sync="userDrawer" direction="rtl">
                <div style="width:100%;height:100%;padding:5px">
                    <el-image
                        style="position: relative;left: 50%;transform: translateX(-50%);width: 100px; height: 100px;border-radius: 5px;"
                        :src="resUrlSplicing(user.avatar)" :preview-src-list="[resUrlSplicing(user.avatar)]">
                    </el-image>
                    <div class="row">
                        <p>uid：</p><span>{{ user.uid }}</span>
                    </div>
                    <div class="row">
                        <p>当前状态：</p><span>{{ user.state }}</span>
                    </div>
                    <div class="row">
                        <p>是否VIP:</p><span>{{ user.vip ? '是' : '否' }}</span>
                    </div>
                    <div class="row">
                        <p>用户名：</p><span>{{ user.uname }}</span>
                    </div>
                    <div class="row">
                        <p>简介：</p><span>{{ user.describes }}</span>
                    </div>
                    <div class="row">
                        <p>等级：</p><span>{{ user.level }}</span>
                    </div>
                    <div class="row">
                        <p>经验值：</p><span>{{ user.experience }}</span>
                    </div>
                    <div class="row">
                        <p>粉丝数：</p><span>{{ user.fans }}</span>
                    </div>
                    <div class="row">
                        <p>关注数：</p><span>{{ user.concerns }}</span>
                    </div>
                    <div class="row">
                        <p>最后登录时间：</p><span>{{ user.lastLogin }}</span>
                    </div>
                </div>
            </el-drawer>
            <el-drawer title="依附评论详情" :visible.sync="adhereDrawer" direction="rtl">
                <div style="width:100%;height:100%;padding:5px">

                </div>
            </el-drawer>
        </div>
    </div>
</template>

<script>
import request from '@/utils/adminRequest'
import deta from '@/utils/deta'
import global from '@/assets/global'
export default {


    data() {
        return {
            comments: {},
            page: {
                uid: '',
                vid: '',
                PageSize: 5,
                PageNum: 1,
                state: '2',
            },
            audit: [],
            userDrawer: false,
            user: {},
            adhereDrawer: false,
            adhere: {},
        }
    },
    created() {
        this.load();
    },
    methods: {
        search() {
            this.load()
        },
        pass(id) {
            this.$confirm('确认将这条评论通过审核？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                request.post("Admin/passComments/" + id).then(res => {
                    if (res.code == 200) {
                        this.$notify.success("审核成功！");
                        this.load();
                    } else {
                        this.$notify.error(res.msg)
                    }
                }).catch(e => {
                    console.log("审核失败！", e)
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消审核'
                });
            });

        },
        notPass(id) {
            this.$confirm('确认将这条评论设定为未通过审核？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                request.delete("Admin/deleteCommentsById/" + id).then(res => {
                    if (res.code == 200) {
                        this.$notify.success("审核成功！");
                        this.load();
                    } else {
                        this.$notify.error(res.msg)
                    }
                }).catch(e => {
                    console.log("审核失败！", e)
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消审核'
                });
            });

        },
        handleSizeChange(val) {
            this.page.PageSize = val
            this.load()
        },
        handleCurrentChange(val) {
            this.page.PageNum = val
            this.load()
        },
        handleSeleUId(uid) {
            if(uid==this.user.uid){
                this.userDrawer = true;
                return;
            }
            request.get("Admin/getUserInfoByUid", { params: { uid: uid } }).then(res => {
                console.log("获取用户详情成功", res);
                if (res.code == 200) {
                    this.userDrawer = true;
                    this.user = res.data
                } else {
                    this.$notify.error(res.msg);
                }

            }).catch(e => {
                console.log("获取用户详情失败", e);
            })
        },
        resUrlSplicing(src) {
            if (src) {
                return global.reshost + src;
            }
        },
        load() {
            let params = {
                uid: this.page.uid == '' ? 0 : Number(this.page.uid),
                vid: this.page.vid == '' ? 0 : Number(this.page.vid),
                text: this.page.text,
                state: this.page.state,
                PageSize: this.page.PageSize,
                PageNum: this.page.PageNum,
            }
            if (isNaN(params.uid)) {
                this.$notify.error("uid必须为数字");
                return;
            }
            if (isNaN(params.vid)) {
                this.$notify.error("uid必须为数字");
                return;
            }
            request.get("Admin/getAllCommentsListPage", { params: params }).then(res => {
                console.log("获取评论分页", res);
                if (res.code == 200) {
                    this.comments = res.data;
                } else {
                    this.$notify.error(res.msg);
                    this.$router.go(-1);
                }
            }).catch(e => {
                console.log("获取评论分页错误", e)
            });
            request.get("/Admin/getAllAudit").then(res => {
                console.log("获取审核状态成功", res);
                if (res.code == 200) {
                    this.audit = res.data
                }
            }).catch(e => {
                console.log("获取审核状态失败", e);
            })
        },
    }
}
</script>

<style  scoped>
.sear {
    margin-top: 10px;
    display: flex;
}

.sear div {
    min-width: 200px;
    margin: 0px 10px;
}

.page {
    text-align: center;
    position: fixed;
    top: 100%;
    transform: translateY(-150%);
    background-color: white;
    border: 1px solid;
    box-shadow: 0px 0px 3px;
    z-index: 100;
    border-radius: 5px;
    padding: 5px;
    margin-left: 20px;

}

.row {
    margin-top: 10px;
    border: 1px solid #888;
    padding: 10px;
    border-radius: 5px;
    background-color: #EEE;
    box-shadow: 0px 0px 2px;
    display: flex;
}

.row p {
    width: 30%;
    color: cornflowerblue;
}

.row span {
    width: 70%;
    margin-top: 4px;
}
</style>