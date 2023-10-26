package com.andy.domain.policy;

import com.andy.domain.entity.Event;
import com.andy.domain.value_objects.Activity;
import com.andy.domain.value_objects.EventId;
import com.andy.domain.value_objects.Protocol;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class RegexEventParser implements EventParser {

    @Override
    public Event parseEvent(String event) {
        final String regex = "(\"[^\"]+\")|\\S+";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(event);

        var fields = new ArrayList<>();
        while (matcher.find()) {
            fields.add(matcher.group(0));
        }

        var timestamp = LocalDateTime.parse(matcher.group(0), formatter).atOffset(ZoneOffset.UTC);
        var id = EventId.of(matcher.group(1));
        var protocol = Protocol.valueOf(matcher.group(2));
        var activity = new Activity(matcher.group(3), matcher.group(5));

        return new Event(timestamp, id, protocol, activity);
    }
}
