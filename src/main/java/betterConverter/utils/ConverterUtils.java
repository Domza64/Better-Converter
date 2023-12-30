package betterConverter.utils;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConverterUtils {

    public static File downloadSong(String id, String type) {
        YoutubeDownloader downloader = new YoutubeDownloader();

        RequestVideoInfo request = new RequestVideoInfo(id);
        Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(request);
        VideoInfo videoInfo = videoInfoResponse.data();

        RequestVideoFileDownload downloadResuest;
        if (type.equals("mp4")) {
            downloadResuest = new RequestVideoFileDownload(videoInfo.bestVideoWithAudioFormat());
        }
        else {
            downloadResuest = new RequestVideoFileDownload(videoInfo.bestAudioFormat());
        }
        downloadResuest.renameTo(videoInfo.details().title());

        Response<File> file = downloader.downloadVideoFile(downloadResuest);

        if (file.data().toPath().toString().endsWith(".m4a")) {
            return convertM4AToMP3(file.data().toPath().toString());
        }
        else {
            System.out.println(file.data().toPath());
        }

        return file.data();
    }

    public static File convertM4AToMP3(String inputFilePath) {
        try {
            String currentWorkingDir = System.getProperty("user.dir");
            // TODO - Cleanup converted files and handle spaces and maybe special characters in titles
            inputFilePath = currentWorkingDir + "/" + inputFilePath;
            String outputFilePath = inputFilePath.replace(".m4a", ".mp3");
            String command = String.format("ffmpeg -i %s -acodec libmp3lame -aq 4 -y %s", inputFilePath, outputFilePath);

            System.out.println("Current Working Directory: " + outputFilePath);

            Process process = Runtime.getRuntime().exec(command);

            // Read the output to check for any errors
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Conversion successful");
                return new File(outputFilePath);
            } else {
                System.err.println("Conversion failed");
                return null;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
