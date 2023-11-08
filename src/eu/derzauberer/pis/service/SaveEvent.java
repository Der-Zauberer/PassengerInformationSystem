package eu.derzauberer.pis.service;

import java.util.Optional;

public record SaveEvent<T>(Optional<T> oldEntity, T newEntity) {

}
