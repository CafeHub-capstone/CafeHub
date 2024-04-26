package com.cafehub.cafehub.cafe.repository;

import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.request.CafeListRequest;
import org.springframework.data.domain.Slice;

public interface CafeRepositoryCustom {

    Slice<Cafe> findAllFetch(CafeListRequest cafeListRequest);

    Slice<Cafe> findByThemeFetchSortedByType(CafeListRequest cafeListRequest);

}
