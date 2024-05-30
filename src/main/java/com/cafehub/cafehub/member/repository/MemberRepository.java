package com.cafehub.cafehub.member.repository;

import com.cafehub.cafehub.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // cafe 쪽에서 필요함
    Member findByEmail(String currentMemberEmail);
}
