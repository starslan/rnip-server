package ru.starslan.rnip_server.model;

/**
 * Статус изменения данных
 */
public enum ChangeStatusMeaning {
    NEW("1", "Новый"),
    CLARIFICATION("2", "Уточнение"),
    CANCELLATION("3", "Аннулирование"),
    DECANCELLATION("4", "Деаннулирование");

    private final String code;
    private final String description;

    ChangeStatusMeaning(String code, String description) {
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
    public static ChangeStatusMeaning fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ChangeStatusMeaning meaning : values()) {
            if (meaning.code.equals(code)) {
                return meaning;
            }
        }
        throw new IllegalArgumentException("Неизвестный код статуса изменения: " + code);
    }
}
