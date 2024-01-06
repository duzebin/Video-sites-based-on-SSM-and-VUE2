<template>
    <div>
        <div class="video">
            <div class="upload">
                <div style="display: flex;margin: auto;flex-shrink: 0;">
                    <el-upload class="upload-demo" action="#" drag :show-file-list="false" :auto-upload="false"
                        :on-change="uploadChange" multiple>
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    </el-upload>
                    <div style="margin: 0px 20px;" v-if="progress > 0">
                        <p style="text-align: center;">上传进度</p>
                        <el-progress type="circle" :percentage="progress" :format="format"
                            class="video_progress"></el-progress>
                    </div>
                    <el-upload v-if="uploadcover != '' || imageUrl != ''" :auto-upload="false" :show-file-list="false"
                        :on-change="coverUploadChange" action="#" class="cover_upload">
                        <p style="position: absolute;width: 320px;color: white;text-shadow:0 0 5px black;">
                            视频封面默认为视频第一帧（可更改）
                        </p>
                        <img v-if="uploadcover" :src="resUrlSplicing(uploadcover)" class="video_cover">
                        <img v-else-if="imageUrl" :src="imageUrl" class="video_cover">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </div>
            </div>
            <div class="from">
                <el-form :model="video" label-width="50px" ref="video_form" class="Video_form">
                    <el-form-item prop="title" label="标题">
                        <el-input v-model="video.title" maxlength="100" show-word-limit placeholder="请输入视频标题"></el-input>
                    </el-form-item>
                    <el-form-item prop="partition" label="分区">
                        <el-select v-model="video.partition" placeholder="请选择视频分区">
                            <el-option v-for="(item, index) in partition" :key="index" :label="item.name"
                                :value="item.name"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="partition" label="合集">
                        <el-select v-model="video.episode" placeholder="您可以选择将您的视频放到视频合集中" clearable>
                            <el-option v-for="(item, index) in episode" :key="index" :label="item.ename"
                                :value="item.id"></el-option>
                        </el-select>
                        <el-button type="primary" style="margin-left: 20px;" @click="addEpisode()">创建合集</el-button>
                    </el-form-item>
                    <el-form-item prop="tag" label="标签">
                        <div style="display: flex;">
                            <el-input v-model="tag" maxlength="20" show-word-limit placeholder="请输入标签"></el-input>
                            <el-button type="primary" style="margin-left: 20px;" @click="addTag()">
                                添加标签(剩余：{{ 10 - video.tag.length }})
                            </el-button>
                        </div>
                        <div class="tag_box">
                            <span v-for="(item, index) in video.tag" :key="index" @click="delVideoTag(index)">
                                {{ item }}
                            </span>
                        </div>
                    </el-form-item>
                    <el-form-item prop="synopsis" label="简介">
                        <el-input type="textarea" maxlength="1000" show-word-limit :autosize="{ minRows: 4, maxRows: 10 }"
                            placeholder="好好的介绍你的视频吧" v-model="video.synopsis">
                        </el-input>
                    </el-form-item>
                    <el-button type="primary" style="width: 100%;margin: auto;" @click="upload()">发布</el-button>
                </el-form>
            </div>

        </div>

    </div>
</template>

<script>
import { filePare } from '@/utils/filesUtils'
import request from '@/utils/request';
import axios from 'axios';
import global from '@/assets/global';
import Cookies from 'js-cookie';
import judge from '@/utils/judge';
import deta from '@/utils/deta';

