package com.jesse.translater.service.interfacekeyservice;

import com.jesse.translater.entity.interfacekeyinfo.InterfaceKeyInfoEntity;

public interface InterfaceKeyServiceInterface
{
    InterfaceKeyInfoEntity getKeyInfoByInterfaceUser(String interfaceUser);

    String addNewKeyInfo(InterfaceKeyInfoEntity interfaceKeyInfoEntity);

    String updateKeyInfo(InterfaceKeyInfoEntity interfaceKeyInfoEntity);

    String deleteKeyInfoByInterfaceUser(String interfaceName);
}
