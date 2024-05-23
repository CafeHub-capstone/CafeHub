package com.cafehub.cafehub.cafe.repository;


import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.request.CafeListRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.cafehub.cafehub.cafe.entity.QCafe.cafe;
import static com.cafehub.cafehub.theme.entity.QTheme.theme;



// 해당 레포지토리에 대해서는 QueryDSL, Spring Data JPA 공부후 개선이 필요함, 완벽히 알고 친 코드가 아님
@RequiredArgsConstructor
public class CafeRepositoryCustomImpl implements CafeRepositoryCustom{

    private static final int CAFELIST_PAGING_SIZE = 10;

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Slice<Cafe> findAllFetch(CafeListRequest cafeListRequest){

        List<Cafe> cafeList =  jpaQueryFactory.selectFrom(cafe)
                .orderBy(getOrderSpecifier(cafeListRequest.getSortedByType()))
                .orderBy(cafe.id.desc())
                .offset(cafeListRequest.getCurrentPage() * CAFELIST_PAGING_SIZE)  // offset : 페이지 시작 지점
                .limit(CAFELIST_PAGING_SIZE +1)  // 시작 지점 부터 몇개까지
                .fetch();


        boolean Last = cafeList.size() <= CAFELIST_PAGING_SIZE; // 마지막 페이지라면 11개의 데이터를 불러와서 false, 마지막 페이지라면 10개(현재 페이징 사이즈) 이하라서 true
        if (!Last) cafeList.remove(cafeList.size()-1);


        // 해당 코드에 대해서는 좀 더 알아볼 필요가 있음
        return new SliceImpl<>(cafeList, PageRequest.of(cafeListRequest.getCurrentPage(), CAFELIST_PAGING_SIZE), Last);
    }



    // QueryDSL을 똑바로 쓰고 있는건지 잘 모르겠음
    @Override
    public Slice<Cafe> findAllByThemeFetch(CafeListRequest cafeListRequest){

        List<Cafe> cafeList =  jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.theme, theme)
                .where(theme.name.eq(cafeListRequest.getTheme()))
                .orderBy(getOrderSpecifier(cafeListRequest.getSortedByType()))
                .orderBy(cafe.id.desc())
                .offset(cafeListRequest.getCurrentPage() * CAFELIST_PAGING_SIZE)
                .limit(CAFELIST_PAGING_SIZE +1)
                .fetch();

        boolean Last = cafeList.size() <= CAFELIST_PAGING_SIZE;
        if (!Last) cafeList.remove(cafeList.size()-1);

        return new SliceImpl<>(cafeList, PageRequest.of(cafeListRequest.getCurrentPage(), CAFELIST_PAGING_SIZE), Last);
    }


    private OrderSpecifier<?> getOrderSpecifier(String sortedByType){

        if (sortedByType.equals("name")) return cafe.name.asc();
        else if (sortedByType.equals("rating")) return cafe.rating.desc();
        else if (sortedByType.equals("reviewNum")) return cafe.reviewCount.desc();
        else return null; // 잘못된 입력인 경우, 예외처리는 나중에
    }


}
