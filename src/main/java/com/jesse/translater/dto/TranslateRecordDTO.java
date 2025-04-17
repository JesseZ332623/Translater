package com.jesse.translater.dto;

import com.jesse.translater.entity.translaterecord.TranslateRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.*;

import java.time.LocalDateTime;

/**
 * 翻译记录表数据传输类（省略了 id 字段）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslateRecordDTO
{
    private LocalDateTime queryTime;
    private String sourceLanguageCode;
    private String targetLanguageCode;
    private String sourceContent;
    private String targetContent;
    private Integer responseCode;

    /**
     * 来自 TranslateRecord 的转换。
     */
    @NotNull
    @Contract("_ -> new")
    public static TranslateRecordDTO from(
            @NotNull
            TranslateRecordEntity translateRecordEntity
    ) {
        return new TranslateRecordDTO(
                translateRecordEntity.getQueryTime(),
                translateRecordEntity.getSourceLanguageCode(),
                translateRecordEntity.getTargetLanguageCode(),
                translateRecordEntity.getSourceContent(),
                translateRecordEntity.getTargetContent(),
                translateRecordEntity.getResponseCode()
        );
    }
}
