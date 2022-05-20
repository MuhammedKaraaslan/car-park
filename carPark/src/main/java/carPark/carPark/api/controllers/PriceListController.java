package carPark.carPark.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import carPark.carPark.core.utilities.results.DataResult;
import carPark.carPark.core.utilities.results.Result;
import carPark.carPark.entities.dtos.PriceListDto;
import carPark.carPark.service.abstracts.PriceListService;


@RestController
@RequestMapping("/api/pricelist")
public class PriceListController {

	private PriceListService priceListService;
	
	@Autowired
	public PriceListController(PriceListService priceListService) {
		super();
		this.priceListService = priceListService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody PriceListDto priceList) {
		return this.priceListService.add(priceList);
	}
	
	@DeleteMapping("/delete")
	public Result delete(@RequestBody PriceListDto priceList) {
		return this.priceListService.delete(priceList);
	}	
	
	@GetMapping("/getAll")
	public DataResult<List<PriceListDto>> getAll() {
		return this.priceListService.getAll();
	}
}
