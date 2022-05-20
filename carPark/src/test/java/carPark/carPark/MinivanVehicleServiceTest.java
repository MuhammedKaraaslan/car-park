package carPark.carPark;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import carPark.carPark.core.utilities.results.ErrorDataResult;
import carPark.carPark.core.utilities.results.ErrorResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.core.utilities.results.SuccessDataResult;
import carPark.carPark.core.utilities.results.SuccessResult;
import carPark.carPark.dataAccess.VehicleDao;
import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.vehicles.VehicleType;
import carPark.carPark.service.abstracts.VehicleCheckService;
import carPark.carPark.service.abstracts.VehicleCommonMethodsService;
import carPark.carPark.service.concretes.MinivanVehicleService;


@SpringBootTest
class MinivanVehicleServiceTest {
	
	private MinivanVehicleService minivanVehicleService;
	
	private VehicleDao vehicleDao;
	private VehicleCheckService vehicleCheckService;
	private VehicleCommonMethodsService vehicleCommonMethodsService;
	private ModelMapper modelMapper;

	@BeforeEach
	public void setUp() {
		vehicleDao = Mockito.mock(VehicleDao.class);
		vehicleCheckService = Mockito.mock(VehicleCheckService.class);
		vehicleCommonMethodsService = Mockito.mock(VehicleCommonMethodsService.class);
		modelMapper = Mockito.mock(ModelMapper.class);
		
		minivanVehicleService = new MinivanVehicleService(vehicleDao ,vehicleCheckService, vehicleCommonMethodsService, modelMapper);
	}
	
	@Test
	void testAddWithValidInput_shouldReturnSuccess() {
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		VehicleDto vehicleDto = new VehicleDto("example", VehicleType.MINIVAN, 1.15, parkingRecords);
		
		Mockito.when(vehicleCheckService.checkSaveConditions(vehicleDto)).thenReturn(new SuccessResult());
		
		Result result = minivanVehicleService.add(vehicleDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testAddWithInValidInput_shouldReturnError() {
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		VehicleDto vehicleDto = new VehicleDto("", VehicleType.MINIVAN, 1.15, parkingRecords);
		
		Mockito.when(vehicleCheckService.checkSaveConditions(vehicleDto)).thenReturn(new ErrorResult());

		assertThrows(IllegalArgumentException.class ,() -> minivanVehicleService.add(vehicleDto).isSuccess());
	}
	
	@Test
	void testCheckInWithValidInput_shouldReturnSuccess() {
		
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		
		VehicleDto vehicleDto = new VehicleDto("example", VehicleType.MINIVAN, 1.15, parkingRecords);
		
		LocalDateTime checkInDate = LocalDateTime.now();
		
		Mockito.when(vehicleCommonMethodsService.checkIn(checkInDate, vehicleDto, 1, minivanVehicleService)).thenReturn(new SuccessResult());
		
		Result result = minivanVehicleService.checkIn(checkInDate, vehicleDto, 1);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckInWithInValidInput_shouldReturnError() {
		
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		
		VehicleDto vehicleDto = new VehicleDto("", VehicleType.MINIVAN, 1.15, parkingRecords);
		
		LocalDateTime checkInDate = LocalDateTime.now();
		
		Mockito.when(vehicleCommonMethodsService.checkIn(checkInDate, vehicleDto, 1, minivanVehicleService)).thenReturn(new ErrorResult());
		
		Result result = minivanVehicleService.checkIn(checkInDate, vehicleDto, 1);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckOutWithValidInput_shouldReturnSuccess() {
		
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		
		VehicleDto vehicleDto = new VehicleDto("example", VehicleType.MINIVAN, 1.15, parkingRecords);
		
		Mockito.when(vehicleCommonMethodsService.checkOut(vehicleDto)).thenReturn(new SuccessResult());
		
		Result result = minivanVehicleService.checkOut(vehicleDto);
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckOutWithInValidInput_shouldReturnError() {
		
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		
		VehicleDto vehicleDto = new VehicleDto("", VehicleType.MINIVAN, 1.15, parkingRecords);
		
		Mockito.when(vehicleCommonMethodsService.checkOut(vehicleDto)).thenReturn(new ErrorResult());
		
		Result result = minivanVehicleService.checkOut(vehicleDto);
		
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testGetVehicleWithParkingRecordsWithValidInput_shouldReturnSuccess() {
		
		String licensePlate = "example";
		
		Mockito.when(vehicleCommonMethodsService.getVehicleWithParkingRecords(licensePlate)).thenReturn(new SuccessDataResult<>());
		
		Result result = minivanVehicleService.getVehicleWithParkingRecords(licensePlate);
		
		Assertions.assertEquals(new SuccessDataResult<>().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testGetVehicleWithParkingRecordsWithInValidInput_shouldReturnError() {
		
		String licensePlate = "";
		
		Mockito.when(vehicleCommonMethodsService.getVehicleWithParkingRecords(licensePlate)).thenReturn(new ErrorDataResult<>());
		
		Result result = minivanVehicleService.getVehicleWithParkingRecords(licensePlate);
		
		Assertions.assertEquals(new ErrorDataResult<>().isSuccess(), result.isSuccess());
	}
	

}
