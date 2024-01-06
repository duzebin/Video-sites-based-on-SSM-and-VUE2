import Vue from 'vue'
import VueRouter from 'vue-router'
import User from '../views/User.vue'
import Admin from '../views/Admin.vue'
import HomeView from '@/views/user/Home.vue'
import Cookies from 'js-cookie';
import judge from '@/utils/judge'


Vue.use(VueRouter)

const routes = [
  {
    path: '/admin',
    component: Admin,
    children: [
      {
        path: "",
        name: 'Main',
        meta: { isAuth: true, type: 'admin' },
        component: () => import("@/views/admin/main/Main.vue"),
        children: [
          {
            path: "",
            name: "AdminHome",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/home/Home.vue')
          },
          {
            path: "commentsAudit",
            name: "AdminCommentsAudit",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/comments/CommentsAudit.vue')
          },
          {
            path: "commentsList",
            name: "AdminCommentsList",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/comments/CommentsList.vue')
          },
          {
            path: "videoList",
            name: "AdminVideoList",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/video/VideoList.vue')
          },
          {
            path: "videoAudit",
            name: "AdminVideoAudit",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/video/VideoAudit.vue')
          },
          {
            path: "videoDetail",
            name: "AdminVideoDetail",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/video/VideoDetail.vue')
          },
          {
            path: "userList",
            name: "AdminUserList",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/user/UserList.vue')
          },
          {
            path: "addAdmin",
            name: "AddAdmin",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/admin/AddAdmin.vue')
          }
          ,
          {
            path: "adminOperate",
            name: "AdminOperate",
            meta: { isAuth: true, type: 'admin' },
            component: () => import('@/views/admin/main/admin/AdminOperate.vue')
          }
        ]
      },
      {
        path: "login",
        name: "AdminLogin",
        meta: { isAuth: false, type: 'admin' },
        component: () => import("@/views/admin/login/Login.vue"),
      }
    ],
  }, {
    path: '/',
    component: User,
    children: [
      {
        path: '',
        name: 'HomeView',
        component: HomeView,
        meta: {
          isAuth: false,
          type: 'user',
          title: '首页',
          keepAlive: true
        }
      },
      {
        path: 'manuscript',
        name: 'Manuscript',
        meta: { isAuth: true, type: 'user' },
        component: () => import('@/views/user/manuscript/Manuscript.vue'),
        children: [
          {
            path: '',
            name: 'ManuscriptHome',
            meta: { isAuth: true, type: 'user' },
            component: () => import('@/views/user/manuscript/ManuscriptHome.vue'),
          },
          {
            path: 'submission',
            name: 'Submission',
            meta: { isAuth: true, type: 'user' },
            component: () => import('@/views/user/manuscript/Submission.vue'),
          },
          {
            path: 'videoManage',
            name: 'VideoManage',
            meta: { isAuth: true, type: 'user' },
            component: () => import('@/views/user/manuscript/video/VideoManage.vue'),
          },
          {
            path: 'videoDetail',
            name: 'VideoDetail',
            meta: { isAuth: true, type: 'user' },
            component: () => import('@/views/user/manuscript/video/VideoDetail.vue'),
          },
          {
            path: 'videoComplaint',
            name: 'VideoComplaint',
            meta: { isAuth: true, type: 'user' },
            component: () => import('@/views/user/manuscript/video/VideoComplaint.vue'),
          },
          {
            path: 'commentsList',
            name: 'CommentsList',
            meta: { isAuth: true, type: 'user' },
            component: () => import('@/views/user/manuscript/interaction/CommentsList.vue'),
          }
        ]
      },
      {
        path: 'video',
        name: 'Video',
        meta: { isAuth: false, type: 'user' },
        component: () => import('@/views/user/video/Video.vue'),
      },
      {
        path: "login",
        name: "Login",
        meta: { isAuth: false, type: 'user' },
        component: () => import("@/views/user/login/Login.vue")
      },
      {
        path: "enroll",
        name: "Enroll",
        meta: { isAuth: false, type: 'user' },
        component: () => import("@/views/user/login/Enroll.vue")
      },
      {
        path: "userInfo",
        name: "UserInfo",
        meta: { isAuth: true, type: 'user' },
        component: () => import("@/views/user/userInfo/UserInfo.vue")
      },
      {
        path: "dynamic",
        name: "Dynamic",
        meta: { isAuth: true, type: 'user' },
        component: () => import("@/views/user/dynamic/Dynamic.vue")
      },
      {
        path: "search",
        name: "Search",
        meta: { isAuth: false, type: 'user' },
        component: () => import("@/views/user/search/Search.vue")
      },
      {
        path: "message",
        name: "Message",
        meta: { isAuth: true, type: 'user' },
        component: () => import("@/views/user/message/Message.vue")
      },
      {
        path: "userEdit",
        name: "UserEdit",
        meta: { isAuth: true, type: 'user' },
        component: () => import("@/views/user/login/UserEdit.vue")
      }
    ],
  }, {
    path: '*',
    component: () => import('@/views/404.vue')
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

router.beforeEach((to, from, next) => {
  if (to.meta.isAuth) {
    if (to.meta.type === 'user') {
      const userJSON = Cookies.get('user');
      if (!judge.judgeNull(userJSON) && !judge.judgeUndefined(userJSON)) {
        next();
      } else {
        return next("/login");
      }
    } else {
      const adminJSON = Cookies.get('admin');
      if (!judge.judgeNull(adminJSON) && !judge.judgeUndefined(adminJSON)) {
        next();
      } else {
        return next("/admin/login");
      }
    }

  } else {
    next();
  }
})

export default router
