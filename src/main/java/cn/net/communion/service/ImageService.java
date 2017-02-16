package cn.net.communion.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.net.communion.constant.ErrCode;
import cn.net.communion.hepler.ImageHelper;
import cn.net.communion.hepler.NetImageHelper;
import cn.net.communion.tool.JsonObject;



@Service
@Configuration
@PropertySource("classpath:image.properties")
public class ImageService {
    @Autowired
    private ImageHelper imageHepler;
    @Autowired
    private NetImageHelper netImageHepler;
    private String serverIp;

    @Autowired
    public ImageService(Environment env) {
        serverIp = env.getProperty("image.server.ip");
    }

    public String upload(MultipartFile img, String quality) {
        try {
            return new JsonObject().setErrcode(ErrCode.Success)
                    .putData("url", serverIp + imageHepler.upload(reduceImg(img, quality)))
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    public String upload(MultipartFile img, String quality, int width, int height) {
        try {
            return new JsonObject().setErrcode(ErrCode.Success)
                    .putData("url",
                            serverIp + imageHepler.upload(reduceImg(img, quality, width, height)))
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    public String upload(MultipartFile[] imgs, String quality) {
        try {
            List<String> urls = new ArrayList<String>();
            for (MultipartFile img : imgs) {
                urls.add(serverIp + imageHepler.upload(reduceImg(img, quality)));
            }
            return new JsonObject().dataListString(ErrCode.Success, urls);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    public String upload(MultipartFile[] imgs, String quality, int width, int height) {
        try {
            List<String> urls = new ArrayList<String>();
            for (MultipartFile img : imgs) {
                urls.add(serverIp + imageHepler.upload(reduceImg(img, quality, width, height)));
            }
            return new JsonObject().dataListString(ErrCode.Success, urls);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    public String upload(String url, String quality) {
        try {
            return new JsonObject().setErrcode(ErrCode.Success)
                    .putData("url",
                            serverIp + imageHepler
                                    .upload(reduceImg(netImageHepler.getBuffer(url), quality)))
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    public String upload(String url, String quality, Integer width, Integer height) {
        try {
            return new JsonObject().setErrcode(ErrCode.Success)
                    .putData("url", serverIp + imageHepler.upload(
                            reduceImg(netImageHepler.getBuffer(url), quality, width, height)))
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    public String upload(String[] imgurls, String quality) {
        try {
            List<String> urls = new ArrayList<String>();
            for (String url : imgurls) {
                urls.add(serverIp
                        + imageHepler.upload(reduceImg(netImageHepler.getBuffer(url), quality)));
            }
            return new JsonObject().dataListString(ErrCode.Success, urls);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    public String upload(String[] imgurls, String quality, Integer width, Integer height) {
        try {
            List<String> urls = new ArrayList<String>();
            for (String url : imgurls) {
                urls.add(serverIp + imageHepler
                        .upload(reduceImg(netImageHepler.getBuffer(url), quality, width, height)));
            }
            return new JsonObject().dataListString(ErrCode.Success, urls);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject().setErrcode(ErrCode.Upload_Image_Failed).toString();
        }
    }

    private byte[] reduceImg(MultipartFile img, String quality) {
        try {
            BufferedImage image = ImageIO.read(img.getInputStream());
            BufferedImage newImage = image;
            if ("png".equals(FilenameUtils.getExtension(img.getOriginalFilename()).toLowerCase())) {
                newImage = new BufferedImage(image.getWidth(), image.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                newImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            }
            return compress(newImage, quality);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] reduceImg(MultipartFile img, String quality, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(img.getInputStream());
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            newImage.createGraphics().drawImage(
                    image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0,
                    Color.WHITE, null);
            return compress(newImage, quality);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] reduceImg(BufferedImage image, String quality) {
        try {
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            return compress(newImage, quality);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] reduceImg(BufferedImage image, String quality, int width, int height) {
        try {
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            newImage.createGraphics().drawImage(
                    image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0,
                    Color.WHITE, null);
            return compress(newImage, quality);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] compress(BufferedImage image, String quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = (ImageWriter) writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        float level = quality.equals("high") ? 0.8f : quality.equals("middle") ? 0.4f : 0.2f;
        // Check if canWriteCompressed is true
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(level);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);
        ios.close();
        bos.close();
        System.gc();
        return bos.toByteArray();
    }
}
