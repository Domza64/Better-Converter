package betterConverter.converterUtils;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import org.springframework.security.core.parameters.P;

import java.io.File;

public class VideoDownloader {

    public static File downloadSong(String id, String type) {
        System.out.println("Downloading!");

        YoutubeDownloader downloader = new YoutubeDownloader();

        RequestVideoInfo request = new RequestVideoInfo(id);
        Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(request);
        VideoInfo videoInfo = videoInfoResponse.data();

        RequestVideoFileDownload downloadResuest;
        if (type.equals("mp4")) {
            downloadResuest = new RequestVideoFileDownload(videoInfo.bestVideoFormat());
        }
        else {
            downloadResuest = new RequestVideoFileDownload(videoInfo.bestAudioFormat());
        }
        downloadResuest.renameTo(videoInfo.details().title());
        // TODO - Takes really long to download videos, audio is really quick... see why
        Response<File> file = downloader.downloadVideoFile(downloadResuest);
        return file.data();
    }
}
