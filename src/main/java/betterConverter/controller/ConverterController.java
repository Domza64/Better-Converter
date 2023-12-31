package betterConverter.controller;

import betterConverter.utils.DownloadUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConverterController {

    @GetMapping("/getVideo")
    public ResponseEntity<Resource> getVideo(@RequestParam String id, @RequestParam String type) {
        Resource resource = DownloadUtils.getVideoOrSong(id, type);

        return ResponseEntity.ok()
                .header("X-Filename", resource.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
