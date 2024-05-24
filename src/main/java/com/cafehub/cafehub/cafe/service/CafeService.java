package com.cafehub.cafehub.cafe.service;

import com.cafehub.cafehub.cafe.request.CafeInfoRequest;
import com.cafehub.cafehub.cafe.request.CafeListRequest;
import com.cafehub.cafehub.cafe.response.CafeInfoResponse;
import com.cafehub.cafehub.cafe.response.CafeListResponse;

public interface CafeService {

    CafeListResponse getCafeListResponseSortedByType(CafeListRequest cafeListRequest);

    CafeInfoResponse getCafeInfo(CafeInfoRequest cafeInfoRequest);
}