export default {
    name: "Submission",

    data() {
        return {
            user: {},
            chunkSize: 5 * 1024 * 1024,
            chunks: [],
            md5: '',
            noUpChunkNumList: [],
            imageUrl: '',
            uploadcover: '',
            uploadVideo: '',
            video: {
                tag: [],
                title: ''
            },
            progress: 0,
            partition: {},
            episode: [],
            tag: '',
        }
    },
    created() {
        this.load()
    },
    methods: {
        addEpisode() {
            this.$prompt('请输入合集名', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(({ value }) => {
                request.post("Episode/insertEpisodeByEpisode", { ename: value, crtime: deta.getTimes() }).then(res => {
                    if (res.code == 200) {
                        this.episode.push(res.data);
                        console.log("添加合集成功", res.data);
                    } else {
                        this.$notify.error(res.msg);
                    }
                }).catch(e => {
                    console.log("添加合集失败", e);
                })
            }).catch(() => {
            });
        },
        upload() {
            console.log(this.video);
            if (judge.judgeUndefined(this.video.title)) {
                this.$message.error("视频标题不能为空");
                return;
            }
            if (this.video.title.length > 100) {
                this.$message.error("视频标题不能超过100个字");
                return;
            }
            if (judge.judgeUndefined(this.video.partition)) {
                this.$message.error("请选择视频分区");
                return;
            }
            let videoData = new FormData();
            if (!judge.judgeUndefined(this.video.coverfile)) {
                videoData.append("file", this.video.coverfile,);
            } else {
                videoData.append("tempVideoCoverPath", this.uploadcover);
            }
            videoData.append("tempVideoPath", this.uploadVideo);
            videoData.append("title", this.video.title);
            videoData.append("partition", this.video.partition);
            videoData.append("tagList", this.video.tag);
            if (!judge.judgeUndefined(this.video.synopsis)) {
                videoData.append("synopsis", this.video.synopsis);
            }
            videoData.append("md5", this.md5);
            videoData.append("count",this.chunks.length);
            if (!judge.judgeUndefined(this.video.episode)) {
                videoData.append("episode", this.video.episode);
            }

            let config = {
                headers: {
                    "Content-Type": "multipart/form-data",
                    "token": this.user.token,
                }
            };
            axios.post(global.apihost + "/Video/uploadVideo", videoData, config).then(res => {
                console.log("上传视频返回", res.data);
                if (res.data.data == true) {
                    this.$notify.success("上传视频成功");
                    this.load()
                } else {
                    this.$notify.error(res.data.msg);
                }
            }).catch(e => {
                console.log("上传视频错误返回", e);
            });
        },
        delVideoTag(index) {
            this.$confirm('此操作将删除这个标签, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.video.tag.splice(index, 1);
            }).catch(() => {

            });
        },
        addTag() {
            if (this.video['tag'].length >= 10) {
                this.$notify.error("超出视频能添加的标签个数，请删除多余标签后再尝试添加。");
                return;
            }
            this.video['tag'].push(this.tag);
            this.tag = '';
        },
        load() {
            this.user={}
            this.chunks= []
            this.md5= ''
            this.noUpChunkNumList=[]
            this.imageUrl= ''
            this.uploadcover= ''
            this.uploadVideo= ''
            this.video= {
                tag: [],
                title: ''
            },
            this.progress= 0
            this.partition= {}
            this.episode= []
            this.tag= ''
            const userJSON = Cookies.get('user');
            if (judge.judgeNull(userJSON) || judge.judgeUndefined(userJSON)) {
                this.$notify.error("请先登录");
                return;
            }
            this.user = JSON.parse(userJSON);
            request.get('Partition/getAllPartitionList').then(res => {
                this.partition = res.data;
                console.log("获取分区数据", this.partition);
            }).catch(e => {
                console.log("获取分区数据错误", e);
            });
            request.get('Episode/getUserAllEpisode').then(res => {
                this.episode = res.data;
                console.log("获取用户创建的合集", this.episode);
            }).catch(e => {
                console.log("获取用户创建的合集错误", e);
            });
        },
        resUrlSplicing(src) {
            return global.reshost + src;
        },
        format(percentage) {
            if (percentage == 1) {
                return "校验Md5中";
            } else if (percentage == 100) {
                return "视频上传成功";
            }
            return percentage.toFixed(2) + '%';
        },
        coverUploadChange(file, fileList) {
            this.video.coverfile = file.raw;
            this.imageUrl = URL.createObjectURL(file.raw);
            this.uploadcover = '';
        },
        createChunk(file, chunkSize) {
            const result = [];
            this.progress = 1;
            for (let i = 0; i < file.size; i += chunkSize) {
                result.push(file.slice(i, i + chunkSize));
            }
            return result;
        },
        async uploadChange(file, fileList) {
            this.md5 = '';
            this.progress = 0;
            this.noUpChunkNumList = [];
            const isMP4 = file.raw.type === 'video/mp4';
            if (!isMP4) {
                this.$message.error('上传的视频只能是 MP4 格式!');
                return;
            }
            this.chunks = this.createChunk(file.raw, this.chunkSize);
            this.md5 = await filePare(this.chunks)
            this.video.title = file.name.substring(0, file.name.indexOf(".")).slice(0, 50);;
            console.log(this.chunks);
            console.log(this.md5);
            request.get("Video/getVideoByMd5AndCount", { params: { md5: this.md5, count: this.chunks.length } }).then(res => {
                console.log("获取视频数据成功", res.data);
                if (res.code == 200) {
                    if (res.data.state) {
                        if (res.data.me) {
                            if (res.data.video) {
                                if (res.data.master) {
                                    this.$notify.error("当前已有相同视频上传，如有疑问请联系管理员");
                                    this.progress = 0;
                                    return;
                                } else {
                                    this.uploadVideo = res.data.tempVideoPath;
                                    this.uploadcover = res.data.tempCoverPath;
                                    this.progress = 100;
                                    return;
                                }
                            } else {
                                for (let i = res.data.uploadedChunksNumList[res.data.uploadedChunksNumList.length - 1] + 1; i <= this.chunks.length; i++) {
                                    this.noUpChunkNumList.push(i);
                                }
                                console.log("再次上传切片", this.noUpChunkNumList);;
                                this.upfileChunks(0, this.md5, this.noUpChunkNumList, this.chunks);
                            }
                        } else {
                            this.$notify.error("当前已有相同视频上传，如有疑问请联系管理员");
                            this.progress = 0;
                            return;
                        }
                    } else {
                        for (let i = 1; i <= this.chunks.length; i++) {
                            this.noUpChunkNumList.push(i);
                        }
                        this.upfileChunks(0, this.md5, this.noUpChunkNumList, this.chunks);
                    }
                } else {
                    this.$notify.error(res.msg);
                    this.progress = 0;
                }

            }).catch(e => {
                console.log("获取视频数据错误", e);
                this.$notify.error(e.msg);
                this.progress = 0;
            })

        },
        upfileChunks(i, md5, noUpChunkNumList, chunks) {
            if (i >= noUpChunkNumList.length) {
                console.log("上传成功！！！");
                request.get("/Video/uploadFileChunkMergeByMd5", { params: { md5: md5 } }).then(res => {
                    if (res.code == 200) {
                        this.uploadcover = res.data.tempCoverPath;
                        this.uploadVideo = res.data.tempVideoPath;
                        this.progress = 100;
                        console.log("合并文件成功！", res);
                    } else {
                        console.log("合并文件失败")
                    }

                }).catch(e => {
                    console.log("合并文件错误", e)
                    this.$notify.error("获取视频第一帧错误，请刷新重试！")
                });
                return;
            }
            let videoChunkData = new FormData();
            videoChunkData.append("md5", md5);
            videoChunkData.append("num", noUpChunkNumList[i]);
            videoChunkData.append("count", chunks.length);
            videoChunkData.append("file", chunks[noUpChunkNumList[i] - 1])
            let config = {
                headers: {
                    "Content-Type": "multipart/form-data",
                    "token": this.user.token,
                }
            };
            axios.post(global.apihost + "/Video/uploadVideoChunkFile", videoChunkData, config).then(res => {
                console.log("上传返回", i, res.data);
                if (res.data.code == 200) {
                    this.upfileChunks(i + 1, md5, noUpChunkNumList, chunks);
                    this.progress = this.progress + (100 / noUpChunkNumList.length) >= 99 ? 99.99 : this.progress + (100 / noUpChunkNumList.length);
                } else {
                    this.$notify.error("视频上传错误", res.msg);
                }
            }).catch(e => {
                console.log("上传错误返回", e);
            });
        }

    }
}
</script>

