package com.cafehub.cafehub.bookmark.repository;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
import com.cafehub.cafehub.bookmark.response.CafeForMarkedResponseDTO;
import com.cafehub.cafehub.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{


    boolean existsByCafeIdAndMemberId(Long cafeId, Long memberId);  
  
    @Query("select new com.cafehub.cafehub.bookmark.response.CafeForMarkedResponseDTO" +
            "(b.cafe.id, b.cafe.cafePhotoUrl, b.cafe.name, b.cafe.rating, b.cafe.theme.name, b.cafe.reviewCount) " +
            "from Bookmark b " +
            "where b.member.id = :memberId")
    List<CafeForMarkedResponseDTO> findCafesByBookmarkIdAndMemberId(@Param("memberId") Long memberId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from Bookmark b where b.member.id = :memberId and b.cafe.id = :cafeId")
    void deleteByBookmarkIdAndMemberId(@Param("cafeId") Long cafeId, @Param("memberId") Long memberId);

}