package com.fombico.batchsample;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Represents an external place to send results to.
@Component
public class ResultService {
    private List<Double> results = new ArrayList<>();

    public void sendResult(List<? extends Double> list) {
        if (list != null) {
            results.addAll(list.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }
    }

}
