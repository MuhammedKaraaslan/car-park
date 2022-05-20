package carPark.carPark;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import carPark.carPark.core.utilities.results.ErrorResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.PriceListDao;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.PriceList;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.PriceListDto;
import carPark.carPark.entities.dtos.PricePeriodDto;
import carPark.carPark.service.abstracts.PricePeriodService;
import carPark.carPark.service.concretes.PriceListManager;



@SpringBootTest
class PriceListServiceTest {

	private PriceListManager priceListService;
	
	private PriceListDao priceListDao;
	private PricePeriodService pricePeriodService;
	private ModelMapper modelMapper;
	
	@BeforeEach
	public void setUp() {
		priceListDao = Mockito.mock(PriceListDao.class);
		pricePeriodService = Mockito.mock(PricePeriodService.class);
		modelMapper = Mockito.mock(ModelMapper.class);
		priceListService = new PriceListManager(priceListDao, pricePeriodService, modelMapper );
	}
	
	@Test
	void testGetByIdWithValidInput_shouldReturnSuccess() {
		int id = 1;
		PriceList priceList = new PriceList(1,new ArrayList<ParkingArea>(), new ArrayList<PricePeriod>());
		PriceListDto priceListDto = new PriceListDto(1,new ArrayList<ParkingArea>(), new ArrayList<PricePeriod>());
		
		Mockito.when(priceListDao.findById(id)).thenReturn(Optional.of(priceList));
		Mockito.when(modelMapper.map(priceList, PriceListDto.class)).thenReturn(priceListDto);
		
		Result result = priceListService.getById(id);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testGetByIdWithInValidInput_shouldReturnError() {
		int id = 1;
		PriceList priceList = new PriceList(1,new ArrayList<ParkingArea>(), new ArrayList<PricePeriod>());
		PriceListDto priceListDto = new PriceListDto(1,new ArrayList<ParkingArea>(), new ArrayList<PricePeriod>());
		
		Mockito.when(priceListDao.findById(id)).thenReturn(Optional.empty());
		Mockito.when(modelMapper.map(priceList, PriceListDto.class)).thenReturn(priceListDto);
		
		Result result = priceListService.getById(id);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckPeriodLimitsWithValidInput_shouldReturnSuccess() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriod period = new PricePeriod(1, 0, 4, 10, new PriceList());
		PricePeriod period2 = new PricePeriod(1, 4, 24, 10, new PriceList());
		pricePeriods.add(period);
		pricePeriods.add(period2);
		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);		
		
		Result result = priceListService.checkPeriodLimits(priceList);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckPeriodLimitsWithInValidInput_shouldReturnSuccess() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriod period = new PricePeriod(1, 0, 4, 10, new PriceList());
		PricePeriod period2 = new PricePeriod(1, 6, 24, 10, new PriceList());
		pricePeriods.add(period);
		pricePeriods.add(period2);
		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);		
		
