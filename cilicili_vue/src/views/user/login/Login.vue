<template>
    <div style="height: 100vh;width: 100vw;">
        <div
            style="width: 500px;background-color: white;border-radius: 10px;position: relative;top: 50%;transform: translateY(-50%);margin: auto;padding: 50px;">
            <div style="margin-bottom:30px;text-align: center;font-size: 30px;font-weight: bold;color: dodgerblue;">登 录
            </div>
            <el-form :model="user" ref="loginForm" :rules="rules">
                <el-form-item prop="account">
                    <el-input v-model="user.account" placeholder="请输入账号" prefix-icon="el-icon-user"
                        size="medium"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="user.password" show-password placeholder="请输入密码" prefix-icon="el-icon-lock"
                        size="medium"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button size="medium" type="primary" style="width: 100%;margin: auto;" @click="login">登录</el-button>
                </el-form-item>
                <div style="text-align: end;">
                    <el-button type="text" style="margin: 0px;" @click="toEnroll()">没有账号？去注册</el-button>
                    <span style="text-align: center;">|</span>
                    <el-button type="text" style="margin: 0px;">找回密码</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>


<script>
import request from '@/utils/request';
import Cookies from 'js-cookie';

export default {
    name: 'LOGIN',
    data() {
        return {
            user: {},
            rules: {
                account: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
                ]
            },
        }
    },
    created() {

    },
    methods: {
        toEnroll() {
            this.$router.push({ name: 'Enroll' }).catch();
        },
        login() {
            this.$refs['loginForm'].validate((valid) => {
                if (valid) {
                    request.post('User/login', this.user).then(res => {
                        if (res.code === '200') {
                            console.log("用户信息获取成功:", res.data)
                            if (res.data.state ==  '正常') {
                                this.$notify.success("登陆成功,欢迎您:"+res.data.uname);
                            } else {
                                this.$notify.warning({dangerouslyUseHTMLString: true,message:"登陆成功,欢迎您:&nbsp;&nbsp;"+res.data.uname+"<br/>您当前账户状态:&nbsp;&nbsp;" + res.data.state});
                            }
                            if (res.data !== null) {
                                Cookies.set('user', JSON.stringify(res.data));
                            };
                            this.$router.go(-1);
                        } else {
                            this.$notify.error(res.msg);
                        }

                    }).catch(e => {
                        Cookies.remove('user');
                        console.log("用户数据获取失败", e);
                    });
                }
            });
            this.user = {};
        },
    },
}
</script>