package escom.ttbackend.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.math.Fraction;

@Converter(autoApply = true)
public class FractionConverter implements AttributeConverter<Fraction, String> {

    @Override
    public String convertToDatabaseColumn(Fraction fraction) {
        if (fraction == null) {
            return null;
        }
        return fraction.toProperString();
    }

    @Override
    public Fraction convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return Fraction.getFraction(dbData);
    }
}