		assertThrows(IllegalArgumentException.class, () -> priceListService.checkPeriodLimits(priceList));
	}
	
	@Test
	void testCheck24HoursWithValidInput_shouldReturnSuccess() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriod period = new PricePeriod(1, 0, 24, 10, new PriceList());
		pricePeriods.add(period);
		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		
		Result result = priceListService.check24Hours(priceList);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheck24HoursWithInValidInput_shouldReturnError() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriod period = new PricePeriod(1, 0, 20, 10, new PriceList());
		pricePeriods.add(period);
		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		
		assertThrows(IllegalArgumentException.class ,() -> priceListService.add(priceList).isSuccess());
	}
	
	@Test
	void testCheckPeriodConditionsWithValidInput_shouldReturnSuccess() {
		
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		
		PricePeriod period = new PricePeriod(1, 0, 10, 10, new PriceList());
		PricePeriod period2 = new PricePeriod(1, 10, 24, 10, new PriceList());
		
		pricePeriods.add(period);
		pricePeriods.add(period2);
		
		PricePeriodDto periodDto = modelMapper.map(period, PricePeriodDto.class);
		PricePeriodDto periodDto2 = modelMapper.map(period, PricePeriodDto.class);
		
		Mockito.when(pricePeriodService.checkAll(periodDto)).thenReturn(new SuccessResult());
		Mockito.when(pricePeriodService.checkAll(periodDto2)).thenReturn(new SuccessResult());
		
		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		
		Result result = priceListService.checkPeriodConditions(priceList);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckPeriodConditionsWithInValidInput_shouldReturnError() {
		
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		
		PricePeriod period = new PricePeriod(1, 0, 10, 10, new PriceList());
		PricePeriod period2 = new PricePeriod(1, 10, 9, 10, new PriceList());
		
		pricePeriods.add(period);
		pricePeriods.add(period2);
		
		PricePeriodDto periodDto = modelMapper.map(period, PricePeriodDto.class);
		PricePeriodDto periodDto2 = modelMapper.map(period2, PricePeriodDto.class);
		
		Mockito.when(pricePeriodService.checkAll(periodDto)).thenReturn(new SuccessResult());
		Mockito.when(pricePeriodService.checkAll(periodDto2)).thenReturn(new ErrorResult());
		
		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		
		Result result = priceListService.checkPeriodConditions(priceList);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckAllWithValidInput_shouldReturnSuccess() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriodDto periodDto = new PricePeriodDto(1, 0, 24, 10, new PriceList());
		PricePeriod period = new PricePeriod(1, 0, 24, 10, new PriceList());
		pricePeriods.add(period);

		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		priceList.setPricePeriods(pricePeriods);
		
		Mockito.when(modelMapper.map(period, PricePeriodDto.class)).thenReturn(periodDto);
		Mockito.when(pricePeriodService.checkAll(periodDto)).thenReturn(new SuccessResult());
		
		Result result = priceListService.checkAll(priceList);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckAllWithInValidInput_shouldReturnError() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriodDto periodDto = new PricePeriodDto(1, 0, 22, 10, new PriceList());
		PricePeriod period = new PricePeriod(1, 0, 22, 10, new PriceList());
		pricePeriods.add(period);

		PriceListDto priceList = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		priceList.setPricePeriods(pricePeriods);
		
		Mockito.when(modelMapper.map(period, PricePeriodDto.class)).thenReturn(periodDto);
		Mockito.when(pricePeriodService.checkAll(periodDto)).thenReturn(new SuccessResult());
		
		assertThrows(IllegalArgumentException.class ,() -> priceListService.checkAll(priceList).isSuccess());
	}
	
	@Test
	void testAddWithValidInput_shouldReturnSuccess() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriodDto periodDto = new PricePeriodDto(1, 0, 24, 10, new PriceList());
		PricePeriod period = new PricePeriod(1, 0, 24, 10, new PriceList());
		pricePeriods.add(period);

		PriceListDto priceListDto = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		PriceList priceList = new PriceList(1, new ArrayList<ParkingArea>(), pricePeriods);
		
		Mockito.when(modelMapper.map(priceListDto, PriceList.class)).thenReturn(priceList);
		Mockito.when(modelMapper.map(period, PricePeriodDto.class)).thenReturn(periodDto);
		Mockito.when(pricePeriodService.checkAll(periodDto)).thenReturn(new SuccessResult());
		
		Result result = priceListService.add(priceListDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testAddWithInValidInput_shouldReturnError() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		PricePeriodDto periodDto = new PricePeriodDto(1, 0, 22, 10, new PriceList());
		PricePeriod period = new PricePeriod(1, 0, 22, 10, new PriceList());
		pricePeriods.add(period);

		PriceListDto priceListDto = new PriceListDto(1, new ArrayList<ParkingArea>(), pricePeriods);
		PriceList priceList = new PriceList(1, new ArrayList<ParkingArea>(), pricePeriods);
		
		Mockito.when(modelMapper.map(priceListDto, PriceList.class)).thenReturn(priceList);
		Mockito.when(modelMapper.map(period, PricePeriodDto.class)).thenReturn(periodDto);
		Mockito.when(pricePeriodService.checkAll(periodDto)).thenReturn(new SuccessResult());
		
		assertThrows(IllegalArgumentException.class ,() -> priceListService.add(priceListDto).isSuccess());
	}
		
	@Test
	void testDeleteWithValidInput_shouldReturnSuccess() {
		
		PriceListDto priceListDto = new PriceListDto(1, new ArrayList<ParkingArea>(), new ArrayList<PricePeriod>());
		PriceList priceList = new PriceList(1, new ArrayList<ParkingArea>(), new ArrayList<PricePeriod>());
		
		Mockito.when(priceListDao.findById(1)).thenReturn(Optional.of(priceList));
		
		Result result = priceListService.delete(priceListDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testDeleteWithInValidInput_shouldReturnError() {
		
		PriceListDto priceListDto = new PriceListDto(1, new ArrayList<ParkingArea>(), new ArrayList<PricePeriod>());
		
		Mockito.when(priceListDao.findById(1)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class ,() -> priceListService.delete(priceListDto).isSuccess());
	}
	
}


