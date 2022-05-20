package carPark.carPark.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import carPark.carPark.entities.vehicles.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_record")
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class ParkingRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "parking_record_id")
	private int id;
	
	@Column(name = "check_in_date")
	private LocalDateTime checkInDate;
	
	@Column(name = "check_out_date")
	private LocalDateTime checkOutDate;
	
	@Column(name = "fee")
	private double fee;
	
	@ManyToOne
	@JoinColumn(name = "license_plate")
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name = "parking_area_id")
	private ParkingArea parkingArea;

	public ParkingRecord(LocalDateTime checkInDate, Vehicle vehicle, ParkingArea parkingArea) {
		super();
		this.checkInDate = checkInDate;
		this.vehicle = vehicle;
		this.parkingArea = parkingArea;
	}
	

}
