<template>
  <div class="body" id="video" style="min-height:100vh;" v-loading="loading" element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)">
    <my-header></my-header>
    <div class="main" v-if="loading == false">
      <el-container style="height: auto;">
        <el-main style="background-color: white;margin: 5px;padding: 15px;z-index: 2;border-radius: 5px;">
          <div class="video_info">
            <p class="video_title">{{ this.video.episode != 0 ? episode.ename : video.title }}</p>
            <div class="video_data">
              <!--<p class="video_play"><i class="iconfont icon-bofangliang"></i>{{ video.play }}</p>-->
              <p class="video_uptime"><i class="iconfont icon-shijianzhou"></i>{{ video.uptime }}</p>
            </div>
          </div>
          <div class="video_show">
            <barrageVideoplayer
              style="text-align: center;position: relative;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 100%;height: 100%;"
              :src="this.resUrlSplicing(this.video.src)" :cover="this.resUrlSplicing(this.video.cover)"  primaryColor="#FB7299">
            </barrageVideoplayer>
          </div>
          <div class="video_info">
            <div class="video_operate">
              <p @click="videoLikeOperate()" class="pointer" :style="{ color: (this.videoLike ? '#ffcc00' : 'black') }">
                <i class="iconfont icon-dianzan"></i>
                {{ video.like }}
              </p>
              <p @click="favoritesOperate()" class="pointer" :style="{ color: (this.favorites ? '#ffcc00' : 'black') }">
                <i class="iconfont icon-shoucang"></i>
                {{ video.favorites }}
              </p>
            </div>
          </div>
          <div class="video_tag">
            <span v-for="(item, index) in tag" :key="index" @click="toSearch(item.tag.id,item.tag.name)">{{ item.tag.name }}</span>
          </div>
          <div class="video_synopsis">
            <p :class="synopsisState == 0 ? 'synopsis' : ''">{{ video.synopsis }}</p>
            <el-button type="text" @click="synopsis()"
              style="position: relative;left: 100%;transform:translateX(-120%);margin: 0px;padding: 0;margin-top: 1px;">{{
                synopsisState == 1 ? '关闭' : '展开' }}</el-button>
          </div>
        </el-main>
        <el-aside width="300px" style="background-color: white;margin: 5px;border-radius: 5px;">
          <div class="user_info">
            <div class="user_avatar">
              <img :src="resUrlSplicing(video.userInfo.avatar)" lazy style="width: 75px;height: 75px;"/>
            </div>
            <div class="user_operate" :style="{ color: (user.uid === this.video.userInfo.uid ? '#FFB6C1' : '#000') }">
              <p class="user_name">{{
                video.userInfo.uname }}</p>
              <p class="user_describes">{{ video.userInfo.describes }}</p>
              <el-button type="primary" class="user_concern" @click="concernOperate()">
                <span v-if="!concern">+关注</span>
                <span v-else>已关注</span>
                {{ video.userInfo.fans }}
              </el-button>
            </div>
          </div>
          <div class="episode" v-if="video.episode != 0">
            <div class="episode_head">
              <p>视频选集({{ episodeList.length }})</p>
            </div>
            <div class="episode_list">
              <div class="episode_options" v-for="(item, index) in episodeList" :title="item.title" :key="index"
                @click="load(item.vid)">
                <i v-if="item.vid == video.vid" class="el-icon-view" style="margin-right:10px;color:dodgerblue;"></i>
                <p>P{{ index }}</p>
                <p class="text">{{ item.title }}</p>
              </div>
            </div>
          </div>
        </el-aside>
      </el-container>
      <div class="video_comments">
        <div class="comments_title">
          <p style="font-size: 18px;font-weight: bold;">评论<span
              style="font-size: 13px; margin-left: 5px; color: #aaaaaa;">{{ this.comments.commentsCount }}</span></p>
          <div style="margin-left: 10px;position: relative;bottom: 5px;"><el-button type="text"
              :style="'color: black;margin:0;' + commentsHot" @click="sort('Hot')">热门</el-button>丨<el-button type="text"
              :style="'color: black;margin:0;' + commentsNew" @click="sort('New')">最新</el-button></div>
        </div>
        <div class="comments_head">
          <el-input style="margin: 10px;" placeholder="输入一条友善的评论吧" v-model="mainCommentsInput" clearable></el-input>
          <el-button style="margin: 10px;" type="primary" @click="mainCommentsAdd()">发布</el-button>
        </div>
        <div class="comments_list">
          <div class="main_comments" v-for="(item, index) in comments.mainCommentsList" :key="index">
            <div class="comments">
              <div class="comments_user_avatar">
                <el-image class="comments_user_img" :src="resUrlSplicing(item.userInfo.avatar)" lazy>
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
              <div class="coments_content">
                <div class="comments_user_name">
                  <span :style="{ color: (user.uid === item.userInfo.uid ? '#FFB6C1' : '#000') }">
                    {{ item.userInfo.uname }}
                  </span>
                  <i :class="'iconfont icon-level' + item.userInfo.level"></i>
                </div>
                <div class="comments_msg ">
                  <div>
                    {{ item.text }}
                  </div>
                  <div class="comments_operate">
                    <div class="comments_uptime">{{ item.uptime }}</div>
                    <div class="comments_like" :style="{ color: (item.likeState ? '#ffcc00' : '#000') }"
                      @click="commentLikeOperate(item.id, index)">
                      <i class="iconfont icon-dianzan"></i>
                      {{ item.like }}
                    </div>
                    <div class="comments_Reply" @click="replyShow(index)">回复</div>
                    <div class="comments_del" v-if="user.uid == item.userInfo.uid || user.uid == video.userInfo.uid">
                      <el-popconfirm title="确定删除这条评论吗？" @confirm="commentsDel(item.id, 0)" icon="el-icon-info"
                        icon-color="red">
                        <el-button slot="reference" type="text"
                          style="margin: 0px;padding: 0px;margin-top: 4px;">删除</el-button>
                      </el-popconfirm>
                    </div>
                  </div>
                  <div v-if="item.reply">
                    <div style="display: flex;margin:10px 0px;">
                      <el-input placeholder="输入一条友善的回复吧" v-model="childCommentsInput" clearable></el-input>
                      <el-button style="margin-left: 10px;" type="primary"
                        @click="childCommentsAdd(item.id, index)">回复</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="child_comments" v-for="(item1, j) in item.childCommentsList" :key="j">
              <div class="comments">
                <div class="child_comments_user_avatar">
                  <el-image class="child_comments_user_img" :src="resUrlSplicing(item1.userInfo.avatar)" lazy>
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </div>
                <div class="child_coments_content">
                  <div class="child_comments_user_name">
                    <span :style="{ color: (user.uid === item1.userInfo.uid ? '#FFB6C1' : '#000') }">
                      {{ item1.userInfo.uname }}
                    </span>
                    <i :class="'iconfont icon-level' + item1.userInfo.level"></i>
                  </div>
                  <div class="child_comments_msg ">
                    <div>
                      {{ item1.text }}
                    </div>
                    <div class="comments_operate">
                      <div class="comments_uptime">{{ item1.uptime }}</div>
                      <div class="comments_like" :style="{ color: (item1.likeState ? '#ffcc00' : '#000') }"
                        @click="commentLikeOperate(item1.id, index, j)">
                        <i class="iconfont icon-dianzan"></i>
                        {{ item1.like }}
                      </div>
                      <div class="comments_del" v-if="user.uid == item1.userInfo.uid || user.uid == video.userInfo.uid">
                        <el-popconfirm title="确定删除这条评论吗？" @confirm="commentsDel(item1.id, 1, index)" icon="el-icon-info"
                          icon-color="red">
                          <el-button slot="reference" type="text"
                            style="margin: 0px;padding: 0px;margin-top: 4px;">删除</el-button>
                        </el-popconfirm>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <el-pagination layout="prev, pager, next" :page-size="commentsConf.childCommentsPageSize"
                :total="item.childCommentsCount" @current-change="(val) => getChildCommentsPageList(index, val)"
                hide-on-single-page>
              </el-pagination>
            </div>

          </div>
          <div>
            <el-pagination style="text-align: center;margin-top: 10px;" background
              :page-size="commentsConf.mainCommentsPageSize" layout="prev, pager, next, jumper"
              :total="comments.mainCommentsCount" @current-change="getMainCommentsPageList">
            </el-pagination>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script>
