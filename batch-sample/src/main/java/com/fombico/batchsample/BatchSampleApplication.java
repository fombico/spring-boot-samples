package com.fombico.batchsample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class BatchSampleApplication {

	public static void main(String[] args) {
		List<String> params = new ArrayList<>(Arrays.asList(args));
		params.add("uniqueId=" + UUID.randomUUID().toString());
		log.info("Params: {} ", params);
		SpringApplication.run(BatchSampleApplication.class, params.toArray(new String[0]));
	}

}
