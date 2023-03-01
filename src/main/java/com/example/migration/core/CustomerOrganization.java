package com.example.migration.core;

import com.opencsv.bean.CsvBindByPosition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class CustomerOrganization {

    @CsvBindByPosition(position = 0)
    private String uuid;

    @CsvBindByPosition(position = 1)
    private String organization;
}
