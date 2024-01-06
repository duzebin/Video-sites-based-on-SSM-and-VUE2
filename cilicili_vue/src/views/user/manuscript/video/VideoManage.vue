<template>
  <div>
    <div class="videoInfo_box">
      <p>全部稿件:<span style="margin-left:10px">{{ count }}</span></p>
      <div>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane :label="'已通过 ' + state[1]" name="passed">

            <el-table :data="tableData" style="width: 100%" :header-cell-style="{ textAlign: 'center' }"
              :cell-style="{ textAlign: 'center', }">
              <el-table-column prop="uptime" label="日期" width="150" />
              <el-table-column prop="cover" label="封面" width="150">
                <template slot-scope="scope">
                  <el-image :src="resUrlSplicing(scope.row.cover)" fit="contain"
                    :preview-src-list="[resUrlSplicing(scope.row.cover)]" class="img">
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="视频标题">
                <template slot-scope="scope">
                  <p class="text_ellipsis" :title="scope.row.title">{{ scope.row.title }}</p>
                </template>
              </el-table-column>
              <el-table-column prop="synopsis" label="视频简介">
                <template slot-scope="scope">
                  <p class="text_ellipsis" :title="scope.row.synopsis">{{ scope.row.synopsis }}</p>
                </template>
              </el-table-column>
              <!-- <el-table-column prop="play" label="视频播放量" width="100"/> -->
              <el-table-column prop="like" label="视频点赞数" width="100" />
              <el-table-column prop="favorites" label="视频收藏数" width="100" />
              <el-table-column fixed="right" label="操作" width="200">
                <template slot-scope="scope">
                  <el-button type="primary" @click="del(scope.row.vid)">删除</el-button>
                  <el-button type="primary" @click="detail(scope.row.vid)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>

          </el-tab-pane>


          <el-tab-pane :label="'审核中 ' + state[2]" name="ongoing">

            <el-table :data="tableData" style="width: 100%" :header-cell-style="{ textAlign: 'center' }"
              :cell-style="{ textAlign: 'center', }">
              <el-table-column prop="uptime" label="日期" width="150" />
              <el-table-column prop="cover" label="封面" width="150">
                <template slot-scope="scope">
                  <el-image :src="resUrlSplicing(scope.row.cover)" fit="contain"
                    :preview-src-list="[resUrlSplicing(scope.row.cover)]" class="img">
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="视频标题">
                <template slot-scope="scope">
                  <p class="text_ellipsis" :title="scope.row.title">{{ scope.row.title }}</p>
                </template>
              </el-table-column>
              <el-table-column prop="synopsis" label="视频简介">
                <template slot-scope="scope">
                  <p class="text_ellipsis" :title="scope.row.synopsis">{{ scope.row.synopsis }}</p>
                </template>
              </el-table-column>
              <!-- <el-table-column prop="play" label="视频播放量" width="100"/> -->
              <el-table-column prop="like" label="视频点赞数" width="100" />
              <el-table-column prop="favorites" label="视频收藏数" width="100" />
              <el-table-column fixed="right" label="操作" width="200">
                <template slot-scope="scope">
                  <el-button type="primary" @click="del(scope.row.vid)">删除</el-button>
                  <el-button type="primary" @click="detail(scope.row.vid)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>


          </el-tab-pane>


          <el-tab-pane :label="'未通过 ' + state[3]" name="failed">

            <el-table :data="tableData" style="width: 100%" :header-cell-style="{ textAlign: 'center' }"
              :cell-style="{ textAlign: 'center', }">
              <el-table-column prop="uptime" label="日期" width="150" />
              <el-table-column prop="cover" label="封面" width="150">
                <template slot-scope="scope">
                  <el-image :src="resUrlSplicing(scope.row.cover)" fit="contain"
                    :preview-src-list="[resUrlSplicing(scope.row.cover)]" class="img">
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="视频标题">
                <template slot-scope="scope">
                  <p class="text_ellipsis" :title="scope.row.title">{{ scope.row.title }}</p>
                </template>
              </el-table-column>
              <el-table-column prop="synopsis" label="视频简介">
                <template slot-scope="scope">
                  <p class="text_ellipsis" :title="scope.row.synopsis">{{ scope.row.synopsis }}</p>
                </template>
              </el-table-column>
              <!-- <el-table-column prop="play" label="视频播放量" width="100"/> -->
              <el-table-column prop="like" label="视频点赞数" width="100" />
              <el-table-column prop="favorites" label="视频收藏数" width="100" />
              <el-table-column fixed="right" label="操作" width="200">
                <template slot-scope="scope">
                  <el-button type="primary" @click="del(scope.row.vid)">删除</el-button>
                  <el-button type="primary" @click="detail(scope.row.vid)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <br /><br /><br />
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
      :current-page="page.PageNum" :page-sizes="[5, 10, 20, 50, 100]" :page-size="page.PageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="state[this.page.state]" class="page">
    </el-pagination>
  </div>
