package betterConverter.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.IOException;

@Controller
public class HelloController {
    private static LinkObject linkObject = new LinkObject("");
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newLink", linkObject);
        return "index";
    }

    @GetMapping("/download")
    public String info(Model model) {
        model.addAttribute("newLink", linkObject);
        return "download";
    }

    @PostMapping("/get-link")
    public String newLink(@ModelAttribute LinkObject newLink) {
        downloadSong(newLink.getValue());
        linkObject.setValue(newLink.getValue());
        return "redirect:/download";
    }

    private void downloadSong(String link) {
        String[] downloadSong = {"yt-dlp", "-x", "--audio-format", "mp3", link};
        try {
            Process execCommandSecond = new ProcessBuilder(downloadSong).directory(new File(System.getProperty("user.dir"))).start();
            execCommandSecond.waitFor();
            System.out.println("Sucess!");
        } catch (IOException e) {
            // handle exceptions
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
