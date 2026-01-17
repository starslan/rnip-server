package ru.starslan.rnip_server.model;

/**
 * Тип начисления (признак предварительного начисления)
 */
public enum ChargeOrigin {
    PRIOR("PRIOR", "Предварительное начисление"),
    TEMP("TEMP", "Временное начисление");

    private final String code;
    private final String description;

    ChargeOrigin(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Получить enum по строковому коду
     */
    public static ChargeOrigin fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ChargeOrigin origin : values()) {
            if (origin.code.equals(code)) {
                return origin;
            }
        }
        throw new IllegalArgumentException("Неизвестный код типа начисления: " + code);
    }
}
