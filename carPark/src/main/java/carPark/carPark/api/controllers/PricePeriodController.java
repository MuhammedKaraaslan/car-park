package carPark.carPark.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.dtos.PricePeriodDto;
import carPark.carPark.service.abstracts.PricePeriodService;


@RestController
@RequestMapping("/api/priceperiod")
public class PricePeriodController {
	
	private PricePeriodService pricePeriodService;

	@Autowired
	public PricePeriodController(PricePeriodService pricePeriodService) {
		super();
		this.pricePeriodService = pricePeriodService;
	}
	
	@PostMapping("/add")
	public Result add(@RequestBody PricePeriodDto pricePeriod ) {
		return this.pricePeriodService.add(pricePeriod);
	}
	
	@DeleteMapping("/delete")
	public Result delete(@RequestBody PricePeriodDto pricePeriod) {
		return this.pricePeriodService.delete(pricePeriod);
	}

	

}
