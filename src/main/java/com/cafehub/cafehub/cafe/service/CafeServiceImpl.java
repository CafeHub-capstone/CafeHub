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




    // 카페 전체 리스트를 불러오는 로직 : DB와 통신하는 횟수 : 1번
    @Override
    public CafeListResponse getCafeListResponseSortedByType(CafeListRequest cafeListRequest) {

        Slice<Cafe> cafes;

        // 현재 카페의 테마에는 Study, Date, Meet, Dessert 가 있고 모든 테마를 조회 하고 싶을 때는 All
        if (cafeListRequest.getTheme().equals("All")) cafes = cafeRepository.findAllFetch(cafeListRequest);
        else cafes = cafeRepository.findAllByThemeFetch(cafeListRequest);

        List<Cafe> cafeList = cafes.getContent();

        // (체크 포인트) 여기서 계속 new DTO가 생성되는데 괜찮을까에 대한 고민을 한번 해봐야 할듯
        List<CafeResponseForCafeList> cafeListResponse = cafeList.stream()
                .map(cafe -> new CafeResponseForCafeList(
                        cafe.getId(),
                        cafe.getCafePhotoUrl(),
                        cafe.getName(),
                        cafe.getTheme().getName(),
                        cafe.getRating(),
                        cafe.getReviewCount()))
                .toList(); // 자바 16 이상부터 .collect(Collectors.toList()) => toList() 가능

        return new CafeListResponse(true, cafeListResponse,cafes.hasNext(),cafes.getNumber(), "ok");
    }



}
