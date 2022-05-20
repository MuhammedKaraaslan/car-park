package carPark.carPark;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.PricePeriodDao;
import carPark.carPark.entities.PriceList;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.PricePeriodDto;
import carPark.carPark.service.concretes.PricePeriodManager;


@SpringBootTest
class PricePeriodTest {
	
	private PricePeriodManager pricePeriodManager;
	
	private PricePeriodDao pricePeriodDao;	
	private ModelMapper modelMapper;
	
	@BeforeEach
	public void setUp() {
		pricePeriodDao = Mockito.mock(PricePeriodDao.class);
		modelMapper = Mockito.mock(ModelMapper.class);
		pricePeriodManager = new PricePeriodManager(pricePeriodDao, modelMapper );
	}
	
	@Test
	void testCheckStartHourWithValidInput_shouldReturnSuccess() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 0, 24, 10, new PriceList());
		
		Result result = pricePeriodManager.checkStartHour(pricePeriod);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckStartHourWithInValidInput_shouldReturnError() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 5, 4, 10, new PriceList());
		
		assertThrows(IllegalArgumentException.class ,() -> pricePeriodManager.checkStartHour(pricePeriod).isSuccess());
	}
	
	@Test
	void testCheckEndHourWithValidInput_shouldReturnSuccess() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 0, 24, 10, new PriceList());
		
		Result result = pricePeriodManager.checkEndHour(pricePeriod);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckEndHourWithInValidInput_shouldReturnError() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 5, -1, 10, new PriceList());
		
		assertThrows(IllegalArgumentException.class ,() -> pricePeriodManager.checkEndHour(pricePeriod).isSuccess());
	}
	
	@Test
	void testCheckPriceWithValidInput_shouldReturnSuccess() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 0, 24, 10, new PriceList());
		
		Result result = pricePeriodManager.checkPrice(pricePeriod);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckPriceWithInValidInput_shouldReturnError() {
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 5, 24, -1, new PriceList());
		
		assertThrows(IllegalArgumentException.class ,() -> pricePeriodManager.checkPrice(pricePeriod).isSuccess());
	}
	
	
	@Test
	void testCheckAllWithValidInput_shouldReturnSuccess() {
		
		PricePeriodDto pricePeriodDto = new PricePeriodDto(3, 0, 24, 10, new PriceList());
		
		PricePeriod pricePeriod = new PricePeriod(3, 0, 24, 10, new PriceList());
		
		Mockito.when(pricePeriodDao.findById(pricePeriodDto.getId())).thenReturn(Optional.of(pricePeriod));
		
		Result result = pricePeriodManager.checkAll(pricePeriodDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckAllWithInValidInput_shouldReturnError() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 5, -1, -1, new PriceList());
		
		assertThrows(IllegalArgumentException.class ,() -> pricePeriodManager.checkAll(pricePeriod).isSuccess());
	}
	
	@Test
	void testAddWithValidInput_shouldReturnSuccess() {
		
		PricePeriodDto pricePeriodDto = new PricePeriodDto(3, 0, 24, 10, new PriceList());
		
		PricePeriod pricePeriod = new PricePeriod(3, 0, 24, 10, new PriceList());
		
		Mockito.when(pricePeriodDao.findById(pricePeriodDto.getId())).thenReturn(Optional.of(pricePeriod));
		
		Result result = pricePeriodManager.add(pricePeriodDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testAddWithInValidInput_shouldReturnError() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 5, -1, -1, new PriceList());
		
		assertThrows(IllegalArgumentException.class ,() -> pricePeriodManager.add(pricePeriod).isSuccess());
	}
	
	@Test
	void testDeleteWithValidInput_shouldReturnSuccess() {
		
		PricePeriodDto pricePeriodDto = new PricePeriodDto(3, 0, 24, 10, new PriceList());
		
		PricePeriod pricePeriod = new PricePeriod(3, 0, 24, 10, new PriceList());
		
		Mockito.when(pricePeriodDao.findById(pricePeriodDto.getId())).thenReturn(Optional.of(pricePeriod));
		
		Result result = pricePeriodManager.delete(pricePeriodDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testDeleteWithInValidInput_shouldReturnError() {
		
		PricePeriodDto pricePeriod = new PricePeriodDto(1, 0, 24, 10, new PriceList());
		
		Mockito.when(pricePeriodDao.findById(1)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class ,() -> pricePeriodManager.delete(pricePeriod));
	}

	
}
