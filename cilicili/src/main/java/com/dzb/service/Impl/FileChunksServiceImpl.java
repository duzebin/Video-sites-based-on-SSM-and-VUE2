package com.dzb.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.dzb.controller.dto.VideoFilesDto;
import com.dzb.dao.FileChunksDao;
import com.dzb.entity.FileChunks;
import com.dzb.entity.UserInfo;
import com.dzb.entity.Video;
import com.dzb.entity.VideoFiles;
import com.dzb.exception.ServiceException;
import com.dzb.service.FileChunksService;
import com.dzb.service.VideoFilesService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileChunksServiceImpl implements FileChunksService {
    @Autowired
    private FileChunksDao fileChunksDao;
    @Autowired
    private VideoFilesService videoFilesService;

    private static final String BASE_FILE_PATH = System.getProperty("user.dir") + "/files";
    private static final String AVATAR_FILE_PATH = "/avatar/";
    private static final String VIDEO_FILE_PATH = "/video/";
    private static final String VIDEO_COVER_FILE_PATH = VIDEO_FILE_PATH + "cover/";
    private static final String VIDEO_CHUNK_FILE_PATH = VIDEO_FILE_PATH + "chunk/";
    private static final String VIDEO_TEMPORARY_PATH = VIDEO_CHUNK_FILE_PATH + "temporary/";
    private static final String VIDEO_TEMPORARY_COVER_FILE_PATH = VIDEO_TEMPORARY_PATH + "cover/";


    private static String splicingFileName(int uid, String md5, String suffix) {
        return uid + "_" + md5 + suffix;
    }

    private static void delFileChunkDirectory(File file) {
        if (!file.exists()) {
            throw new ServiceException("删除文件错误，请联系管理员");
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    delFileChunkDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        file.delete();
    }

    private static void fetchPic(File file, String framefile) throws Exception {
        FileUtil.mkParentDirs(framefile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
        ff.start();
        int lenght = ff.getLengthInFrames();

        File targetFile = new File(framefile);
        int i = 0;

        Frame frame = null;
        while (i < lenght) {

            frame = ff.grabFrame();

            if ((i > 20) && (frame.image != null)) {
                break;
            }
            i++;
        }

        String imgSuffix = "jpg";
        if (framefile.indexOf('.') != -1) {
            String[] arr = framefile.split("\\.");
            if (arr.length >= 2) {
                imgSuffix = arr[1];
            }
        }

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        BufferedImage bi = new BufferedImage(owidth, oheight, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(owidth, oheight, Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(bi, imgSuffix, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ff.stop();

    }

    private static void upFile(MultipartFile file, String filePath) {
        String path = BASE_FILE_PATH + filePath;
        try {
            FileUtil.mkParentDirs(path);
            file.transferTo(FileUtil.file(path));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("文件上传错误,请联系管理员");
        }

    }

    @Override
    public String upAvatar(MultipartFile file, String uid) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!".jpg".equals(suffix)) {
            throw new ServiceException("仅支持.jpg格式图片");
        }
        String avatarPath = AVATAR_FILE_PATH + uid + suffix;
        upFile(file, avatarPath);
        return avatarPath;
    }

    @Override
    public List<Integer> getChunksNumListByMd5(String md5) {
        return this.fileChunksDao.getChunksNumListByMd5(md5);
    }

    @Override
    public boolean uploadVideoChunkFile(MultipartFile file, String md5, int num) {
        String videoChunkPath = BASE_FILE_PATH + VIDEO_CHUNK_FILE_PATH + md5 + "/" + num;
        FileUtil.mkParentDirs(videoChunkPath);
        try {
            file.transferTo(FileUtil.file(videoChunkPath));
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public int insertFileChunksInfo(FileChunks fileChunks) {
        return this.fileChunksDao.insertFileChunksInfo(fileChunks);
    }

    @Override
    public List<FileChunks> getFileChunkListByMd5(String md5) {
        return this.fileChunksDao.getFileChunkListByMd5(md5);
    }

    @Override
    public Map<String, Object> uploadFileChunkMergeByMd5AndUid(String md5, int uid) {
        String videoChunkPath = BASE_FILE_PATH + VIDEO_CHUNK_FILE_PATH + md5 + "/";
        String videoSuffix = ".mp4";
        String imgSuffix = ".jpg";
        String temporaryVideoPath = BASE_FILE_PATH + VIDEO_TEMPORARY_PATH + splicingFileName(uid, md5, videoSuffix);
        String temporaryCoverPath = BASE_FILE_PATH + VIDEO_TEMPORARY_COVER_FILE_PATH + splicingFileName(uid, md5, imgSuffix);
        FileUtil.mkParentDirs(temporaryVideoPath);

        if (StrUtil.isEmpty(videoChunkPath)) {
            throw new ServiceException("系统错误，请联系管理员");
        }
        File[] files = new File(videoChunkPath).listFiles();
        List<File> fileList = Arrays.stream(files)
                .sorted((o1, o2) -> Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName()))
                .collect(Collectors.toList());
        fileList.forEach(it -> {
            System.out.println("fileName: " + it.getName());
        });

        RandomAccessFile write = null;
        try {
            write = new RandomAccessFile(temporaryVideoPath, "rw");
            byte[] buffer = new byte[1024 * 1024];
            for (File file : fileList) {
                RandomAccessFile read = new RandomAccessFile(file, "r");
                int len = -1;
                while ((len = read.read(buffer)) != -1) {
                    write.write(buffer, 0, len);
                }
                read.close();
            }
            delFileChunkDirectory(new File(videoChunkPath));
            VideoFiles videoFiles = new VideoFiles();
            videoFiles.setUid(uid);
            videoFiles.setMd5(md5);
            videoFiles.setCount(this.fileChunksDao.deleteFileChunkInfo(md5));
            videoFiles.setState(1);
            this.videoFilesService.updateVideoFilesStateByVideoFile(videoFiles);
            fetchPic(new File(temporaryVideoPath), temporaryCoverPath);
            Map<String, Object> map = new HashMap<>();
            map.put("upload", true);
            map.put("tempCoverPath", VIDEO_TEMPORARY_COVER_FILE_PATH + splicingFileName(uid, md5, imgSuffix));
            map.put("tempVideoPath", VIDEO_TEMPORARY_PATH + splicingFileName(uid, md5, videoSuffix));
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (write != null) {
                try {
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void deleteFileChunkInfo(String md5) {
        this.fileChunksDao.deleteFileChunkInfo(md5);
    }

    @Override
    public VideoFilesDto getTempVideoInfoByMd5AndUid(String md5, int uid) {
        VideoFilesDto videoFilesDto = new VideoFilesDto();
        String videoSuffix = ".mp4";
        String imgSuffix = ".jpg";
        videoFilesDto.setTempVideoPath(VIDEO_TEMPORARY_PATH + splicingFileName(uid, md5, videoSuffix));
        videoFilesDto.setTempCoverPath(VIDEO_TEMPORARY_COVER_FILE_PATH + splicingFileName(uid, md5, imgSuffix));
        return videoFilesDto;
    }

    @Override
    public String moveTempVideoCoverToVideoCoverPath(String tempVideoCoverPath, int uid, String md5) {
        File tempFile = new File(BASE_FILE_PATH + tempVideoCoverPath);
        if (!tempFile.exists()) {
            throw new ServiceException("上传视频失败，请联系管理员");
        }
        String videoCoverPath = VIDEO_COVER_FILE_PATH + uid + "_" + md5 + tempFile.getName().substring(tempFile.getName().lastIndexOf("."));
        File file = new File(BASE_FILE_PATH + videoCoverPath);
        try {
            if (file.exists()) {
                delFileChunkDirectory(file);
            }
            if (tempFile.renameTo(file)) {
                return videoCoverPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadVideoCover(MultipartFile file, int uid, String md5) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!".jpg".equals(suffix)) {
            throw new ServiceException("仅支持.jpg格式图片");
        }
        String videoCoverPath = VIDEO_COVER_FILE_PATH + uid + "_" + md5 + suffix;
        upFile(file, videoCoverPath);
        return videoCoverPath;
    }

    @Override
    public String moveTempVideoToVideoPath(String tempVideoPath, int uid, String md5) {
        File tempFile = new File(BASE_FILE_PATH + tempVideoPath);
        if (!tempFile.exists()) {
            throw new ServiceException("上传视频失败，请联系管理员");
        }
        String videoPath = VIDEO_FILE_PATH + uid + "_" + md5 + tempFile.getName().substring(tempFile.getName().lastIndexOf("."));
        File file = new File(BASE_FILE_PATH + videoPath);
        try {
            if (file.exists()) {
                delFileChunkDirectory(file);
            }
            if (tempFile.renameTo(file)) {
                return videoPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteVideoFileByVideo(Video video) {
        delFileChunkDirectory(new File(BASE_FILE_PATH + video.getCover()));
        delFileChunkDirectory(new File(BASE_FILE_PATH + video.getSrc()));
        this.videoFilesService.deleteVideoFileByVid(video.getVid());
    }

    @Override
    public String updateVideoCover(MultipartFile file, String filePath, int uid) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!".jpg".equals(suffix)) {
            throw new ServiceException("仅支持.jpg格式图片");
        }
        delFileChunkDirectory(new File(BASE_FILE_PATH + filePath));
        String newFilePath = VIDEO_COVER_FILE_PATH + uid + "_" + System.currentTimeMillis() + suffix;
        upFile(file, newFilePath);
        return newFilePath;
    }

    @Override
    public boolean deleteVideoFileByUid(Integer uid) {
        List<VideoFiles> videoFilesList = this.videoFilesService.getVideoFileListByUid(uid);
        for (int i = 0; i < videoFilesList.size(); i++) {
            delFileChunkDirectory(new File(splicingFileName(uid, videoFilesList.get(i).getMd5(), ".mp4")));
        }
        return false;
    }

    @Override
    public boolean deleteUserAvatar(Integer uid) {
        delFileChunkDirectory(new File(BASE_FILE_PATH + AVATAR_FILE_PATH + uid + ".jpg"));
        return true;
    }

    @Override
    public String reviseAvatar(MultipartFile file, UserInfo userInfo) {
        delFileChunkDirectory(new File(BASE_FILE_PATH + userInfo.getAvatar()));
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!".jpg".equals(suffix)) {
            throw new ServiceException("仅支持.jpg格式图片");
        }
        String avatarPath = AVATAR_FILE_PATH + userInfo.getUid() + "_" + System.currentTimeMillis() + suffix;
        upFile(file, avatarPath);
        return avatarPath;
    }


}
