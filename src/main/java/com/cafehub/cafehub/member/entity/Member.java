package com.cafehub.cafehub.member.entity;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
import com.cafehub.cafehub.comment.entity.Comment;
import com.cafehub.cafehub.common.entity.BaseEntity;
import com.cafehub.cafehub.likeReview.entity.LikeReview;
import com.cafehub.cafehub.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String nickname;

    @Lob
    private String userPhotoUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }


    /**
     * Member 테이블의 경우는 양방향 매핑을 해서는 안된다.
     * 개인적인 생각으로는 Member 도메인은 OneToOne매핑도 걸지 않고 그 순수성을 가져가야 한다고 생각한다.
     *
     * 양방향 매핑의 경우 다음과 같은 단점이 있다.
     * 1. 메모리 사용
     * 양방향 매핑을 사용하면 연관된 엔티티를 로드할 때 메모리 사용량이 증가할수 있다.
     * 특히 지연로딩을 사용할 때, 프록시 객체를 유지하기 위해 추가적인 메모리가 필요하다.
     * 2. 성능 문제
     * 한 엔티티를 조회할 때 연관된 엔티티를 프록시로 가져오기 때문에 추가적인 쿼리가 실행될 수 있다.
     * 3. 순환 참조 문제
     * lombok의 toString / @Responsebody / HttpEntitu / ResponseEntitu에서
     * 엔티티를 그대로 사용할 경우
     * 4. 유지 보수 및 복잡성
     * 두 엔티티 간의 일관성을 계속 유지해야 함으로써 문제가 생길 수 있다.
     * 또한 한 트랜잭션 안에서 영속성 컨텍스트를 이용할 경우 데이터 적합성이 불일치 할 수 있다.
     */
//    @OneToMany(mappedBy = "member")
//    private List<Bookmark> bookmarks = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<Review> reviews = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<Comment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<LikeReview> likeReviews = new ArrayList<>();

}
