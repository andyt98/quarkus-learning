package com.redhat.training;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssociateRepository implements PanacheRepositoryBase<Associate, Long> {
}
