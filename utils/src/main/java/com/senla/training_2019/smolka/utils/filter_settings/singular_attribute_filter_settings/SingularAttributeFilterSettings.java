package com.senla.training_2019.smolka.utils.filter_settings.singular_attribute_filter_settings;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SingularAttributeFilterSettings {

    private final Map<SingularAttribute<?, ?>, SearchInstruction<?>> filters;
    private final Map<SingularAttribute<?, ?>, Attribute<?, ?>> joinRules;

    public SingularAttributeFilterSettings() {
        filters = new HashMap<>();
        joinRules = new HashMap<>();
    }

    public boolean isEmpty() {
        return filters.isEmpty();
    }

    public <S> SingularAttributeFilterSettings putToSettingsWithLikeInstruction(SingularAttribute<S, String> attr, String value) {
        if (value == null) {
            throw new IllegalArgumentException("search value can'not be null!");
        }
        filters.put(attr, new SearchInstruction<>(SearchInstructionType.LIKE, value));
        return this;
    }

    public <S, E> SingularAttributeFilterSettings putToSettingsWithEqualInstruction(SingularAttribute<S, E> attr, E value) {
        if (value == null) {
            throw new IllegalArgumentException("search value can'not be null!");
        }
        filters.put(attr, new SearchInstruction<>(SearchInstructionType.EQUAL, value));
        return this;
    }

    public <S, E> SingularAttributeFilterSettings putJoinToSettingsWithLikeInstruction(Attribute<S, E> attrForJoin, SingularAttribute<E, String> attr, String value) {
        if (value == null) {
            throw new IllegalArgumentException("search value can'not be null!");
        }
        filters.put(attr, new SearchInstruction<>(SearchInstructionType.LIKE, value));
        joinRules.put(attr, attrForJoin);
        return this;
    }

    public <S, E, K> SingularAttributeFilterSettings putJoinToSettingsWithEqualInstruction(Attribute<S, ? extends E> attrForJoin, SingularAttribute<E, K> attr, K value) {
        if (value == null) {
            throw new IllegalArgumentException("search value can'not be null!");
        }
        filters.put(attr, new SearchInstruction<>(SearchInstructionType.EQUAL, value));
        joinRules.put(attr, attrForJoin);
        return this;
    }

    public boolean joinIsRequired(Attribute<?, ?> attr) {
        return joinRules.containsKey(attr);
    }

    public Attribute<?, ?> getAttributeForJoin(Attribute<?, ?> attr) {
        return joinRules.get(attr);
    }

    public Set<SingularAttribute<?, ?>> getAttributes() {
        return filters.keySet();
    }

    public SearchInstruction<?> getFilterVal(Attribute<?, ?> attr) {
        return filters.get(attr);
    }
}
