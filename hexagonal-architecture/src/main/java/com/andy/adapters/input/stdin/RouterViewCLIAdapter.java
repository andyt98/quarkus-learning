package com.andy.adapters.input.stdin;


import com.andy.adapters.output.file.RouterViewFileAdapter;
import com.andy.application.ports.input.RouterViewInputPort;
import com.andy.application.usecases.RouterViewUseCase;
import com.andy.domain.entity.Router;
import com.andy.domain.value_objects.RouterType;

import java.util.List;

public class RouterViewCLIAdapter {

    private RouterViewUseCase routerViewUseCase;

    public RouterViewCLIAdapter() {
        setAdapters();
    }

    public List<Router> obtainRelatedRouters(String type) {
        return routerViewUseCase.getRouters(
                Router.filterRouterByType(RouterType.valueOf(type)));
    }

    private void setAdapters() {
        this.routerViewUseCase = new RouterViewInputPort(RouterViewFileAdapter.getInstance());
    }
}
