<template>
  <div style="height: 100%;min-width: 1200px;">
    <div style="height: 5%;">
      <el-page-header @back="goBack" content="详情页面"
        style="position: relative;top: 50%;transform: translateY(-50%);background-color: white;padding: 10px;border-radius: 5px;border: 1px solid #888;box-shadow: 0 0 3px #aaa;">
      </el-page-header>
    </div>
    <div style="height: 40%;border-radius: 10px;display: flex;">
      <div style="flex-basis: 50%;padding: 10px;margin-top: 10px;">
        <el-carousel style="height: 100%;" direction="vertical" >
          <el-carousel-item v-for="(item, index) in carousel" :key="index">
            <el-image :src="resUrlSplicing(item.img)" fit="fill" style="cursor: pointer;">
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div style="flex-basis:25%;display: flex;flex-direction: column; padding: 10px;">
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">粉丝数：
              <span>{{ userInfo.fans }}</span>
            </p>
          </div>
        </div>
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">获得点赞数：
              <span>{{ likeCount }}</span>
            </p>
          </div>
        </div>
      </div>
      <div style="flex-basis:25%;display: flex;flex-direction: column; padding: 10px;">
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">发布视频数：
              <span>{{ uploadVideoCount }}</span>
            </p>
          </div>
        </div>
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">审核通过数：
              <span>{{ passCount }}</span>
            </p>
          </div>
        </div>
      </div>
    </div>
    <div
      style="height: 55%;background-color: white;border-radius: 10px;box-shadow: 0 0 3px;overflow: hidden;display: flex;padding: 10px;">
      <div style="flex-basis:40%;height: 100%;border-radius: 10px;border: 1px solid #aaa;box-shadow: 0 0 3px #aaa;margin-right: 20px;">
        <p style="width: 100%;position: relative;top: 50%;transform: translateY(-50%);text-align:center;font-size: 50px;">
          欢迎您 {{ userInfo.uname }}
        </p>
      </div>
      <div style="flex-basis: 60%;">
        <div style="width: 100%;height: 15%;">
          <h1 style="text-align: center;position: relative;">
            <el-select v-model="timeRange" placeholder="请选择" @change="timeRangeChange"
              style="position: absolute;left: 0;">
              <el-option v-for="(item, index) in timeRangeList" :key="index" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            网站数据
          </h1>
        </div>
        <div id="userLineBox"
          style="width: 100%;height:85%;border: 1px solid #aaa;border-radius: 5px;padding: 10px;box-shadow: 0 0 3px #888;">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import global from '@/assets/global';
import request from '@/utils/request';
import judge from '@/utils/judge';
import Cookies from 'js-cookie';
import * as echarts from 'echarts'

export default {
  data() {
    return {
      carousel: [],
      userInfo: '',
      uploadVideoCount: 0,
      likeCount: 0,
      passCount: 0,
      lineBox: null,
      timeRange: 'Week',
      timeRangeList: [
        {
          value: 'Week',
          label: '最近一周',
        },
        {
          value: 'Month1',
          label: '最近一个月',
        },
        {
          value: 'Month2',
          label: '最近两个月',
        },
        {
          value: 'Month3',
          label: '最近三个月',
        }
      ],
    }
  },
  mounted() {
    this.initLineBox();
  },
  created() {
    this.load();
  },
  methods: {
    getUserVideoAllLikeCount() {
      request.get("Video/getUserVideoAllLikeCount").then(res => {
        console.log("获取用户全部视频获得的点赞", res)
        if (res.code == 200) {
          this.likeCount = res.data;
        }
      }).catch(e => {

      });
    },
    timeRangeChange(val) {
      this.timeRange = val
      console.log(val);
      this.initLineBox();
    },
    getUploadVideoCountByUid() {
      request.get("Video/getUserAllVideoStateCount").then(res => {
        console.log("获取全部上传视频状态数量", res.data);
        this.uploadVideoCount = res.data[1] + res.data[2] + res.data[3]
        this.passCount = res.data[1];
      }).catch(e => {
        console.log("获取全部上传视频状态数量错误", e);
      });
    },
    goBack() {
      this.$router.go(-1);
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
      this.getPublicizeByType({ type: "投稿活动轮播" });
      this.getUploadVideoCountByUid();
      this.getUserVideoAllLikeCount();
    },
    getPublicizeByType(p) {
      request.get('Publicize/getPublicizeByType', { params: p }).then(res => {
        this.carousel = res.data;
        console.log(p.type, "数据：", res);
      }).catch(e => {
        console.log(p.type, "数据获取错误：", e);
      })
    },
    resUrlSplicing(src) {
      return global.reshost + src;
    },
    initLineBox() {
      if (!this.lineBox) {
        this.lineBox = echarts.init(document.getElementById('userLineBox'))
      }
      request.get("Video/getUserVideoStatisticsByTimeRange", { params: { 'timeRange': this.timeRange } }).then(res => {
        console.log("网站统计数据返回", res);
        if (res.code == 200) {
          const option = {
            title: {
              text: this.userInfo.uname
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['视频发布数量', '视频通过审核数量', '视频审核中数量', '视频未通过审核数量']
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            toolbox: {
              feature: {
                saveAsImage: {}
              }
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: res.data.date
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '视频发布数量',
                type: 'line',
                smooth: true,
                data: res.data.videoUploadData
              },
              {
                name: '视频通过审核数量',
                type: 'line',
                smooth: true,
                data: res.data.videoPassData
              },
              {
                name: '视频审核中数量',
                type: 'line',
                smooth: true,
                data: res.data.videoAuditData
              },
              {
                name: '视频未通过审核数量',
                type: 'line',
                smooth: true,
                data: res.data.videoNotPassData
              }
            ]
          }
          this.lineBox.setOption(option)
        }
      }).catch(e => {
        console.log("网站统计数据返回", e);
      })
    },
  }
}
</script>

<style scoped>
.el-carousel__item h3 {

  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}
</style>