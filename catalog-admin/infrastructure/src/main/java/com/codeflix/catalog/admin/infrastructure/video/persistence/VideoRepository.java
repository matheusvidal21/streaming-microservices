package com.codeflix.catalog.admin.infrastructure.video.persistence;

import com.codeflix.catalog.admin.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {

    @Query("""
            SELECT DISTINCT new com.codeflix.catalog.admin.domain.video.VideoPreview(
                    v.id AS id,
                    v.title AS title,
                    v.description AS description,
                    v.createdAt AS createdAt,
                    v.updatedAt AS updatedAt
            )
            FROM Video v
                 LEFT JOIN v.members members
                 LEFT JOIN v.categories categories
                 LEFT JOIN v.genres genres
            WHERE
                 ( :terms is null or UPPER(v.title) like :terms )
            AND 
                 ( :castMembers is null or members.id.castMemberId IN :castMembers )
            AND
                 ( :categories is null or categories.id.categoryId IN :categories )
            AND
                 ( :genres is null or genres.id.genreId IN :genres )
     """)
    Page<VideoPreview> findAll(
            @Param("terms") String terms,
            @Param("castMembers") Set<String> castMembers,
            @Param("categories") Set<String> categories,
            @Param("genres") Set<String> genres,
            Pageable page
    );

}
