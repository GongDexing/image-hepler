package cn.net.communion.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.net.communion.service.ImageService;

@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/app/img/upload")
    public String imgUpload(@RequestParam final MultipartFile img,
            @RequestParam(required = false) final Integer width,
            @RequestParam(required = false) final Integer height,
            // low:低质量，middle:中等质量，high:高质量
            @RequestParam(defaultValue = "low", required = false) final String quality) {
        return (width == null || height == null || width <= 0 || height <= 0)
                ? imageService.upload(img, quality)
                : imageService.upload(img, quality, width, height);
    }

    @RequestMapping(value = "/app/imgs/upload")
    public String imgsUpload(@RequestParam final MultipartFile[] imgs,
            @RequestParam(required = false) final Integer width,
            @RequestParam(required = false) final Integer height,
            // low:低质量，middle:中等质量，high:高质量
            @RequestParam(defaultValue = "low", required = false) final String quality) {
        return (width == null || height == null || width <= 0 || height <= 0)
                ? imageService.upload(imgs, quality)
                : imageService.upload(imgs, quality, width, height);
    }

    @RequestMapping(value = "/app/imgurl/upload")
    public String imgurlUpload(@RequestParam final String url,
            @RequestParam(required = false) final Integer width,
            @RequestParam(required = false) final Integer height,
            // low:低质量，middle:中等质量，high:高质量
            @RequestParam(defaultValue = "low", required = false) final String quality) {
        return (width == null || height == null || width <= 0 || height <= 0)
                ? imageService.upload(url, quality)
                : imageService.upload(url, quality, width, height);

    }

    @RequestMapping(value = "/app/imgurls/upload")
    public String imgurlsUpload(@RequestParam final String urls,
            @RequestParam(required = false) final Integer width,
            @RequestParam(required = false) final Integer height,
            // low:低质量，middle:中等质量，high:高质量
            @RequestParam(defaultValue = "low", required = false) final String quality) {
        return (width == null || height == null || width <= 0 || height <= 0)
                ? imageService.upload(urls.split(","), quality)
                : imageService.upload(urls.split(","), quality, width, height);

    }
}
