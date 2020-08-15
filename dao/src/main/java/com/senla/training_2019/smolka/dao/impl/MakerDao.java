package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.IMakerDao;
import com.senla.training_2019.smolka.model.entities.Maker;
import com.senla.training_2019.smolka.model.entities.Maker_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;

@Repository
public class MakerDao extends ADao<Maker, Long> implements IMakerDao {
    @Override
    protected EntityGraph<Maker> createExtendedEntityGraph() {
        EntityGraph<Maker> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(Maker_.country.getName());
        return entityGraph;
    }
}
