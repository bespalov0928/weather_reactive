package com.example.weather_reactive.service;

import com.example.weather_reactive.model.Weather;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class WeatherService {
    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();

    {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 15));
        weathers.put(4, new Weather(4, "Smolensk", 15));
        weathers.put(5, new Weather(5, "Kiev", 30));
        weathers.put(6, new Weather(6, "Minsk", 30));
    }

    public Mono<Weather> findById(Integer id) {
        return Mono.justOrEmpty(weathers.get(id));
    }

    public Flux<Weather> all() {
        return Flux.fromIterable(weathers.values());
    }

    public Flux<Weather> findHostsCity() {
        Flux<Weather> flux = Flux.fromIterable(weathers.values());
        Collection<Weather> list = weathers.values();

        Mono<Integer> age = Flux.fromIterable(list).map(Weather::getTemperature).reduce(Integer::max);
        final Integer[] ageValue = new Integer[1];
        age.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                ageValue[0] = integer;
            }
        });
        return flux.filter(weather -> weather.getTemperature() == ageValue[0]);
    }

    public Flux<Weather> findCityMoreTemp(Integer temperature) {
        Flux<Weather> flux = Flux.fromIterable(weathers.values());
        return flux.filter(weather -> weather.getTemperature() > temperature);
    }

}
