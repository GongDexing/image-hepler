package cn.net.communion.hepler;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.stereotype.Component;

@Component
public class ImageHelper {
    private StorageClient1 client;

    public ImageHelper() {
        try {
            ClientGlobal.init("/client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            client = new StorageClient1(trackerServer, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String upload(byte[] pic, String name, long size)
            throws FileNotFoundException, IOException, MyException {
        String extension = FilenameUtils.getExtension(name);
        // 设置图片meta信息
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("filename", name);
        meta_list[1] = new NameValuePair("fileext", extension);
        meta_list[2] = new NameValuePair("filesize", String.valueOf(size));
        // 上传且返回path
        return client.upload_file1(pic, extension, null);
    }

    public String upload(byte[] pic) throws FileNotFoundException, IOException, MyException {
        if (client == null) {
            System.out.println("[ERROR]client is null");
        }
        return client.upload_file1(pic, "jpg", null);
    }
}
