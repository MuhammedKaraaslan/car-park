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
import carPark.carPark.dataAccess.ParkingRecordDao;
import carPark.carPark.dataAccess.VehicleDao;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.PriceList;
import carPark.carPark.entities.PricePeriod;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.dtos.VehicleWithParkingDetailDto;
import carPark.carPark.entities.vehicles.Vehicle;
import carPark.carPark.entities.vehicles.VehicleType;
import carPark.carPark.service.abstracts.VehicleCheckService;
import carPark.carPark.service.concretes.SedanVehicleService;
import carPark.carPark.service.concretes.VehicleCommonMethodsManager;


@SpringBootTest
class VehicleCommonMethodsServiceTest {

	
	private VehicleCommonMethodsManager vehicleCommonMethodsManager;

	private VehicleDao vehicleDao;
	private VehicleCheckService vehicleCheckService;
	private ParkingRecordDao parkingRecordDao;
	private ParkingAreaDao parkingAreaDao;
	private ModelMapper modelMapper;

	@BeforeEach
	public void setUp() {
		vehicleDao = Mockito.mock(VehicleDao.class);
		vehicleCheckService = Mockito.mock(VehicleCheckService.class);
		parkingRecordDao = Mockito.mock(ParkingRecordDao.class);
		parkingAreaDao = Mockito.mock(ParkingAreaDao.class);
		modelMapper = Mockito.mock(ModelMapper.class);
		
		vehicleCommonMethodsManager = new VehicleCommonMethodsManager(vehicleDao, vehicleCheckService, parkingRecordDao, parkingAreaDao, modelMapper);
	}
	
	@Test
	void testCheckInWithValidInput_shouldReturnSuccess() {
		VehicleDto vehicleDto = new VehicleDto("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		ParkingArea parkingArea = new ParkingArea(1, "parking area", 10, 0, "Istanbul", new PriceList(), new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleCheckService.checkAllCheckInConditions("vehicle", 1)).thenReturn(new SuccessResult());
		Mockito.when(vehicleDao.findById("vehicle")).thenReturn(Optional.of(vehicle));
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.of(parkingArea));
		Mockito.when(modelMapper.map(vehicleDto, Vehicle.class)).thenReturn(vehicle);
		
		Result result = vehicleCommonMethodsManager.checkIn(LocalDateTime.now(), vehicleDto, 1, new SedanVehicleService(vehicleDao, vehicleCheckService, vehicleCommonMethodsManager, modelMapper));
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckInWithInValidInput_shouldReturnError() {
		VehicleDto vehicleDto = new VehicleDto("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleCheckService.checkAllCheckInConditions("vehicle", 1)).thenReturn(new SuccessResult());
		Mockito.when(vehicleDao.findById("vehicle")).thenReturn(Optional.of(vehicle));
		Mockito.when(parkingAreaDao.findById(1)).thenReturn(Optional.empty());
		Mockito.when(modelMapper.map(vehicleDto, Vehicle.class)).thenReturn(vehicle);
		
		Result result = vehicleCommonMethodsManager.checkIn(LocalDateTime.now(), vehicleDto, 1, new SedanVehicleService(vehicleDao, vehicleCheckService, vehicleCommonMethodsManager, modelMapper));
	
		Assertions.assertEquals(new ErrorResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckOutWithValidInput_shouldReturnSuccess() {
		List<PricePeriod> pricePeriods = new ArrayList<PricePeriod>();
		pricePeriods.add(new PricePeriod(1, 0, 24, 10, new PriceList()));

		List<ParkingArea> parkingAreas = new ArrayList<ParkingArea>();
		parkingAreas.add(new ParkingArea(1, "parking area", 10, 1, "Istanbul", new PriceList(1, parkingAreas, pricePeriods), new ArrayList<ParkingRecord>()));
		
		VehicleDto vehicleDto = new VehicleDto("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		
		ParkingRecord parkingRecord = new ParkingRecord(LocalDateTime.now().minusHours(1), vehicle, parkingAreas.get(0));
		List<ParkingRecord> parkingRecords = new ArrayList<ParkingRecord>();
		parkingRecords.add(parkingRecord);
		vehicle.setParkingRecords(parkingRecords);
				
		
		Mockito.when(vehicleDao.getByLicensePlate("vehicle")).thenReturn(vehicle);
		Mockito.when(parkingRecordDao.getByVehicle(vehicle)).thenReturn(parkingRecords);
		Mockito.when(parkingRecordDao.getByVehicleOrderByIdDesc(vehicle)).thenReturn(parkingRecord);
		
		Result result = vehicleCommonMethodsManager.checkOut(vehicleDto);
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testCheckOutWithInValidInput_shouldReturnError() {		
		VehicleDto vehicleDto = new VehicleDto("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		Vehicle vehicle = new Vehicle("vehicle", VehicleType.SEDAN, 1, new ArrayList<ParkingRecord>());
		
		Mockito.when(vehicleDao.getByLicensePlate("vehicle")).thenReturn(vehicle);
		Mockito.when(parkingRecordDao.getByVehicle(vehicle)).thenReturn(null);

		assertThrows(IllegalArgumentException.class ,() -> vehicleCommonMethodsManager.checkOut(vehicleDto).isSuccess());
	}
	
	@Test
	void testGetVehicleWithParkingRecordsWithValidInput_shouldReturnSuccess() {
		VehicleWithParkingDetailDto vehicleWithParkingRecords = new VehicleWithParkingDetailDto("vehicle", LocalDateTime.now(), LocalDateTime.now(), 10);
		List<VehicleWithParkingDetailDto> vehiclesWithParkingRecordsArray = new ArrayList<VehicleWithParkingDetailDto>();
		vehiclesWithParkingRecordsArray.add(vehicleWithParkingRecords);
		
		Mockito.when(vehicleDao.getVehicleWithParkingRecords("vehicle")).thenReturn(vehiclesWithParkingRecordsArray);
		
		Result result = vehicleCommonMethodsManager.getVehicleWithParkingRecords("vehicle");
		
		Assertions.assertEquals(new SuccessResult().isSuccess(), result.isSuccess());
	}
	
	@Test
	void testGetVehicleWithParkingRecordsWithInValidInput_shouldReturnError() {		
		List<VehicleWithParkingDetailDto> vehiclesWithParkingRecordsArray = new ArrayList<VehicleWithParkingDetailDto>();
		
		Mockito.when(vehicleDao.getVehicleWithParkingRecords("vehicle")).thenReturn(vehiclesWithParkingRecordsArray);
		
		assertThrows(IllegalArgumentException.class ,() -> vehicleCommonMethodsManager.getVehicleWithParkingRecords("vehicle").isSuccess());
	}

	
}
