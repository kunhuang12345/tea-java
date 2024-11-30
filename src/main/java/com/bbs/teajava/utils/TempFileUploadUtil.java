package com.bbs.teajava.utils;

import com.bbs.teajava.constants.BucketNameEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;

/**
 * @author kunhuang
 */
@Component
public class TempFileUploadUtil {
    public static void upload(MinioUtil minio, MultipartFile file) throws IOException {
        HttpSession session = SessionUtils.getSession();
        Random random = RandomUtil.getRandom();
        int randomNum = random.nextInt(10000000);
        String randomStr = String.format("%08d", randomNum);
        session.setAttribute("randomStr", randomStr);
        session.setAttribute("fileName", file.getOriginalFilename());
        minio.uploadFile(BucketNameEnum.TEMP.getValue(), FileNameUtils.attachment(file.getOriginalFilename(), randomStr), file.getInputStream());
    }

    private TempFileUploadUtil() {
    }
}
