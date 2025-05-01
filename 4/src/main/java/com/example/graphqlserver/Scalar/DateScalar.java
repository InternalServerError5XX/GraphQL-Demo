package com.example.graphqlserver.Scalar;

import graphql.language.StringValue;
import graphql.schema.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateScalar {

    public static final GraphQLScalarType DATE = GraphQLScalarType.newScalar()
            .name("Date")
            .description("Custom scalar for LocalDate in format yyyy-MM-dd")
            .coercing(new Coercing<LocalDate, String>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                @Override
                public String serialize(Object dataFetcherResult) {
                    if (dataFetcherResult instanceof LocalDate date) {
                        return date.format(formatter);
                    }
                    throw new CoercingSerializeException("Expected a LocalDate.");
                }

                @Override
                public LocalDate parseValue(Object input) {
                    try {
                        return LocalDate.parse(input.toString(), formatter);
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Invalid date format.");
                    }
                }

                @Override
                public LocalDate parseLiteral(Object input) {
                    if (input instanceof StringValue value) {
                        try {
                            return LocalDate.parse(value.getValue(), formatter);
                        } catch (Exception e) {
                            throw new CoercingParseLiteralException("Invalid date format.");
                        }
                    }
                    throw new CoercingParseLiteralException("Expected a string.");
                }
            })
            .build();
}
