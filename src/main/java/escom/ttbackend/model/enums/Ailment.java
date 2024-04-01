package escom.ttbackend.model.enums;

import lombok.Getter;

@Getter
public enum Ailment {
    OVERWEIGHT("Sobrepeso"),
    OBESITY("Obesidad"),
    DIABETES("Diabetes"),
    HYPOTHYROIDISM("Hipotiroidismo"),
    CARDIAC_PROBLEMS("Problemas Cardiacos"),
    GASTROINTESTINAL_PROBLEMS("Problemas Gastrointestinales"),
    SKIN_PROBLEMS("Problemas de la Piel"),
    CIRCULATORY_PROBLEMS("Problemas de Circulación"),
    DENTAL_PROBLEMS("Problemas dentales");

    private String ailment;

    private Ailment(String ailment) {
        this.ailment =ailment;
    }
}
