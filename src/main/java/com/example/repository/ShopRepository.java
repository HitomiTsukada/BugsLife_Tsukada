package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

	@Query("SELECT s FROM Shop s WHERE s.name LIKE %:search%")
	List<Shop> findShopsWithPartialName(@Param("search") String search);

}
