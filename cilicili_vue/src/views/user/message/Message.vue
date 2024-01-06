<template>
  <div style="width:100%;height:calc( 100vh - 70px) ;">
    <my-header></my-header>
    <div
      style="padding: 20px;width: 1200px;margin: auto;margin-top: 70px;min-height:100%;background-color: white;border-radius: 10px;border: 1px solid #888;box-shadow: 0 0 3px #888;">
      <el-tabs tab-position="left" style="height: 100%" @tab-click="handleClick" v-model="activeName">
        <el-tab-pane style="height: 100%;padding: 30px;" name="notice">
          <span slot="label" style="font-size: 25px;">全站通知</span>
          <el-timeline style="height: 100%;">
            <el-timeline-item :timestamp="item.uptime" placement="top" v-for="(item, index) in notice.noticeList"
              :key="index">
              <el-card>
                <h2 style="word-break:break-all;word-break:break-all;">{{ item.title }}</h2>
                <el-divider></el-divider>
                <p style="word-break:break-all;word-break:break-all;">{{ item.msg }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-pagination @size-change="NoticeHandleSizeChange" @current-change="NoticeHandleCurrentChange" style="text-align: center;"
            :current-page="this.noticePage.PageNum" :page-sizes="[5, 10, 20, 50]" :page-size="this.noticePage.PageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="this.notice.noticeCount">
          </el-pagination>
        </el-tab-pane>
        <el-tab-pane name="videoMessage">
          <span slot="label" style="font-size: 25px;" >系统通知</span>
          <el-timeline style="height: 100%;">
            <el-timeline-item :timestamp="item.time" placement="top" v-for="(item, index) in videoMessage.videoMessageList"
              :key="index">
              <el-card>
                <h2 style="word-break:break-all;word-break:break-all;">
                  您投稿的
                  <el-button type="text" @click="toVideoDetail(item.vid)">{{ item.videoTitle }}</el-button>
                  已于&nbsp;&nbsp;{{ item.time }}&nbsp;&nbsp;审核完毕<br/>审核结果：{{ item.stateName }}
                </h2>
                <el-divider v-if="item.state==3"></el-divider>
                <p style="word-break:break-all;word-break:break-all;" v-if="item.state==3">审核不通过原因:{{ item.msg }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-pagination @size-change="videoMessageHandleSizeChange" @current-change="videoMessageHandleCurrentChange" style="text-align: center;"
            :current-page="this.videoMessagePage.PageNum" :page-sizes="[5, 10, 20, 50]" :page-size="this.videoMessagePage.PageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="this.videoMessage.videoMessageCount">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import myHeader from '@/components/my-header.vue'
import request from '@/utils/request'
export default {
  components: { myHeader },
  data() {
    return {
      activeName:'notice',
      notice: {},
      noticePage: {
        PageNum: 1,
        PageSize: 5,
      },
      videoMessage:{},
      videoMessagePage: {
        PageNum: 1,
        PageSize: 5,
      },
    }
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.getNoticeListPage();
    },
    toVideoDetail(vid){
      this.$router.push({ name: 'VideoDetail', query: { "vid": vid } }).catch();
    },
    handleClick(tab,x){
      if(tab.name=='notice'){
        this.getNoticeListPage();
      }
      if(tab.name=='videoMessage'){
        this.getVideoMessageListPage();
      }
    },
    videoMessageHandleSizeChange(val) {
      this.videoMessagePage.PageSize = val;
      this.getVideoMessageListPage();
    },
    videoMessageHandleCurrentChange(val) {
      this.videoMessagePage.PageNum = val;
      this.getVideoMessageListPage();
    },
    NoticeHandleSizeChange(val) {
      this.noticePage.PageSize = val;
      this.getNoticeListPage();
    },
    NoticeHandleCurrentChange(val) {
      this.noticePage.PageNum = val;
      this.getNoticeListPage();
    },
    getNoticeListPage() {
      request.get("Notice/getAllNoticeListPage", { params: this.noticePage }).then(res => {
        console.log("获取全站消息成功", res)
        if (res.code == 200) {
          this.notice = res.data;
        } else {
          this.$notify.error("获取消息失败，请检查网络", e);
        }
      }).catch(e => {
        console.log("获取全站消息失败", e)
      })
    },
    getVideoMessageListPage() {
      request.get("Video/getVideoMessageListPageByUid", { params: this.videoMessagePage }).then(res => {
        console.log("获取视频消息成功", res)
        if (res.code == 200) {
          this.videoMessage = res.data;
        } else {
          this.$notify.error("获取消息失败，请检查网络", e);
        }
      }).catch(e => {
        console.log("获取视频消息失败", e)
      })
    }
    
  }
}
</script>

<style></style>