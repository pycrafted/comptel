package com.comptel.backend.domain;

/***
 * Ce record définit une structure pour recevoir les identifiants de connexion. Comme c'est un record,
 * il n'a pas de méthodes explicites'
 * @param username
 * @param password
 */
public record AccountCredentials(String username, String password) {}