import request from "@/utils/request";
import global from "@/assets/global";
import deta from "@/utils/deta";
import barrageVideoplayer from "@/components/barrage-videoplayer.vue";
import Cookies from "js-cookie";
import judge from '@/utils/judge'


export default {
  name: "Video",
  components: {
    barrageVideoplayer,
  },
  data() {
    return {
      synopsisState: 0,
      commentsConf: {
        mainCommentsPageSize: 10,
        childCommentsPageSize: 5,
        sort: 'Hot',
      },
      loading: true,
      video: {},
      favorites: false,
      videoLike: false,
      concern: false,
      episodeList: {},
      episode: {},
      tag: {},
      comments: {},
      mainCommentsInput: '',
      childCommentsInput: '',
      commentsHot: '',
      commentsNew: 'opacity:0.2 ;',
      currentPage: 1,
      user: {},
    };
  },
  created() {
    this.load(this.$route.query.vid);
  },
  methods: {
    toSearch(id,name){
      this.$router.push({ name: 'Search', query: { 'type':'Tag','typeId':id,'typeName':name } }).catch();
    },
    getConcernState() {
      if (this.user.uid == this.video.userInfo.uid) {
        this.concern = false;
        return;
      }
      let concernOperate = {
        uid: this.video.userInfo.uid,
      };
      request.get("User/getConcernsStateByUid", { params: concernOperate }).then(res => {
        if (res.code == 200) {
          this.concern = res.data;
        }
        console.log("关注信息:", res)
      }).catch(e => {
        console.log("关注信息错误:", e)
      });
    },
    synopsis() {
      this.synopsisState = !this.synopsisState;
    },
    concernOperate() {
      if (this.user.uid == this.video.userInfo.uid) {
        this.$message({
          showClose: true,
          message: '不能关注自己',
          type: 'error'
        });
        return;
      }
      let concernOperate = {
        uid: this.video.userInfo.uid,
        type: ''
      };
      if (this.concern) {
        concernOperate.type = 'cancel';
      } else {
        concernOperate.type = 'add';
      }
      request.post("User/updateConcernByConcernsOperate", concernOperate).then(res => {
        if (res.code == 200) {
          this.concern = !this.concern;
          if (this.concern) {
            this.$notify.success("关注成功");
            this.video.userInfo.fans++;
          } else {
            this.$notify.success("取消关注成功");
            this.video.userInfo.fans--;
          }
        } else {
          this.$notify.error(res.msg);
        }
        console.log("关注信息返回:", res)
      }).catch(e => {
        console.log("关注信息错误:", e)
      });

    },
    commentLikeOperate(id, i, j) {
      if (judge.judgeEmptyCollection(this.user)) {
        this.$notify.warning("请先登录！");
        return;
      }
      let CommentsLikeOperate = {
        cid: id,
        type: '',
      }
      let likeState = false;
      if (judge.judgeUndefined(j) || judge.judgeNull(j)) {
        likeState = this.comments.mainCommentsList[i].likeState;
      } else {
        likeState = this.comments.mainCommentsList[i].childCommentsList[j].likeState;
      };
      if (likeState) {
        CommentsLikeOperate.type = 'cancel';
      } else {
        CommentsLikeOperate.type = 'add';
      };
      request.post("Comments/updateCommentsLikeByCommentsLikeOperate", CommentsLikeOperate).then(res => {
        console.log(res);
        if (res.code == 200) {
          if (judge.judgeUndefined(j) || judge.judgeNull(j)) {
            this.comments.mainCommentsList[i].likeState = !this.comments.mainCommentsList[i].likeState;
            if (!likeState) {
              this.comments.mainCommentsList[i].like++;
            } else {
              this.comments.mainCommentsList[i].like--;
            }
          } else {
            this.comments.mainCommentsList[i].childCommentsList[j].likeState = !this.comments.mainCommentsList[i].childCommentsList[j].likeState;
            if (!likeState) {
              this.comments.mainCommentsList[i].childCommentsList[j].like++;
            } else {
              this.comments.mainCommentsList[i].childCommentsList[j].like--;
            }
          };
          if (!likeState) {
            this.$notify.success("点赞成功");
          } else {
            this.$notify.success("取消点赞成功");
          }
        } else {
          this.$notify.error(res.msg);
        }
      }).catch(e => {
        console.log("点赞失败", e);
      });
    },
    getCommentsLikeState(p) {
      request.post("Comments/getCommentsListLikeStateByCommentsList", p).then(res => {
        console.log("获取评论点赞数据", res)
        this.comments.mainCommentsList.forEach(function (item, index, array) {
          if (p.length > 1 || !judge.judgeNull(p[0].cid)) {
            array[index].likeState = res.data[index].mainCommentsLikeState;
          }
          array[index].childCommentsList.forEach(function (item1, index1, array1) {
            array1[index1].likeState = res.data[index].childCommentsLikeState[index1];
          });
        });
        this.$forceUpdate();
      }).catch(e => {
        console.log("获取评论点赞数据错误", e)
      })
      console.log("CommentsList数据", p);
    },
    videoLikeOperate() {
      if (judge.judgeEmptyCollection(this.user)) {
        this.$notify.warning("请先登录！");
        return;
      }
      let VideoLikeOperate = {
        vid: this.video.vid,
        type: '',
      }
      if (this.videoLike) {
        VideoLikeOperate.type = 'cancel';
      } else {
        VideoLikeOperate.type = 'add';
      }
      request.post("Video/updateVideoLikeByVideoLikeOperate", VideoLikeOperate).then(res => {
        console.log("点赞操作返回", res);
        if (res.code == 200) {
          this.videoLike = !this.videoLike;
          if (this.videoLike) {
            this.$notify.success("点赞成功");
            this.video.like++;
          } else {
            this.$notify.success("取消点赞成功");
            this.video.like--;
          }
        } else {
          this.$notify.error(res.msg);
        }
      }).catch(e => {
        console.log("收藏失败", e);
      })
    },
    getVideoLikeState() {
      request.get("Video/getVideoLikeStateByVid", { params: { vid: this.video.vid } }).then(res => {
        if (res.code == 200) {
          this.videoLike = res.data;
        }
        console.log("获取用户视频点赞状态", res)
      }).catch(e => {
        console.log("获取用户点赞")
      })
    },
    getFavoritesState() {
      request.get("Video/getVideoFavoritesStateByVid", { params: { vid: this.video.vid } }).then(res => {
        if (res.code == 200) {
          this.favorites = res.data;
        } else {
          this.$notify.error(res.msg)
        }
        console.log("获取收藏数据", res);
      }).catch(e => {
        console.log("获取收藏数据错误", e);
      })
    },
    favoritesOperate() {
      if (judge.judgeEmptyCollection(this.user)) {
        this.$notify.warning("请先登录！");
        return;
      }
      let favoritesOperate = {
        vid: this.video.vid,
        type: '',
      }
      if (this.favorites) {
        favoritesOperate.type = 'cancel';
      } else {
        favoritesOperate.type = 'add';
      }
      request.post("Video/updateVideoFavoritesByFavoritesOperate", favoritesOperate).then(res => {
        console.log("收藏操作返回", res);
        if (res.code == 200) {
          this.favorites = !this.favorites;
          if (this.favorites) {
            this.$notify.success("收藏成功");
            this.video.favorites++;
          } else {
            this.$notify.success("取消收藏成功");
            this.video.favorites--;
          }
        } else {
          this.$notify.error(res.msg);
        }
      }).catch(e => {
        console.log("收藏失败", e);
      });
    },
    commentsDel(id, type, index) {
      request.delete("Comments/deleteCommentsById/" + id).then(res => {
        if (res.code == '200') {
          this.$notify.success("删除成功！");
        }
        if (type == 0) {
          this.getMainCommentsPageList(1);
        } else {
          this.getChildCommentsPageList(index, 1);
          this.comments.commentsCount--;
        }
        console.log("删除评论成功", res)
      }).catch(e => {
        console.log("删除评论失败！", e);
      });
    },
    sort(x) {
      if (x === 'New') {
        this.commentsConf.sort = 'New';
        this.commentsNew = '';
        this.commentsHot = 'opacity:0.2 ;';
      } else {
        this.commentsConf.sort = 'Hot';
        this.commentsNew = 'opacity:0.2 ;';
        this.commentsHot = '';
      }
      this.getMainCommentsPageList(this.currentPage);
    },
    replyShow(index) {
      this.childCommentsInput = '';
      if (this.comments.mainCommentsList[index].reply == true) {
        this.comments.mainCommentsList[index].reply = false;
        this.$forceUpdate();
        return;
      }
      for (var i = 0; i < this.comments.mainCommentsList.length; i++) {
        this.comments.mainCommentsList[i].reply = false;
      }
      this.comments.mainCommentsList[index].reply = true;
      this.$forceUpdate();
    },
    childCommentsAdd(id, index) {
      let Comments = {
        text: this.childCommentsInput,
        adhere: id,
      };
      this.addCommentsByComments(Comments, index);
      this.childCommentsInput = '';
      this.comments.commentsCount++;
    },
    mainCommentsAdd() {
      let Comments = {
        text: this.mainCommentsInput,
        adhere: null,
      };
      this.addCommentsByComments(Comments);
      this.mainCommentsInput = '';
    },
    load(vid) {
      const userJSON = Cookies.get('user');
      if (userJSON) {
        this.user = JSON.parse(userJSON)
      }
      request.get("Video/getVideoByVid", { params: { vid: vid } }).then(res => {
        if (res.code == 200) {
          this.video = res.data;
          this.loading = false;
          let vid = { vid: this.video.vid };
          this.getVideoTagByVid();
          this.getMainCommentsPageList(1);
          if (!judge.judgeEmptyCollection(this.user)) {
            this.getFavoritesState();
            this.getVideoLikeState();
            this.getConcernState();
          }
          if (this.video.episode != 0) {
            this.getEpisodeListByEpisodeId(this.video.episode);
            this.getEpisodeInfoByEpisodeId(this.video.episode);
          }
          console.log("视频数据：", this.video);
        } else {
          this.$message.error("未找到该视频，请刷新后重试");
          this.$router.push('/').catch();
        }
      }).catch(e => {
        console.log("视频数据获取错误：", e);
        this.$message.error('抱歉，视频数据获取错误,请检查网络');
        this.$router.push('/').catch();
      });
    },
    getEpisodeListByEpisodeId(episode) {
      request.get("Video/getEpisodeListByEpisodeId", { params: { episodeId: episode } }).then(res => {
        this.episodeList = res.data;
        for (var i = 0; i < this.episodeList.length; i++) {
          this.episodeList[i].userInfo = this.video.userInfo;
        }
        console.log("获取分集数据成功", this.episodeList);
      }).catch(e => {
        this.$message.error('抱歉，视频分集数据获取错误,请检查网络');
        console.log("获取分集数据错误", e);
      });
    },
    getEpisodeInfoByEpisodeId(episode) {
      request.get("Episode/getEpisodeInfoByEpisodeId", { params: { episodeId: episode } }).then(res => {
        this.episode = res.data;
        console.log("获取视频分集信息：", this.episode);
      }).catch(e => {
        this.$message.error('抱歉，视频分集信息获取错误,请检查网络');
        console.log("获取视频分集信息错误：", e);
      });
    },
    getChildCommentsPageList(mainCommentsId, val) {
      let childCommentsPageConf = {
        pageNum: val,
        pageSize: this.commentsConf.childCommentsPageSize,
        adhereId: this.comments.mainCommentsList[mainCommentsId].id
      };
      request.get("Comments/getChildCommentsPageList", { params: childCommentsPageConf }).then(res => {
        this.comments.mainCommentsList[mainCommentsId].childCommentsList = res.data.childCommentsList;
        this.comments.mainCommentsList[mainCommentsId].childCommentsCount = res.data.childCommentsCount;
        let CommentsList = [];
        let comments = { cid: null };
        let childCommentsList = [];
        this.comments.mainCommentsList[mainCommentsId].childCommentsList.forEach(function (item, index, array) {
          childCommentsList.push(item.id);
        })
        comments.childCommentsList = childCommentsList;
        CommentsList.push(comments);
        if (!judge.judgeEmptyCollection(this.user)) {
          this.getCommentsLikeState(CommentsList);
        }
        console.log("获取子评论数据：", this.comments.mainCommentsList[mainCommentsId].childCommentsList);
      }).catch(e => {
        this.$message.error('抱歉，获取子评论数据错误,请检查网络');
        console.log("获取子评论数据错误：", e);
      });
    },
    getMainCommentsPageList(pageNum) {
      this.currentPage = pageNum;
      let mainCommentsPageConf = {
        pageNum: pageNum,
        pageSize: this.commentsConf.mainCommentsPageSize,
        childPageSize: this.commentsConf.childCommentsPageSize,
        vid: this.video.vid,
        sort: this.commentsConf.sort,
      };
      request.get("Comments/getMainCommentsPageList", { params: mainCommentsPageConf }).then(res => {
        this.comments = res.data;
        let CommentsList = [];
        this.comments.mainCommentsList.forEach(function (item, index, array) {
          array[index].reply = false;
          array[index].likeState = false;
          let comments = {};
          comments.cid = item.id;
          let childCommentsList = [];
          array[index].childCommentsList.forEach(function (item1, index1) {
            childCommentsList.push(item1.id);
          });
          comments.childCommentsList = childCommentsList;
          CommentsList.push(comments);
        });
        if (!judge.judgeEmptyCollection(this.user)) {
          this.getCommentsLikeState(CommentsList);
        }
        console.log("第", pageNum, "页评论数据:", this.comments);
      }).catch(e => {
        this.$message.error("获取第", pageNum, "页评论数据错误,请检查网络");
        console.log("获取第", pageNum, "页评论数据错误:", e);
      });
    },
    addCommentsByComments(p, index) {
      if (judge.judgeEmptyCollection(this.user)) {
        this.$notify.warning("请先登录！");
        return;
      }
      if (p.text === '') {
        this.$notify.warning("不能发送空信息");
        return;
      }
      p.vid = this.video.vid;
      p.uptime = deta.getTimes();
      request.post("Comments/insertCommentsByComments", p).then(res => {
        console.log(res);
        if (res.data === 1) {
          this.$notify.success("评论成功");
          /*if (judge.judgeUndefined(index)) {
            this.getMainCommentsPageList(this.currentPage)
          } else {
            this.getChildCommentsPageList(index, 1)
          }*/
        } else {
          this.$notify.error(res.msg);
        }
      }).catch(e => {
        console.log("评论失败", e);
      });
    },
    getVideoTagByVid() {
      let videoTagConf = {
        vid: this.video.vid,
      }
      request.get("Video/getVideoTagByVid", { params: videoTagConf }).then(res => {
        this.tag = res.data;
        console.log("视频标签数据:", this.tag);
      }).catch(e => {
        this.$message.error("视频标签数据获取错误,请检查网络");
        console.log("视频标签数据获取错误:", e);
      });
    },
    parseVideoTime(time) {
      return deta.parseVideoTime(time)
    },
    resUrlSplicing(src) {
      return global.reshost + src;
    },
  }
};
</script>

