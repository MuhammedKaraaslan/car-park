package carPark.carPark.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.dtos.ParkingAreaDto;
import carPark.carPark.service.abstracts.ParkingAreaService;


@RestController
@RequestMapping("/api/parkingarea")
public class ParkingAreaController {
	
	private ParkingAreaService parkingAreaService;
	
	@Autowired
	public ParkingAreaController(ParkingAreaService parkingAreaService) {
		super();
		this.parkingAreaService = parkingAreaService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody ParkingAreaDto parkingArea) {
		return this.parkingAreaService.add(parkingArea);
	}
	
	@GetMapping("/getByName")
	public DataResult<ParkingArea> getByName(@RequestParam String name){
		return this.parkingAreaService.getByName(name);
	}
	
	@DeleteMapping("/delete")
	public Result delete(@RequestBody ParkingAreaDto parkingArea) {
		return this.parkingAreaService.delete(parkingArea);
	}
	
	@PostMapping("/update")  
	public Result update(@RequestBody ParkingAreaDto parkingArea) {
		return this.parkingAreaService.update(parkingArea);
	}
	
	@GetMapping("/getAll")
	public DataResult<List<ParkingArea>> getAll(){
		return this.parkingAreaService.getAll();
	}
	
	@GetMapping("/getDailyIncome")
	public Result getDailyIncome(int parkingAreaId, int year, int month, int day) {
		return this.parkingAreaService.getDailyIncome(parkingAreaId, year, month, day);
	}
	
}
