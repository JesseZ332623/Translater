package com.jesse.translater.entity.translaterecord;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 翻译记录表实体类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "translate_record")
public class TranslateRecordEntity
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                 // 记录 ID

    @Column(name = "query_time")
    private LocalDateTime queryTime;    // 查询翻译那一刻的时间（ISO 标准时间精确到秒）

    @Column(name = "source_language")
    private String sourceLanguageCode;  // 源语言代码

    @Column(name = "target_language")
    private String targetLanguageCode;  // 目标语言代码

    @Column(name = "source_content")
    private String sourceContent;       // 原文（前 20 个字）

    @Column(name = "target_content")
    private String targetContent;       // 译文（前 20 个字）

    @Column(name = "response_code")
    private Integer responseCode;       // 错误响应码（正常情况下都为 0）
}
