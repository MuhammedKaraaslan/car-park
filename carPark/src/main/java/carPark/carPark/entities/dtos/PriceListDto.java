package carPark.carPark.entities.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import carPark.carPark.entities.ParkingArea;
import carPark.carPark.entities.PricePeriod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListDto {
	
	private int id;
	
	@JsonIgnore
	private List<ParkingArea> parkingAreas;
	
	private List<PricePeriod> pricePeriods;
	
}
