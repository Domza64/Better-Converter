package betterConverter.springboot;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {
    private List<Song> downloadedSongs = new ArrayList<>(); // TODO - Replace with h2 database
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
        return "redirect:/?download=" + hashCode;
    }

    // TODO - Song object, has download hash id thing for download and file name and byte thing for storing actual file

//    @GetMapping("/download")
//    public StreamingResponseBody downloadFile(HttpServletResponse response, @RequestParam(required = false) String id) {
//        if (id == null) return null;
//
//        Song song = null;
//        for (Song s : downloadedSongs) {
//            if (String.valueOf(s.getId()).equals(id)) {
//                song = s;
//            }
//        }
//        if (song == null) return null; // TODO - Redirect to index
//
////        File file = new File(System.getProperty("user.dir") + "/tempDownloads/" + id + ".m4a");
//
//        File songFile = song.getData();
//
//        response.setContentType(song.getContentType());
//        response.setHeader(
//                HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + song.getTitle() + "\"");
//
//        return outputStream -> {
//            int bytesRead;
//            byte[] buffer = new byte[100];
//            try (InputStream inputStream = file.toURI().toURL().openStream()) {
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//        };
//    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadSong(@RequestParam(required = false) String id) {
        if (id == null) return null;

        System.out.println(downloadedSongs);

        Song song = null;
        for (Song s : downloadedSongs) {
            if (String.valueOf(s.getId()).equals(id)) {
                song = s;
            }
        }
        if (song == null) return null; // TODO - Redirect to index

        System.out.println("hmhm");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + song.getTitle() + "\"")
                .body(song.getData());
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
        try {
            String extension = "." + video.audioFormats().get(0).extension().value();
            File data = new File(System.getProperty("user.dir") + "/tempDownloads/" + hashCode + extension);
            Song song = new Song(video.details().title(), hashCode, extension, Files.readAllBytes(data.toPath()));
            downloadedSongs.add(song);
            // Delete song from filesistem
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Err");
        }
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
