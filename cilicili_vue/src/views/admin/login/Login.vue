<template>
    <div style="height: 100vh;width: 100vw;">
        <div
            style="width: 500px;background-color: white;border-radius: 10px;position: relative;top: 50%;transform: translateY(-50%);margin: auto;padding: 50px;">
            <div style="margin-bottom:30px;text-align: center;font-size: 30px;font-weight: bold;color: dodgerblue;">登 录（管理员）
            </div>
            <el-form :model="admin" ref="loginForm" :rules="rules">
                <el-form-item prop="account">
                    <el-input v-model="admin.account" placeholder="请输入账号" prefix-icon="el-icon-user"
                        size="medium"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="admin.password" show-password placeholder="请输入密码" prefix-icon="el-icon-lock"
                        size="medium"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button size="medium" type="primary" style="width: 100%;margin: auto;" @click="login">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>


<script>
import Rrequest from '@/utils/adminRequest';
import Cookies from 'js-cookie';
import request from '@/utils/request';

export default {
    name: 'LOGIN',
    data() {
        return {
            admin: {},
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
        login() {
            this.$refs['loginForm'].validate((valid) => {
                if (valid) {
                    request.post('Admin/login', this.admin).then(res => {
                        if (res.code === '200') {
                            console.log("管理员信息获取成功:", res.data)
                            this.$notify.success("登陆成功,欢迎您:" + res.data.name);
                            if (res.data !== null) {
                                Cookies.set('admin', JSON.stringify(res.data));
                            };
                            this.$router.push({name:'Main'});
                        } else {
                            this.$notify.error(res.msg);
                        }
                    }).catch(e => {
                        Cookies.remove('admin');
                        console.log("管理员数据获取失败", e);
                    });
                }
            });
            this.admin = {};
        },
    },
}
</script>