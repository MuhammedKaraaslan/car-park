package carPark.carPark.api.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.dtos.VehicleDto;
import carPark.carPark.entities.dtos.VehicleWithParkingDetailDto;
import carPark.carPark.service.concretes.SUVVehicleService;



@RestController
@RequestMapping("/api/suv")
public class SUVController {
	
	private SUVVehicleService suvVehicleService;
	
	@Autowired
	public SUVController(SUVVehicleService suvVehicleService) {
		super();
		this.suvVehicleService = suvVehicleService;
	}


	@PostMapping("/add")
	public Result add(@RequestBody VehicleDto suv) {
		return this.suvVehicleService.add(suv);
	}
	
	@PostMapping("/checkIn")
	public Result checkIn(@RequestBody VehicleDto vehicle, @RequestParam int parkingAreaId) {
		LocalDateTime date = LocalDateTime.now();
		return this.suvVehicleService.checkIn(date, vehicle, parkingAreaId);
	}
	
	@PostMapping("/checkOut")
	public Result checkOut(@RequestBody VehicleDto vehicle) {
		return this.suvVehicleService.checkOut(vehicle);
	}
	
	@GetMapping("/getVehicleWithParkingRecords")
	public DataResult<List<VehicleWithParkingDetailDto>> getVehicleWithParkingRecords(@RequestParam String licensePlate) {
		return this.suvVehicleService.getVehicleWithParkingRecords(licensePlate);
	}
}
