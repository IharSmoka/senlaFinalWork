package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.IUserInfoDao;
import com.senla.training_2019.smolka.model.entities.UserInfo;
import com.senla.training_2019.smolka.model.entities.UserInfo_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;

@Repository
public class UserInfoDao extends ADao<UserInfo, Long> implements IUserInfoDao {
    @Override
    protected EntityGraph<UserInfo> createExtendedEntityGraph() {
        EntityGraph<UserInfo> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(UserInfo_.credential.getName());
        return entityGraph;
    }
}
