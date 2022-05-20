package carPark.carPark.entities.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleWithParkingDetailDto {
	
	private String licensePlate;
	private LocalDateTime checkInDate;
	private LocalDateTime checkOutDate;
	private double fee;
}
