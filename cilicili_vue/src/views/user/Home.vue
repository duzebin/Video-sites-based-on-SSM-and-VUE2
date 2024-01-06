<template>
  <div id="home">
    <my-header></my-header>
    <div class="header_img">
      <img src="@/assets/header_background.png">
    </div>
    <div class="main">
      <div class="partition" style="min-height:10vh;" v-loading="partitionLoding" element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)">
        <div class="partition_left">
          <div>
            <div style="position: relative;top: 50%;transform: translateY(-50%);margin: 0 20px;" @click="toDynamic">
              <el-button type="warning" circle><i
                  class="iconfont icon-airudiantubiaohuizhi-zhuanqu_zixundongtai"></i></el-button>
              <p>动态</p>
            </div>
          </div>

        </div>
        <div class="partition_center">
          <div class="kind">
            <div v-for="(item, index) in partition.slice(0, 19)" :key="index" class="">
              <el-button type="info" v-if="index < 19" @click="toSearch(item.name)">{{ item.name }}</el-button>
            </div>
            <el-dropdown @command="handleCommand">
              <el-button type="info">
                更多
              </el-button>
              <el-dropdown-menu slot="dropdown" @command="handleCommand">
                <el-dropdown-item :command="item.name" v-for="(item, index) in partition.slice(19, partition.length)"
                  :key="index">
                  {{ item.name }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <div class="partition_right" style="display: flex;">
          <FlipClock style="transform: scale(0.7);" />
        </div>
      </div>
      <div class="carousel" style="min-height:10vh;" v-loading="carouselLoding" element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)">
        <el-carousel height="400px" :interval="4000" type="card">
          <el-carousel-item v-for="(item, index) in carousel" :key="index">
            <el-image :src="resUrlSplicing(item.img)" fit="fill" class="carousel_cover">
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="video_frames" style="min-height:10vh;" v-loading="videoLoding" element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.8)">
        <div v-for="(item, index) in video.videoList" @click="toVideo(item.vid)" :key="index" class="video_grid">
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
                <p class="video_author_name"><i class="iconfont icon-zuozhe"></i>{{ item.userInfo.uname }}</p>
                <p class="video_uptime">{{ parseTime(item.uptime, "{y}-{m}-{d}") }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div style="margin-bottom: 10px;text-align: center;position: relative;left: 50%;transform: translateX(-50%);"
        v-if="!videoLoding">
        <el-pagination @size-change="VideoHandleSizeChange" @current-change="VideoHandleCurrentChange" background
          :current-page="videoPage.PageNum" :page-sizes="[20, 50, 100, 200]" :page-size="videoPage.PageSize"
          layout="total, sizes, prev, pager, next, jumper" :total="video.videoCount">
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>

import FlipClock from 'kuan-vue-flip-clock';
import request from '@/utils/request';
import global from '@/assets/global';
import deta from '@/utils/deta';
import { all } from 'axios';

export default {
  name: "Home",
  components: {
    FlipClock,
  },
  data() {
    return {
      videoPage: {
        PageNum: 1,
        PageSize: 20,

      },
      partition: [],
      carousel: [],
      video: [],
      partitionLoding: true,
      carouselLoding: true,
      videoLoding: true,
    };
  },
  created() {
    this.load();

  },
  methods: {
    handleCommand(command) {
      this.toSearch(command);
    },
    VideoHandleSizeChange(val) {
      this.videoPage.PageSize = val;
      this.getVideo();
    },
    VideoHandleCurrentChange(val) {
      this.videoPage.PageNum = val;
      this.getVideo();
    },
    toSearch(name) {
      this.$router.push({ name: 'Search', query: { "type": 'Partition', "typeName": name } }).then(() => {
        location.reload()
      }).catch(e => { });
    },
    toDynamic() {
      this.$router.push({ name: 'Dynamic' }).catch(e => { });
    },
    load() {
      this.getVideo();
      this.getPublicizeByType({ type: "首页轮播" });
      this.getPartition();
    },
    toVideo(index) {
      this.$router.push({ name: 'Video', query: { "vid": index } }).catch(e => { });
    },
    getPartition() {
      let partition = {
        pageSize: 20,
        pageNum: 1
      }
      request.get('Partition/getPartitionList', { params: partition }).then(res => {
        this.partition = res.data;
        this.partitionLoding = false;
        console.log("获取分区数据", this.partition);
      }).catch(e => {
        console.log("获取分区数据错误", e);
      });
    },
    getVideo() {
      /*request.get('Video/getAllVideo').then(res => {
        this.video = res.data;
        this.videoLoding = false;
        console.log("视频数据：", res);
      }).catch(e => {
        console.log("视频数据获取错误：", e)
      });*/
      request.get("Video/getVideoInfoListPage", { params: this.videoPage }).then(res => {
        console.log("获取全部视频数据返回", res);
        if (res.code == 200) {
          this.video = res.data;
          this.videoLoding = false;
        } else {
          this.$notify.error("您的网络似乎存在问题，请刷新重试，或检查网络连接。")
        }
      }).catch(e => {
        console.log("获取全部视频数据返回", e);
      });
    },
    getPublicizeByType(p) {
      request.get('Publicize/getPublicizeByType', { params: p }).then(res => {
        this.carousel = res.data;
        this.carouselLoding = false;
        console.log(p.type, "数据：", res);
      }).catch(e => {
        console.log(p.type, "数据获取错误：", e);
      })
    },
    resUrlSplicing(src) {
      return global.reshost + src;
    },
    parseTime(time, cFormat) {
      return deta.parseTime(time, cFormat);
    }
  }

}
</script>
<style scoped>
.header_img {
  margin-top: 60px;
  min-width: 1400px;
  height: 150px;
  overflow: hidden;
}

.main {
  width: 1400px;
  margin: auto;
  margin-top: 20px;
  padding: 10px;
}

.partition {
  display: flex;
  text-align: center;
  flex-shrink: 0;
}

.partition_left {
  display: flex;
}

.partition_left div {
  margin: 0px 10px;
}

.partition_left i {
  font-size: 24px;
}

.partition_center {
  margin: 0px 10px;
}

.kind {
  display: flex;
  flex-wrap: wrap;
}

.kind div {
  flex-basis: 10%;
  margin: 5px 0px;
}

.carousel {
  margin: 20px 0px;
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

.el-icon-picture-outline {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 100px;
}
</style>