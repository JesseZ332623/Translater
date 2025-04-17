package com.jesse.translater.entity.translaterecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TranslateRecordRepository extends JpaRepository<TranslateRecordEntity, Integer>
{
    /**
     * 清除表中的所有数据。
     */
    @Modifying
    @Query(value = "TRUNCATE translate_record", nativeQuery = true)
    void truncateAllRecord();
}