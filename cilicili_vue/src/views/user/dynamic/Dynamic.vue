import { Loading } from 'element-ui';
<template>
    <div>
        <my-header></my-header>
        <div style="display: flex;width: 1200px;margin: auto;margin-top: 80px;">
            <div style="width: 400px;background-color: white;margin-right: 10px;border-radius: 10px;height: 180px;position: sticky;top: 80px;">
                <div>
                    <img :src="resUrlSplicing(userInfo.avatar)" style="width: 70px;height: 70px;border-radius: 50%;border: 1px solid #aaa;
                    box-shadow: 0 0 3px #111;margin-top: 20px;margin-left: 20px;" />
                    <span style="font-size: 30px;position: relative;top: -20px;margin-left: 20px;">{{ userInfo.uname
                    }}</span>
                </div>
                <div style="display: flex;text-align: center;margin: 20px 0px;">
                    <div style="flex-basis: 50%;">
                        {{ userInfo.concerns }}
                        <p>关注</p>
                    </div>
                    <div style="flex-basis: 50%;">
                        {{ userInfo.fans }}
                        <p>粉丝</p>
                    </div>
                </div>
            </div>
            <div style="width:800px;background-color: white;border-radius: 10px;position: relative;">
                <div v-if="JSON.stringify(video) == '{}'">
                    <h1
                        style="font-size: 50px;opacity: 0.5;position: absolute;left: 50%;top: 50%;transform: translate(-50%,-50%);">
                        暂无数据</h1>
                </div>
                <h1 v-else style="width: 100%;text-align:center;padding:10px;color:deepskyblue;">动态</h1>
                <div class="video_main">
                    <el-timeline>
                        <el-timeline-item :timestamp="item.uptime" placement="top" v-for="(item, index) in video.videoList" :key="index" style="margin-left: 50px;">
                            <div class="video_card" @click="toVideo(item.vid)">
                                <div class="user_info" style="display: flex;">
                                    <img :src="resUrlSplicing(item.userInfo.avatar)"
                                        style="width: 50px;border-radius: 50%;border: 1px solid #aaa;box-shadow: 0 0 3px #888;margin-left: 20px;" />
                                    <div
                                        style="position:relative;display: flex;flex-direction: column;margin-left: 10px;margin-top: 5px;">
                                        <span style="font-size: 20px;">{{ item.userInfo.uname }}</span>
                                        <span style="font-size: 15px;opacity: 0.5;">{{ item.userInfo.describes }}</span>
                                    </div>

                                </div>
                                <div class="video">
                                    <img :src="resUrlSplicing(item.cover)" class="video_cover">
                                    <div class="video_info">
                                        <p>{{ item.title }}</p>
                                        <p>{{ item.synopsis }}</p>
                                    </div>
                                </div>
                                <div class="video_operate">
                                    <p><i class="iconfont icon-dianzan"></i>{{ item.like }}</p>
                                    <p><i class="iconfont icon-shoucang"></i>{{ item.favorites }}</p>
                                </div>
                            </div>
                        </el-timeline-item>
                    </el-timeline>
                </div>
                <div style="text-align:center;padding:10px">
                    <el-pagination @current-change="VideohandleCurrentChange" :current-page="page.PageNum" background
                        :hide-on-single-page="JSON.stringify(video) == '{}'" :page-size="page.PageSize"
                        layout="total, prev, pager, next, jumper" :total="video.videoCount">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import myHeader from '@/components/my-header.vue'
import request from '@/utils/request';
import judge from '@/utils/judge'
import Cookies from 'js-cookie';
import global from '@/assets/global'

export default {
    components: { myHeader },
    data() {
        return {
            userInfo: '',
            video: {},
            page: {
                PageNum: 1,
                PageSize: 10,
            },
        }
    },
    created() {
        this.load();
    },
    methods: {
        load() {
            if (judge.judgeLogin()) {
                this.userInfo = JSON.parse(Cookies.get('user'));
                console.log(this.userInfo);
            } else {
                this.userInfo = '';
                this.$notify.error("请先登录");
                this.$router.go(-1);
            }
            this.getDynamicVideoInfoByUid();
        },
        toVideo(vid) {
            this.$router.push({ name: 'Video', query: { "vid": vid } }).catch(e => { });
        },
        getDynamicVideoInfoByUid() {
            request.get("Video/getConcernUserVideoInfoListPageByUid", { params: this.page }).then(res => {
                console.log("获取动态返回", res);
                if (res.code == 200) {
                    if (!judge.judgeNull(res.data)) {
                        this.video = res.data;
                    }
                }
            }).catch(e => {
                console.log("获取动态返回", e);
            })
        },
        VideohandleCurrentChange(val) {
            this.page.PageNum = val;
            this.load();
        },
        resUrlSplicing(src) {
            if (src) {
                return global.reshost + src;
            }
        },
    }
}
</script>

<style scoped>
.video_head {
    margin-left: 10px;
}

.video_card {
    position: relative;
    left: 50%;
    transform: translateX(-50%);
    padding: 10px;
    width: 80%;
    border-radius: 5px;
    background-color: #fff;
    box-shadow: 0 0 2px #aaa;
    transition: .3s;
    margin-left: -30px;
}

.video_card:hover {
    box-shadow: 0 0 5px;
}

.video_main {
    width: 100%;
}

.video {
    margin: 15px 0px;
    display: flex;
}

.video_cover {
    width: 200px;
    height: 113px;
    object-fit: contain;
    background-color: #000;
    border-radius: 5px;
    box-shadow: 0 0 3px;
    margin: 0px 20px;
    border: 1px solid #888;
}

.video_info {
    display: flex;
    flex-direction: column;
}

.video_info p {
    height: 50%;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
}

.video_info p:nth-last-child(1) {
    opacity: 0.5;
}

.video_operate {
    font-size: 20px;
    display: flex;
    margin-bottom: 5px;
}

.video_operate p {
    flex-basis: 50%;
    text-align: center;
}
</style>