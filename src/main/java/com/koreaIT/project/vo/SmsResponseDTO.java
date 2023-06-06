package com.koreaIT.project.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsResponseDTO {

	private String requestId;
	private LocalDateTime requestTime;
	private String statusCode;
	private String statusName;
	
}
