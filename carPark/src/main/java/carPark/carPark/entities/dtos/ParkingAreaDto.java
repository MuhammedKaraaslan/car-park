package carPark.carPark.entities.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.PriceList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingAreaDto {
	
	
	private int id;
	
	private String name;
	
	private int capacity;
	
	private int currentVehicleNumber;
	
	private String city;
	
	private PriceList priceList;
	
	@JsonIgnore
	private List<ParkingRecord> parkingRecords;
}
