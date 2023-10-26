package com.andy.adapters.output.file;

import com.andy.application.ports.output.RouterViewOutputPort;
import com.andy.domain.entity.Router;
import com.andy.domain.value_objects.RouterId;
import com.andy.domain.value_objects.RouterType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class RouterViewFileAdapter implements RouterViewOutputPort {

    private static RouterViewFileAdapter instance;

    private RouterViewFileAdapter() {
    }

    private static List<Router> readFileAsString() {
        List<Router> routers = new ArrayList<>();
        try (Stream<String> stream = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(RouterViewFileAdapter.class.getClassLoader().
                                getResourceAsStream("routers.txt")))).lines()) {
            stream.forEach(line -> {
                String[] routerEntry = line.split(";");
                var id = routerEntry[0];
                var type = routerEntry[1];
                Router router = new Router(RouterType.valueOf(type), RouterId.withId(id));
                routers.add(router);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routers;
    }

    public static RouterViewFileAdapter getInstance() {
        if (instance == null) {
            instance = new RouterViewFileAdapter();
        }
        return instance;
    }

    @Override
    public List<Router> fetchRouters() {
        return readFileAsString();
    }
}
