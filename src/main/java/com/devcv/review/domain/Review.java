package com.devcv.review.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.Member;
import com.devcv.order.domain.Order;
import com.devcv.resume.domain.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_review")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"resume", "member", "order", "commentList"})
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;

    private String text;

    private String reviewerNickname;
    private String sellerNickname;

    // 댓글 리스트
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setReview(this);
    }

    public void removeComment(Comment comment) {
        commentList.remove(comment);
        comment.setReview(null);
    }

    // 수정 관련 메서드
    public void changeGrade(int grade) {
        this.grade=grade;
    }

    public void changeText(String text) {this.text = text;}

}
