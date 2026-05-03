package pl.tomaszlink.electionapplication.voters.helpers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.tomaszlink.electionapplication.voters.exceptions.VoterTooYoungException;
import pl.tomaszlink.electionapplication.voters.properties.AdulthoodProperty;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class AdultPolicyHelper {

    private final AdulthoodProperty adulthoodProperty;

    public void checkAgeMajority(@NotNull LocalDate birthDate) throws VoterTooYoungException{
        if(!this.isAdult(birthDate)){
            throw new VoterTooYoungException();
        }
    }

    private boolean isAdult(@NotNull LocalDate birthDate){
        LocalDate today = LocalDate.now();
        LocalDate adultThresholdDate = today.minusYears(adulthoodProperty.thresholdAge());
        return !birthDate.isAfter(adultThresholdDate);
    }
}
