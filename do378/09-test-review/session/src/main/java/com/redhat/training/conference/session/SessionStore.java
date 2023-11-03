package com.redhat.training.conference.session;

import com.redhat.training.conference.speaker.SpeakerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SessionStore {

    @Inject
    @RestClient
    SpeakerService speakerService;

    public List<SessionWithSpeaker> getAll() throws Exception {
        List<Session> sessions = Session.findAll().list();

        return sessions.stream()
                .map(this::toSessionWithSpeaker)
                .collect(Collectors.toList());
    }

    public SessionWithSpeaker getById(Long sessionId) {
        Optional<Session> optional = Session.findByIdOptional(sessionId);
        Session session = optional.orElseThrow(NotFoundException::new);

        return toSessionWithSpeaker(session);
    }

    @Transactional
    public Session save(Session session) {
        session.persist();
        return session;
    }

    private SessionWithSpeaker toSessionWithSpeaker(Session session) {
        var speaker = speakerService.getById(session.speakerId);
        return session.withSpeaker(speaker);
    }

}