</template>

<script>
import request from '@/utils/request'
import global from '@/assets/global'

export default {
  name: "VideoManage",
  data() {
    return {
      page: {
        PageSize: 5,
        PageNum: 1,
        state: 1,
      },
      videoList: [],
      audit: [],
      activeName: 'passed',
      state: [],
      tableData: [],
      count: 0,
    }
  },
  created() {
    this.load()
  },
  methods: {
    del(vid) {
      this.$confirm('这个操作将永久删除这个视频, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete("Video/deleteVideoByVid/" + vid).then(res => {
          console.log("删除视频返回", res);
          if (res.code == 200) {
            this.$notify.success("删除视频成功", res.data);
            this.getVideoInfo();
          } else {
            this.$notify.error("删除视频失败", res.msg);
          }
        }).catch(e => {
          console.log("删除视频错误", e);
        })

      }).catch(() => {
        
      });
    },
    detail(vid) {
      this.$router.push({ name: 'VideoDetail', query: { "vid": vid } }).catch();
    },
    load() {
      request.get("Audit/getAllAudit").then(res => {
        this.audit = res.data;
        console.log("获取审核状态列表", res.data);

      }).catch(e => {
        console.log("获取审核状态列表错误", e)
      });
      request.get("Video/getUserAllVideoStateCount").then(res => {
        console.log("获取全部上传视频状态数量", res.data);
        this.state = res.data;
        this.getVideoInfo();
      }).catch(e => {
        console.log("获取全部上传视频状态数量错误", e);
      });
    },
    getVideoInfo() {
      request.get("Video/getUserUploadVideoInfoPageList", { params: this.page }).then(res => {
        console.log("获取视频信息成功", res.data);
        this.tableData = res.data.videoList;
        this.state[this.page.state] = res.data.count;
        this.count = this.state[1] + this.state[2] + this.state[3];
      }).catch(e => {
        console.log("获取视频信息错误", e);
      });
    },
    resUrlSplicing(src) {
      if (src) {
        return global.reshost + src;
      }
    },
    handleClick(tab, event) {
      this.page.state = Number(tab.index) + 1;
      this.getVideoInfo();
    },
    handleSizeChange(val) {
      this.page.PageSize = val;
      this.getVideoInfo();
    },
    handleCurrentChange(val) {
      this.page.PageNum = val;
      this.getVideoInfo();
    },
  },
}
</script>

<style>
.img{
  height: 57px;
  width: 100px;
  background-color: black;
}

.videoInfo_box {
  width: 90%;
  background: white;
  margin: auto;
  margin-top: 20px;
  border-radius: 5px;
  box-shadow: 0px 0px 3px #aaa;
  padding: 20px;
}

.text_ellipsis {
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.page {
  text-align: center;
  position: fixed;
  top: 100%;
  transform: translateY(-150%);
  background-color: white;
  border: 1px solid;
  box-shadow: 0px 0px 3px;
  z-index: 100;
  border-radius: 5px;
  padding: 5px;
  margin-left: 20px;
}
</style>