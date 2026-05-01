package pl.tomaszlink.electionapplication.voters.helpers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.tomaszlink.electionapplication.voters.properties.AdulthoodProperty;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class AdultPolicyHelper {

    private final AdulthoodProperty adulthoodProperty;

    public boolean isAdult(LocalDate birthDate){
        LocalDate today = LocalDate.now();
        LocalDate adultThresholdDate = today.minusYears(adulthoodProperty.thresholdAge());
        return !birthDate.isAfter(adultThresholdDate);
    }
}
