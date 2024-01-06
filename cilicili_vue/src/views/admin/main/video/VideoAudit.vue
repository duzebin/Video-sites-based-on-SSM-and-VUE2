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
            <el-button slot="append" icon="el-icon-search" @click="search()"></el-button>
        </div>
        <div class="main" v-if="loading == false">
            <div style="display: flex;margin-top: 10px;">
                <div
                    style="margin:auto;background-color: white;margin: 5px;padding: 15px;z-index: 2;border-radius: 5px;width: 900px;">
                    <div class="video_info">
                        <p class="video_title">{{ video.title }}</p>
                        <div class="video_data">
                            <!--<p class="video_play"><i class="iconfont icon-bofangliang"></i>{{ video.play }}</p>-->
                            <p class="video_uptime"><i class="iconfont icon-shijianzhou"></i>{{ video.uptime }}</p>
                        </div>
                    </div>
                    <div class="video_show">
                        <barrageVideoplayer
                            style="text-align: center;position: relative;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 100%;height: 100%;"
                            :src="this.resUrlSplicing(this.video.src)" :cover="this.resUrlSplicing(this.video.cover)"
                            primaryColor="#FB7299">
                        </barrageVideoplayer>
                    </div>

                    <div class="video_tag">
                        <span v-for="(item, index) in tagList" :key="index" class="tag">
                            {{ item.tag.name }}
                        </span>
                    </div>
                    <div class="video_synopsis">
                        <p>{{ video.synopsis }}</p>
                    </div>
                </div>
                <div style="margin-top: 100px;margin-left: 20px;">
                    <img v-if="video.cover" :src="resUrlSplicing(video.cover)" class="video_cover">
                    <div style="text-align: center; margin-top: 10px;">
                        <el-button type="primary" @click="pass(video.vid)">通过审核</el-button>
                        <el-button type="primary" @click="notPass()">未通过审核</el-button>
                    </div>
                </div>
            </div>
        </div>
        <br><br><br>
        <el-pagination @current-change="handleCurrentChange" background :current-page="page.PageNum"
            :page-size="page.PageSize" layout="total, prev, pager, next" :total="videoCount" class="page">
        </el-pagination>

        <el-dialog title="审核不通过原因" :visible.sync="notPassDialog">
            <el-form>
                <el-form-item label="预设原因：" style="margin-left: 5px;">
                    <el-select v-model="cause" clearable placeholder="请选择">
                        <el-option label="视频未通过审核" value="视频未通过审核"></el-option>
                        <el-option label="视频封面未通过审核" value="视频封面未通过审核"></el-option>
                        <el-option label="视频标题未通过审核" value="视频标题未通过审核"></el-option>
                        <el-option label="视频简介未通过审核" value="视频简介未通过审核"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="自定义原因：" label-width="100px">
                    <el-input v-model="cause" placeholder="自定义未通过原因"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel()">取 消</el-button>
                <el-button type="primary" @click="confirm()">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from '@/utils/adminRequest';
import global from '@/assets/global';
import barrageVideoplayer from '@/components/barrage-videoplayer.vue';

export default {
    name: "Video",
    components: {
        barrageVideoplayer,
    },
    data() {
        return {
            synopsisState: 0,
            video: {},
            page: {
                PageSize: 1,
                PageNum: 1,
            },
            auditList: [],
            partitionList: [],
            uid: '',
            vid: '',
            state: 2,
            partition: '',
            tagList: [],
            loading: true,
            videoCount: 0,
            notPassDialog: false,
            cause: '',
        }
    },
    created() {
        this.load();
    },
    methods: {
        pass() {
            this.$confirm('确定将此视频通过审核吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                request.post("Admin/videoPass", { vid: this.video.vid }).then(res => {
                    console.log("视频审核成功", res);
                    if (res.code == 200) {
                        this.$notify.success("视频审核成功");
                        if (this.page.PageNum > 1) {
                            this.page.PageNum = this.page.PageNum - 1;
                        }
                        this.load();
                    } else {
                        this.$notify.error(res.msg);
                    }
                }).catch(e => {
                    console.log("视频审核失败", e);

                })
            }).catch(() => {

            });
        },
        confirm() {
            if (this.cause.trim() == '') {
                this.$notify.error("未通过原因不能为空");
                return;
            }
            let notPass = {
                vid: this.video.vid,
                cause: this.cause,
            };
            request.post("Admin/videoNotPass", notPass).then(res => {
                console.log("审核成功", res);
                if (res.code == 200) {
                    this.$notify.success("审核成功");
                    if (this.page.PageNum > 1) {
                        this.page.PageNum = this.page.PageNum - 1;
                    }
                    this.load();
                } else {
                    this.$notify.error(res.msg);
                }
            }).catch(e => {
                console.log("审核失败", e);
            });
            this.cancel()
        },
        cancel() {
            this.notPassDialog = false;
            this.cause = '';
        },
        notPass() {
            this.notPassDialog = true;
        },
        search() {
            this.load()
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
                    if (res.data.videoList.length == 0) {
                        this.$notify.error("当前条件下没有要审核的视频")
                        this.loading = true;
                        return;
                    }
                    this.video = res.data.videoList[0];
                    this.videoCount = res.data.videoCount;
                    this.getVideoTagByVid(this.video.vid);
                    this.loading = false;
                } else {
                    this.$notify.error(res.msg);
                    this.$router.go(-1);
                    return;
                }
            }).catch(e => {
                console.log("获取视频分页错误", e)
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
        handleCurrentChange(val) {
            this.page.PageNum = val
            this.load()
        },
        getVideoTagByVid(vid) {
            request.get("Admin/getVideoTagByVid", { params: { vid: vid } }).then(res => {
                this.tagList = res.data;
                console.log("视频标签数据:", this.tagList);
            }).catch(e => {
                this.$message.error("视频标签数据获取错误,请检查网络");
                console.log("视频标签数据获取错误:", e);
            });
        },
    }
}
</script>

<style scoped>
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

.video_title {
    font-size: 18px;
    font-weight: bold;

}

.video_data {
    display: flex;
}

.video_play,
.video_uptime {
    font-size: 17px;
    margin: 10px 5px;
}

.video_show {
    background-color: black;
    width: 848px;
    height: 477px;
    border: 1px solid black;
}



.video_tag {
    word-wrap: break-word;
    word-break: break-all;
    width: 98%;
    margin-top: 15px;
    min-height: 40px;
    border-radius: 5px;
    border: 1px solid #aaa;
}

.video_tag span {
    line-height: 40px;
    white-space: nowrap;
    margin: 0px 10px;
    padding: 5px;
    border-radius: 5px;
    font-size: 15px;
    box-shadow: 0 0 3px;
    background-color: white;
}

.video_synopsis {
    width: 98%;
    margin-top: 10px;
    border: 1px solid #aaa;
    border-radius: 5px;
    padding: 10px;
}


.video_tag p {
    background-color: #dfdfdf;
    border-radius: 20px;
    font-size: 12px;
    padding: 10px;
    flex-wrap: wrap;
    margin: 0px 5px;
}

.video_cover {
    width: 320px;
    height: 180px;
    object-fit: contain;
    background: #000;
}
</style>