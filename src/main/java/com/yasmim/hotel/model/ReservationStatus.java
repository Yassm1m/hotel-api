package com.yasmim.hotel.model;

public enum ReservationStatus {
    OPEN,       // reserva criada, ainda não iniciada
    IN_USE,     // hóspede está no quarto agora
    FINISHED,   // reserva encerrada normalmente
    CANCELLED   // reserva cancelada
}