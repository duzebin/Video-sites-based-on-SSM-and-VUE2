<template>
  <div v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(255, 255, 255, 0.8)">
    <el-page-header @back="goBack" content="详情页面" class="head">
    </el-page-header>
    <span class="watermark">当前页面为模拟展示页，可能与正式显示有所出入，请以实际网页显示为准<b>对于视频信息的修改请单击要修改的地方即可进行修改</b></span>
    <div style="display: flex;margin-top: 10px;">
      <div
        style="margin:auto;background-color: white;margin: 5px;padding: 15px;z-index: 2;border-radius: 5px;width: 900px;"
        v-if="loading == false">
        <div class="video_info">
          <p class="video_title" @click="reviseTitle()">{{ video.title }}</p>
          <div class="video_data">
            <!--<p class="video_play"><i class="iconfont icon-bofangliang"></i>{{ video.play }}</p>-->
            <p class="video_uptime"><i class="iconfont icon-shijianzhou"></i>{{ video.uptime }}</p>
          </div>
        </div>
        <div class="video_show">
          <barrageVideoplayer
            style="text-align: center;position: relative;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 100%;height: 100%;"
            :src="this.resUrlSplicing(this.video.src)" :cover="this.video.cover"
            primaryColor="#FB7299">
          </barrageVideoplayer>
        </div>
        <div class="video_info">
          <div class="video_operate">
            <p class="pointer">
              <i class="iconfont icon-dianzan"></i>
              {{ video.like }}
            </p>
            <p class="pointer">
              <i class="iconfont icon-shoucang"></i>
              {{ video.favorites }}
            </p>
            <div style="display: flex;margin-left: 10px;">
              <el-input v-model="tag" maxlength="20" show-word-limit placeholder="请输入标签"></el-input>
              <el-button type="primary" style="margin-left: 20px;" @click="addTag()">
                添加标签(剩余：{{ 10 - this.tagList.length }})
              </el-button>
            </div>
          </div>
        </div>

        <div class="video_tag">
          <span v-for="(item, index) in tagList" :key="index" @click="delTag(index)" class="tag">
            {{ item.tag.name }}
          </span>
        </div>


        <div class="video_synopsis">
          <p :class="synopsisState == 0 ? 'synopsis' : ''" @click="updateSynopsid()">{{ video.synopsis }}</p>
          <el-button type="text" @click="synopsis()"
            style="position: relative;left: 100%;transform:translateX(-120%);margin: 0px;padding: 0;margin-top: 1px;">{{
              synopsisState == 1 ? '关闭' : '展开' }}</el-button>
        </div>
      </div>

      <div style="margin-left: 20px;">
        <el-upload :auto-upload="false" :show-file-list="false" :on-change="coverUploadChange" action="#"
          class="cover_upload">
          <p style="position: absolute;width: 320px;color: white;text-shadow:0 0 5px black;">
            视频封面默认为视频第一帧（可更改）
          </p>
          <img v-if="video.cover" :src="video.cover" class="video_cover">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <div style="margin-top: 20px;">
          <span>当前视频分集:</span>
          <el-select v-model="video.episode" placeholder="请选择" style="margin-left: 10px;">
            <el-option label="无" :value="0"></el-option>
            <el-option v-for="(item, index) in episodeList" :key="index" :label="item.ename" :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div style="text-align: center; margin-top: 10px;">
          <el-button type="primary" @click="load(video.vid)">取消修改</el-button>
          <el-button type="primary" @click="confirm()">确认修改</el-button>
        </div>
      </div>
    </div>
    <br><br><br>
  </div>
</template>

<script>
import request from '@/utils/request';
import barrageVideoplayer from "@/components/barrage-videoplayer.vue";
import global from '@/assets/global';
import axios from 'axios';
import Cookies from 'js-cookie';
import judge from '@/utils/judge';

