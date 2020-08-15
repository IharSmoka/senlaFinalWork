package com.senla.training_2019.smolka.dao.impl;

import com.senla.training_2019.smolka.api.dao.IAddressDao;
import com.senla.training_2019.smolka.model.entities.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;

@Repository
public class AddressDao extends ADao<Address, Long> implements IAddressDao {

    @Override
    protected EntityGraph<Address> createExtendedEntityGraph() {
        EntityGraph<Address> entityGraph = super.createExtendedEntityGraph();
        Subgraph<City> citySubGraph =  entityGraph.addSubgraph(Address_.city.getName());
        citySubGraph.addAttributeNodes(City_.country.getName());
        return entityGraph;
    }
}