<style scoped>
.pointer {
  cursor: pointer;
}

.main {
  position: relative;
  top: 75px;
  width: 1200px;
  margin: auto;
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

.video_operate {
  font-size: 15px;
  display: flex;
  box-shadow: 0 0 3px 0px;
  padding: 5px;
  border-radius: 5px;
}

.video_operate i {
  font-size: 30px;
  margin: 0px 10px;
}

.video_tag {
  word-wrap: break-word;
  word-break: break-all;
  width: 100%;
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
  border: 1px solid #fff;
  background-color: white;
  transition: .5s;
}

.video_tag span:hover {
  border: 1px solid ;
  box-shadow: 0 0 5px;
  
}

.video_synopsis {
  margin-top: 10px;
  border: 1px solid #aaa;
  border-radius: 5px;
  padding: 10px;
}

.synopsis {
  width: 100%;
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}


.video_tag p {
  background-color: #dfdfdf;
  border-radius: 20px;
  font-size: 12px;
  padding: 10px;
  flex-wrap: wrap;
  margin: 0px 5px;
}

.user_info {
  display: flex;
  padding: 10px 0px;
  box-shadow: 0 0 3px 0px;
}

.user_avatar {
  width: 25%;
  
  margin: 0px 20px;
}

.user_avatar img {
  width: 100%;
  border-radius: 50%;
  position: relative;
  top: 50%;
  transform: translateY(-50%);
}

.user_operate {
  width: 60%;
  font-size: 20px;
}

.user_name {
  margin: 0;
  font-weight: bold;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  -o-text-overflow: ellipsis;
}

.user_describes {
  margin: 0;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  -o-text-overflow: ellipsis;
}

.user_concern {
  margin: 0;
  width: 80%;
  font-size: 15px;
}

.episode {
  background: #ddd;
  border-radius: 10px;
  margin: 15px;
  border: 1px solid #bbb;
  box-shadow: 0px 0px 3px 0px #5E5E5E;
}

.episode_head {
  width: 100%;
  background: white;
  box-shadow: 0px 7px 7px -7px #5E5E5E;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  background: #ddd;
  padding: 10px 0px;
  padding-left: 20px;
}

.episode_list {
  height: 350px;
  overflow-y: auto;
  padding: 0px 10px 0px 10px;
  margin: 0px 5px;
}

.episode ::-webkit-scrollbar {
  width: 0px;
}

.episode_options {
  display: flex;
  width: 100%;
  padding: 15px 15px 10px 15px;
  background: white;
  margin-top: 10px;
  border-radius: 5px;
  transition: all .2s;
}

.episode_options:hover {
  background: white;
  margin-top: 10px;
  border-radius: 10px;
  box-shadow: 0px 0px 3px 0px #5E5E5E;
}

.episode_options .text {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-left: 5px;
  margin-top: -4px;
}

.video_comments {
  width: 880px;
  background-color: white;
  margin: 5px;
  padding: 10px;
  border-radius: 5px;
}

.comments_title {
  display: flex;

}


.comments_head {
  background-color: white;
  border: 1px solid;
  display: flex;
  justify-content: center;
  position: sticky;
  top: 70px;
  width: 860px;
  z-index: 1;
  box-shadow: 0px 0px 3px 0px #5E5E5E;
  border-radius: 5px;
}

.main_comments {
  width: 100%;
  padding: 5px;
  border-bottom: 1px solid;
}

.child_comments {
  width: 95%;
  position: relative;
  left: 100%;
  transform: translateX(-100%);
  margin-top: 5px;
}

.comments {
  width: 100%;
  display: flex;
}

.comments_user_avatar {
  width: 10%;
  flex-shrink: 0;
}

.comments_user_img {
  width: 70%;
  position: relative;
  left: 50%;
  top: 10px;
  transform: translateX(-50%);
  border-radius: 50px;
  box-shadow: 0 0 3px 0px;
}

.coments_content {
  position: relative;
  width: 90%;
  margin-top: 5px;
  flex-shrink: 0;
}

.comments_user_name {
  font-size: 18px;
  font-weight: bold;
}

.comments_user_name i {
  font-size: 30px;
  font-weight: 0;
  margin-left: 10px;
}

.comments_msg {
  width: 100%;
  margin-top: 5px;
  font-size: 15px;

}

.comments_operate {
  width: 100%;
  display: flex;
  margin-top: 10px;
}

.comments_uptime {
  margin-right: 10px;
  zoom: 0.8;
  margin-top: 5px;
}

.comments_like {
  margin: 0px 5px;
  margin-top: 2px;
  cursor: pointer;
}



.comments_Reply {
  margin: 0px 5px;
  opacity: 0.5;
  transition: all .2s;
  cursor: pointer;
}

.comments_Reply:hover {
  color: dodgerblue;
  opacity: 0.9;
}

.comments_del {
  margin-left: 5px;
  opacity: 0.5;
  transition: all .2s;
  cursor: pointer;
}

.comments_del:hover {
  color: dodgerblue;
  opacity: 0.9;
}

.child_comments_user_avatar {
  width: 10%;
  flex-shrink: 0;
}

.child_comments_user_img {
  width: 60%;
  position: relative;
  left: 50%;
  top: 10px;
  transform: translateX(-50%);
  border-radius: 50px;
  box-shadow: 0 0 3px 0px;
}

.child_coments_content {
  position: relative;
  width: 90%;
  margin-top: 5px;
  flex-shrink: 0;
}

.child_comments_user_name {
  font-size: 15px;
  font-weight: bold;
}

.child_comments_user_name i {
  font-size: 20px;
  font-weight: 0;
  margin-left: 10px;
}

.child_comments_msg {
  width: 100%;
  margin-top: 5px;
  font-size: 13px;

}
</style>