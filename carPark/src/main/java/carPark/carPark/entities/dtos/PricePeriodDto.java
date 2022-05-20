package carPark.carPark.entities.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import carPark.carPark.entities.PriceList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricePeriodDto {

	
	private int id;
	
	private int startHour;
	
	private int endHour;
	
	private int price;
	
	@JsonIgnore
	private PriceList priceList;
}
