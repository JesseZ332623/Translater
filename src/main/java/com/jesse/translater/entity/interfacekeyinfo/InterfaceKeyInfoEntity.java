package com.jesse.translater.entity.interfacekeyinfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "interface_keyinfo")
public class InterfaceKeyInfoEntity
{
    @Id
    @Column(name = "interface_user")
    private String interfaceUser;       // 接口拥有者

    @Column(name = "app_key")
    private String appKey;              // 应用 ID

    @Column(name = "secret_key")
    private String secretKey;           // 应用秘钥
}
