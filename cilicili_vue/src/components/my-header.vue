<template>
  <div id="my-header">
    <div class="header">
      <div style="display: flex;">
        <div>
          <i class="el-icon-back" style="font-size: 30px;line-height: 60px;cursor: pointer;" title="返回上一页"
            @click="Back()"></i>
        </div>
        <div class="logo" @click="toHome">
          <img src="@/assets/logo.png" alt="" class="logo_img">
          <p class="logo_title">CiliCili</p>
        </div>
      </div>
      <div class="search">
        <el-input v-model="search" placeholder="请输入内容" class="input-with-select">
          <el-button slot="append" icon="el-icon-search" @click="toSearch()"></el-button>
        </el-input>
      </div>
      <div class="user">
        <div class="user_img_container">
          <div class="user_console">
            <img :src="resUrlSplicing(this.userInfo.avatar)" class="user_img" ref="userImg" />
            <div class="user_ui">
              <div style="width: 95%;margin: auto;">
                <div class="user_name">
                  <p>{{ userInfo.uname }}</p>
                </div>
                <div class="user_level" v-if="this.userInfo !== ''">
                  <i :class="'iconfont icon-level' + this.userInfo.level"></i>
                </div>
                <div class="exp_tip" v-if="this.userInfo !== ''">
                  <p>
                    <span>当前经验值：</span>{{ this.userInfo.experience }}<br>
                    <span>距离下一级还差：</span>{{ this.userInfo.levelUpExperience != 0 ? this.userInfo.levelUpExperience -
                      this.userInfo.experience : 0 }}
                  </p>
                </div>
                <div class="personal_center" v-if="this.userInfo !== ''" @click="toUserInfo">
                  <p>
                    <i class="el-icon-user"></i>个人中心<span><i class="el-icon-arrow-right"></i></span>
                  </p>
                </div>
                <div class="personal_center" v-if="this.userInfo !== ''" @click="toManuscript">
                  <p>
                    <i class="el-icon-user"></i>投稿管理<span><i class="el-icon-arrow-right"></i></span>
                  </p>
                </div>
                <hr style="opacity: 0.5;">
                <div class="personal_center">
                  <p @click="toLogin" v-if="this.userInfo === ''">
                    <i class="el-icon-user"></i>登录<span><i class="el-icon-arrow-right"></i></span>
                  </p>
                  <p @click="loginOut" v-else>
                    <i class="el-icon-user"></i>退出登录<span><i class="el-icon-arrow-right"></i></span>
                  </p>
                </div>
              </div>

            </div>
          </div>
        </div>
        <div class="ribbon" @click="toMessage">
          <i class="iconfont icon-shouye ribbon_icon"></i>
          <p class="ribbon_title">消息</p>
        </div>
        <div class="ribbon" @click="toDynamic">
          <i class="iconfont icon-airudiantubiaohuizhi-zhuanqu_zixundongtai ribbon_icon"></i>
          <p class="ribbon_title">动态</p>
        </div>
        <div class="ribbon" @click="toCollection">
          <i class="iconfont icon-shoucang ribbon_icon"></i>
          <p class="ribbon_title">收藏</p>
        </div>
        <div class="ribbon" @click="toManuscript">
          <i class="iconfont icon-chuangzuo ribbon_icon"></i>
          <p class="ribbon_title">创作中心</p>
        </div>
      </div>
      <div class="Submission" @click="toSubmission">
        <el-button type="primary"><i class="iconfont icon-shangchuan_huaban Submission_icon"></i>投稿</el-button>
      </div>
    </div>
  </div>
</template>
  
<script>
import global from '@/assets/global';
import Cookies from 'js-cookie';
import judge from '@/utils/judge';


