package betterConverter.utils;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Path;

public class DownloadUtils {

    public static Resource getVideoOrSong(String id, String type) {
        // Initialize downloader
        YoutubeDownloader downloader = new YoutubeDownloader();

        // Get video info
        VideoInfo videoInfo = getVideoInfo(downloader, id);

        // Get file
        File file = downloadFile(type, videoInfo, downloader);

        // If audio, convert to mp3
        Path filePath = file.toPath();
        if (filePath.toString().endsWith(".m4a")) {
            File mp3File = ConvertUtils.convertM4AToMP3(filePath);
            if (mp3File != null) file = mp3File;
        }

        // Get title and rename file
        String title = videoInfo.details().title();
        File newFile = new File(file.getParent() + "/" + title + getFileExtension(file.toPath()));
        boolean isRenamed = file.renameTo(newFile);

        // Create resource to return it
        Resource resource;
        if (isRenamed) {
            resource = new FileSystemResource(newFile);
        } else {
            resource = new FileSystemResource(file);
        }

        // Delete files on the disk
        cleanup();

        return resource;
    }

    private static File downloadFile(String type, VideoInfo videoInfo, YoutubeDownloader downloader) {
        RequestVideoFileDownload downloadRequest;
        if (type.equals("mp4")) {
            downloadRequest = new RequestVideoFileDownload(videoInfo.bestVideoWithAudioFormat());
        } else {
            downloadRequest = new RequestVideoFileDownload(videoInfo.bestAudioFormat());
        }
        return downloader.downloadVideoFile(downloadRequest).data();
    }

    private static VideoInfo getVideoInfo(YoutubeDownloader downloader, String id) {
        return downloader.getVideoInfo(new RequestVideoInfo(id)).data();
    }

    private static void cleanup() {
        // TODO - Delete converted files in the end
    }

    private static String getFileExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }

        return "." + fileName.substring(dotIndex + 1);
    }
}
