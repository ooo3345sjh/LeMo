package kr.co.Lemo.service;

import kr.co.Lemo.dao.MyDAO;
import kr.co.Lemo.domain.UserVO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @since 2023/03/26
 * @author 서정현
 * @apiNote 나의 정보 Service(충돌방지를 위해 임시로 만듬)
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MyInfoService {
    private final MyDAO myDAO;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // @since 2023/03/27
    public String getUploadPath(String pathDir)throws Exception{
        log.debug("Myservice getUploadPath...");

        File profileDir = new File(uploadPath+"/"+pathDir);
        if(!profileDir.exists())
            Files.createDirectories(profileDir.toPath());

        return profileDir.getAbsolutePath();
    }

    // @since 2023/03/27
    public int usaveProfile(MultipartFile photo, UserVO userVO) {
        log.debug("Myservice usaveProfile...");

        int result = 0;
        String newName = null;
        try{
            String oriName = photo.getOriginalFilename();
            String ext = oriName.substring(oriName.indexOf("."));
            newName = UUID.randomUUID() + ext;

            removeFile(userVO.getPhoto());
            myDAO.updateProfile(newName, userVO.getUser_id());
            photo.transferTo(new File(getUploadPath("profile"), newName));
            result = 1;
        } catch (Exception e){
            result = 0;
            log.error(e.getMessage());
            log.error(e.toString());
        } finally {
            if(result == 1){
                userVO.setPhoto(newName);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
                );
            }
        }
        return result;
    }

    // @since 2023/03/27
    public int removeProfile(UserVO userVO) {
        int result = 0;
        try{
            result = myDAO.updateProfile(null, userVO.getUser_id());

            if(result == 1)
                removeFile(userVO.getPhoto());
        } catch (Exception e){
            log.error(e.getMessage());
            log.error(e.getLocalizedMessage());
            result = 0;
        } finally {
            if(result == 1)
                userVO.setPhoto(null);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
                );
        }
        return result;
    }


    // @since 2023/03/27
    public void removeFile(String fileName) throws Exception {
        log.debug("Myservice removeFile...");

        if(fileName != null){
            // 시스템 경로
            File file = new File(getUploadPath("profile"), fileName);
            if(file.exists())
                file.delete();
        }
    }
}
