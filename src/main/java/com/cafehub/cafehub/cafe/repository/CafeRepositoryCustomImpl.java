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


@RequiredArgsConstructor
public class CafeRepositoryCustomImpl implements CafeRepositoryCustom{

    private static final int CAFELIST_PAGING_SIZE = 10;

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Slice<Cafe> findAllFetch(CafeListRequest cafeListRequest){

        List<Cafe> cafeList =  jpaQueryFactory.selectFrom(cafe)
                .orderBy(getOrderSpecifier(cafeListRequest.getSortedByType()))
                .offset(cafeListRequest.getCurrentPage() * CAFELIST_PAGING_SIZE)
                .limit(CAFELIST_PAGING_SIZE +1)
                .fetch();


        boolean Last = cafeList.size() <= CAFELIST_PAGING_SIZE;
        if (!Last) cafeList.remove(cafeList.size()-1);

        return new SliceImpl<>(cafeList, PageRequest.of(cafeListRequest.getCurrentPage(), CAFELIST_PAGING_SIZE), Last);
    }

    @Override
    public Slice<Cafe> findByThemeFetchSortedByType(CafeListRequest cafeListRequest){

        List<Cafe> cafeList =  jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.theme, theme)
                .where(theme.name.eq(cafeListRequest.getTheme()))
                .orderBy(getOrderSpecifier(cafeListRequest.getSortedByType()))
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
