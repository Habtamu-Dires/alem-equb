package com.ekub.round;


import java.util.UUID;

public record LastRoundResponse(
        UUID id,
        Integer version,
        Integer roundNumber,
        String winnerId,
        String winnerUsername,
        Boolean paid
) {}
