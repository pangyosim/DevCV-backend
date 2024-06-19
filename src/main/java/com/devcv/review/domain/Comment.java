package com.devcv.review.domain;

import com.devcv.common.domain.BaseTimeEntity;
import com.devcv.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"review", "member"})
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String text;

    private String commenterNickname;
}