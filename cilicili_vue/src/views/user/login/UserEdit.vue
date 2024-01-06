<template>
    <div>
        <my-header></my-header>
        <div
            style="width: 500px;background-color: white;border-radius: 10px;position: absolute;top: 50%;left: 50%;transform: translate(-50%,-50%);margin: auto;padding: 50px;">
            <div style="margin-bottom:30px;text-align: center;font-size: 30px;font-weight: bold;color: dodgerblue;">修改个人信息
            </div>
            <div style="display: flex;">
                <el-form :model="user" ref="EditForm" :rules="rules">
                    <el-form-item prop="uname" style="margin-top: 30px;">
                        <el-input v-model="user.uname" placeholder="修改" prefix-icon="el-icon-user" size="medium"></el-input>
                    </el-form-item>
                    <el-form-item prop="describes" style="margin-top: 60px;">
                        <el-input v-model="user.describes" placeholder="修改简介" prefix-icon="el-icon-tickets"
                            size="medium"></el-input>
                    </el-form-item>
                </el-form>
                <el-upload class="avatar-uploader" action="#" :show-file-list="false" :on-change="uploadChange">
                    <img v-if="imageUrl" :src="imageUrl" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </div>
            <el-button size="medium" type="primary" style="width: 100%;margin: auto;margin-top: 30px;"
                @click="edit()">修改</el-button>
        </div>
    </div>
</template>

<script>
import myHeader from '@/components/my-header.vue';
import judge from '@/utils/judge';
import Cookies from 'js-cookie';
import global from '@/assets/global';
import axios from 'axios';

export default {
    components: { myHeader },
    data() {
        return {
            user: {
                uname: '',
                describes: '',
            },
            backUser: {
                uname: '',
                describes: '',
            },
            imageUrl: '',
            backImageUrl: '',
            rules: {
                uname: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
                ],
                describes: [
                    { min: 0, max: 64, message: '最多可输入 64个字符', trigger: 'blur' }
                ],
            }
        }
    },
    created() {
        this.load();
    },
    methods: {
        edit() {
            let userData = new FormData();
            if ((JSON.stringify(this.user) == JSON.stringify(this.backUser)) && JSON.stringify(this.imageUrl) == JSON.stringify(this.backImageUrl)) {
                this.$notify.error("您好像没有对信息进行修改");
                return;
            }
            if (!judge.judgeUndefined(this.user.file)) {
                userData.append("file", this.user.file,);
            }
            userData.append("uname", this.user.uname);
            userData.append("describes", this.user.describes);
            let config = {
                headers: {
                    "Content-Type": "multipart/form-data",
                    "token": JSON.parse(Cookies.get('user')).token
                }
            };
            this.$refs['EditForm'].validate((valid) => {
                if (valid) {
                    axios.post(global.apihost + "/User/updateUserInfoByUserInfo", userData, config).then(res => {
                        console.log("修改返回", res.data);
                        if (res.data.code == 200) {
                            if (res.data.data !== null) {
                                Cookies.set('user', JSON.stringify(res.data.data));
                            }
                            this.$notify.success("修改信息成功,欢迎您:" + res.data.data.uname);
                            this.$router.push({ name: "HomeView" }).catch();
                        } else {
                            this.$notify.error(res.data.msg);
                        }
                    }).catch(e => {
                        console.log("修改错误返回", e);
                    });
                    this.user = {};
                    this.imageUrl = '';
                } else {
                    this.$notify.error("请按规则填写数据");
                }
            })
        },
        load() {
            if (judge.judgeLogin()) {
                const userInfo = JSON.parse(Cookies.get('user'));
                console.log(userInfo);
                this.user.uname = userInfo.uname;
                this.user.describes = userInfo.describes;
                this.backUser.uname = JSON.parse(JSON.stringify(userInfo.uname));
                this.backUser.describes = JSON.parse(JSON.stringify(userInfo.describes));
                this.imageUrl = global.reshost + userInfo.avatar;
                this.backImageUrl = JSON.parse(JSON.stringify(global.reshost + userInfo.avatar));
            } else {
                this.userInfo = '';
                this.$notify.error("请先登录");
                this.$router.go(-1);
            }
        },
        uploadChange(file, fileList) {
            this.user.file = file.raw;
            this.imageUrl = URL.createObjectURL(file.raw);
        },
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