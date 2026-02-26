package com.itxiop.tech.supplier.sustainability;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(getterVisibility = Visibility.ANY, isGetterVisibility = Visibility.ANY)
public class SustainabilityRating {

    private int duns;

    private String score;

}
