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

  /*  @Query("select new com.cafehub.cafehub.bookmark.response.CafeForMarkedResponseDTO" +
            "(b.cafe.id, b.cafe.cafePhotoUrl, b.cafe.name, b.cafe.rating, b.cafe.theme.name, b.cafe.reviewCount) " +
            "from Bookmark b " +
            "where b.member.id = :memberId")
    List<CafeForMarkedResponseDTO> findAllByMemberId(@Param("memberId") Long memberId);*/


    void deleteByCafeIdAndMemberId(Long cafeId, Long memberId);

    @Query("select c from Cafe c JOIN Bookmark b ON c.id = b.cafe.id WHERE b.member.id = :memberId")
    List<Cafe> findAllByMember_Id(Long memberId);
}