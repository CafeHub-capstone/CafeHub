package com.cafehub.cafehub.cafe.repository;

import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.request.CafeListGetRequestDTO;
import org.springframework.data.domain.Slice;

public interface CafeRepositoryCustom {

    Slice<Cafe> findAllFetch(CafeListGetRequestDTO cafeListGetRequestDTO);

    Slice<Cafe> findAllByThemeFetch(CafeListGetRequestDTO cafeListGetRequestDTO);

}