<style scoped>
.upload {
    display: flex;
}

.video_progress {
    position: relative;
    top: 50%;
    transform: translateY(-50%);
}

.cover_upload {
    position: relative;
    border: 1px dashed #888;
    width: 320px;
    height: 180px;
    border-radius: 5px;
}

.cover_upload:hover {
    border: 1px dashed rgb(0, 150, 255);
}

.video_cover {
    width: 320px;
    height: 180px;
    object-fit: contain;
    background: #000;
}

.avatar-uploader-icon {
    font-size: 48px;
    color: #8c939d;
    width: 320px;
    height: 180px;
    line-height: 180px;
    text-align: center;
}

.from {
    width: 800px;
    margin: auto;
    padding: 30px;
    margin-top: 20px;
    background-color: white;
    border: 1px solid #888;
    box-shadow: 0px 0px 1px;
    border-radius: 5px;
}



.tag_box {
    display: block;
    word-wrap: break-word;
    word-break: break-all;
    width: 100%;
    margin-top: 15px;
    min-height: 40px;
    background-color: #EEE;
    border-radius: 5px;
    border: 1px solid #aaa;
}

.tag_box span {
    white-space: nowrap;
    margin: 0px 10px;
    padding: 5px;
    border-radius: 5px;
    font-size: 15px;
    box-shadow: 0 0 3px;
    background-color: white;
}
</style>