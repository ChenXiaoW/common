package ink.cwblog.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @auther chenw
 * @date 2021/10/22 14:42
 *
 * formdata 请求上传文件
 *
 */
@Slf4j
public class FileUploadUtil {

    /**
     * 上传文件
     * @param url
     * @param bytes
     * @param fileName
     * @return
     * @throws IOException
     */
    public static JSONObject httpClientUploadFile(String url, byte[] bytes, String fileName) throws IOException {
        //文件名
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        String boundary = "--------------" + System.currentTimeMillis();
        httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);

        //HttpEntity builder
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //字符编码
        builder.setCharset(Charset.forName("UTF-8"));
        //模拟浏览器
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //boundary
        builder.setBoundary(boundary);
        //multipart/form-data
        builder.addPart("multipartFile", new ByteArrayBody(bytes, fileName));
        //其他参数
        builder.addTextBody("filename", fileName, ContentType.create("text/plain", Consts.UTF_8));
        //HttpEntity
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        @Cleanup CloseableHttpClient httpClient = HttpClients.createDefault();
        // 执行提交
        HttpResponse response = httpClient.execute(httpPost);
        log.info("企业微信上传文件响应信息:{}", response);
        //响应
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            // 将响应内容转换为字符串
            String resp = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            JSONObject result = JSONObject.parseObject(resp);
            if (result.getInteger("errcode") != 0) {
                log.error("企业微信上传文件请求失败:{}", resp);
                return null;
            }
            return result;
        }
        return null;
    }
}
