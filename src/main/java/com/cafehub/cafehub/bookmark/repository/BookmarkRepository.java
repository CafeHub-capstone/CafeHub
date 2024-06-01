package com.cafehub.cafehub.bookmark.repository;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
import com.cafehub.cafehub.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{


    boolean existsByCafeIdAndMemberId(Long cafeId, Long memberId);

    void deleteByCafeIdAndMemberId(Long cafeId, Long memberId);

    @Query("select distinct b from Bookmark b join fetch b.cafe c join fetch c.theme where b.member.id = :memberId")
    List<Bookmark> findAllByMemberId(Long memberId);
}