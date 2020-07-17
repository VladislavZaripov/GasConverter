package com.example.gasconverter;

import Gasconverter.Converter;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConverterTest {

    @Test
    public void converter() {
        Converter converter = new Converter();
        converter.createData();

        Double actual = converter.convert(10.00,190.00,320.00);
        Double expected = 80.00;
        assertEquals(expected,actual);

        actual = converter.convert(-30.00,10.00,1.00);
        expected = 0.011;
        assertEquals(expected,actual);

        actual = converter.convert(40.00,200.00,1.00);
        expected = 0.2326;
        assertEquals(expected,actual);

        actual = converter.convert(-25.00,85.00,1.00);
        expected = 0.13255;
        assertEquals(expected,actual);
    }
}