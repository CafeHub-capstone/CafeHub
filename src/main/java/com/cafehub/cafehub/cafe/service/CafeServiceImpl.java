package com.cafehub.cafehub.cafe.service;


import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.repository.CafeRepository;
import com.cafehub.cafehub.cafe.request.CafeListRequest;
import com.cafehub.cafehub.cafe.response.CafeListResponse;
import com.cafehub.cafehub.cafe.response.CafeResponseForCafeList;
import com.cafehub.cafehub.menu.entity.Menu;
import com.cafehub.cafehub.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeServiceImpl implements CafeService{

    private final CafeRepository cafeRepository;





    @Override
    public CafeListResponse getCafeListResponseSortedByType(CafeListRequest cafeListRequest) {

        Slice<Cafe> cafes;
        if (cafeListRequest.getTheme().equals("All")) cafes = cafeRepository.findAllFetch(cafeListRequest);
        else cafes = cafeRepository.findByThemeFetchSortedByType(cafeListRequest);

        List<Cafe> cafeList = cafes.getContent();
        List<CafeResponseForCafeList> cafeResponseForCafeListList = new ArrayList<>();

        for (Cafe cafe : cafeList){
            CafeResponseForCafeList cafeResponseForCafeList = new CafeResponseForCafeList();
            setCafeResponse(cafe, cafeResponseForCafeList);
            cafeResponseForCafeListList.add(cafeResponseForCafeList);
        }

        return new CafeListResponse(true, cafeResponseForCafeListList,cafes.hasNext(),cafeListRequest.getCurrentPage(), "Theme : " + cafeListRequest.getTheme() + ", SortedByType : " + cafeListRequest.getSortedByType() + " 으로 카페리스트 반환 성공");
    }

    private void setCafeResponse(Cafe cafe, CafeResponseForCafeList cafeResponseForCafeList){

        cafeResponseForCafeList.setCafeId(cafe.getId());
        cafeResponseForCafeList.setCafePhotoUrl(cafe.getCafePhotoUrl());
        cafeResponseForCafeList.setCafeName(cafe.getName());
        cafeResponseForCafeList.setCafeRating(cafe.getRating());
        cafeResponseForCafeList.setCafeTheme(cafe.getTheme().getName());
        cafeResponseForCafeList.setCafeReviewNum(cafe.getReviewCount());
    }



}
