package com.github.wenhao.geohash;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class GeoHashTest implements Serializable {

    @Test
    public void should_be_able_to_convert_coordinate_to_bits() {
        // given

        // when
        GeoHash geoHash = GeoHash.fromCoordinate(30.5451620000, 104.0620180000);

        // then
        assertThat(geoHash.toLong()).isEqualTo(4024745045182841L);
    }

    @Test
    public void should_be_able_to_convert_bits_to_coordinate() {
        // given

        // when
        GeoHash geoHash = GeoHash.fromLong(4024745045182841L);

        // then
        assertThat(geoHash.toLong()).isEqualTo(4024745045182841L);
        assertThat(geoHash.coordinate()).isNotNull();
    }

    @Test
    public void should_be_able_to_get_adjacent_coordinates() {
        // given

        // when
        GeoHash geoHash = GeoHash.fromCoordinate(30.5451620000, 104.0620180000);

        // then
        assertThat(geoHash.getAdjacent().size()).isEqualTo(8);
        assertThat(geoHash.getAdjacent().get(0).coordinate()).isNotNull();
    }

    @Test
    public void should_be_able_to_get_distance() {
        // given

        // when
        GeoHash geoHash = GeoHash.fromCoordinate(30.5451620000, 104.0620180000);
        double distance = geoHash.distance(30.5665420000, 104.0754680000);

        // then
        assertThat(distance).isEqualTo(2704.544654847537D);
    }
}
