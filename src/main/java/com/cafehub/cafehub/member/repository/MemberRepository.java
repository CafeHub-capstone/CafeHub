package com.cafehub.cafehub.member.repository;

import com.cafehub.cafehub.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    // 각 댓글의 멤버를 한번에 가져와서, 그 뒤로는 더이상 db에 접근하지 않도록 하기 위함.
    @Query("SELECT DISTINCT c.member FROM Comment c WHERE c.id IN :commentIds")
    List<Member> findAllByCommentIdIn(@Param("commentIds") List<Long> commentIds);
}
