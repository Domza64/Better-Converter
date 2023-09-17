package betterConverter.springboot;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.File;
import java.io.InputStream;

@Controller
public class HelloController {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("linkDto", new LinkDto());
        return "index";
    }

    @PostMapping("/process-link")
    public String newLink(@ModelAttribute LinkDto linkDto) {
        String link = linkDto.getLinkString();
        if (link == null || link.equals("")) return "redirect:/?inputempty";
        int hashCode = linkDto.hashCode();
        /*
            While the song is downloading on the server, it will not redirect so
            js function on frontend should display some kind of "Please wait..." until song is downloaded
            and client is redirected
        */
        downloadSong(linkDto.getLinkString(), hashCode);
        return "redirect:/download?id=" + hashCode;
    }

    // TODO - Song object, has download hash id thing for download and file name and byte thing for storing actual file

    @GetMapping("/download")
    public StreamingResponseBody downloadFile(HttpServletResponse response, @RequestParam(required = false) String id) {

        if (id == null) return null;

        File file = new File(System.getProperty("user.dir") + "/tempDownloads/" + id + ".m4a");

        response.setContentType("m4a");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + id + "\"");

        return outputStream -> {
            int bytesRead;
            byte[] buffer = new byte[100];
            try (InputStream inputStream = file.toURI().toURL().openStream()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        };
    }

    private void downloadSong(String linkString, int hashCode) {
        YoutubeDownloader downloader = new YoutubeDownloader();

        RequestVideoInfo request = new RequestVideoInfo(getIdFromLink(linkString));
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        VideoInfo video = response.data();

        // sync downloading
        RequestVideoFileDownload requestt = new RequestVideoFileDownload(video.audioFormats().get(0))
                // optional params
                .saveTo(new File(System.getProperty("user.dir") + "/tempDownloads")) // by default "videos" directory
                // Replace name with hashCode
                .renameTo(String.valueOf(hashCode)) // video.details().title()
                .overwriteIfExists(true); // if false and file with such name already exits sufix will be added video(1).mp4
        Response<File> responsee = downloader.downloadVideoFile(requestt);
        File data = responsee.data();
    }

    private String getIdFromLink(String linkString) {
        try {
            return linkString.split("=")[1];
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