export default {
  name: "my-header",
  data() {
    return {
      userInfo: '',
      experience: -1,
      search: '',
    };
  },
  methods: {
    toMessage(){
      this.$router.push({ name: 'Message'}).then(() => {
        location.reload()
      }).catch(e => { });
    },
    toSearch() {
      if (this.search.trim() == '') {
        this.$notify.error("搜索内容不能为空");
        return;
      }
      this.$router.push({ name: 'Search', query: { "name": this.search } }).then(() => {
        location.reload()
      }).catch(e => { });
    },
    toDynamic() {
      this.$router.push({ name: 'Dynamic' }).catch(e => { });
    },
    toCollection() {
      this.$router.push({ name: 'UserInfo', query: { "type": "Collection" } }).catch(e => { });
    },
    Back() {
      this.$router.go(-1);
    },
    toUserInfo() {
      this.$router.push({ name: 'UserInfo' }).catch(e => { });
    },
    toHome() {
      this.$router.push({ name: 'HomeView' }).catch();
    },
    toSubmission() {
      this.$router.push({ name: 'Submission' }).catch();
    },
    toManuscript() {
      this.$router.push('/manuscript').catch(e => { console.log(e) });
    },
    toLogin() {
      this.$router.push('/login').catch();
    },
    loginOut() {
      Cookies.remove('user');
      this.$router.go(0)
    },
    load() {

      if (judge.judgeLogin()) {
        this.userInfo = JSON.parse(Cookies.get('user'));
      } else {
        this.userInfo = '';
      }

    },
    resUrlSplicing(src) {
      if (src) {
        return global.reshost + src;
      }
    },
  },
  created() {
    this.load();
  },
  watch: {
    '$route': 'load'
  }

};
</script>
  
<style>
.header {
  width: 100%;
  height: 60px;
  display: flex;
  padding: 0px 20px;
  position: fixed;
  top: 0px;
  z-index: 999;
  background: #fff;
  box-shadow: 0px 1px 10px 1px black;
  ;
}

.logo {
  height: 40px;
  margin: 0 20px;
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
}

.logo_img {
  height: 100%;
  margin-right: 5px;
}

.logo_title {
  height: 100%;
  font-size: 28px;
  color: #17abe3;
  line-height: 40px;
  margin: 0;
}

.search {
  margin: auto;
  width: 400px;
  min-width: 200px;
}

.input-with-select .el-input-group__prepend {
  background-color: #fff;
}

.user {
  margin-left: 10px;
  height: 40px;
  display: flex;
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  flex-shrink: 0;
}

.user_img_container {
  position: relative;
  margin: 0 5px;
  transition: all .5s;
  width: 40px;
}

.user_img_container:hover {
  width: 200px;
}

.user_img_container:hover .user_img {
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 80px;
}

.user_img_container:hover .user_ui {
  opacity: 1;
  width: 200px;
  max-height: 350px;
}

.user_img {
  width: 40px;
  height: 40px;
  border: 1px solid black;
  border-radius: 100%;
  overflow: hidden;
  position: relative;
  left: 100%;
  transform: translateX(-100%);
  transition: all .5s;
  z-index: 3;
  background-image: url('../assets/头像.png');
  background-size: cover;
  background-repeat: repeat;
  background-color: white;
}

.user_ui {
  width: 0px;
  max-height: 0px;
  border-radius: 10px;
  white-space: nowrap;
  text-align: center;
  padding-top: 40px;
  margin-top: -40px;
  background: white;
  overflow: hidden;
  opacity: 0;
  transition: all .5s;
  box-shadow: 0px 0px 5px 0px;
}

.user_ui p {
  margin: 0;
  padding: 0;
}

.user_ui span {
  margin: 0;
  padding: 0;
  opacity: 0.5;
}

.user_console {
  z-index: 1;
}

.user_name {
  margin: 5px 0;
  width: 100%;
  font-size: 20px;
  font-weight: bold;
}

.user_level {
  font-size: 24px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}

.exp_tip p {
  font-size: 10px;
}

.user_ex {
  width: 50%;
}

.icon-level1 {
  color: #17abe3;
}

.icon-level2 {
  color: #17abe3;
}

.icon-level3 {
  color: #17abe3;
}

.icon-level4 {
  color: #17abe3;
}

.icon-level5 {
  color: #17abe3;
}

.icon-level6 {
  color: #17abe3;
}

.personal_center {
  border-radius: 5px;
  padding: 10px;
  margin: 5px;
  text-align: start;
  transition: all .3s;
  cursor: pointer;
}

.personal_center:hover {
  background-color: #BBB;
}

.personal_center span {
  float: right;
}


.ribbon {
  height: 40px;
  margin: 0 10px;
  flex-shrink: 0;
  text-align: center;
  position: relative;
  cursor: pointer;
}

.ribbon:hover .ribbon_icon {
  animation: icon_animation 400ms ease-in-out;
}

@keyframes icon_animation {
  0% {
    top: 0;
  }

  50% {
    top: -7px;
  }

  100% {
    top: 0;
  }
}

.ribbon_icon {
  font-size: 24px;
  position: relative;
}

.ribbon_title {
  font-size: 10px;
  margin: 0;
  padding: 0;
}

.Submission {
  height: 40px;
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  margin: 0 20px;
}

.Submission_icon {
  font-size: 18px;
  margin-right: 10px;
}
</style>