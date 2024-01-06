<template>
    <div style="height: 100vh;width: 100vw;">
        <div
            style="width: 500px;background-color: white;border-radius: 10px;position: relative;top: 50%;transform: translateY(-50%);margin: auto;padding: 50px;">
            <div style="margin-bottom:30px;text-align: center;font-size: 30px;font-weight: bold;color: dodgerblue;">注册</div>
            <div style="display: flex;">
                <el-form :model="user" ref="enrollForm" :rules="rules">
                    <el-form-item prop="uname">
                        <el-input v-model="user.uname" placeholder="请输入用户名" prefix-icon="el-icon-user"
                            size="medium"></el-input>
                    </el-form-item>
                    <el-form-item prop="describes">
                        <el-input v-model="user.describes" placeholder="一句话介绍自己" prefix-icon="el-icon-tickets"
                            size="medium"></el-input>
                    </el-form-item>
                    <el-form-item prop="account">
                        <el-input v-model="user.account" placeholder="请输入账号" prefix-icon="el-icon-user"
                            size="medium"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="user.password" show-password placeholder="请输入密码" prefix-icon="el-icon-lock"
                            size="medium"></el-input>
                    </el-form-item>
                    <el-form-item prop="confirmPassword">
                        <el-input v-model="user.confirmPassword" show-password placeholder="请确认密码"
                            prefix-icon="el-icon-lock" size="medium"></el-input>
                    </el-form-item>
                </el-form>
                <el-upload class="avatar-uploader" action="http://localhost:8080/cilicili/User/enroll"
                    :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload"
                    :auto-upload="false" :on-change="uploadChange">
                    <img v-if="imageUrl" :src="imageUrl" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>

            </div>
            <el-button size="medium" type="primary" style="width: 100%;margin: auto;" @click="enroll()">注册</el-button>
        </div>
    </div>
</template>

<script>
import global from "@/assets/global";
import judge from "@/utils/judge";
import request from "@/utils/request";
import axios from 'axios'
import Cookies from 'js-cookie';

export default {
    name: "Enroll",

    data() {
        var validateCheckPass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.user.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            imageUrl: '',
            user: {},
            rules: {
                uname: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
                ],
                describes: [
                    { min:0, max: 64, message: '最多可输入 64个字符', trigger: 'blur' }
                ],
                account: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 20, message: '长度在 3 到 20个字符', trigger: 'blur' }
                ],
                confirmPassword: [
                    { validator: validateCheckPass, trigger: 'blur' }
                ],
            },
        };
    },
    methods: {
        enroll() {
            let userData = new FormData();
            if (!judge.judgeUndefined(this.user.file)) {
                userData.append("file", this.user.file,);
            } else {
                this.$notify.warning("头像不能为空");
                return;
            }
            userData.append("uname", this.user.uname);
            userData.append("describes", this.user.describes);
            userData.append("account", this.user.account);
            userData.append("password", this.user.password);
            let config = {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            };
            this.$refs['enrollForm'].validate((valid) => {
                if (valid) {
                    axios.post(global.apihost + "/User/enroll", userData, config).then(res => {
                        console.log("注册返回", res.data);
                        if (res.data.data !== null) {
                            Cookies.set('user', JSON.stringify(res.data.data));
                        }
                        if (res.data.code == 200) {
                            this.$notify.success("登陆成功,欢迎您:"+res.data.data.uname);
                            this.$router.push({ name: "HomeView" });
                        } else {
                            this.$notify.error(res.data.msg);
                        }
                    }).catch(e => {
                        console.log("注册错误返回", e);
                    });
                    this.user = {};
                    this.imageUrl = '';
                } else {
                    this.$notify.error("请按规则填写数据");
                }
            }
            )

        },
        uploadChange(file, fileList) {
            this.user.file = file.raw;
            this.imageUrl = URL.createObjectURL(file.raw);
        },
        handleAvatarSuccess(res, file) {
            console.log(file)
            this.imageUrl = URL.createObjectURL(file.raw);
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        }
    }
}


</script>

<style>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    top: 50%;
    transform: translateY(-50%);
    overflow: hidden;
    margin-left: 50px;

}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>