package pl.tomaszlink.electionapplication.voters.mappers;

import pl.tomaszlink.electionapplication.model.VoterListItemResponse;
import pl.tomaszlink.electionapplication.model.VoterResponse;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;

public class VoterConverter {
    private VoterConverter(){}

    public static VoterResponse toVoterResponse(VoterEntity voterEntity) {
        return new VoterResponse()
                .id(voterEntity.getId())
                .firstName(voterEntity.getFirstName())
                .lastName(voterEntity.getLastName())
                .pesel(voterEntity.getPesel())
                .birthDate(voterEntity.getBirthDate())
                .blocked(voterEntity.isBlocked());
    }

    public static VoterListItemResponse toVoterListItemResponse(VoterEntity voterEntity){
        return  new VoterListItemResponse()
                .id(voterEntity.getId())
                .firstName(voterEntity.getFirstName())
                .lastName(voterEntity.getLastName())
                .birthDate(voterEntity.getBirthDate())
                .blocked(voterEntity.isBlocked());
    }
}
