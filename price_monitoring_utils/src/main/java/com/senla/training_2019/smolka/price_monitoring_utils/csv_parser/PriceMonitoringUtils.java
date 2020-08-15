package com.senla.training_2019.smolka.price_monitoring_utils.csv_parser;

import com.senla.training_2019.smolka.model.dto.update_dto.CostDynamicChangeDto;
import com.senla.training_2019.smolka.model.dto.update_dto.ProductChangeDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PriceMonitoringUtils {

    private static final int MINOR_UNIT = 100;
    private static final char COST_SEPARATOR = '.';

    public static String getCostFromMinorVal(Long longCost) {
        long minor = Math.abs(longCost % MINOR_UNIT);
        long par = longCost / MINOR_UNIT;
        return par + String.valueOf(COST_SEPARATOR) + minor;
    }

    public static Long getMinorCostFromString(String strCost) {
        int separatorPosition = strCost.indexOf(COST_SEPARATOR);
        if (separatorPosition == -1) {
            return Long.parseLong(strCost) * MINOR_UNIT;
        }
        String minor = strCost.substring(separatorPosition + 1);
        String par = strCost.substring(0, separatorPosition);
        return (Long.parseLong(par) * MINOR_UNIT) + Long.parseLong(minor);
    }

    public static List<ProductChangeDto> getProductDtoListFromCsvText(String text) throws CsvParserException {
        try {
            List<ProductChangeDto> productUpdateDtoList = new ArrayList<>();
            CSVParser parser = CSVParser.parse(text, CSVFormat.RFC4180);
            for (CSVRecord record : parser) {
                String productName = record.get(0);
                Long makerId = Long.parseLong(record.get(1));
                Integer categoryId = Integer.parseInt(record.get(2));
                productUpdateDtoList.add(new ProductChangeDto(productName, makerId, categoryId));
            }
            return productUpdateDtoList;
        }
        catch (IOException | IndexOutOfBoundsException | NumberFormatException exc) {
            throw new CsvParserException(exc.getMessage());
        }
    }

    public static List<CostDynamicChangeDto> getCostDynamicDtoListFromCsvText(String text) throws CsvParserException {
        try {
            List<CostDynamicChangeDto> costDynamicUpdateDtoList = new ArrayList<>();
            CSVParser parser = CSVParser.parse(text, CSVFormat.RFC4180);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (CSVRecord record : parser) {
                Long positionId = Long.parseLong(record.get(0));
                String cost = record.get(1);
                Date date = dateFormat.parse(record.get(2));
                costDynamicUpdateDtoList.add(new CostDynamicChangeDto(null, positionId, cost, date));
            }
            return costDynamicUpdateDtoList;
        }
        catch (IOException | IndexOutOfBoundsException | ParseException | NumberFormatException exc) {
            throw new CsvParserException(exc.getMessage());
        }
    }
}
