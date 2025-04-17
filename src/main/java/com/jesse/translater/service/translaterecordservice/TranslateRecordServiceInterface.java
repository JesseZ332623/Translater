package com.jesse.translater.service.translaterecordservice;

import com.jesse.translater.dto.TranslateRecordDTO;
import com.jesse.translater.entity.translaterecord.TranslateRecordEntity;

public interface TranslateRecordServiceInterface
{
    /**
     * 通过 id 查询翻译记录。
     */
    TranslateRecordEntity getRecordById(Integer id);

    /**
     * 添加一条新的翻译记录。
     */
    Integer addNewRecord(TranslateRecordEntity record);

    /**
     * 更新一条翻译记录的字段。
     */
    Integer updateRecordById(Integer id, TranslateRecordDTO translateRecordDTO);

    /**
     * 删除一条翻译记录。
     */
    Integer deleteRecordById(Integer id);

    /**
     * 清除表中的所有数据。
     */
    void truncateAllRecord();
}
