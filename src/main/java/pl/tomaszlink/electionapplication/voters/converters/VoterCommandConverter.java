package pl.tomaszlink.electionapplication.voters.converters;

import jakarta.validation.constraints.NotNull;
import pl.tomaszlink.electionapplication.model.RegisterVoterRequest;
import pl.tomaszlink.electionapplication.model.UpdateVoterBlockStatusRequest;
import pl.tomaszlink.electionapplication.model.UpdateVoterRequest;
import pl.tomaszlink.electionapplication.voters.commands.RegisterVoterCommand;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterBlockStatusCommand;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterCommand;
import pl.tomaszlink.electionapplication.voters.helpers.PeselHelper;

import java.util.UUID;

public class VoterCommandConverter {
    private VoterCommandConverter(){}

    public static RegisterVoterCommand toRegisterVoterCommand(@NotNull RegisterVoterRequest registerVoterRequest){
        return new RegisterVoterCommand(
                registerVoterRequest.getFirstName(),
                registerVoterRequest.getLastName(),
                registerVoterRequest.getPesel(),
                PeselHelper.extractBirthDateFromPesel(registerVoterRequest.getPesel())
        );
    }

    public static UpdateVoterCommand toUpdateVoterCommand(@NotNull UUID id, @NotNull UpdateVoterRequest updateVoterRequest){
        return new UpdateVoterCommand(
                id,
                updateVoterRequest.getFirstName(),
                updateVoterRequest.getLastName(),
                updateVoterRequest.getPesel(),
                PeselHelper.extractBirthDateFromPesel(updateVoterRequest.getPesel())
        );
    }

    public static UpdateVoterBlockStatusCommand toUpdateVoterBlockStatusCommand(@NotNull UUID id, @NotNull UpdateVoterBlockStatusRequest updateVoterBlockStatusRequest){
        return new UpdateVoterBlockStatusCommand(
                id,
                updateVoterBlockStatusRequest.getBlocked()
        );
    }

}
