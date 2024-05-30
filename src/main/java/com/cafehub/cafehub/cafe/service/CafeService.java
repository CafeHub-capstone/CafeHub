package com.cafehub.cafehub.cafe.service;

import com.cafehub.cafehub.cafe.request.CafeInfoRequestDTO;
import com.cafehub.cafehub.cafe.request.CafeListGetRequestDTO;
import com.cafehub.cafehub.common.dto.ResponseDto;

public interface CafeService {

    ResponseDto<?> getCafeListSortedByType(CafeListGetRequestDTO cafeListGetRequestDTO);

    ResponseDto<?> getCafeInfo(CafeInfoRequestDTO cafeInfoRequestDTO);
}
