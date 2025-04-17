package com.jesse.translater.service.interfacekeyservice;

import com.jesse.translater.entity.interfacekeyinfo.InterfaceKeyInfoEntity;
import com.jesse.translater.entity.interfacekeyinfo.InterfaceKeyInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class InterfaceKeyService implements InterfaceKeyServiceInterface
{
    private final InterfaceKeyInfoRepository interfaceKeyInfoRepository;

    @Autowired
    public InterfaceKeyService(InterfaceKeyInfoRepository interfaceKeyInfoRepository) {
        this.interfaceKeyInfoRepository = interfaceKeyInfoRepository;
    }

    @Override
    public InterfaceKeyInfoEntity getKeyInfoByInterfaceUser(String interfaceUser)
    {
        return this.interfaceKeyInfoRepository.findById(interfaceUser)
                                              .orElseThrow(
                                                      () -> new NoSuchElementException(
                                                              String.format(
                                                                      "Interface User: [%s] not exist.", interfaceUser
                                                              )
                                                      )
                                              );
    }

    @Override
    @Transactional
    public String addNewKeyInfo(InterfaceKeyInfoEntity interfaceKeyInfoEntity)
    {
        Optional<InterfaceKeyInfoEntity> interfaceKeyInfoOptional
                = this.interfaceKeyInfoRepository.findById(
                        interfaceKeyInfoEntity.getInterfaceUser()
                );

        if (interfaceKeyInfoOptional.isPresent())
        {
            throw new IllegalStateException(
                    String.format(
                            "Interface User [%s] has been exist.",
                            interfaceKeyInfoEntity.getInterfaceUser()
                    )
            );
        }
        else
        {
            return this.interfaceKeyInfoRepository.save(interfaceKeyInfoEntity)
                                                  .getInterfaceUser();
        }
    }

    @Override
    @Transactional
    public String updateKeyInfo(InterfaceKeyInfoEntity interfaceKeyInfoEntity)
    {
        Optional<InterfaceKeyInfoEntity> interfaceKeyInfoOptional
                = this.interfaceKeyInfoRepository.findById(
                interfaceKeyInfoEntity.getInterfaceUser()
        );

        if (interfaceKeyInfoOptional.isPresent())
        {
            InterfaceKeyInfoEntity interfaceKeyInfoQueryRes
                        = interfaceKeyInfoOptional.get();

            if (!Objects.equals(interfaceKeyInfoQueryRes.getInterfaceUser(), interfaceKeyInfoEntity.getInterfaceUser())) {
                interfaceKeyInfoQueryRes.setInterfaceUser(interfaceKeyInfoEntity.getInterfaceUser());
            }

            interfaceKeyInfoQueryRes.setAppKey(interfaceKeyInfoEntity.getAppKey());
            interfaceKeyInfoQueryRes.setSecretKey(interfaceKeyInfoEntity.getSecretKey());

            return this.interfaceKeyInfoRepository.save(interfaceKeyInfoQueryRes)
                                                  .getInterfaceUser();
        }
        else
        {
            throw new IllegalStateException(
                    String.format(
                            "Interface User [%s] hasn't been exist.",
                            interfaceKeyInfoEntity.getInterfaceUser()
                    )
            );
        }
    }

    @Override
    @Transactional
    public String deleteKeyInfoByInterfaceUser(String interfaceName)
    {
        Optional<InterfaceKeyInfoEntity> interfaceKeyInfoOptional
                = this.interfaceKeyInfoRepository.findById(interfaceName);

        if (interfaceKeyInfoOptional.isPresent())
        {
            this.interfaceKeyInfoRepository.deleteById(interfaceName);

            return interfaceKeyInfoOptional.get().getInterfaceUser();
        }
        else
        {
            throw new IllegalStateException(
                    String.format(
                            "Interface User [%s] hasn't been exist.", interfaceName
                    )
            );
        }
    }
}
