package com.senla.training_2019.smolka.price_monitoring_utils.sort_utils;

import com.senla.training_2019.smolka.utils.pagination_settings.PaginationSettings;
import org.springframework.stereotype.Component;

@Component
public class PriceMonitoringPaginationSettingsBuilder {

    public static PaginationSettings buildPaginationSettings(Integer page, Integer pageSize, String sortKey) throws IllegalArgumentException {
        Integer limit = null;
        Integer offset = null;
        if (page != null && pageSize != null) {
            limit = pageSize;
            offset = (page-1) * pageSize;
        }
        if (sortKey == null) {
            return new PaginationSettings(limit, offset);
        } else {
            PriceMonitoringSortKey enumSortKey = PriceMonitoringSortKey.valueOf(sortKey);
            return new PaginationSettings(enumSortKey.getSortOrder(), enumSortKey.getJoinAttr(), enumSortKey.getSortAttr(),  limit, offset);
        }
    }
}
