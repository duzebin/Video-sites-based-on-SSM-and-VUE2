<template>
  <div v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(255, 255, 255, 0.8)">
    <el-page-header @back="goBack" content="详情页面" class="head"></el-page-header>
    <div class="main" v-if="loading == false">
      <el-container style="height: auto;">
        <el-main style="background-color: white;margin: 5px;padding: 15px;z-index: 2;border-radius: 5px;">
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
              :src="this.resUrlSplicing(this.video.src)" :cover="this.resUrlSplicing(this.video.cover)" primaryColor="#FB7299">
            </barrageVideoplayer>
          </div>
          <div class="video_info">
            <div class="video_operate">
              <p>
                <i class="iconfont icon-dianzan"></i>
                {{ video.like }}
              </p>
              <p>
                <i class="iconfont icon-shoucang"></i>
                {{ video.favorites }}
              </p>
            </div>
          </div>
          <div class="video_tag">
            <span v-for="(item, index) in tag" :key="index">{{ item.tag.name }}</span>
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
              <img :src="resUrlSplicing(video.userInfo.avatar)" lazy />
            </div>
            <div class="user_operate" :style="{ color: (user.uid === this.video.userInfo.uid ? '#FFB6C1' : '#000') }">
              <p class="user_name">{{
                video.userInfo.uname }}</p>
              <p class="user_describes">{{ video.userInfo.describes }}</p>
              <el-button type="primary" class="user_concern" disabled>
                <span>+关注</span>
                {{ video.userInfo.fans }}
              </el-button>
            </div>
          </div>
          <div style="">
            <img v-if="video.cover" :src="resUrlSplicing(video.cover)" style="width: 300px;height: 168px;object-fit: contain;background-color: black;margin-top: 20px;">
            <div style="text-align: center; margin-top: 10px;">
              <el-button type="primary" @click="notPass()">有违规</el-button>
            </div>
          </div>
        </el-aside>
      </el-container>
    </div>
    <el-dialog title="审核不通过原因" :visible.sync="notPassDialog">
      <el-form>
        <el-form-item label="预设原因：" style="margin-left: 5px;">
          <el-select v-model="cause" clearable placeholder="请选择">
            <el-option label="视频抽检未通过审核" value="视频抽检未通过审核"></el-option>
            <el-option label="视频抽检封面未通过审核" value="视频抽检封面未通过审核"></el-option>
            <el-option label="视频抽检标题未通过审核" value="视频抽检标题未通过审核"></el-option>
            <el-option label="视频抽检简介未通过审核" value="视频抽检简介未通过审核"></el-option>
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
import request from "@/utils/adminRequest";
import global from "@/assets/global";
import deta from "@/utils/deta";
import barrageVideoplayer from "@/components/barrage-videoplayer.vue";
import Cookies from "js-cookie";


export default {
  name: "Video",
  components: {
    barrageVideoplayer,
  },
  data() {
    return {
      synopsisState: 0,
      loading: true,
      video: {},
      tag: {},
      currentPage: 1,
      user: {},
      notPassDialog: false,
      cause: '',
    };
  },
  created() {
    this.load(this.$route.query.vid);
  },
  methods: {
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
    goBack() {
      this.$router.go(-1)
    },
    synopsis() {
      this.synopsisState = !this.synopsisState;
    },
    load(vid) {
      const userJSON = Cookies.get('user');
      if (userJSON) {
        this.user = JSON.parse(userJSON)
      }
      request.get("Admin/getVideoInfoByVid", { params: { vid: vid } }).then(res => {
        if (res.code == 200) {
          this.video = res.data;
          this.loading = false;
          let vid = { vid: this.video.vid };
          this.getVideoTagByVid();
          console.log("视频数据：", this.video);
        } else {
          this.$message.error("未找到该视频，请刷新后重试");
          this.$router.go(-1);
        }
      }).catch(e => {
        console.log("视频数据获取错误：", e);
        this.$message.error('抱歉，视频数据获取错误,请检查网络');
        this.$router.go(-1)
      });
    },
    getVideoTagByVid() {
      let videoTagConf = {
        vid: this.video.vid,
      }
      request.get("Admin/getVideoTagByVid", { params: videoTagConf }).then(res => {
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
.head {
  background-color: white;
  padding: 10px;
  box-shadow: 0 0 3px #aaa;
}

.main {
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
  background-color: white;
}

.video_synopsis {
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
</style>