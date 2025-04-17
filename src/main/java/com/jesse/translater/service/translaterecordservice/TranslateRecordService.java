package com.jesse.translater.service.translaterecordservice;

import com.jesse.translater.dto.TranslateRecordDTO;
import com.jesse.translater.entity.translaterecord.TranslateRecordEntity;
import com.jesse.translater.entity.translaterecord.TranslateRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TranslateRecordService implements TranslateRecordServiceInterface
{
    private final TranslateRecordRepository translateRecordRepository;

    @Autowired
    public TranslateRecordService(TranslateRecordRepository translateRecordRepository) {
        this.translateRecordRepository = translateRecordRepository;
    }

    /**
     * 通过 id 查询翻译记录。
     *
     * @param id 记录 ID
     */
    @Override
    public TranslateRecordEntity getRecordById(Integer id)
    {
        return this.translateRecordRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                String.format(
                                        "[NoSuchElementException] ID: {%d} not exist in this table.", id
                                )
                        )
                );
    }

    /**
     * 添加一条新地翻译记录。
     *
     * @param translateRecordEntity 新记录
     */
    @Override
    public Integer addNewRecord(TranslateRecordEntity translateRecordEntity)
    {
        // 由于数据表主键设置了 AUTO_INCREMENT，所以不需要通过 ID 进行手动校验，直接存就行。
        return this.translateRecordRepository.save(translateRecordEntity).getId();
    }

    /**
     * 更新一条翻译记录的字段。
     *
     * @param id 记录 ID
     * @param translateRecordDTO 更新完成的记录
     */
    @Override
    @Transactional
    public Integer updateRecordById(Integer id, TranslateRecordDTO translateRecordDTO)
    {
        Optional<TranslateRecordEntity> queryResultOptional
                = this.translateRecordRepository.findById(id);

        if (queryResultOptional.isPresent())
        {
            TranslateRecordEntity queryResult = queryResultOptional.get();

            queryResult.setQueryTime(translateRecordDTO.getQueryTime());
            queryResult.setSourceLanguageCode(translateRecordDTO.getSourceLanguageCode());
            queryResult.setTargetLanguageCode(translateRecordDTO.getTargetLanguageCode());
            queryResult.setSourceContent(translateRecordDTO.getSourceContent());
            queryResult.setTargetContent(translateRecordDTO.getTargetContent());
            queryResult.setResponseCode(translateRecordDTO.getResponseCode());

            return this.translateRecordRepository.save(queryResult).getId();
        }
        else
        {
            throw new IllegalStateException(
                    String.format(
                            "[IllegalStateException] ID: {%d} has not been exist in this table.", id
                    )
            );
        }
    }

    /**
     * 删除一条翻译记录。
     *
     * @param id 记录 ID
     */
    @Override
    public Integer deleteRecordById(Integer id)
    {
        TranslateRecordEntity queryResult
                = this.translateRecordRepository.findById(id)
                      .orElseThrow(
                              () -> new NoSuchElementException(
                                  String.format(
                                          "[NoSuchElementException] ID: {%d} not exist in this table.", id
                                  )
                              )
                      );

        this.translateRecordRepository.deleteById(id);

        return queryResult.getId();
    }

    /**
     * 清除表中的所有数据。
     */
    @Override
    @Transactional
    public void truncateAllRecord() {
        this.translateRecordRepository.truncateAllRecord();
    }
}
