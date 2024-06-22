package com.devcv.resume.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.Member;
import com.devcv.resume.domain.enumtype.ResumeStatus;
import com.devcv.resume.infrastructure.ListStringConverter;
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
    @Column(name = "resumeid")
    private Long resumeId;

    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String resumeFilePath;
    @Column(nullable = false)
    private boolean delFlag;

    // 관리자 승인 상태 = 승인대기 default
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ResumeStatus status = ResumeStatus.승인대기;

    // 기술스택
    @Convert(converter = ListStringConverter.class)
    private List<String> stack;

    // 관련 이미지(썸네일, 상세이미지)
    @ElementCollection
    @Builder.Default
    private List<ResumeImage> imageList = new ArrayList<>();// ResumeImage와 일대다 관계

    // 카테고리 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    // 회원 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void addImage(ResumeImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    // 수정 관련 메서드
    public void changePrice(Integer price) {
        this.price = price;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }


    public void changeStack(List<String> stack) {
        this.stack = stack;
    }
    public void changeCategory(Category category) {
        this.category = category;
    }

    public void clearList() {
        this.imageList.clear();
    }


    public void addImageString(String resumeImgPath) {
        ResumeImage resumeImage = ResumeImage.builder()
                .resumeImgPath(resumeImgPath)
                .build();
        addImage(resumeImage);
    }




}
