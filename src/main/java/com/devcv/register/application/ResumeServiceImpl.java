package com.devcv.register.application;

import com.devcv.register.domain.Resume;
import com.devcv.register.domain.Category;
import com.devcv.common.util.S3Uploader;
import com.devcv.register.domain.ResumeImage;
import com.devcv.register.domain.dto.CategoryDTO;
import com.devcv.register.domain.dto.ResumeDTO;
import com.devcv.register.domain.dto.ResumeImageDTO;
import com.devcv.register.domain.enumtype.ResumeStatus;
import com.devcv.register.repository.CategoryRepository;
import com.devcv.register.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.devcv.register.application.ResumeMapper.dtoToEntity;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

   private  final ResumeRepository resumeRepository;
   private final CategoryRepository categoryRepository;
   private final S3Uploader s3Uploader;

    @Override
    public ResumeDTO register(ResumeDTO resumeDTO, MultipartFile resumeFile, List<MultipartFile> images) {
        try {
            // Category 저장
            CategoryDTO categoryDTO = resumeDTO.getCategory();
            Category category = categoryRepository.findByCompanyTypeAndStackType(
                    categoryDTO.getCompanyType(),
                    categoryDTO.getStackType()
            ).orElseGet(() -> {
                Category newCategory = new Category(categoryDTO.getCompanyType(), categoryDTO.getStackType());
                return categoryRepository.save(newCategory);
            });


            // PDF 파일 업로드
            String resumeFilePath = s3Uploader.upload(resumeFile);
            log.debug(resumeFilePath);
            resumeDTO.setResumeFilePath(resumeFilePath);

            Resume resume = dtoToEntity(resumeDTO, category);

            // 상세이미지 업로드
            if (images != null && !images.isEmpty()) {
                for (int i = 0; i < images.size(); i++) {
                    MultipartFile image = images.get(i);
                    String imagePath = s3Uploader.upload(image);
                    ResumeImageDTO resumeImageDTO = ResumeImageDTO.builder()
                            .resumeImgPath(imagePath)
                            .ord(i)
                            .build();
                    resume.addImage(ResumeImageMapper.dtoToEntity(resumeImageDTO));
                }
            }

            // 상태 설정
            resume.setStatus(ResumeStatus.승인대기);

            Resume savedResume = resumeRepository.save(resume);
            log.info("Saved resume ID: " + savedResume.getResumeId());
            return ResumeMapper.entityToDto(savedResume);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

}
