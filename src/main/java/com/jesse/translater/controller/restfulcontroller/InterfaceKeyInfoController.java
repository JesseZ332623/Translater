package com.jesse.translater.controller.restfulcontroller;

import com.jesse.translater.entity.interfacekeyinfo.InterfaceKeyInfoEntity;
import com.jesse.translater.service.interfacekeyservice.InterfaceKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        path     = "/api/interface_info",
        produces = "application/json"
)
public class InterfaceKeyInfoController
{
    private final InterfaceKeyService interfaceKeyService;

    @Autowired
    public InterfaceKeyInfoController(InterfaceKeyService interfaceKeyService) {
        this.interfaceKeyService = interfaceKeyService;
    }

    /**
     * 通过 接口拥有者姓名 查询对应的 APP ID 和 APP 秘钥，以 JSON 作为响应。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/interface_info/search/Jesse">
     *          Get 方法查询接口拥有者 Jesse 的APP ID 和 APP 秘钥
     *      </a>
     * </p>
     */
    @GetMapping(path = "/search/{interfaceName}")
    public ResponseEntity<?> getInterfaceKeyInfoByUser(
            @PathVariable String interfaceName
    ) {
        try
        {
            InterfaceKeyInfoEntity interfaceKeyInfo
                    = this.interfaceKeyService.getKeyInfoByInterfaceUser(interfaceName);

            return ResponseEntity.ok(interfaceKeyInfo);
        }
        catch (Exception exception)
        {
            log.error(
                    "InterfaceKeyInfoController::getInterfaceKeyInfoByUser() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(exception.getMessage());
        }
    }

    /**
     *添加新的接口信息，以 JSON 作为响应，发送数据示例如下：
     *
     * <pre>
     *     {
     *         "interfaceUser" : "Jack"
     *         "appKey"        : "5a3273d519430504"
     *         "secretKey"     : "ouRMKkDxnmwenRmD8p6Lb3QBWlsPutKt"
     *     }
     * </pre>
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/interface_info/add_new_interface_key">
     *          Post 方法添加新的接口信息
     *      </a>
     * </p>
     */
    @PostMapping(path = "/add_new_interface_key")
    public ResponseEntity<?> addNewInterfaceKey(
            @RequestBody
            InterfaceKeyInfoEntity interfaceKeyInfoEntity
    ) {
        try
        {
            String interfaceUser
                    = this.interfaceKeyService.addNewKeyInfo(interfaceKeyInfoEntity);

            return ResponseEntity.ok(
                    String.format(
                            "Add new interface key (User: [%s]) complete.", interfaceUser
                    )
            );
        }
        catch (Exception exception)
        {
            log.error(
                    "InterfaceKeyInfoController::addNewInterfaceKey() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

    /**
     * 根据请求体中的用户名，修改相应的信息。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/interface_info/update_interface_key">
     *          Put 方法修改接口信息
     *      </a>
     * </p>
     */
    @PutMapping(path = "/update_interface_key")
    public ResponseEntity<?> updateInterfaceKey(
            @RequestBody
            InterfaceKeyInfoEntity interfaceKeyInfoEntity
    ) {
        try
        {
            return ResponseEntity.ok(
                    this.interfaceKeyService.updateKeyInfo(interfaceKeyInfoEntity)
            );
        }
        catch (Exception exception)
        {
            log.error(
                    "InterfaceKeyInfoController::updateInterfaceKey() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

    /**
     * 根据请求体中的用户名，删除对应的信息。
     *
     * <p>可能的 URL 为：
     *      <a href="http://localhost:8091/api/interface_info/delete/Jesse">
     *          Delete 方法根据用户名删除对应接口信息
     *      </a>
     * </p>
     */
    @DeleteMapping(path = "/delete/{interfaceUser}")
    public ResponseEntity<?>
    deleteInterfaceKeyByUser(@PathVariable String interfaceUser)
    {
        try
        {
            return ResponseEntity.ok(
                    this.interfaceKeyService.deleteKeyInfoByInterfaceUser(interfaceUser)
            );
        }
        catch (Exception exception)
        {
            log.error(
                    "deleteInterfaceKeyByUser::updateInterfaceKey() {}",
                    exception.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }
}
