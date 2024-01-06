<template>
    <div>
        <div
            style=" padding: 50px;background-color: #fff;;margin:auto;width: 500px;border-radius:10px;border:1px solid;position: relative;top:50%;transform:translateY(-50%);box-shadow:0 0 3px #aaa">
            <h1 style="font-size:35px;text-align:center;color:deepskyblue;margin-top: 20px;margin-bottom: 20px;">添加管理员</h1>
            <el-form :model="admin" >
              <el-form-item  label="" style="margin-top: 10px;">
                <el-input v-model="admin.name" placeholder="请输入管理员名" maxlength="10"  show-word-limit></el-input>
              </el-form-item>
              <el-form-item  label="" style="margin-top: 10px;">
                <el-input v-model="admin.account" placeholder="请输入账号" maxlength="20" show-word-limit></el-input>
              </el-form-item>
              <el-form-item  label="" style="margin-top: 10px;">
                <el-input v-model="admin.password" show-password placeholder="请输入密码" maxlength="20"></el-input>
              </el-form-item>
              <el-form-item  label="" style="margin-top: 10px;">
                <el-input v-model="admin.repeatPassword" show-password placeholder="请再次输入密码" maxlength="20" ></el-input>
              </el-form-item>
              <el-form-item label="" style="margin-top: 10px;">
                <el-select v-model="admin.privileges" placeholder="请选择管理员权限" >
                  <el-option v-for="(item,index) in privilegesList" :label="item.name" :value="item.id" :key="index"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="" style="margin-top: 20px;">
                <el-button type="primary" style="width: 100%;text-align: center;font-size: 20px;" @click="addAdmin">添加管理员</el-button>
              </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
import request from '@/utils/adminRequest'

export default {
    name: 'LOGIN',
    data() {
        return {
            admin: {},
            privilegesList:[],
        }
    },
    created() {
        this.load()
    },
    methods: {
        load(){
            request.get("Admin/getAllAdminPrivilegesList").then(res=>{
                console.log("获取管理员权限名单",res);
                if(res.code==200){
                    this.privilegesList=res.data;

                }
            }).catch(e=>{
                console.log("获取管理员权限名单",e);
            })
        },
        addAdmin(){
            if(this.admin.name==''||this.admin.name.lengtn<5||this.admin.name.length>20){
                this.$message.warning("管理员名必须为2-10位字符");
                return;
            }
            if(this.admin.account==''||this.admin.account.lengtn<5||this.admin.account.length>20){
                this.$message.warning("账号必须为5-20位字符");
                return;
            }
            if(this.admin.password==''||this.admin.password.lengtn<5||this.admin.password.length>20||this.admin.password!=this.admin.repeatPassword){
                this.$message.warning("密码必须为5-20位字符,且两次密码相同");
                return;
            }
            if(this.admin.privileges==''){
                this.$message.warning("账号权限不能为空");
                return;
            }
            request.post("Admin/Enroll",this.admin).then(res=>{
                console.log("注册返回",res);
                if(res.code==200){
                    this.$notify.success("添加管理员成功!");
                    this.admin={};
                }else[
                    this.$notify.error(res.msg)
                ]
            }).catch(e=>{
                console.log("注册返回",e);
            })
        }
    },
}
</script>

<style scoped>

</style>