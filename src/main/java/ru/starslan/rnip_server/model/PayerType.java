package ru.starslan.rnip_server.model;

public enum PayerType {
    INDIVIDUAL("ФЛ"),      // Физическое лицо
    LEGAL_ENTITY("ЮЛ"),    // Юридическое лицо
    ENTREPRENEUR("ИП");    // Индивидуальный предприниматель

    private final String name;

    PayerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
