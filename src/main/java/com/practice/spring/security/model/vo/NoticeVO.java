package com.practice.spring.security.model.vo;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeVO {

    private Long id;

    /**
     * 공지사항 제목
     */
    private String title;

    /**
     * 공지사항 내용
     */
    @Lob
    private String content;

    /**
     * 등록일시
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public NoticeVO(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }
}