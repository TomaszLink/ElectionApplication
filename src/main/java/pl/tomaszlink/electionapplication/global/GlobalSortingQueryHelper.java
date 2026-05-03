package pl.tomaszlink.electionapplication.global;

import org.springframework.data.domain.Sort;


public class GlobalSortingQueryHelper {

    private static final String DEFAULT_VOTER_SORT_BY_PROPERTY = "lastName";
    private static final String DEFAULT_ELECTION_SORT_BY = "name";
    private static final Sort.Direction DEFAULT_ELECTION_SORT_DIRECTION = Sort.Direction.ASC;


    public static Sort createVoterSort(String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection == null    ?
                Sort.Direction.ASC    :
                Sort.Direction.fromString(sortDirection);

        String property = sortBy == null    ?
                DEFAULT_VOTER_SORT_BY_PROPERTY  :
                sortBy;

        return Sort.by(direction, property);
    }

    public static Sort createElectionSort(String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection == null    ?
                DEFAULT_ELECTION_SORT_DIRECTION    :
                Sort.Direction.fromString(sortDirection);

        String property = sortBy == null    ?
                DEFAULT_ELECTION_SORT_BY  :
                sortBy;

        return Sort.by(direction, property);
    }
}
