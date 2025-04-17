package com.jesse.translater.controller.restfulcontroller;

import com.jesse.translater.dto.TranslateRecordDTO;
import com.jesse.translater.entity.translaterecord.TranslateRecordEntity;
import com.jesse.translater.service.translaterecordservice.TranslateRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 翻译记录 Restful 控制器。
 * <p> 控制器根 URL："/api/translate_record" </p>
 * <p> 客户端仅接受 JSON 格式的数据提交     </p>
 */
@Slf4j
@RestController
@RequestMapping(
        path     = "/api/translate_record",
        produces = "application/json"
)
public class TranslateRecordController
{
    private final TranslateRecordService translateRecordService;

    @Autowired
    public TranslateRecordController(TranslateRecordService translateRecordService) {
        this.translateRecordService = translateRecordService;
    }

    /**
     * 通过 ID 查询某条翻译记录，以 JSON 作为响应。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/translate_record/search/18">
     *          Get 方法查询 ID 为 18 的翻译记录记录
     *      </a>
     * </p>
     */
    @GetMapping(path = "/search/{id}")
    ResponseEntity<?> searchRecordById(@PathVariable Integer id)
    {
        try
        {
            TranslateRecordEntity queryResult
                    = this.translateRecordService.getRecordById(id);

            return ResponseEntity.ok(queryResult);
        }
        catch (Exception exception)
        {
            log.error(
                    "TranslateRecordController::searchRecordById() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(exception.getMessage());
        }
    }

    /**
     * 添加一条翻译记录，以 JSON 作为响应。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/translate_record/add_new_record">
     *          POST 方法添加一条新的翻译记录
     *      </a>
     * </p>
     */
    @PostMapping(path = "/add_new_record")
    ResponseEntity<?> addNewRecord(
            @RequestBody
            TranslateRecordEntity translateRecordEntity
    ) {
        try
        {

            Integer newRecordId
                    = this.translateRecordService.addNewRecord(translateRecordEntity);

            return ResponseEntity.ok(
                    String.format(
                            "Add new translate record (ID = {%d}) complete.",
                            translateRecordEntity.getId()
                    )
            );
        }
        catch (Exception exception)
        {
            log.error(
                    "TranslateRecordController::addNewRecord() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(exception.getMessage());
        }
    }

    /**
     * 通过 id 更新一条翻译记录，以 JSON 作为响应。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/translate_record/update_record/18">
     *          PUT 方法更新一条 ID 为 18 的翻译记录
     *      </a>
     * </p>
     */
    @PutMapping(path = "/update_record/{id}")
    ResponseEntity<?> updateRecord(
            @PathVariable Integer           id,
            @RequestBody TranslateRecordDTO updatedRecordDTO
    )
    {
        try
        {
            Integer newRecordId
                    = this.translateRecordService.updateRecordById(
                            id, updatedRecordDTO
                    );

            return ResponseEntity.ok(
                    String.format(
                            "Add new translate record (ID = {%d}) complete.",
                            newRecordId
                    )
            );
        }
        catch (Exception exception)
        {
            log.error(
                    "TranslateRecordController::updateRecord() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

    /**
     * 清空表中所有的翻译记录，以 JSON 作为响应。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/translate_record/truncate_record">
     *          PUT 方法删除所有的翻译记录
     *      </a>
     * </p>
     */
    @PutMapping(path = "/truncate_record")
    ResponseEntity<?> truncateRecord()
    {
        this.translateRecordService.truncateAllRecord();

        return ResponseEntity.ok("Truncate all translate record complete.");
    }

    /**
     * 通过 id 删除一条翻译记录，以 JSON 作为响应。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/translate_record/delete_record/18">
     *          DELETE 方法删除一条 ID 为 18 的翻译记录
     *      </a>
     * </p>
     */
    @DeleteMapping(path = "/delete_record/{id}")
    ResponseEntity<?> deleteRecord(@PathVariable Integer id)
    {
        try
        {
            Integer newRecordId
                    = this.translateRecordService.deleteRecordById(id);

            return ResponseEntity.ok(
                    String.format(
                            "Add new translate record (ID = {%d}) complete.",
                            newRecordId
                    )
            );
        }
        catch (Exception exception)
        {
            log.error(
                    "TranslateRecordController::deleteRecord() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }
}
