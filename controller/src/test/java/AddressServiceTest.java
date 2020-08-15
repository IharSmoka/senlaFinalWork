import com.senla.training_2019.smolka.api.dao.ICountryDao;
import com.senla.training_2019.smolka.api.exceptions.service.EntityNotFoundException;
import com.senla.training_2019.smolka.api.exceptions.service.InternalServiceException;
import com.senla.training_2019.smolka.api.service.IAddressService;
import com.senla.training_2019.smolka.api.service.ICityService;
import com.senla.training_2019.smolka.api.service.ICountryService;
import com.senla.training_2019.smolka.config.file_config.FileConfig;
import com.senla.training_2019.smolka.config.security_config.WebSecurityConfig;
import com.senla.training_2019.smolka.model.dto.CountryDto;
import com.senla.training_2019.smolka.model.dto.simple.AddressSimpleDto;
import com.senla.training_2019.smolka.model.dto.update_dto.AddressChangeDto;
import com.senla.training_2019.smolka.model.dto.update_dto.CityChangeDto;
import com.senla.training_2019.smolka.model.entities.Address;
import com.senla.training_2019.smolka.model.entities.City;
import com.senla.training_2019.smolka.model.entities.Country;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class, WebSecurityConfig.class})
@Transactional
public class AddressServiceTest {

    private static final String countryName = "Country1";
    private static final String cityName = "City1";
    private static final String addressName = "Address1";
    private Integer cityId = null;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private ICountryService countryService;

    @Autowired
    private ICityService cityService;

    @Before
    public void setUp() throws InternalServiceException, EntityNotFoundException {
        CountryDto countryDto = new CountryDto();
        countryDto.setCountryName(countryName);
        Country country = countryService.createCountry(countryDto);
        CityChangeDto cityDto = new CityChangeDto();
        cityDto.setCityName(cityName);
        cityDto.setCountryId(country.getId());
        City city = cityService.createCity(cityDto);
        cityId = city.getId();
    }

    @Test
    public void createSuccessfulAddress() throws InternalServiceException, EntityNotFoundException {
        AddressChangeDto addressDto = new AddressChangeDto();
        addressDto.setStreetName(addressName);
        addressDto.setCityId(cityId);
        Address address = addressService.createAddress(addressDto);
        AddressSimpleDto addressToComp = addressService.findAddressesByNameAndCityId(addressName, cityId, null, null, null).get(0);
        Assert.assertEquals(address.getId(), addressToComp.getId());
    }

    @Test
    public void createUnsuccessfulAddress() {
        AddressChangeDto addressDto = new AddressChangeDto();
        addressDto.setStreetName(addressName);
        addressDto.setCityId(cityId);
        try {
            addressService.createAddress(addressDto);
        }
        catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        Assert.assertThrows(InternalServiceException.class, () -> addressService.createAddress(addressDto));
    }

    @After
    public void clear() throws InternalServiceException, EntityNotFoundException {
        List<CountryDto> countryDtoList = countryService.findCountriesByName(countryName, null, null, null);
        if (countryDtoList == null || countryDtoList.isEmpty()) {
            return;
        }
        countryService.deleteCountry(countryDtoList.get(0).getId());
    }
}
