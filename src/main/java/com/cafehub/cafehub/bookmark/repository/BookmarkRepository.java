package com.cafehub.cafehub.bookmark.repository;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByCafeIdAndMemberId(Long cafeId, Long memberId);

}
