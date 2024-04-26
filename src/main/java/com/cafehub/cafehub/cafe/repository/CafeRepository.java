package com.cafehub.cafehub.cafe.repository;

import com.cafehub.cafehub.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {

}
