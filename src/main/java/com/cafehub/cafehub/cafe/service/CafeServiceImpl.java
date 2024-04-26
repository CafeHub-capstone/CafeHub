package com.cafehub.cafehub.cafe.service;


import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.repository.CafeRepository;
import com.cafehub.cafehub.cafe.request.CafeInfoRequest;
import com.cafehub.cafehub.cafe.request.CafeListRequest;
import com.cafehub.cafehub.cafe.response.CafeInfoResponse;
import com.cafehub.cafehub.cafe.response.CafeListResponse;
import com.cafehub.cafehub.cafe.response.CafeResponseForCafeList;
import com.cafehub.cafehub.menu.entity.Menu;
import com.cafehub.cafehub.menu.repository.MenuRepository;
import com.cafehub.cafehub.menu.response.MenuResponse;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.review.repository.ReviewRepository;
import com.cafehub.cafehub.review.response.BestReviewResponse;
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

    private final MenuRepository menuRepository;

    private final ReviewRepository reviewRepository;



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




    @Override
    public CafeInfoResponse getCafeInfo(CafeInfoRequest cafeInfoRequest){

        CafeInfoResponse cafeInfoResponse = new CafeInfoResponse();

        // optional 처리 나중에
        Cafe cafe = cafeRepository.findById(cafeInfoRequest.getCafeId()).get();
        setCafeInfoWithoutMenuAndReview(cafe, cafeInfoResponse);


        List<Menu> bestMenus = menuRepository.findBestMenuFetch(cafeInfoRequest.getCafeId());
        List<MenuResponse> bestMenuList = new ArrayList<>();

        for (Menu menu : bestMenus){

            MenuResponse menuResponse = new MenuResponse();
            setCafeInfoBestMenu(menu, menuResponse);
            bestMenuList.add(menuResponse);
        }

        cafeInfoResponse.setBestMenuList(bestMenuList);



        // 이 부분은 내가 리뷰를
        List<Review> bestReview = reviewRepository.findBestReviewFetch(cafe.getId());
        List<BestReviewResponse> bestReviewResponseList = new ArrayList<>();
        // 아직 리뷰, 리뷰 좋아요 쪽이 구현 안돼서 이쪽은 보류하고 임시로 사용함
        for (Review review : bestReview){

            BestReviewResponse bestReviewResponse= new BestReviewResponse(review.getRating(),review.getContent());
            bestReviewResponseList.add(bestReviewResponse);
        }

        cafeInfoResponse.setBestReviewList(bestReviewResponseList);


        cafeInfoResponse.setSuccess(true);
        cafeInfoResponse.setErrorMessage("ok");

        return cafeInfoResponse;
    }

    private void setCafeInfoWithoutMenuAndReview(Cafe cafe, CafeInfoResponse cafeInfoResponse){

        cafeInfoResponse.setCafePhotoUrl(cafe.getCafePhotoUrl());
        cafeInfoResponse.setCafename(cafe.getName());
        cafeInfoResponse.setCafeOperationHour(cafe.getOperationHours());
        cafeInfoResponse.setCafeAddress(cafe.getAddress());
        cafeInfoResponse.setCafePhone(cafe.getPhone());
        // cafe rating 임시로 넣어둠
        cafeInfoResponse.setCafeRating(cafe.getRating());
    }

    private void setCafeInfoBestMenu(Menu menu, MenuResponse menuResponse){

        menuResponse.setCategory(menu.getCategory());
        menuResponse.setName(menu.getName());
        menuResponse.setPrice(menu.getPrice());
    }

}
