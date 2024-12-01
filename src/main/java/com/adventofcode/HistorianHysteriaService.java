package com.adventofcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistorianHysteriaService implements InitializingBean {

    private static final String INPUT = "classpath:input.txt";
    private final ResourceLoader resourceLoader;
    private final List<Location> locations = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        loadInput();
        execute();
    }

    public void execute() {
        var leftLocations = new ArrayList<Long>();
        var rightLocations = new ArrayList<Long>();
        for (var location : locations) {
            leftLocations.add(location.left());
            rightLocations.add(location.right());
        }
        Collections.sort(leftLocations);
        Collections.sort(rightLocations);

        long diffsSum = 0;
        for (int i = 0; i < leftLocations.size(); i++) {
            long left = leftLocations.get(i);
            long right = rightLocations.get(i);
            long result = left - right;
            if (result < 0) {
                result = result * -1;
            }
            diffsSum += result;
        }
        log.info("Answer 1: {}", diffsSum);

        long occursSum = 0;
        for (var left : leftLocations) {
            long occurs = rightLocations.stream().filter(r -> r.equals(left)).count();
            long result = left * occurs;
            occursSum += result;
        }
        log.info("Answer 2: {}", occursSum);
    }

    private void loadInput() {
        var resource = resourceLoader.getResource(INPUT);
        try (var is = resource.getInputStream()) {
            var input = new String(is.readAllBytes());
            try (var scanner = new Scanner(input)) {
                while (scanner.hasNextLine()) {
                    var line = scanner.nextLine();
                    var vals = line.split(" {3}");
                    locations.add(new Location(Long.parseLong(vals[0].trim()), Long.parseLong(vals[1].trim())));
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public record Location(long left, long right) {

    }

}
