<template>
    <div>
      <h1 style="text-align: center;">管理员列表</h1>
      <div class="sear">
        <div>
          <el-input placeholder="请输入uid" class="input-with-select" v-model="page.uid" clearable>
          </el-input>
        </div>
        <div>
          <el-input placeholder="请输入用户名" class="input-with-select" v-model="page.uname" clearable>
          </el-input>
        </div>
        <el-button slot="append" icon="el-icon-search" @click="search()" style="margin-left:10px"></el-button>
      </div>
      <div class="main">
        <div class="table">
          <el-table :data="admin.adminList" :header-cell-style="{ textAlign: 'center' }"
            :cell-style="{ textAlign: 'center', }">
            <el-table-column prop="id" label="用户id" width="100" />
            <el-table-column prop="name" label="用户名" width="100" />
            <el-table-column prop="privileges" label="用户状态">
              <template slot-scope="scope">
                <el-select v-model="admin.adminList[scope.$index].privileges" placeholder="请选择" @change="((val)=>{updateAdminPrivileges(val,scope.row.id)})"  >
                  <el-option v-for="(item,index) in privilegesList" :key="index" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="200">
              <template slot-scope="scope">
                <el-button type="primary" @click="del(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <br /><br /><br />
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" background
            :current-page="page.PageNum" :page-sizes="[5, 10, 20, 50, 100]" :page-size="page.PageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="admin.adminCount" class="page">
          </el-pagination>
  
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import request from '@/utils/adminRequest'
  import global from '@/assets/global'
  import judge from '@/utils/judge'
  export default {
  
  
    data() {
      return {
        admin: {},
        privilegesList:[],
        page: {
          id: '',
          name: '',
          PageSize: 5,
          PageNum: 1,
        },
        
      }
    },
    created() {
      this.load();
    },
    methods: {
      search() {
        this.load()
      },
      del(id) {
        this.$confirm('此操作将永久删除该管理员, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            request.delete("Admin/deleteAdminById/"+id).then(res=>{
              console.log("删除管理员成功",res)
              if(res.code==200){
                this.$notify.success("删除管理员成功");
                this.load();
              }else{
                this.$notify.error(res.msg);
              }
            }).catch(e=>{
              console.log("删除管理员错误",e)
            })
  
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
  
      },
      updateAdminPrivileges(privileges,id){
        request.post("Admin/updateAdminPrivilegesByIdAndPrivileges",{privileges:privileges,id:id}).then(res=>{
          console.log("改变管理员状态返回",res);
          if(res.code==200){
            this.$notify.success("更改管理员状态成功");
          }else{
            this.$notify.error(res.msg);
          }
        }).catch(e=>{
          console.log("改变管理员状态返回",e);
        })
      },
      handleSizeChange(val) {
        this.page.PageSize = val
        this.load()
      },
      handleCurrentChange(val) {
        this.page.PageNum = val
        this.load()
      },
      resUrlSplicing(src) {
        if (src) {
          return global.reshost + src;
        }
      },
      load() {
        let params={
          id:this.page.id==''? 0:Number(this.page.id),
          name:this.page.name,
          PageNum:this.page.PageNum,
          PageSize:this.page.PageSize,
        };
        request.get("Admin/getAllAdminListPage",{params:params}).then(res=>{
          console.log("获取管理员分页列表成功",res);
          if(res.code==200){
            this.admin=res.data;
          }else{
            this.$notify.error(res.msg);
            this.$router.go(-1)
          }
        }).catch(e=>{
          console.log("获取管理员分页列表错误",e)
        });
        request.get("Admin/getAllAdminPrivilegesList").then(res=>{
          console.log("获取管理员状态成功",res);
          if(res.code==200){
            this.privilegesList=res.data;
          }
        }).catch(e=>{
          console.log("获取用户状态失败",e);
        })
      },
    }
  }
  </script>
  
  <style  scoped>
  .sear {
    margin-top: 10px;
    display: flex;
  }
  
  .sear div {
    min-width: 200px;
    margin: 0px 10px;
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
  
  .row {
    margin-top: 10px;
    border: 1px solid #888;
    padding: 10px;
    border-radius: 5px;
    background-color: #EEE;
    box-shadow: 0px 0px 2px;
    display: flex;
  }
  
  .row p {
    width: 30%;
    color: cornflowerblue;
  }
  
  .row span {
    width: 70%;
    margin-top: 4px;
  }
  </style>