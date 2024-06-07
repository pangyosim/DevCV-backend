package com.devcv.register.application;

import com.devcv.register.domain.dto.ResumeDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
public interface ResumeService {
    //이력서 판매등록
    ResumeDTO register(ResumeDTO resumeDTO, MultipartFile resumeFile, List<MultipartFile> images);


}
