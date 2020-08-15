package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.IProductDao;
import com.senla.training_2019.smolka.model.entities.Maker;
import com.senla.training_2019.smolka.model.entities.Maker_;
import com.senla.training_2019.smolka.model.entities.Product;
import com.senla.training_2019.smolka.model.entities.Product_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;

@Repository
public class ProductDao extends ADao<Product, Long> implements IProductDao {
    @Override
    protected EntityGraph<Product> createExtendedEntityGraph() {
        EntityGraph<Product> entityGraph = super.createExtendedEntityGraph();
        entityGraph.addAttributeNodes(Product_.category.getName(), Product_.maker.getName());
        Subgraph<Maker> makerSubGraph = entityGraph.addSubgraph(Product_.maker.getName());
        makerSubGraph.addAttributeNodes(Maker_.country.getName());
        return entityGraph;
    }
}