export default {
  name: "VideoDetail",
  components: {
    barrageVideoplayer,
  },
  data() {
    return {
      synopsisState: 0,
      user: {},
      video: {},
      backupVideo: {},
      tagList: [],
      backupTagList: [],
      tag: '',
      loading: true,
      coverFiles: [],
      episodeList: [],
    }
  },
  created() {
    this.load(this.$route.query.vid);
  },
  methods: {
    updateSynopsid() {
      this.$prompt('请输入视频简介', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        if(value.length>2000){
          this.$notify.error("简介不能超出2000字");
          return;
        }
        this.video.synopsis = value;
      }).catch(() => {

      });
    },
    synopsis() {
      this.synopsisState = !this.synopsisState;
    },
    confirm() {
      console.log(this.video)
      if (JSON.stringify(this.video) === JSON.stringify(this.backupVideo) && JSON.stringify(this.tagList) === JSON.stringify(this.backupTagList)) {
        this.$notify.error("您貌似没有对视频进行修改！");
        return;
      }
      let videoData = new FormData();
      videoData.append("vid", this.video.vid);
      if (this.video.title !== this.backupVideo.title) {
        videoData.append("title", this.video.title);
      }
      if (this.video.synopsis !== this.backupVideo.synopsis) {
        if (this.video.synopsis === '') {
          videoData.append("synopsis", null);
        } else {
          videoData.append("synopsis", this.video.synopsis);
        }
      }
      if (this.video.episode !== this.backupVideo.episode) {
        videoData.append("episode", this.video.episode)
      }
      if (JSON.stringify(this.tagList) === '[]') {
        videoData.append("tagNameList", null)
      } else {
        let tagNameList=[];
        for(let i=0;i<this.tagList.length;i++){
          tagNameList.push(this.tagList[i].tag.name);
        }
        if (JSON.stringify(this.tagList) !== JSON.stringify(this.backupTagList)) {
          videoData.append("tagNameList", tagNameList);
        }
      }
      console.log(videoData.get('tagNameList'));
      if (this.video.cover !== this.backupVideo.cover) {
        videoData.append("file", this.coverFiles);
      }
      let config = {
        headers: {
          "Content-Type": "multipart/form-data",
          "token": this.user.token,
        }
      };
      axios.post(global.apihost + "Video/updateVideo", videoData, config).then(res => {
        console.log("修改视频返回", res.data);
        if (res.data.data == true) {
          this.$notify.success("修改视频成功，等待审核");
        } else {
          this.$notify.error("修改视频失败", res.data.msg);
        }
      }).catch(e => {
        console.log("修改视频错误返回", e);
      });
    },
    load(vid) {
      const userJSON = Cookies.get('user');
      if (judge.judgeNull(userJSON) || judge.judgeUndefined(userJSON)) {
        this.$notify.error("请先登录");
        return;
      }
      this.user = JSON.parse(userJSON);
      request.get("Video/getVideoDetailByVid", { params: { vid: vid } }).then(res => {
        console.log("视频详情获取成功", res.data);
        if (res.code == 200) {
          this.video = res.data;
          this.video.cover = this.resUrlSplicing(this.video.cover);
          this.backupVideo = JSON.parse(JSON.stringify(this.video));
          this.loading = false;
          this.getUserEpisode();
        } else {
          this.goBack();
        }
      }).catch(e => {
        console.log("视频详情获取错误", e)
      });
      request.get("Video/getVideoTagByVid", { params: { vid: vid } }).then(res => {
        this.tagList = res.data;
        this.backupTagList = JSON.parse(JSON.stringify(this.tagList));
        console.log("视频标签数据:", this.tagList);
      }).catch(e => {
        this.$message.error("视频标签数据获取错误,请检查网络");
        console.log("视频标签数据获取错误:", e);
      });
    },
    goBack() {
      this.$router.go(-1)
    },
    resUrlSplicing(src) {
      return global.reshost + src;
    },
    coverUploadChange(file, fileList) {
      this.coverFiles = file.raw;
      this.video.cover = URL.createObjectURL(file.raw);
    },
    getUserEpisode() {
      request.get('Episode/getUserAllEpisode').then(res => {
        this.episodeList = res.data;
        console.log("获取用户创建的合集", this.episodeList);
      }).catch(e => {
        console.log("获取用户创建的合集错误", e);
      });
    },
    delTag(index) {
      this.$confirm('此操作将删除这个标签, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tagList.splice(index, 1);
        this.$notify.success("删除标签成功");
      }).catch(() => {

      });
    },
    reviseTitle() {
      this.$prompt('请输入视频标题', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        if(value.length>100){
          this.$notify.error("标题不能超出100字");
          return;
        }
        if (value.trim() == '') {
          this.$notify.error("不能将标题设置为空");
          return;
        }
        this.video.title = value;
      }).catch(() => {

      });

    },
    addTag() {
      if (this.tagList.length >= 10) {
        this.$notify.error("超出视频能添加的标签个数，请删除多余标签后再尝试添加。");
        return;
      }
      let tag = {
        id: 0,
        tag: {
          id: 0,
          name: this.tag
        },
        tid: 0,
        vid: this.video.vid
      }
      this.tagList.push(tag);
      this.tag = '';
    },
  }
}
</script>

<style scoped>
.head {
  background-color: white;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 0 3px #aaa;
}

.watermark {
  position: absolute;
  left: 100%;
  top: 100%;
  transform: translate(-105%, -180%);
  text-shadow: white 1px 0 0, white 0 1px 0, white -1px 0 0, white 0 -1px 0;
  white-space: nowrap;
  z-index: 10;
  color: black;
  opacity: 0.8;
}

.pointer {
  cursor: pointer;
}

.video_title {
  font-size: 18px;
  font-weight: bold;
  transition: .5s;

}

.video_title:hover {
  color: red;
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
  margin: auto;
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
  margin: 0px 10px;
}

.video_operate i {
  font-size: 30px;
  margin: 0px 10px;
}

.video_tag {
  word-wrap: break-word;
  word-break: break-all;
  width: 98%;
  margin:10px;
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
  transition: .5s;
}

.video_tag span:hover {
  color: red;
}

.video_synopsis:hover{
  color: red;
}

.video_synopsis {
  margin: 10px;
  margin-top: 10px;
  border: 1px solid #aaa;
  border-radius: 5px;
  padding: 10px;
  transition: .5s;
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


.cover_upload {
  position: relative;
  border: 1px dashed #888;
  width: 320px;
  height: 180px;
  border-radius: 5px;
}

.cover_upload:hover {
  border: 1px dashed rgb(0, 150, 255);
}

.video_cover {
  width: 320px;
  height: 180px;
  object-fit: contain;
  background: #000;
}

.avatar-uploader-icon {
  font-size: 48px;
  color: #8c939d;
  width: 320px;
  height: 180px;
  line-height: 180px;
  text-align: center;
}
</style>