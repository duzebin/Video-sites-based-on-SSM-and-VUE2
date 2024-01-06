<template>
    <div>
        <h1 style="text-align: center;">视频列表</h1>
        <div class="sear">
            <div>
                <el-input placeholder="请输入vid" class="input-with-select" v-model="vid" clearable>
                </el-input>
            </div>
            <div>
                <el-input placeholder="请输入uid" class="input-with-select" v-model="uid" clearable>
                </el-input>
            </div>
            <div>
                <el-select v-model="partition" placeholder="分区" clearable>
                    <el-option v-for="(item, index) in partitionList" :label="item.name" :value="item.name"
                        :key="index"></el-option>
                </el-select>
            </div>
            <div style="margin-left: 0px;">
                <el-select v-model="state" placeholder="审核状态" clearable>
                    <el-option v-for="(item, index) in auditList" :label="item.name" :value="item.id"
                        :key="index"></el-option>
                </el-select>
            </div>
            <el-button slot="append" icon="el-icon-search" @click="search()"></el-button>
        </div>
        <div class="main">
            <div class="table">
                <el-table :data="video.videoList" :header-cell-style="{ textAlign: 'center' }"
                    :cell-style="{ textAlign: 'center', }">
                    <el-table-column prop="vid" label="视频id" width="100" sortable>
                    </el-table-column>
                    <el-table-column prop="uid" label="发布人id" width="100" sortable>
                        <template slot-scope="scope">
                            <div @click="handleSeleUId(scope.row.uid)" style="color:blue;">{{ scope.row.uid }}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="cover" label="封面" width="100">
                        <template slot-scope="scope">
                            <el-image fit="contain" style="position: relative;left: 50%;transform: translateX(-50%);"
                                :src="resUrlSplicing(scope.row.cover)" :preview-src-list="[resUrlSplicing(scope.row.cover)]"
                                class="img">
                            </el-image>
                        </template>
                    </el-table-column>
                    <el-table-column prop="title" label="标题">
                        <template slot-scope="scope">
                            <p :title="scope.row.title" class="ellipsis">
                                {{ scope.row.title }}
                            </p>
                        </template>
                    </el-table-column>
                    <el-table-column prop="favorites" label="收藏数" width="100" sortable />
                    <el-table-column prop="partition" label="分区" width="100" />
                    <el-table-column prop="like" label="被赞数" width="100" sortable />
                    <el-table-column prop="uptime" label="发布时间" width="200" sortable />
                    <el-table-column prop="stateName" label="当前状态" width="100" />
                    <el-table-column fixed="right" label="操作" width="200">
                        <template slot-scope="scope">
                            <el-button type="primary" @click="detail(scope.row.vid)">详细信息</el-button>
                            <el-button type="primary" @click="del(scope.row.vid)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <br /><br /><br />
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
                    :current-page="page.PageNum" :page-sizes="[5, 10, 20, 50, 100]" :page-size="page.PageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="video.videoCount" class="page">
                </el-pagination>

            </div>
            <el-drawer title="用户详情" :visible.sync="userDrawer" direction="rtl">
                <div style="width:100%;height:100%;padding:5px">
                    <el-image
                        style="position: relative;left: 50%;transform: translateX(-50%);width: 100px; height: 100px;border-radius: 5px;border: 1px solid #888;"
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
        </div>
    </div>
</template>

<script>
import request from '@/utils/adminRequest';
import global from '@/assets/global';

export default {
    data() {
        return {
            video: {},
            page: {
                PageSize: 5,
                PageNum: 1,
            },
            auditList: [],
            partitionList: [],
            userDrawer: false,
            user: {},
            uid: '',
            vid: '',
            state: '',
            partition: '',
        }
    },
    created() {
        this.load();
    },
    methods: {
        search() {
            this.load()
        },
        detail(vid) {
            this.$router.push({ name: 'AdminVideoDetail', query: { "vid": vid } }).catch(e => { });
        },
        del(vid) {
            this.$confirm('此操作将永久删除该视频, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                request.delete("Admin/deleteVideoByVid/" + vid).then(res => {
                    if (res.code == 200) {
                        this.$notify.success("删除成功！");
                        this.load();
                    } else {
                        this.$notify.error(res.msg)
                    }
                }).catch(e => {
                    console.log("删除视频失败", e)
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        load() {
            let params = {
                uid: this.uid,
                vid: this.vid,
                state: this.state,
                partition: this.partition,
                PageSize: this.page.PageSize,
                PageNum: this.page.PageNum,
            }
            request.get("Admin/getAllVideoListPage", { params: params }).then(res => {
                console.log("获取视频分页", res);
                if (res.code == 200) {
                    this.video = res.data;
                } else {
                    this.$notify.error(res.msg);
                    this.$router.go(-1);

                    return;
                }
            }).catch(e => {
                console.log("获取评论分页错误", e)
            });
            request.get("/Admin/getAllAudit").then(res => {
                console.log("获取审核状态成功", res);
                if (res.code == 200) {
                    this.auditList = res.data
                }
            }).catch(e => {
                console.log("获取审核状态失败", e);
            });
            request.get("/Admin/getAllPartition").then(res => {
                console.log("获取分区成功", res);
                if (res.code == 200) {
                    this.partitionList = res.data
                }
            }).catch(e => {
                console.log("获取审核状态失败", e);
            });
        },
        resUrlSplicing(src) {
            if (src) {
                return global.reshost + src;
            }
        },
        handleSeleUId(uid) {
            if (uid == this.user.uid) {
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
        handleSizeChange(val) {
            this.page.PageSize = val
            this.load()
        },
        handleCurrentChange(val) {
            this.page.PageNum = val
            this.load()
        },
    }
}
</script>

<style scoped>
.img {
    height: 57px;
    width: 100px;
    background-color: black;
}

.ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
}

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