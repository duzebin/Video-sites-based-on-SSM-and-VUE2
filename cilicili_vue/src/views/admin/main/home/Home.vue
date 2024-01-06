<template>
  <div style="height: 100%;min-width: 1190px;position: relative;">
    <div style="height: 40%;border-radius: 10px;display: flex;">
      <div
        style="flex-basis: 50%;padding: 10px;margin-top: 10px;border: 1px solid; width: 90%;height: 90%;box-shadow: 0 0 3px #888;border-radius: 10px;">
        <p style="font-size: 50px;text-align: center;position: relative;top: 50%;transform: translateY(-50%);">
          欢迎您{{ adminInfo.name }}<br/>
          <span style="font-size: 15px;">您的权限：{{ adminInfo.privilegesName }}</span>
        </p>
      </div>
      <div style="flex-basis:25%;display: flex;flex-direction: column; padding: 10px;">
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">待审核视频数：
              <span>{{ allVideoCount }}</span>
            </p>
          </div>
        </div>
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">待审核评论数：
              <span>{{ allCommentsCount }}</span>
            </p>
          </div>
        </div>
      </div>
      <div style="flex-basis:25%;display: flex;flex-direction: column; padding: 10px;">
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">今日待审核视频数：
              <span>{{ todayVideoCount }}</span>
            </p>
          </div>
        </div>
        <div style="width: 100%;height: 100%;flex-basis: 50%;padding: 10px;">
          <div
            style="height: 100%;width: 100%;border-radius: 10px;background-color: rgba(100, 255, 255, 0.5);border:1px solid #888;box-shadow: 0 0 3px ;">
            <p style="font-size: 25px;color:rgba(100, 100, 100, 0.8);margin-left: 20px;margin-top: 15px;">今日待审核评论数：
              <span>{{ todayCommentsCount }}</span>
            </p>
          </div>
        </div>
      </div>
    </div>
    <div style="height: 60%;background-color: white;border-radius: 10px;box-shadow: 0 0 3px;display: flex;">
      <div style="flex-basis: 40%;padding: 10px;">
        <div style="min-height: 15%;">
          <h1 style="text-align: center;position: relative;">
            <el-button type="primary" style="position: absolute;left: 0;" @click="showNoticeBox()"
              :disabled="adminInfo.privileges != 1">发布通知</el-button>
            全站通知
          </h1>
        </div>
        <div
          style="border-radius: 5px;border: 1px solid #aaa;height: 85%;width: 100%;;overflow-y: scroll;padding: 10px;box-shadow: 0 0 3px #888;"
          class="noScroll">
          <el-timeline>
            <el-timeline-item :timestamp="item.uptime" placement="top" v-for="(item, index) in notice.noticeList"
              :key="index">
              <el-card>
                <div slot="header" style="display: flex;">
                  <h2 style="word-break:break-all;word-break:break-all;flex-basis: 80%;">{{ item.title }}</h2>
                  <el-button style="padding: 3px 0;flex-basis: 20%;" type="text"
                    @click="delNotice(item.id)">删除</el-button>
                </div>
                <p style="word-break:break-all;word-break:break-all;">{{ item.msg }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-pagination @size-change="NoticeHandleSizeChange" @current-change="NoticeHandleCurrentChange"
            :current-page="this.noticePage.PageNum" :page-sizes="[5, 10, 20, 50]" :page-size="this.noticePage.PageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="this.notice.noticeCount">
          </el-pagination>
        </div>
      </div>
      <div style="flex-basis: 60%;padding: 10px;display: flex;flex-direction: column;">
        <div style="min-height: 15%;">
          <h1 style="text-align: center;position: relative;">
            <el-select v-model="timeRange" placeholder="请选择" @change="timeRangeChange"
              style="position: absolute;left: 0;">
              <el-option v-for="(item, index) in timeRangeList" :key="index" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            网站数据
          </h1>
        </div>
        <div id="line"
          style="width: 100%;height: 85%;border: 1px solid #aaa;border-radius: 5px;padding: 10px;box-shadow: 0 0 3px #888;">
        </div>
      </div>
    </div>
    <div :class="display ? '' : 'Null'"
      style="width: 100%;height: 100%;position: absolute;top:0;left:0;background-color: rgba(0, 0, 0, 0.5);border-radius: 10px;transition: .5s;">
      <el-form :model="noticeFrom"
        style="padding: 50px;position: relative;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 50%;background: white;box-shadow: 0 0 3px #aaa;border: 1px solid #888;border-radius: 5px;">
        <el-form-item label="通知标题">
          <el-input v-model="noticeFrom.title" placeholder="通知标题"></el-input>
        </el-form-item>
        <el-form-item label="通知内容">
          <el-input v-model="noticeFrom.msg" placeholder="通知内容"></el-input>
        </el-form-item>
        <el-form-item>
          <div style="display: flex;">
            <el-button type="primary" style="flex-basis: 50%;" @click="upNotice()">发布</el-button>
            <el-button type="primary" style="flex-basis: 50%;" @click="noShowNoticeBox()">取消</el-button>
          </div>

        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import global from '@/assets/global';
import request from '@/utils/adminRequest';
import judge from '@/utils/judge';
import Cookies from 'js-cookie';
import deta from '@/utils/deta';
import * as echarts from 'echarts'


export default {
  data() {
    return {
      adminInfo: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      carousel: [],
      allVideoCount: 0,
      todayVideoCount: 0,
      allCommentsCount: 0,
      todayCommentsCount: 0,
      noticePage: {
        PageNum: 1,
        PageSize: 5,
      },
      notice: {},
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
      display: false,
      noticeFrom: {
        title: '',
        msg: '',
      }
    }

  },
  mounted() {
    this.load();
  },
  created() {

  },
  methods: {
    load() {
      this.initLineBox();
      this.getAllReviewedVideoCount();
      this.getTodayVideoCount();
      this.getAllReviewedCommentsCount();
      this.getTodayCommentsCount();
      this.getNoticeListPage();
    },
    delNotice(id) {
      this.$confirm('确定删除这个通知?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete("Admin/deleteNoticeById/" + id).then(res => {
          console.log("删除通知成功", res)
          if (res.code == 200) {
            this.$notify.success("删除通知成功");
            this.getNoticeListPage();
          } else {
            this.$notify.error("删除通知失败，请检查网络");
          }
        }).catch(e => {
          console.log("删除通知失败，请检查网络", e)
        });
      }).catch(() => {
      });

      this.getNoticeListPage();
    },
    NoticeHandleSizeChange(val) {
      this.noticePage.PageSize = val;
      this.getNoticeListPage();
    },
    NoticeHandleCurrentChange(val) {
      this.noticePage.PageNum = val;
      this.getNoticeListPage();
    },
    upNotice() {
      if (this.noticeFrom.title.trim() == '') {
        this.$notify.error("通知标题不能为空");
        return;
      }
      if (this.noticeFrom.msg.trim() == '') {
        this.$notify.error("通知内容不能为空");
        return;
      }
      this.$confirm('确定提交这个通知?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post("Admin/upNotice", this.noticeFrom).then(res => {
          console.log("提交通知成功", res);
          if (res.code == 200) {
            this.$notify.success("提交通知成功");
            this.noShowNoticeBox();
            this.getNoticeListPage();
            this.noticeFrom = {
              title: '',
              msg: '',
            }
          } else {
            this.$notify.error("提交通知失败，请检查网络");
          }
        }).catch(e => {
          console.log("提交通知失败", e)
        })
      }).catch(() => {
      });

    },
    noShowNoticeBox() {
      this.display = false;
    },
    showNoticeBox() {
      this.display = true;
    },
    timeRangeChange(val) {
      this.timeRange = val
      console.log(val);
      this.initLineBox();
    },
    initLineBox() {
      if (!this.lineBox) {
        this.lineBox = echarts.init(document.getElementById('line'))
      }
      request.get("Admin/getWebStatisticsByTimeRange", { params: { 'timeRange': this.timeRange } }).then(res => {
        console.log("网站统计数据返回", res);
        if (res.code == 200) {
          const option = {
            title: {
              text: 'CiliCili数据统计'
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['视频发布数量', '评论数量', '视频审核数量']
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
                name: '评论数量',
                type: 'line',
                smooth: true,
                data: res.data.commentsUpDate
              },
              {
                name: '视频审核数量',
                type: 'line',
                smooth: true,
                data: res.data.videoAuditDate
              }
            ]
          }
          this.lineBox.setOption(option)
        }
      }).catch(e => {
        console.log("网站统计数据返回", e);
      })
    },
    getNoticeListPage() {
      request.get("Admin/getAllNoticeListPage", { params: this.noticePage }).then(res => {
        console.log("获取全站通知返回", res);
        this.notice = res.data;
      }).catch(e => {
        console.log("获取全站通知返回", e);
      })
    },
    getAllReviewedVideoCount() {
      request.get("Admin/getTheVideoForReview").then(res => {
        console.log("获取全部待审核视频数量返回", res);
        if (res.code == 200) {
          this.allVideoCount = res.data;
        }
      }).catch(e => {
        console.log("获取全部待审核视频数量返回", e);
      });
    },
    getAllReviewedCommentsCount() {
      request.get("Admin/getTheCommentsForReview").then(res => {
        console.log("获取全部待审核评论数量返回", res);
        if (res.code == 200) {
          this.allCommentsCount = res.data
        }
      }).catch(e => {
        console.log("获取全部待审核视频数量返回", e);
      });
    },
    getTodayVideoCount() {
      request.get("Admin/getTheVideoForReviewByTime", { params: { 'time': deta.parseTime(new Date().setHours(0, 0, 0, 0)) } }).then(res => {
        console.log("获取今天待审核视频数量返回", res);
        if (res.code == 200) {
          this.todayVideoCount = res.data;
        }
      }).catch(e => {
        console.log("获取今天待审核视频数量返回", e);
      });
    },
    getTodayCommentsCount() {
      request.get("Admin/getTheCommentsForReviewByTime", { params: { 'time': deta.parseTime(new Date().setHours(0, 0, 0, 0)) } }).then(res => {
        console.log("获取今天待审核评论数量返回", res);
        if (res.code == 200) {
          this.todayCommentsCount = res.data;
        }
      }).catch(e => {
        console.log("获取今天待审核视频数量返回", e);
      });
    }

  }
}


</script>

<style scoped>
.noScroll::-webkit-scrollbar {
  width: 0px;
}

.Null {
  opacity: 0;
  z-index: -1;
}
</style>