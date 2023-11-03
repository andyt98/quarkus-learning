package com.redhat.training.speaker;

import com.redhat.training.speaker.idgenerator.IdGenerator;
import io.quarkus.test.Mock;

import javax.inject.Singleton;
import java.util.UUID;

@Mock
@Singleton
public class DeterministicIdGenerator implements IdGenerator {

    private UUID nextUUID = new UUID(0, 0);

    public String generate() {
        UUID result = nextUUID;
        nextUUID = null;
        return result.toString();
    }

    public void setNextUUID(final UUID nextUUID) {
        this.nextUUID = nextUUID;
    }

}
