package carPark.carPark.entities.dtos;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;

import carPark.carPark.entities.ParkingRecord;
import carPark.carPark.entities.vehicles.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
	
	private String licensePlate;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING) 
	private VehicleType type;
	
	@JsonIgnore
	private double feeFactor;
	
	@JsonIgnore
	private List<ParkingRecord> parkingRecords;
	
}
