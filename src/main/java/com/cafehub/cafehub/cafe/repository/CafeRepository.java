package com.cafehub.cafehub.cafe.repository;

import com.cafehub.cafehub.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {

    // 카페 info API를 위해서 cafe를 찾아온 후 cafe의 theme을 보여줘야 하는데 cafe 와 theme는 서로 다른 테이블에 있음
    // Cafe와 Theme은 fetch join 하더라도 테이블의 row * col 이 크지 않음
    // col : Cafe + Theme 의 속성 전체
    // row : 카페 1개
    // 따라서 여기서는 fetch join을 통한 즉시 로딩을 사용하겠음
    @Query("SELECT c FROM Cafe c JOIN FETCH c.theme WHERE c.id = :cafeId")
    Optional<Cafe> findByIdWithTheme(@Param("cafeId") Long cafeId);
}
