<template>
    <div>
        <my-header></my-header>
        <div style="border: 1px solid  #eee;box-shadow: 0 0 3px #888;width: 1200px;margin: auto;
            border-radius: 10px;margin-top: 75px;overflow: hidden;">
            <div>
                <div class="head">
                    <el-button type="text"
                        style="position: absolute;left: 100%;transform: translateX(-130%);cursor: pointer;z-index: 999;"
                        @click="toUpdateUserInfo">修改个人信息</el-button>
                    <img src="@/assets/userHead_img.png" class="head_img">
                    <div class="user_info" style="position: absolute;top: 0;left: 0;height: 100%;width: 100%;">
                        <img :src="resUrlSplicing(userInfo.avatar)" class="user_avatar" />
                        <p class="user_name">{{ userInfo.uname }} <i
                                :class="'iconfont icon-level' + this.userInfo.level"></i>
                        </p>
                        <p class="user_describes">{{ userInfo.describes }}</p>
                        <div class="user_fans">
                            <p>关注数：{{ userInfo.concerns >= 1000 ? (userInfo.concerns / 1000).toFixed(1) :
                                userInfo.concerns }}<span v-if="userInfo.concerns >= 1000">K</span></p>
                            <p style="margin-left: 20px;">粉丝数：{{ userInfo.fans >= 1000 ? (userInfo.fans / 1000).toFixed(1) :
                                userInfo.fans }}<span v-if="userInfo.fans >= 1000">K</span></p>
                        </div>
                    </div>
                </div>
                <div style="margin-top: -5px;">
                    <el-tabs type="border-card" @tab-click="handleClick" v-model="activeName">
                        <el-tab-pane label="投稿" name="Submission">

                            <h1 style="margin:10px">我的投稿</h1>
                            <div class="video_head">
                                <span :class="videoSort.newState ? 'chosen' : 'noChosen'" style="cursor: pointer;"
                                    @click="VideoSort('New')">最新视频</span><span style="margin:0px 10px">|</span>
                                <span :class="videoSort.likeState ? 'chosen' : 'noChosen'" style="cursor: pointer;"
                                    @click="VideoSort('MostLike')">最多点赞</span><span style="margin:0px 10px">|</span>
                                <span :class="videoSort.collectionState ? 'chosen' : 'noChosen'" style="cursor: pointer;"
                                    @click="VideoSort('MostCollectible')">最多收藏</span>
                            </div>
                            <div class="video_main">
                                <div class="video_card" v-for="(item, index) in video.videoList" :key="index"
                                    @click="toVideo(item.vid)">
                                    <div class="vMain" >
                                        <div class="video">
                                            <img :src="resUrlSplicing(item.cover)" class="video_cover">
                                            <div class="video_info">
                                                <p style="width: 100%;word-break:break-all; ">{{ item.title }}</p>
                                                <p style="width: 100%;word-break:break-all; ">{{ item.synopsis }}</p>
                                            </div>
                                        </div>
                                        <div class="video_operate">
                                            <p><i class="iconfont icon-dianzan"></i>{{ item.like }}</p>
                                            <p><i class="iconfont icon-shoucang"></i>{{ item.favorites }}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <el-pagination @current-change="VideohandleCurrentChange" :current-page="videoPage.PageNum"
                                    :page-size="videoPage.PageSize" layout="total, prev, pager, next, jumper"
                                    :total="video.videoCount">
                                </el-pagination>
                            </div>

                        </el-tab-pane>
                        <el-tab-pane label="合集" name="Episode">

                            <el-table :data="episode.episodeList" border style="width: 100%"
                                :header-cell-style="{ textAlign: 'center' }" :cell-style="{ textAlign: 'center', }">
                                <el-table-column prop="crtime" label="创建时间">
                                </el-table-column>
                                <el-table-column prop="ename" label="合集名">
                                </el-table-column>
                                <el-table-column fixed="right" label="操作" width="300">
                                    <template slot-scope="scope">
                                        <el-button @click="toEpisodevideo(scope.row.id)" type="primary">查看合集视频</el-button>
                                        <el-button @click="delEpisode(scope.row.id)" type="primary">删除合集</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <div>
                                <el-pagination @current-change="EpisodehandleCurrentChange"
                                    :current-page="EpisodePage.PageNum" :page-size="EpisodePage.PageSize"
                                    layout="total, prev, pager, next, jumper" :total="episode.episodeCount">
                                </el-pagination>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="收藏" name="Collection">

                            <h1 style="margin:10px">我的收藏</h1>
                            <div class="video_main">
                                <div class="video_card" v-for="(item, index) in collection.videoList" :key="index"
                                    @click="toVideo(item.vid)">
                                    <div class="vMain">
                                        <div class="video">
                                            <img :src="resUrlSplicing(item.cover)" class="video_cover">
                                            <div class="video_info">
                                                <p style="width: 100%;word-break:break-all; ">{{ item.title }}</p>
                                                <p style="width: 100%;word-break:break-all; ">{{ item.synopsis }}</p>
                                            </div>
                                        </div>
                                        <div class="video_operate">
                                            <p><i class="iconfont icon-dianzan"></i>{{ item.like }}</p>
                                            <p><i class="iconfont icon-shoucang"></i>{{ item.favorites }}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <el-pagination @current-change="CollectionhandleCurrentChange"
                                    :current-page="collectionPage.PageNum" :page-size="collectionPage.PageSize"
                                    layout="total, prev, pager, next, jumper" :total="collection.videoCount">
                                </el-pagination>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
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
            activeName: 'Submission',
            userInfo: '',
            video: {},
            videoPage: {
                PageNum: 1,
                PageSize: 10,
                sort: 'New',
                uid: 0,
            },
            collection: {},
            collectionPage: {
                PageNum: 1,
                PageSize: 10,
                sort: 'New',
            },
            episode: {},
            EpisodePage: {
                PageNum: 1,
                PageSize: 10,
            },
            videoSort: {
                newState: true,
                likeState: false,
                collectionState: false,
            }
        };
    },
    created() {
        this.load()
    },
    methods: {
        toUpdateUserInfo() {
            this.$router.push({ name: 'UserEdit' }).catch(e => { });
        },
        delEpisode(id) {
            this.$confirm('此操作将永久删除该合集, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                request.delete("Episode/deleteEpisodeById/" + id).then(res => {
                    console.log("删除合集成功", res)
                    if (res.code == 200) {
                        this.$notify.success("删除合集成功");
                        this.getEpisodeListPage();
                    } else {
                        this.$notify.error(res.msg);
                    }
                }).catch(e => {
                    console.log("删除合集失败", e)
                });
            }).catch(() => {
            });

        },
        VideoSort(sortName) {
            this.videoPage.sort = sortName;
            if (sortName == 'New') {
                this.videoSort.newState = true;
                this.videoSort.likeState = false;
                this.videoSort.collectionState = false;
            }
            if (sortName == 'MostLike') {
                this.videoSort.newState = false;
                this.videoSort.likeState = true;
                this.videoSort.collectionState = false;
            }
            if (sortName == 'MostCollectible') {
                this.videoSort.newState = false;
                this.videoSort.likeState = false;
                this.videoSort.collectionState = true;
            }
            this.getVideoInfoListPage();
        },
        CollectionSort(sortName) {
            this.videoPage.sort = sortName;
            if (sortName == 'New') {
                this.collectionSort.newState = true;
                this.collectionSort.likeState = false;
                this.collectionSort.collectionState = false;
            }
            if (sortName == 'MostLike') {
                this.collectionSort.newState = false;
                this.collectionSort.likeState = true;
                this.collectionSort.collectionState = false;
            }
            if (sortName == 'MostCollectible') {
                this.collectionSort.newState = false;
                this.collectionSort.likeState = false;
                this.collectionSort.collectionState = true;
            }
            this.getCollectionListPage();
        },
        VideohandleCurrentChange(val) {
            this.videoPage.PageNum = val;
            this.getVideoInfoListPage();
        },
        EpisodehandleCurrentChange(val) {
            this.EpisodePage.PageNum = val;
            this.getEpisodeListPage();
        },
        CollectionhandleCurrentChange(val) {
            this.collectionPage.PageNum = val;
            this.getCollectionListPage();
        },
        load() {
            if (judge.judgeLogin()) {
                this.userInfo = JSON.parse(Cookies.get('user'));
                console.log(this.userInfo);
            } else {
                this.userInfo = '';
                this.$notify.error("请先登录");
                this.$router.go(-1);
            }
            if (!judge.judgeUndefined(this.$route.query.type)) {
                if (this.$route.query.type == 'Collection') {
                    this.activeName = 'Collection';
                    this.getCollectionListPage();
                    return;
                }

            }
            this.getVideoInfoListPage();
        },
        handleClick(tab, event) {
            if (tab.label == '投稿') {
                this.getVideoInfoListPage();
            }
            if (tab.label == '合集') {
                this.getEpisodeListPage();
            }
            if (tab.label == '收藏') {
                this.getCollectionListPage();
            }
        },
        resUrlSplicing(src) {
            if (src) {
                return global.reshost + src;
            }
        },
        getVideoInfoListPage() {
            request.get("Video/getVideoInfoListPageByUid", { params: this.videoPage }).then(res => {
                console.log("获取视频列表返回", res);
                if (res.code == 200) {
                    this.video = res.data;
                } else {
                    this.$notify.error(res.msg);
                    this.$router.go(-1);
                }
            }).catch(e => {
                console.log("获取视频列表错误", e);
            });
        },
        getEpisodeListPage() {
            request.get("Episode/getEpisodeListPage", { params: this.EpisodePage }).then(res => {
                console.log("获取合集列表返回", res)
                if (res.code == 200) {
                    this.episode = res.data;
                }
            }).catch(e => {
                console.log("获取合集列表返回", e)
            })
        },
        getCollectionListPage() {
            request.get("Video/getVideoInfoListPageByCollection", { params: this.collectionPage }).then(res => {
                console.log("获取收藏视频返回", res);
                if (res.code == 200) {
                    this.collection = res.data;
                } else {
                    this.$notify.error("当前没有收藏");
                }
            }).catch(e => {
                console.log("获取收藏视频返回", e);
            })
        },
        toVideo(vid) {
            this.$router.push({ name: 'Video', query: { "vid": vid } }).catch(e => { });
        },
        toEpisodevideo(id) {
            request.get("Video/getEpisodeFirstVideoIdByEpisodeId", { params: { id: id } }).then(res => {
                console.log("获取合集第一个视频返回", res);
                if (res.code == 200) {
                    this.toVideo(res.data);
                } else {
                    this.$notify.error("当前合集没有视频");
                }
            }).catch(e => {
                console.log("获取合集第一个视频返回", e);
            })
        }
    }
}
</script>

