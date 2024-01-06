<template>
    <div>
        <my-header></my-header>
        <div
            style="width: 1400px;padding:20px;margin: auto;margin-top: 70px;background: white;border: 1px solid #aaa;border-radius: 5px;box-shadow: 0 0 3px #888;">
            <h1 style="margin-bottom: 10px;"><span v-if="type != ''">关于{{ type }}下全部</span><span
                    v-if="this.$route.query.name">关于“{{ this.$route.query.name }}”</span>的搜索结果
            </h1>
            <el-tabs v-model="activeName" @tab-click="handleClick" style="min-height: 400px;">

                <el-tab-pane label="视频" name="video">
                    <h1 style="position: absolute;left: 50%;top: 50%;transform: translate(-50%,-50%);" v-if="videoNull">
                        暂无搜索结果</h1>
                    <div class="video_head" v-if="!this.$route.query.typeId">
                        <span :class="videoSort.newState ? 'chosen' : 'noChosen'" style="cursor: pointer;"
                            @click="VideoSort('New')">最新视频</span><span style="margin:0px 10px">|</span>
                        <span :class="videoSort.likeState ? 'chosen' : 'noChosen'" style="cursor: pointer;"
                            @click="VideoSort('MostLike')">最多点赞</span><span style="margin:0px 10px">|</span>
                        <span :class="videoSort.collectionState ? 'chosen' : 'noChosen'" style="cursor: pointer;"
                            @click="VideoSort('MostCollectible')">最多收藏</span>
                    </div>
                    <div class="video_frames" style="min-height:10vh;" v-loading="videoLoding" element-loading-text="拼命加载中"
                        element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)">
                        <div v-for="(item, index) in video.videoList" @click="toVideo(item.vid)" :key="index"
                            class="video_grid">
                            <div class="video">
                                <div>
                                    <el-image :src="resUrlSplicing(item.cover)" fit="contain" class="video_cover" lazy>
                                        <div slot="error" class="image-slot">
                                            <i class="el-icon-picture-outline"></i>
                                        </div>
                                    </el-image>
                                </div>
                                <div class="video_info">
                                    <p class="video_title">{{ item.title }}</p>
                                    <div class="video_author">
                                        <p class="video_author_name"><i class="iconfont icon-zuozhe"></i>
                                            {{ item.userInfo.uname }}
                                        </p>
                                        <p class="video_uptime">{{ parseTime(item.uptime, "{y}-{m}-{d}") }}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="margin-bottom: 10px;text-align: center;" v-if="!videoNull">
                        <el-pagination @size-change="VideoHandleSizeChange" @current-change="VideoHandleCurrentChange"
                            background :current-page="videoPage.PageNum" :page-sizes="[20, 50, 100, 200]"
                            :page-size="videoPage.PageSize" layout="total, sizes, prev, pager, next, jumper"
                            :total="video.videoCount">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <!--
                    <el-tab-pane label="用户" name="user">

                    </el-tab-pane>
                -->
            </el-tabs>
        </div>
    </div>
</template>

<script>
import myHeader from '@/components/my-header.vue';
import request from '@/utils/request';
import judge from '@/utils/judge';
import deta from '@/utils/deta';
import global from '@/assets/global';

