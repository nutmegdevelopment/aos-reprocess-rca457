package com.example.migration.core.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static org.apache.commons.lang3.StringUtils.isBlank;

@AllArgsConstructor
@Getter
public enum UserOrganisation {

    NUTMEG(Organisation.NUTMEG),
    JLP(Organisation.JLP),
    CHASE_GBR(Organisation.CHASE_GBR);

    private final Organisation organisation;

    private static final Map<String, UserOrganisation> lookup =
            Arrays.stream(UserOrganisation.values())
                    .collect(Collectors.toMap(UserOrganisation::keyAsLowerCaseCode, Function.identity()));

    private static String keyAsLowerCaseCode(UserOrganisation org) {
        return org.getCode().toLowerCase(Locale.ROOT);
    }


    public static UserOrganisation valueOfIgnoreCase(final String codeValue) {
        return lookup.get(codeValue.toLowerCase(Locale.ROOT));
    }

    public static UserOrganisation valueOfWithDefault(final String codeValue) {
        if (isBlank(codeValue)) {
            return NUTMEG;
        } else {
            return UserOrganisation.valueOfIgnoreCase(codeValue);
        }
    }

    public String getCode() {
        return organisation.getCode();
    }
}
