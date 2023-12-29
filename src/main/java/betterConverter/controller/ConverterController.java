package betterConverter.controller;

import betterConverter.converterUtils.VideoDownloader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;

@RestController
public class ConverterController {

    @GetMapping("/getVideo")
    public ResponseEntity<Resource> getVideo(@RequestParam String id, @RequestParam String type) {
        File file = VideoDownloader.downloadSong(id, type);
        assert file != null;
        Resource resource = new FileSystemResource(file);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Filename", resource.getFilename());


        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
