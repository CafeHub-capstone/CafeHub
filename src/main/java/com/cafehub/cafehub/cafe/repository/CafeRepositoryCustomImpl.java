package com.cafehub.cafehub.cafe.repository;

import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.request.CafeListGetRequestDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.cafehub.cafehub.cafe.entity.QCafe.*;
import static com.cafehub.cafehub.theme.entity.QTheme.*;


// 해당 레포지토리에 대해서는 QueryDSL, Spring Data JPA 공부후 개선이 필요함, 완벽히 알고 친 코드가 아님
@RequiredArgsConstructor
public class CafeRepositoryCustomImpl implements CafeRepositoryCustom{

    private static final int CAFELIST_PAGING_SIZE = 10;

    private final JPAQueryFactory jpaQueryFactory;


    // 우리 서비스에서 cafe를 가져올때 theme을 join해서 가져오더라도 해당 테이블은 속성이 많지 않음 그래서 해당 방식을 이용함
    @Override
    public Slice<Cafe> findAllFetch(CafeListGetRequestDTO request){

        List<Cafe> cafeList =  jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.theme, theme)
                .orderBy(getOrderSpecifier(request.getSortedByType()))
                .orderBy(cafe.id.desc())                                                // 정렬조건이 한개 밖에 없으면 두 조건이 같은게 있으면 곤란해짐
                .offset(request.getCurrentPage() * CAFELIST_PAGING_SIZE)                // offset : 페이지 시작 지점
                .limit(CAFELIST_PAGING_SIZE +1)                                         // 시작 지점 부터 몇개까지
                .fetch();


        boolean Last = cafeList.size() <= CAFELIST_PAGING_SIZE;                         // 마지막 페이지라면 10개(현재 페이징 사이즈) 이하라서 true
        if (!Last) cafeList.remove(cafeList.size()-1);


        return new SliceImpl<>(cafeList, PageRequest.of(request.getCurrentPage(), CAFELIST_PAGING_SIZE), Last);
    }


    @Override
    public Slice<Cafe> findAllByThemeFetch(CafeListGetRequestDTO request){

        List<Cafe> cafeList =  jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.theme, theme)
                .where(theme.name.eq(request.getTheme()))
                .orderBy(getOrderSpecifier(request.getSortedByType()))
                .orderBy(cafe.id.desc())
                .offset(request.getCurrentPage() * CAFELIST_PAGING_SIZE)
                .limit(CAFELIST_PAGING_SIZE +1)
                .fetch();

        boolean Last = cafeList.size() <= CAFELIST_PAGING_SIZE;
        if (!Last) cafeList.remove(cafeList.size()-1);

        return new SliceImpl<>(cafeList, PageRequest.of(request.getCurrentPage(), CAFELIST_PAGING_SIZE), Last);
    }


    private OrderSpecifier<?> getOrderSpecifier(String sortedByType){

        if (sortedByType.equals("name")) return cafe.name.asc();
        else if (sortedByType.equals("rating")) return cafe.rating.desc();
        else if (sortedByType.equals("reviewNum")) return cafe.reviewCount.desc();
        else return null; // 잘못된 입력인 경우, 예외처리는 나중에
    }

}
