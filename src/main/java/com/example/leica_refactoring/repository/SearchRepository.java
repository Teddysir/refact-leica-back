package com.example.leica_refactoring.repository;

import com.example.leica_refactoring.entity.SearchPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SearchRepository extends JpaRepository<SearchPost, Long> {

    Page<SearchPost> findBySearchContentContainingOrPostTitleContaining(String keyword, String keyword2, Pageable pageable);

    SearchPost findByPost_Id(Long id);
}