<style scoped>
.user_avatar {
    width: 100px;
    border-radius: 50%;
    border: 1px solid #888;
    box-shadow: 0 0 3px #222;
    position: relative;
    top: 100%;
    transform: translate(20%, -120%);
}

.user_name {
    margin-left: 140px;
    margin-top: -20px;
    color: white;
    text-shadow: #000 1px 0 0, #000 0 1px 0, #000 -1px 0 0, #000 0 -1px 0;
    font-size: 30px;
}

.user_fans {
    background-color: rgba(255, 255, 255, 0.7);
    min-width: 300px;
    height: 70px;
    position: absolute;
    top: 100%;
    left: 100%;
    transform: translate(-100%, -100%);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    padding: 0px 20px;
}

.user_describes {
    margin-left: 140px;
    margin-top: 20px;
    color: white;
    text-shadow: #000 1px 0 0, #000 0 1px 0, #000 -1px 0 0, #000 0 -1px 0;
    font-size: 15px;
    opacity: 0.8;
}

.head {
    position: relative;
}

.head_img {
    width: 100%;
}

.chosen {
    color: deepskyblue;
}

.noChosen {
    opacity: 0.5;
}

.video_head {
    margin-left: 10px;
}

.video_card {
    padding: 10px;
    width: 100%;
    flex-basis: 50%;
    flex-shrink: 0;
}


.vMain {
    padding: 10px;
    width: 100%;
    border-radius: 5px;
    background-color: #fff;
    box-shadow: 0 0 2px #aaa;
    transition: .3s;
}

.vMain:hover {
    box-shadow: 0 0 5px;
}

.video_main {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
}

.video {
    margin: 15px 0px;
    display: flex;
    flex-shrink: 0;
}

.video_cover {
    width: 200px;
    height: 113px;
    flex-shrink: 0;
    object-fit: contain;
    background-color: #000;
    border-radius: 5px;
    box-shadow: 0 0 3px;
    margin: 0px 20px;
    border: 1px solid #888;
}

.video_info {
    display: block;
}

.video_info p {
    height: 50%;
    padding-right: 50px;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
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