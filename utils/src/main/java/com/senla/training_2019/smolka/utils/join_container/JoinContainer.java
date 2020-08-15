package com.senla.training_2019.smolka.utils.join_container;

import com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings.SingularAttributeFilterSettings;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import java.util.HashMap;
import java.util.Map;

public class JoinContainer<Q> {

    private final Map<Class<?>, Join<Q, ?>> map = new HashMap<>();

    public JoinContainer() {

    }

    public <X> Join<Q, X> createJoin(Attribute<?, ?> attr, Root<Q> root) {
        Join<Q, X> join = root.join(attr.getName());
        map.put(join.getJavaType(), join);
        return join;
    }

    public void createJoinsForFilterSettings(SingularAttributeFilterSettings filterSettings, Root<Q> root) {
        for (Attribute<?, ?> attr : filterSettings.getAttributes()) {
            if (filterSettings.joinIsRequired(attr)) {
                Join<Q, ?> join = root.join(filterSettings.getAttributeForJoin(attr).getName());
                map.put(attr.getDeclaringType().getJavaType(), join);
            }
        }
    }

    public Join<Q, ?> getJoin(Attribute<?, ?> attr) {
        return map.get(attr.getDeclaringType().getJavaType());
    }
}