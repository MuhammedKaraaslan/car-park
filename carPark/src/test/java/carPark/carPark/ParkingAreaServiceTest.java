package carPark.carPark;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
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
import carPark.carPark.dataAccess.ParkingAreaDao;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.PriceList;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.ParkingAreaDto;
import carPark.carPark.entities.dtos.PriceListDto;
import carPark.carPark.entities.vehicles.Vehicle;
import carPark.carPark.entities.vehicles.VehicleType;
import carPark.carPark.service.abstracts.PriceListService;
import carPark.carPark.service.concretes.ParkingAreaManager;



@SpringBootTest
class ParkingAreaServiceTest {
	
	private ParkingAreaManager parkingAreaService;

	private ParkingAreaDao parkingAreaDao;
	private PriceListService priceListService;
	private ModelMapper modelMapper;

	@BeforeEach
	public void setUp() {
		parkingAreaDao = Mockito.mock(ParkingAreaDao.class);
		priceListService = Mockito.mock(PriceListService.class);
		modelMapper = Mockito.mock(ModelMapper.class);
		
		parkingAreaService = new ParkingAreaManager(parkingAreaDao ,priceListService, modelMapper);
	}
	
	@Test
	void testGetByIdWithValidInput_shouldReturnSuccess() {
		int id = 1;
		
		ParkingArea parkingArea = new ParkingArea(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		Mockito.when(parkingAreaDao.findById(id)).thenReturn(Optional.of(parkingArea));
		
		Result result = parkingAreaService.getById(id);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testGetByIdWithInValidInput_shouldReturnError() {
		int id = 1;
		
		Mockito.when(parkingAreaDao.findById(id)).thenReturn(Optional.empty());
		
		Result result = parkingAreaService.getById(id);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckNameWithValidInput_shouldReturnSuccess() {
	
		ParkingAreaDto parkingArea = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
				
		Result result = parkingAreaService.checkName(parkingArea);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckNameWithInValidInput_shouldReturnError() {
		ParkingAreaDto parkingArea = new ParkingAreaDto(1, "", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		assertThrows(IllegalArgumentException.class ,() -> parkingAreaService.checkName(parkingArea).isSuccess());
	}
	
	@Test
	void testCheckCapacityWithValidInput_shouldReturnSuccess() {
	
		ParkingAreaDto parkingArea = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
				
		Result result = parkingAreaService.checkCapacity(parkingArea);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckCapacityWithInValidInput_shouldReturnError() {
		ParkingAreaDto parkingArea = new ParkingAreaDto(1, "parkingArea", 0, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		assertThrows(IllegalArgumentException.class ,() -> parkingAreaService.checkCapacity(parkingArea).isSuccess());
	}
	
	@Test
	void testCheckCityWithValidInput_shouldReturnSuccess() {
	
		ParkingAreaDto parkingArea = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
				
		Result result = parkingAreaService.checkCity(parkingArea);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckCityWithInValidInput_shouldReturnError() {
		ParkingAreaDto parkingArea = new ParkingAreaDto(1, "parkingArea", 10, 0, "", new PriceList(), new ArrayList<ParkingRecord>());
		
		assertThrows(IllegalArgumentException.class ,() -> parkingAreaService.checkCity(parkingArea).isSuccess());
	}
	
	@Test
	void testCheckAllWithValidInput_shouldReturnSuccess() {
	
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		PriceListDto priceListDto = modelMapper.map(parkingAreaDto.getPriceList(), PriceListDto.class);
		
		Mockito.when(priceListService.checkAll(priceListDto)).thenReturn(new SuccessResult());
				
		Result result = parkingAreaService.checkAll(parkingAreaDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckAllWithInValidInput_shouldReturnError() {
	
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", null, new ArrayList<ParkingRecord>());
		PriceListDto priceListDto = modelMapper.map(parkingAreaDto.getPriceList(), PriceListDto.class);
		
		Mockito.when(priceListService.checkAll(priceListDto)).thenReturn(new ErrorResult());
				
		Result result = parkingAreaService.checkAll(parkingAreaDto);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testAddWithValidInput_shouldReturnSuccess() {
		List<PricePeriod> periods = new ArrayList<PricePeriod>();
		periods.add(new PricePeriod(1, 0, 24, 10, new PriceList()));
		PriceList priceList = new PriceList(1, new ArrayList<ParkingArea>(), periods);
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", priceList, new ArrayList<ParkingRecord>());
		ParkingArea parkingArea = new ParkingArea(1, "parkingArea", 10, 0, "Istanbul", priceList, new ArrayList<ParkingRecord>());
		PriceListDto priceListDto = modelMapper.map(parkingAreaDto.getPriceList(), PriceListDto.class);
		
		Mockito.when(priceListService.checkAll(priceListDto)).thenReturn(new SuccessResult());
		Mockito.when(modelMapper.map(parkingAreaDto, ParkingArea.class)).thenReturn(parkingArea);
						
		Result result = parkingAreaService.add(parkingAreaDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testAddWithInValidInput_shouldReturnError() {
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		PriceListDto priceListDto = modelMapper.map(parkingAreaDto.getPriceList(), PriceListDto.class);
		
		Mockito.when(priceListService.checkAll(priceListDto)).thenReturn(new ErrorResult());
						
		assertThrows(IllegalArgumentException.class ,() -> parkingAreaService.add(parkingAreaDto).isSuccess());
	}
	
	@Test
	void testDeleteWithValidInput_shouldReturnSuccess() {
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		ParkingArea parkingArea = new ParkingArea(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		Mockito.when(parkingAreaDao.findById(parkingAreaDto.getId())).thenReturn(Optional.of(parkingArea));
		Mockito.when(modelMapper.map(parkingAreaDto, ParkingArea.class)).thenReturn(parkingArea);
						
		Result result = parkingAreaService.delete(parkingAreaDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testDeleteWithInValidInput_shouldReturnError() {
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		ParkingArea parkingArea = new ParkingArea(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		Mockito.when(parkingAreaDao.findById(parkingAreaDto.getId())).thenReturn(Optional.empty());
		Mockito.when(modelMapper.map(parkingAreaDto, ParkingArea.class)).thenReturn(parkingArea);
						
		Result result = parkingAreaService.delete(parkingAreaDto);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testUpdateWithValidInput_shouldReturnSuccess() {
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		ParkingArea parkingArea = new ParkingArea(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		PriceListDto priceListDto = modelMapper.map(parkingArea.getPriceList(), PriceListDto.class);

		Mockito.when(parkingAreaDao.findById(parkingAreaDto.getId())).thenReturn(Optional.of(parkingArea));
		Mockito.when(modelMapper.map(parkingAreaDto, ParkingArea.class)).thenReturn(parkingArea);
		Mockito.when(priceListService.add(priceListDto)).thenReturn(new SuccessResult());
						
		Result result = parkingAreaService.update(parkingAreaDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testUpdateWithInValidInput_shouldReturnError() {
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		ParkingArea parkingArea = new ParkingArea(1, "parkingArea", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		Mockito.when(parkingAreaDao.findById(parkingAreaDto.getId())).thenReturn(Optional.empty());
		Mockito.when(modelMapper.map(parkingAreaDto, ParkingArea.class)).thenReturn(parkingArea);
						
		Result result = parkingAreaService.update(parkingAreaDto);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testgetDailyIncomeWithValidInput_shouldReturnSuccess() {
		
		ParkingArea parkingArea = new ParkingArea(1, "parking area", 10, 1, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		ParkingAreaDto parkingAreaDto = new ParkingAreaDto(1, "parking area", 10, 1, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.MINIVAN, 1.15, parkingRecords);
		LocalDateTime checkInDate = LocalDateTime.now().minusHours(1);
		LocalDateTime checkOutDate = LocalDateTime.now();
		parkingRecords.add(new ParkingRecord(1, checkInDate, checkOutDate, 10, vehicle, parkingArea));
		parkingArea.setParkingRecords(parkingRecords);
		
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.of(parkingArea));
		Mockito.when(parkingAreaService.getById(1).getData()).thenReturn(parkingAreaDto);
		Mockito.when(modelMapper.map(parkingAreaDto, ParkingArea.class)).thenReturn(parkingArea);
		
		Result result = parkingAreaService.getDailyIncome(1, 2022, 4, 28);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testgetDailyIncomeWithInValidInput_shouldReturnSuccess() {
		
		Mockito.when(parkingAreaDao.findById(5)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class ,() -> parkingAreaService.getDailyIncome(1, 2022, 4, 28).isSuccess());		
	}
}