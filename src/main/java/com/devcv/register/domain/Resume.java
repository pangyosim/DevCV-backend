package com.devcv.register.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.register.domain.enumtype.ResumeStatus;
import com.devcv.register.infrastructure.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_resume")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Resume extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumeId;

    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String resumeFilePath;

    // 관리자 승인 상태 = 승인대기 default
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ResumeStatus status = ResumeStatus.승인대기;

    // 기술스택
    @Convert(converter = ListStringConverter.class)
    private List<String> isStack;

    // 관련 이미지(썸네일, 상세이미지)
    @ElementCollection
    @Builder.Default
    private List<ResumeImage> imageList = new ArrayList<>();// ResumeImage와 일대다 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    public void addImage(ResumeImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String resumeImgPath) {
        ResumeImage resumeImage = ResumeImage.builder()
                .resumeImgPath(resumeImgPath)
                .build();
        addImage(resumeImage);
    }




}
