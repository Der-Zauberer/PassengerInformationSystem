package eu.derzauberer.pis.util;

import java.util.Optional;

public record SaveEvent<T>(Optional<T> oldEntity, T newEntity) {

}
