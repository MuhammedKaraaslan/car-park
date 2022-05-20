package carPark.carPark.entities.vehicles;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import carPark.carPark.entities.ParkingRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

	@Id
	@Column(name = "license_plate")
	private String licensePlate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private VehicleType type;
	
	@Column(name = "fee_factor")
	private double feeFactor;
	
	@JsonIgnore
	@OneToMany(mappedBy = "vehicle") 
	private List<ParkingRecord> parkingRecords;
	
}
