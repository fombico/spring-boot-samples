package com.fombico.batchsample;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@EnableBatchProcessing
@AllArgsConstructor
@Configuration
public class BatchJobConfig {

    private static final String PROXY_NUMBERS_PARAM = "";
    private final JobBuilderFactory jobs;
    private final StepBuilderFactory steps;
    private ResultService resultService;
    private final List<Double> numberList = new ArrayList<>();

    @Bean
    Job job() {
        log.info("constructing job");
        return jobs.get("job0")
                .start(convertToDoubles())
                .next(squareNumbers())
                .build();
    }

    @StepScope
    @Bean
    ListItemReader<String> numberParamListItemReader(@Value("#{jobParameters['numbers']}") String numbers) {
        log.info("numbers param is set to {}", numbers);
        List<String> parsedList = Arrays.stream(numbers.split(","))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new ListItemReader<>(parsedList);
    }

    private Step convertToDoubles() {
        return steps.get("step 1")
                .<String, Double>chunk(3)
                .reader(numberParamListItemReader(PROXY_NUMBERS_PARAM))
                .processor((ItemProcessor<String, Double>) item -> {
                    log.info("step 1 - processing item {}", item);
                    return Double.valueOf(item);
                })
                .writer(numberList::addAll)
                .allowStartIfComplete(true)
                .build();
    }

    private Step squareNumbers() {
        return steps.get("step 2")
                .<Double, Double>chunk(2)
                .reader(() -> numberList.isEmpty() ? null : numberList.remove(0))
                .processor((ItemProcessor<Double, Double>) item -> item * item)
                .writer(items -> {
                    log.info("Step 2 result: {}", items);
                    resultService.sendResult(items);
                })
                .allowStartIfComplete(true)
                .build();
    }
}