export default {
    components: { myHeader },
    name: "Search",

    data() {
        return {
            activeName: 'video',
            video: [],
            videoLoding: true,
            videoPage: {
                PageNum: 1,
                PageSize: 20,
                sort: 'New',
                name: this.$route.query.name,
            },
            videoNull: false,
            videoSort: {
                newState: true,
                likeState: false,
                collectionState: false,
            },
            type: '',
            user: {},
            userPage: {
                PageNum: 1,
                PageSize: 20,
            }
        };
    },
    created() {
        this.load();
    },
    methods: {
        toVideo(index) {
            this.$router.push({ name: 'Video', query: { "vid": index } }).catch(e => { });
        },
        VideoSort(sortName) {
            this.videoPage.sort = sortName;
            if (sortName == 'New') {
                this.videoSort.newState = true;
                this.videoSort.likeState = false;
                this.videoSort.collectionState = false;
                this.videoPage.sort = 'New';
            }
            if (sortName == 'MostLike') {
                this.videoSort.newState = false;
                this.videoSort.likeState = true;
                this.videoSort.collectionState = false;
                this.videoPage.sort = 'MostLike';
            }
            if (sortName == 'MostCollectible') {
                this.videoSort.newState = false;
                this.videoSort.likeState = false;
                this.videoSort.collectionState = true;
                this.videoPage.sort = 'MostCollectible';
            }
            this.load();
        },
        handleClick(tab) {
            if (tab.label == '用户') {
                this.getUserInfoListPageBySearch();
            }
        },
        VideoHandleCurrentChange() {
            this.videoPage.PageNum = val;
            this.load();
        },
        VideoHandleSizeChange(val) {
            this.videoPage.PageSize = val;
            this.load();
        },
        load() {
            this.videoNull = false;
            if (judge.judgeUndefined(this.$route.query.name) && judge.judgeUndefined(this.$route.query.type)) {
                this.$notify.error("搜索内容不能为空");
                this.$router.go(-1);
                return;
            }
            this.videoLoding = true;
            if (!judge.judgeUndefined(this.$route.query.type)) {
                if (this.$route.query.type == 'Tag') {
                    this.getVideoInfoListPageByVideoTag();
                    this.type = ' ' + this.$route.query.typeName + ' 标签';
                    return;
                }
                if (this.$route.query.type == 'Partition') {
                    this.getVideoInfoListPageByPartition();
                    this.type = ' ' + this.$route.query.typeName + ' 分区';
                    return;
                }
            }
            this.getVideoInfoListPage();
        },
        resUrlSplicing(src) {
            return global.reshost + src;
        },
        parseTime(time, cFormat) {
            return deta.parseTime(time, cFormat);
        },
        getVideoInfoListPage() {
            request.get("Video/getVideoInfoListPage", { params: this.videoPage }).then(res => {
                console.log("获取视频列表成功", res);
                if (res.code == 200) {
                    if (res.data.videoCount == 0) {
                        this.videoLoding = false;
                        this.videoNull = true;
                        return;
                    }
                    this.video = res.data;
                    this.videoLoding = false;
                } else {
                    this.videoLoding = false;
                    this.videoNull = true;
                }
            }).catch(e => {
                console.log("获取视频列表失败", e);
            });
        },
        getVideoInfoListPageByVideoTag() {
            let params = {
                PageSize: this.videoPage.PageSize,
                PageNum: this.videoPage.PageNum,
                typeName: this.$route.query.typeId,
                sort: this.videoPage.sort,
            }
            request.get("Video/getVideoInfoListPageByVideoTag", { params: params }).then(res => {
                console.log("获取视频列表成功", res);
                if (res.code == 200) {
                    if (res.data.videoCount == 0) {
                        this.videoLoding = false;
                        this.videoNull = true;
                        return;
                    }
                    this.video = res.data;
                    this.videoLoding = false;
                } else {
                    this.videoLoding = false;
                    this.videoNull = true;
                }
            }).catch(e => {
                console.log("获取视频列表失败", e);
            });
        },
        getVideoInfoListPageByPartition() {
            let params = {
                PageSize: this.videoPage.PageSize,
                PageNum: this.videoPage.PageNum,
                typeName: this.$route.query.typeName,
                sort: this.videoPage.sort,
            }
            request.get("Video/getVideoInfoListPageByPartition", { params: params }).then(res => {
                console.log("获取视频列表成功", res);
                if (res.code == 200) {
                    if (res.data.videoCount == 0) {
                        this.videoLoding = false;
                        this.videoNull = true;
                        return;
                    }
                    this.video = res.data;
                    this.videoLoding = false;
                } else {
                    this.videoLoding = false;
                    this.videoNull = true;
                }
            }).catch(e => {
                console.log("获取视频列表失败", e);
            });
        }
    },
    watch: {
        '$route'() {
            this.$router.go(0);
        }
    },
}
</script>

<style scoped>
.chosen {
    color: deepskyblue;
}

.noChosen {
    opacity: 0.5;
}

.video_frames {
    display: flex;
    flex-wrap: wrap;
    margin: 20px 0px;
}

.video_grid {
    padding: 8px;
    flex-basis: 20%;
}

.video {
    position: relative;
    width: 100%;
    height: 100%;
    padding: 5px;
    background: white;
    border-radius: 10px;
    border: 1px solid #bababa;
    transition: all .2s;
}

.video:hover {
    box-shadow: 0px 0px 5px 1px;
}

.video_cover {
    width: 100%;
    height: 140px;
    border-radius: 10px;
    border: 1px solid #bababa;
    background-color: black;
}

.video_title {
    height: 40px;
    word-break: break-all;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    margin: 8px 0px;
    font-weight: 400;
    transition: all .3s;
}

.video_title:hover,
.video_author:hover {

    color: deepskyblue;
}

.video_author {
    align-items: flex-end;
    display: flex;
    transition: all .3s;
}

.video_author_name {
    font-weight: bold;
}

.video_uptime {
    margin-left: 10px;
    margin-top: 2px;
}
</style>